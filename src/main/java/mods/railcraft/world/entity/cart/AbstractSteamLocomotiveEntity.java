package mods.railcraft.world.entity.cart;

import javax.annotation.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.IFluidCart;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.FluidTools.ProcessType;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.level.material.fluid.TankManager;
import mods.railcraft.world.level.material.fluid.steam.IBoilerContainer;
import mods.railcraft.world.level.material.fluid.steam.SteamBoiler;
import mods.railcraft.world.level.material.fluid.steam.SteamConstants;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public abstract class AbstractSteamLocomotiveEntity extends LocomotiveEntity
    implements IFluidCart, IBoilerContainer {

  public static final int SLOT_WATER_INPUT = 0;
  public static final int SLOT_WATER_PROCESSING = 1;
  public static final int SLOT_WATER_OUTPUT = 2;

  private static final DataParameter<Boolean> SMOKE =
      EntityDataManager.defineId(AbstractSteamLocomotiveEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Boolean> STEAM =
      EntityDataManager.defineId(AbstractSteamLocomotiveEntity.class, DataSerializers.BOOLEAN);

  private static final byte TICKS_PER_BOILER_CYCLE = 2;
  private static final int FUEL_PER_REQUEST = 3;

  protected final StandardTank waterTank = new FilteredTank(FluidTools.BUCKET_VOLUME * 6) {
    @Override
    public int fill(FluidStack resource, FluidAction doFill) {
      return super.fill(onFillWater(resource), doFill);
    }
  }.setFilterFluid(() -> Fluids.WATER);

  protected final StandardTank steamTank = new FilteredTank(FluidTools.BUCKET_VOLUME * 16)
      .setFilterFluid(RailcraftFluids.STEAM)
      .disableDrain()
      .disableFill();

  private final SteamBoiler boiler = new SteamBoiler(this.waterTank, this.steamTank)
      .setEfficiencyModifier(RailcraftConfig.server.fuelPerSteamMultiplier.get())
      .setTicksPerCycle(TICKS_PER_BOILER_CYCLE);

  protected final InventoryMapper invWaterInput =
      InventoryMapper.make(this, SLOT_WATER_INPUT, 1)
          .withStackSizeLimit(4);
  protected final InventoryMapper invWaterOutput = InventoryMapper.make(this, SLOT_WATER_OUTPUT, 1);
  protected final InventoryMapper invWaterContainers =
      InventoryMapper.make(this, SLOT_WATER_INPUT, 3);

  private final LazyOptional<TankManager> tankManager =
      LazyOptional.of(() -> new TankManager(this.waterTank, this.steamTank));

  private int update = this.random.nextInt();

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  protected AbstractSteamLocomotiveEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  protected AbstractSteamLocomotiveEntity(ItemStack itemStack, EntityType<?> type, double x,
      double y, double z, ServerWorld world) {
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
  public ActionResultType interact(PlayerEntity player, Hand hand) {
    return FluidTools.interactWithFluidHandler(player, hand, getTankManager())
        ? ActionResultType.SUCCESS
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
      this.update++;

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

      if ((this.update % FluidTools.BUCKET_FILL_TIME) == 0) {
        this.processState = FluidTools.processContainer(this.invWaterContainers,
            this.waterTank, ProcessType.DRAIN_ONLY, this.processState);
      }
      return;
    }
    // future information: renderYaw FACES at -x when at 0deg
    double rads = Math.toRadians(renderYaw);
    if (this.isSmoking()) {
      float offset = 0.4f;
      ClientEffects.INSTANCE.locomotiveEffect(
          this.getX() - Math.cos(rads) * offset, this.getY() + 1.5,
          this.getZ() - Math.sin(rads) * offset);
    }
    // steam spawns ON the engine itself, spreading left or right
    // as the pistons are on the train's sides
    if (this.isSteaming()) {
      float offset = 0.5f;
      double ninetyDeg = Math.toRadians(90) + Math.toRadians(random.nextInt(10)); // 10* bias
      double steamAngularSpeed = 0.01;
      double ycoord = this.getY() + 0.15;
      // right
      ClientEffects.INSTANCE.steamEffect(
          this.getX() - Math.cos(rads + ninetyDeg) * offset, ycoord,
          this.getZ() - Math.sin(rads + ninetyDeg) * offset,
          steamAngularSpeed * Math.cos(rads - ninetyDeg),
          steamAngularSpeed * Math.sin(rads - ninetyDeg));
      // left
      ClientEffects.INSTANCE.steamEffect(
          this.getX() - Math.cos(rads + -ninetyDeg) * offset, ycoord,
          this.getZ() - Math.sin(rads + -ninetyDeg) * offset,
          steamAngularSpeed * Math.cos(rads + ninetyDeg),
          steamAngularSpeed * Math.sin(rads + ninetyDeg));
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

  @Override
  @Nullable
  public SteamBoiler getBoiler() {
    return this.boiler;
  }

  @Override
  public float getTemperature() {
    return this.boiler.getTemperature();
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
  public void addAdditionalSaveData(CompoundNBT data) {
    super.addAdditionalSaveData(data);
    this.getTankManager().writeTanksToNBT(data);
    data.put("boiler", this.boiler.serializeNBT());
    data.putString("processState", this.processState.getSerializedName());
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    this.getTankManager().readTanksFromNBT(data);
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
  public boolean canAcceptPushedFluid(AbstractMinecartEntity requester, FluidStack fluid) {
    return Fluids.WATER.isSame(fluid.getFluid());
  }

  @Override
  public boolean canProvidePulledFluid(AbstractMinecartEntity requester, FluidStack fluid) {
    return false;
  }

  @Override
  public void setFilling(boolean filling) {}

  @Override
  public void steamExplosion(FluidStack resource) {
    this.explode();
  }
}

