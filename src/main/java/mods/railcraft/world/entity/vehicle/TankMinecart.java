package mods.railcraft.world.entity.vehicle;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidTransferHandler;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TankMinecartMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.FluidItemHelper;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankMinecart extends FilteredMinecart
    implements WorldlyContainer, FluidTransferHandler {

  // Can't use FluidStack directly because its equals method doesn't consider amount so will never
  // sync if the amount is changed.
  private static final EntityDataAccessor<CompoundTag> FLUID_STACK_TAG =
      SynchedEntityData.defineId(TankMinecart.class, EntityDataSerializers.COMPOUND_TAG);
  private static final EntityDataAccessor<Boolean> FILLING =
      SynchedEntityData.defineId(TankMinecart.class, EntityDataSerializers.BOOLEAN);
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_PROCESSING = 1;
  public static final int SLOT_OUTPUT = 2;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 3);
  private final StandardTank tank =
      StandardTank
          .ofBuckets(RailcraftConfig.SERVER.tankCartFluidCapacity.get())
          .changeCallback(this::tankChanged)
          .setValidator(fluidStack -> this.getFilterFluid()
              .map(fluidStack::isFluidEqual)
              .orElse(true));
  private final LazyOptional<IFluidHandler> fluidHandlerCapability =
      LazyOptional.of(() -> this.tank);
  private final ContainerMapper invLiquids = ContainerMapper.make(this).ignoreItemChecks();
  private int fluidProcessingTimer;
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  public TankMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public TankMinecart(ItemStack itemStack, double x, double y, double z, Level level) {
    super(itemStack, RailcraftEntityTypes.TANK_MINECART.get(), x, y, z, level);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(FLUID_STACK_TAG, new CompoundTag());
    this.entityData.define(FILLING, false);
  }

  private void tankChanged() {
    this.entityData.set(FLUID_STACK_TAG, this.tank.getFluid().writeToNBT(new CompoundTag()));
  }

  @Override
  public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
    super.onSyncedDataUpdated(key);

    if (!this.level().isClientSide()) {
      return;
    }
    if (key.equals(FLUID_STACK_TAG)) {
      this.tank.setFluid(FluidStack.loadFluidStackFromNBT(this.entityData.get(FLUID_STACK_TAG)));
    }
  }

  public StandardTank getTankManager() {
    return this.tank;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    return capability == ForgeCapabilities.FLUID_HANDLER
        ? this.fluidHandlerCapability.cast()
        : super.getCapability(capability, facing);
  }

  @Override
  public void remove(RemovalReason reason) {
    super.remove(reason);
    Containers.dropContents(this.level(), this, this.invLiquids);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level().isClientSide()) {
      return;
    }

    ContainerTools.dropIfInvalid(this.level(), this.blockPosition(), this, SLOT_INPUT);
    ContainerTools.drop(this.level(), this.blockPosition(), this, SLOT_PROCESSING,
        FluidItemHelper::isContainer);
    ContainerTools.drop(this.level(), this.blockPosition(), this, SLOT_OUTPUT,
        FluidItemHelper::isContainer);

    if (this.fluidProcessingTimer++ >= FluidTools.BUCKET_FILL_TIME) {
      this.fluidProcessingTimer = 0;
      this.processState = FluidTools.processContainer(
          this.invLiquids, this.tank, FluidTools.ProcessType.DRAIN_THEN_FILL, this.processState);
    }
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    if (FluidTools.interactWithFluidHandler(player, hand, getTankManager())) {
      return InteractionResult.SUCCESS;
    }

    return super.interact(player, hand);
  }

  @Override
  public int getContainerSize() {
    return 3;
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.processState = FluidTools.ProcessState.fromTag(tag);
    this.tank.readFromNBT(tag.getCompound("tank"));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putString("processState", this.processState.getSerializedName());
    var tankTag = new CompoundTag();
    this.tank.writeToNBT(tankTag);
    tag.put("tank", tankTag);
  }

  public boolean isFilling() {
    return this.entityData.get(FILLING);
  }

  @Override
  public void setFilling(boolean filling) {
    this.entityData.set(FILLING, filling);
  }

  public Optional<FluidStack> getFilterFluid() {
    return FluidUtil.getFluidContained(this.getFilterItem());
  }

  public Container getInvLiquids() {
    return this.invLiquids;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return slot == SLOT_INPUT && FluidItemHelper.isContainer(stack);
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
    return this.canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
    return slot == SLOT_OUTPUT;
  }

  @Override
  public boolean canPassFluidRequests(FluidStack fluid) {
    return this.getFilterFluid()
        .map(filter -> filter.isFluidEqual(fluid))
        .orElseGet(() -> this.tank.isEmpty() && tank.getFluid().isFluidEqual(fluid));
  }

  @Override
  public boolean canAcceptPushedFluid(RollingStock requester, FluidStack fluid) {
    return this.canPassFluidRequests(fluid);
  }

  @Override
  public boolean canProvidePulledFluid(RollingStock requester, FluidStack fluid) {
    return this.canPassFluidRequests(fluid);
  }

  @Override
  public ItemStack getPickResult() {
    return RailcraftItems.TANK_MINECART.get().getDefaultInstance();
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.TANK_MINECART.get();
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new TankMinecartMenu(id, playerInventory, this);
  }
}
