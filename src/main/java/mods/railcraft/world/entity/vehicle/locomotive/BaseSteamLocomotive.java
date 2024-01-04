package mods.railcraft.world.entity.vehicle.locomotive;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidTransferHandler;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.season.Seasons;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.FluidTools;
import mods.railcraft.util.FluidTools.ProcessType;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.material.RailcraftFluids;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.TankManager;
import mods.railcraft.world.level.material.steam.SteamBoiler;
import mods.railcraft.world.level.material.steam.SteamConstants;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public abstract class BaseSteamLocomotive extends Locomotive implements FluidTransferHandler {

  protected static final int SLOT_WATER_INPUT = 0;
  protected static final int SLOT_WATER_PROCESSING = 1;
  protected static final int SLOT_WATER_OUTPUT = 2;

  private static final EntityDataAccessor<Boolean> SMOKE =
      SynchedEntityData.defineId(BaseSteamLocomotive.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Boolean> STEAM =
      SynchedEntityData.defineId(BaseSteamLocomotive.class, EntityDataSerializers.BOOLEAN);

  private static final byte TICKS_PER_BOILER_CYCLE = 2;
  private static final int FUEL_PER_REQUEST = 3;

  protected final StandardTank waterTank =
      StandardTank.ofBuckets(6)
          .fillProcessor(this::checkFill)
          .filter(() -> Fluids.WATER);

  protected final StandardTank steamTank =
      StandardTank.ofBuckets(16)
          .filter(RailcraftFluids.STEAM)
          .disableDrain()
          .disableFill();

  private final SteamBoiler boiler = new SteamBoiler(this.waterTank, this.steamTank)
      .setEfficiencyModifier(RailcraftConfig.SERVER.fuelPerSteamMultiplier.get())
      .setTicksPerCycle(TICKS_PER_BOILER_CYCLE);

  protected final ContainerMapper invWaterContainers =
      ContainerMapper.make(this, SLOT_WATER_INPUT, 3).ignoreItemChecks();

  private final TankManager tankManager = new TankManager(this.waterTank, this.steamTank);

  private int fluidProcessingTimer = 0;

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  protected BaseSteamLocomotive(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected BaseSteamLocomotive(ItemStack itemStack, EntityType<?> type,
      double x, double y, double z, ServerLevel serverLevel) {
    super(itemStack, type, x, y, z, serverLevel);
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
    return FluidTools.interactWithFluidHandler(player, hand, this.getTankManager())
        ? InteractionResult.SUCCESS
        : super.interact(player, hand);
  }

  public TankManager getTankManager() {
    return this.tankManager;
  }

  @Override
  public void tick() {
    super.tick();

    if (this.isRemoved()) {
      return;
    }

    if (this.level().isClientSide()) {
      this.clientTick();
      return;
    }

    if (this.waterTank.isEmpty()) {
      this.setMode(Mode.SHUTDOWN);
    }
    this.setSteaming(this.steamTank.getFluidAmount() > 0);

    if (this.steamTank.getRemainingSpace() >= SteamConstants.STEAM_PER_UNIT_WATER
        || this.isShutdown()) {
      this.boiler.tick(1);

      this.setSmoking(this.boiler.isBurning());

      // TODO: make venting a toggleable thing (why autodump while train has no coal??)
      if (!this.boiler.isBurning()) {
        this.ventSteam();
      }
    }

    if (++this.fluidProcessingTimer >= FluidTools.BUCKET_FILL_TIME) {
      this.fluidProcessingTimer = 0;
      this.processState = FluidTools.processContainer(this.invWaterContainers,
          this.waterTank, ProcessType.DRAIN_ONLY, this.processState);
    }
  }

  private void clientTick() {
    // future information: renderYaw FACES at -x when at 0deg
    double rads = Math.toRadians(renderYaw);
    if (this.isSmoking()) {
      float offset = 0.4f;

      var x = this.getX() - Math.cos(rads) * offset;
      var y = this.getY() + 1.5;
      var z = this.getZ() - Math.sin(rads) * offset;

      if (Seasons.HALLOWEEN && this.random.nextInt(4) == 0) { // 20%?
        this.level().addParticle(RailcraftParticleTypes.PUMPKIN.get(), x, y, z, 0, 0.02, 0);
      } else {
        // smog obviously.
        this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.02, 0);
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

      this.level().addParticle(RailcraftParticleTypes.STEAM.get(),
          this.getX() - Math.cos(rads + ninetyDeg) * offset, ycoord,
          this.getZ() - Math.sin(rads + ninetyDeg) * offset, vx,
          0.02 + (this.random.nextDouble() * 0.01), vz);

      this.level().addParticle(RailcraftParticleTypes.STEAM.get(),
          this.getX() - Math.cos(rads - ninetyDeg) * offset, ycoord,
          this.getZ() - Math.sin(rads - ninetyDeg) * offset, vx,
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
    this.steamTank.internalDrain(4, IFluidHandler.FluidAction.EXECUTE);
  }

  public SteamBoiler boiler() {
    return this.boiler;
  }

  @Override
  public int retrieveFuel() {
    var steam = this.steamTank.getFluid();
    if (steam == FluidStack.EMPTY) {
      return 0;
    }
    if (steam.getAmount() >= this.steamTank.getCapacity() / 2) {
      this.steamTank.internalDrain(SteamConstants.STEAM_PER_UNIT_WATER, IFluidHandler.FluidAction.EXECUTE);
      return FUEL_PER_REQUEST;
    }
    return 0;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.put("tankManager", this.getTankManager().serializeNBT());
    tag.put("boiler", this.boiler.serializeNBT());
    tag.putString("processState", this.processState.getSerializedName());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.getTankManager().deserializeNBT(tag.getList("tankManager", Tag.TAG_COMPOUND));
    this.boiler.deserializeNBT(tag.getCompound("boiler"));
    this.processState = FluidTools.ProcessState.getByName(tag.getString("processState"))
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
  public boolean canAcceptPushedFluid(RollingStock requester, FluidStack fluid) {
    return Fluids.WATER.isSame(fluid.getFluid());
  }

  @Override
  public boolean canProvidePulledFluid(RollingStock requester, FluidStack fluid) {
    return false;
  }

  @Override
  public void setFilling(boolean filling) {}

  private FluidStack checkFill(FluidStack resource) {
    return this.boiler.checkFill(resource, this::explode);
  }
}

