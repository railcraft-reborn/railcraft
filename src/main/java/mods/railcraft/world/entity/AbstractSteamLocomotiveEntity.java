package mods.railcraft.world.entity;

import javax.annotation.Nullable;
import mods.railcraft.NBTPlugin;
import mods.railcraft.Railcraft;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * @author CovertJaguar <http://www.railcraft.info>
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

  private final SteamBoiler boiler;
  protected final StandardTank waterTank = new FilteredTank(FluidTools.BUCKET_VOLUME * 6) {
    @Override
    public int fill(@Nullable FluidStack resource, FluidAction doFill) {
      return super.fill(onFillWater(resource), doFill);
    }
  }.setFilterFluid(() -> Fluids.WATER);

  protected final StandardTank tankSteam = new FilteredTank(FluidTools.BUCKET_VOLUME * 16)
    .setFilterFluid(RailcraftFluids.STEAM)
    .disableDrain()
    .disableFill();
  // FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt)
  protected final InventoryMapper invWaterInput =
    InventoryMapper.make(this, SLOT_WATER_INPUT, 1)
      .withStackSizeLimit(4);
  protected final InventoryMapper invWaterOutput = InventoryMapper.make(this, SLOT_WATER_OUTPUT, 1);
  protected final InventoryMapper invWaterContainers = InventoryMapper.make(this, SLOT_WATER_INPUT, 3);

  private final LazyOptional<TankManager> tankManager = LazyOptional.of(TankManager::new);

  private int update = this.random.nextInt();

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  public AbstractSteamLocomotiveEntity(EntityType<?> type, World world) {
    super(type, world);
    setMaxReverseSpeed(Speed.SLOWEST);

    this.getTankManager().add(waterTank);
    this.getTankManager().add(tankSteam);

    boiler = new SteamBoiler(waterTank, tankSteam)
      .setEfficiencyModifier(Railcraft.serverConfig.fuelPerSteamMultiplier.get())
      .setTicksPerCycle(TICKS_PER_BOILER_CYCLE);
  }

  public AbstractSteamLocomotiveEntity(EntityType<?> type, double x, double y, double z,
      World world) {
    super(type, x, y, z, world);
    setMaxReverseSpeed(Speed.SLOWEST);

    this.getTankManager().add(waterTank);
    this.getTankManager().add(tankSteam);

    boiler = new SteamBoiler(waterTank, tankSteam)
      .setEfficiencyModifier(Railcraft.serverConfig.fuelPerSteamMultiplier.get())
      .setTicksPerCycle(TICKS_PER_BOILER_CYCLE);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(SMOKE, false);
    this.entityData.define(STEAM, false);
  }

  @Override
  public SoundEvent getWhistle() {
    return RailcraftSoundEvents.STEAM_WHISTLE.get();
  }

  @Override
  public ActionResultType interact(PlayerEntity player, Hand hand) {
    if (FluidTools.interactWithFluidHandler(player, hand, getTankManager())) {
      return ActionResultType.SUCCESS;
    }
    return super.interact(player, hand);
  }

  public TankManager getTankManager() {
    return tankManager.orElseThrow(() -> new IllegalStateException("Expected tank manager"));
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
      return this.tankManager.cast();
    return super.getCapability(capability, facing);
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.level.isClientSide()) {
      update++;

      if (waterTank.isEmpty())
        setMode(Mode.SHUTDOWN);
      setSteaming(tankSteam.getFluidAmount() > 0);

      if (tankSteam.getRemainingSpace() >= SteamConstants.STEAM_PER_UNIT_WATER || isShutdown()) {
        boiler.tick(1);

        setSmoking(boiler.isBurning());

        if (!boiler.isBurning()) // Todo: make venting a toggleable thing (why autodump while train has no coal??)
          ventSteam();
      }

      if (update % FluidTools.BUCKET_FILL_TIME == 0)
        processState = FluidTools.processContainer(invWaterContainers, waterTank,
            ProcessType.DRAIN_ONLY, processState);
      return;
    }
    if (isSmoking()) {
      double rads = renderYaw * Math.PI / 180D;
      float offset = 0.4f;
      ClientEffects.INSTANCE.locomotiveEffect(
          this.getX() - Math.cos(rads) * offset, this.getY() + 1.5f,
          this.getZ() - Math.sin(rads) * offset);
    }
    // steam spawns ON the engine itself, spreading left or right like
    // as steam engines are on the sides of the train
    if (isSteaming()){
      double rads = renderYaw * Math.PI / 180D;
      float offset = 0.8f;
      ClientEffects.INSTANCE.steamEffect(
        this.getX() - Math.cos(rads) * offset, this.getY() + 1.8f,
        this.getZ() - Math.sin(rads) * offset);
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
    tankSteam.internalDrain(4, FluidAction.EXECUTE);
  }

  @Override
  @Nullable
  public SteamBoiler getBoiler() {
    return this.boiler;
  }

  @Override
  public float getTemperature() {
    return (float) this.boiler.getTemperature();
  }

  @Override
  public int getMoreGoJuice() {
    FluidStack steam = tankSteam.getFluid();
    if (steam == FluidStack.EMPTY) {
      return 0;
    }
    if (steam.getAmount() >= tankSteam.getCapacity() / 2) {
      tankSteam.internalDrain(SteamConstants.STEAM_PER_UNIT_WATER, FluidAction.EXECUTE);
      return FUEL_PER_REQUEST;
    }
    return 0;
  }

  @Override
  public void addAdditionalSaveData(CompoundNBT data) {
    super.addAdditionalSaveData(data);
    getTankManager().writeTanksToNBT(data);
    boiler.writeToNBT(data);
    NBTPlugin.writeEnumOrdinal(data, "processState", processState);
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    getTankManager().readTanksFromNBT(data);
    boiler.readFromNBT(data);
    processState = NBTPlugin.readEnumOrdinal(data, "processState", FluidTools.ProcessState.values(),
        FluidTools.ProcessState.RESET);
  }

  public boolean isSafeToFill() {
    return !boiler.isSuperHeated() || !waterTank.isEmpty();
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
    explode();
  }
}

