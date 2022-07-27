package mods.railcraft.world.entity.vehicle.locomotive;

import javax.annotation.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidMinecart;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.season.Seasons;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.FluidTools.ProcessType;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.level.material.fluid.TankManager;
import mods.railcraft.world.level.material.fluid.steam.SteamBoiler;
import mods.railcraft.world.level.material.fluid.steam.SteamConstants;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * The steam locomotive.
 * 
 * @author CovertJaguar (https://www.railcraft.info)
 */
public abstract class BaseSteamLocomotive extends Locomotive implements FluidMinecart {

  public static final int SLOT_WATER_INPUT = 0;
  public static final int SLOT_WATER_PROCESSING = 1;
  public static final int SLOT_WATER_OUTPUT = 2;

  private static final EntityDataAccessor<Boolean> SMOKE =
      SynchedEntityData.defineId(BaseSteamLocomotive.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Boolean> STEAM =
      SynchedEntityData.defineId(BaseSteamLocomotive.class, EntityDataSerializers.BOOLEAN);

  private static final byte TICKS_PER_BOILER_CYCLE = 2;
  private static final int FUEL_PER_REQUEST = 3;

  protected final StandardTank waterTank = new FilteredTank(FluidTools.BUCKET_VOLUME * 6) {
    @Override
    public int fill(FluidStack resource, FluidAction doFill) {
      // handles boiler explotion
      return super.fill(BaseSteamLocomotive.this.checkFill(resource), doFill);
    }
  }.setFilterFluid(() -> Fluids.WATER);

  protected final StandardTank steamTank = new FilteredTank(FluidTools.BUCKET_VOLUME * 16)
      .setFilterFluid(RailcraftFluids.STEAM)
      .disableDrain()
      .disableFill();

  private final SteamBoiler boiler = new SteamBoiler(this.waterTank, this.steamTank)
      .setEfficiencyModifier(RailcraftConfig.server.fuelPerSteamMultiplier.get())
      .setTicksPerCycle(TICKS_PER_BOILER_CYCLE);

  protected final ContainerMapper invWaterInput =
      ContainerMapper.make(this, SLOT_WATER_INPUT, 1)
          .withStackSizeLimit(4);
  protected final ContainerMapper invWaterOutput = ContainerMapper.make(this, SLOT_WATER_OUTPUT, 1);
  protected final ContainerMapper invWaterContainers =
      ContainerMapper.make(this, SLOT_WATER_INPUT, 3);

  private final LazyOptional<TankManager> tankManager =
      LazyOptional.of(() -> new TankManager(this.waterTank, this.steamTank));

  private int fluidProcessingTimer = this.random.nextInt();

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  protected BaseSteamLocomotive(EntityType<?> type, Level world) {
    super(type, world);
  }

  protected BaseSteamLocomotive(ItemStack itemStack, EntityType<?> type, double x,
      double y, double z, ServerLevel world) {
    super(itemStack, type, x, y, z, world);
  }

  @Override
  public Speed getMaxReverseSpeed() {
    return Speed.SLOWEST;
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(SMOKE, false);
    this.entityData.define(STEAM, false);
  }

  @Override
  public boolean isAllowedMode(Mode mode) {
    return this.waterTank.isEmpty() && mode == Mode.SHUTDOWN || super.isAllowedMode(mode);
  }

  @Override
  public SoundEvent getWhistleSound() {
    return RailcraftSoundEvents.STEAM_WHISTLE.get();
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    return FluidTools.interactWithFluidHandler(player, hand, getTankManager())
        ? InteractionResult.SUCCESS
        : super.interact(player, hand);
  }

  public TankManager getTankManager() {
    return this.tankManager.orElseThrow(() -> new IllegalStateException("Expected tank manager"));
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? this.tankManager.cast()
        : super.getCapability(capability, facing);
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.level.isClientSide()) {
      if (this.waterTank.isEmpty()) {
        this.setMode(Mode.SHUTDOWN);
      }
      this.setSteaming(this.steamTank.getFluidAmount() > 0);

      if (this.steamTank.getRemainingSpace() >= SteamConstants.STEAM_PER_UNIT_WATER
          || this.isShutdown()) {
        this.boiler.tick(1);

        this.setSmoking(this.boiler.isBurning());

        // TODO: make venting a toggleable thing (why autodump while train has no
        // coal??)
        if (!this.boiler.isBurning()) {
          this.ventSteam();
        }
      }

      if (this.fluidProcessingTimer++ >= FluidTools.BUCKET_FILL_TIME) {
        this.fluidProcessingTimer = 0;
        this.processState = FluidTools.processContainer(this.invWaterContainers,
            this.waterTank, ProcessType.DRAIN_ONLY, this.processState);
      }
      return; // particles should NOT run serverside (it's a waste)
    }
    // future information: renderYaw FACES at -x when at 0deg
    double rads = Math.toRadians(renderYaw);
    if (this.isSmoking()) {
      float offset = 0.4f;

      var x = this.getX() - Math.cos(rads) * offset;
      var y = this.getY() + 1.5;
      var z = this.getZ() - Math.sin(rads) * offset;

      if (Seasons.HALLOWEEN && this.random.nextInt(4) == 0) { // 20%?
        this.level.addParticle(RailcraftParticleTypes.PUMPKIN.get(), x, y, z, 0, 0.02, 0);
      } else {
        // smog obviously.
        this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.02, 0);
      }
    }
    // steam spawns ON the engine itself, spreading left or right
    // as the pistons are on the train's sides
    if (this.isSteaming()) {
      float offset = 0.5f;
      double ninetyDeg = Math.toRadians(90) + Math.toRadians(this.random.nextInt(10)); // 10* bias
      double steamAngularSpeed = 0.01;
      double ycoord = this.getY() + 0.15;

      var vx = steamAngularSpeed * Math.cos(rads - ninetyDeg);
      var vz = steamAngularSpeed * Math.sin(rads - ninetyDeg);

      this.level.addParticle(RailcraftParticleTypes.STEAM.get(),
          this.getX() - Math.cos(rads + ninetyDeg) * offset, ycoord,
          this.getZ() - Math.sin(rads + ninetyDeg) * offset, vx,
          0.02 + (this.random.nextDouble() * 0.01), vz);

      this.level.addParticle(RailcraftParticleTypes.STEAM.get(),
          this.getX() - Math.cos(rads + -ninetyDeg) * offset, ycoord,
          this.getZ() - Math.sin(rads + -ninetyDeg) * offset, vx,
          0.02 + (this.random.nextDouble() * 0.01), vz);
    }
  }

  public boolean isSmoking() {
    return this.entityData.get(SMOKE);
  }

  private void setSmoking(boolean smoke) {
    this.entityData.set(SMOKE, smoke);
  }

  public boolean isSteaming() {
    return this.entityData.get(STEAM);
  }

  private void setSteaming(boolean steam) {
    this.entityData.set(STEAM, steam);
  }

  private void ventSteam() {
    this.steamTank.internalDrain(4, FluidAction.EXECUTE);
  }

  public SteamBoiler getBoiler() {
    return this.boiler;
  }

  @Override
  public int retrieveFuel() {
    FluidStack steam = steamTank.getFluid();
    if (steam == FluidStack.EMPTY) {
      return 0;
    }
    if (steam.getAmount() >= steamTank.getCapacity() / 2) {
      steamTank.internalDrain(SteamConstants.STEAM_PER_UNIT_WATER, FluidAction.EXECUTE);
      return FUEL_PER_REQUEST;
    }
    return 0;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    data.put("tankManager", this.getTankManager().serializeNBT());
    data.put("boiler", this.boiler.serializeNBT());
    data.putString("processState", this.processState.getSerializedName());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag data) {
    super.readAdditionalSaveData(data);
    this.getTankManager().deserializeNBT(data.getList("tankManager", Tag.TAG_COMPOUND));
    this.boiler.deserializeNBT(data.getCompound("boiler"));
    this.processState = FluidTools.ProcessState.getByName(data.getString("processState"))
        .orElse(FluidTools.ProcessState.RESET);
  }

  public boolean isSafeToFill() {
    return !this.boiler.isSuperHeated() || !this.waterTank.isEmpty();
  }

  @Override
  public boolean canPassFluidRequests(FluidStack fluid) {
    return Fluids.WATER.isSame(fluid.getFluid());
  }

  @Override
  public boolean canAcceptPushedFluid(AbstractMinecart requester, FluidStack fluid) {
    return Fluids.WATER.isSame(fluid.getFluid());
  }

  @Override
  public boolean canProvidePulledFluid(AbstractMinecart requester, FluidStack fluid) {
    return false;
  }

  @Override
  public void setFilling(boolean filling) {}

  private FluidStack checkFill(FluidStack resource) {
    return this.boiler.checkFill(resource, this::explode);
  }
}

