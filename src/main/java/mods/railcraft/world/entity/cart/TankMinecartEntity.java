package mods.railcraft.world.entity.cart;

import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidMinecart;
import mods.railcraft.util.inventory.IExtInvSlot;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.InventoryIterator;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TankMinecartMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.TankManager;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TankMinecartEntity extends FilteredMinecartEntity
    implements ISidedInventory, FluidMinecart {

  // Can't use FluidStack directly because its equals method doesn't consider amount so will never
  // sync if the amount is changed.
  private static final DataParameter<CompoundNBT> FLUID_STACK_TAG =
      EntityDataManager.defineId(TankMinecartEntity.class, DataSerializers.COMPOUND_TAG);
  private static final DataParameter<Boolean> FILLING =
      EntityDataManager.defineId(TankMinecartEntity.class, DataSerializers.BOOLEAN);
  private static final int SLOT_INPUT = 0;
  private static final int SLOT_PROCESSING = 1;
  private static final int SLOT_OUTPUT = 2;
  private static final int[] SLOTS = InvTools.buildSlotArray(0, 3);
  private final StandardTank tank =
      (StandardTank) new FilteredTank(RailcraftConfig.server.getTankCartFluidCapacity())
          .setUpdateCallback(tank -> this.fluidChanged(tank.getFluid()))
          .setValidator(fluidStack -> this.getFilterFluid()
              .map(fluidStack::isFluidEqual)
              .orElse(true));
  private final TankManager tankManager = new TankManager(this.tank);
  private final InventoryMapper invLiquids = InventoryMapper.make(this).ignoreItemChecks();
  private int fluidProcessingTimer;
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  public TankMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public TankMinecartEntity(ItemStack itemStack, double x, double y, double z, World level) {
    super(itemStack, RailcraftEntityTypes.TANK_MINECART.get(), x, y, z, level);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(FLUID_STACK_TAG, new CompoundNBT());
    this.entityData.define(FILLING, false);
  }

  private void fluidChanged(FluidStack fluidStack) {
    this.entityData.set(FLUID_STACK_TAG, fluidStack.writeToNBT(new CompoundNBT()));
  }

  @Override
  public void onSyncedDataUpdated(DataParameter<?> key) {
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
  public void remove() {
    super.remove();
    InventoryHelper.dropContents(this.level, this, this.invLiquids);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide()) {
      return;
    }

    InventoryIterator<IExtInvSlot> it = InventoryIterator.get(this);
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
  public ActionResultType interact(PlayerEntity player, Hand hand) {
    if (FluidTools.interactWithFluidHandler(player, hand, getTankManager())) {
      return ActionResultType.SUCCESS;
    }

    return super.interact(player, hand);
  }

  @Override
  public int getContainerSize() {
    return 3;
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    this.processState = FluidTools.ProcessState.getByName(data.getString("processState"))
        .orElse(FluidTools.ProcessState.RESET);
    this.tankManager.deserializeNBT(data.getList("tankManager", Constants.NBT.TAG_COMPOUND));
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT data) {
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

  public IInventory getInvLiquids() {
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
  public boolean canAcceptPushedFluid(AbstractMinecartEntity requester, FluidStack fluid) {
    return this.canPassFluidRequests(fluid);
  }

  @Override
  public boolean canProvidePulledFluid(AbstractMinecartEntity requester, FluidStack fluid) {
    return this.canPassFluidRequests(fluid);
  }

  @Override
  protected Item getItem() {
    return RailcraftItems.TANK_MINECART.get();
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return new TankMinecartMenu(id, playerInventory, this);
  }
}
