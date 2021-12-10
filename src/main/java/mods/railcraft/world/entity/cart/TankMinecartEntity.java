package mods.railcraft.world.entity.cart;

import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidMinecart;
import mods.railcraft.util.container.ModifiableContainerSlot;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.ContainerIterator;
import mods.railcraft.util.container.wrappers.ContainerMapper;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TankMinecartMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.TankManager;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TankMinecartEntity extends FilteredMinecartEntity
    implements WorldlyContainer, FluidMinecart {

  // Can't use FluidStack directly because its equals method doesn't consider amount so will never
  // sync if the amount is changed.
  private static final EntityDataAccessor<CompoundTag> FLUID_STACK_TAG =
      SynchedEntityData.defineId(TankMinecartEntity.class, EntityDataSerializers.COMPOUND_TAG);
  private static final EntityDataAccessor<Boolean> FILLING =
      SynchedEntityData.defineId(TankMinecartEntity.class, EntityDataSerializers.BOOLEAN);
  private static final int SLOT_INPUT = 0;
  private static final int SLOT_PROCESSING = 1;
  private static final int SLOT_OUTPUT = 2;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 3);
  private final StandardTank tank =
      (StandardTank) new FilteredTank(RailcraftConfig.server.getTankCartFluidCapacity())
          .setUpdateCallback(tank -> this.fluidChanged(tank.getFluid()))
          .setValidator(fluidStack -> this.getFilterFluid()
              .map(fluidStack::isFluidEqual)
              .orElse(true));
  private final TankManager tankManager = new TankManager(this.tank);
  private final ContainerMapper invLiquids = ContainerMapper.make(this).ignoreItemChecks();
  private int fluidProcessingTimer;
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  public TankMinecartEntity(EntityType<?> type, Level world) {
    super(type, world);
  }

  public TankMinecartEntity(ItemStack itemStack, double x, double y, double z, Level level) {
    super(itemStack, RailcraftEntityTypes.TANK_MINECART.get(), x, y, z, level);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(FLUID_STACK_TAG, new CompoundTag());
    this.entityData.define(FILLING, false);
  }

  private void fluidChanged(FluidStack fluidStack) {
    this.entityData.set(FLUID_STACK_TAG, fluidStack.writeToNBT(new CompoundTag()));
  }

  @Override
  public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
    super.onSyncedDataUpdated(key);

    if (!this.level.isClientSide()) {
      return;
    }
    if (key.equals(FLUID_STACK_TAG)) {
      this.tank.setFluid(FluidStack.loadFluidStackFromNBT(this.entityData.get(FLUID_STACK_TAG)));
    }
  }

  public TankManager getTankManager() {
    return this.tankManager;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
        ? LazyOptional.of(() -> this.tankManager).cast()
        : super.getCapability(capability, facing);
  }

  @Override
  public void remove(RemovalReason reason) {
    super.remove(reason);
    Containers.dropContents(this.level, this, this.invLiquids);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide()) {
      return;
    }

    ContainerIterator<ModifiableContainerSlot> it = ContainerIterator.get(this);
    it.slot(SLOT_INPUT).validate(this.level, this.blockPosition());
    it.slot(SLOT_PROCESSING).validate(this.level, this.blockPosition(),
        FluidItemHelper::isContainer);
    it.slot(SLOT_OUTPUT).validate(this.level, this.blockPosition(),
        FluidItemHelper::isContainer);

    if (this.fluidProcessingTimer++ >= FluidTools.BUCKET_FILL_TIME) {
      this.fluidProcessingTimer = 0;
      this.processState = FluidTools.processContainer(
          this.invLiquids, this.tank, FluidTools.ProcessType.DRAIN_ONLY, this.processState);
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
  protected void readAdditionalSaveData(CompoundTag data) {
    super.readAdditionalSaveData(data);
    this.processState = FluidTools.ProcessState.getByName(data.getString("processState"))
        .orElse(FluidTools.ProcessState.RESET);
    this.tankManager.deserializeNBT(data.getList("tankManager", Tag.TAG_COMPOUND));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    data.putString("processState", this.processState.getSerializedName());
    data.put("tankManager", this.tankManager.serializeNBT());
  }

  public boolean isFilling() {
    return this.entityData.get(FILLING);
  }

  @Override
  public void setFilling(boolean filling) {
    this.entityData.set(FILLING, filling);
  }

  public Optional<FluidStack> getFilterFluid() {
    ItemStack filter = this.getFilterItem();
    return filter.isEmpty() ? Optional.empty() : FluidItemHelper.getFluidStackInContainer(filter);
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
  public boolean canAcceptPushedFluid(AbstractMinecart requester, FluidStack fluid) {
    return this.canPassFluidRequests(fluid);
  }

  @Override
  public boolean canProvidePulledFluid(AbstractMinecart requester, FluidStack fluid) {
    return this.canPassFluidRequests(fluid);
  }

  @Override
  protected Item getItem() {
    return RailcraftItems.TANK_MINECART.get();
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new TankMinecartMenu(id, playerInventory, this);
  }
}
