package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.util.inventory.IExtInvSlot;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.InventoryAdvanced;
import mods.railcraft.util.inventory.InventoryIterator;
import mods.railcraft.world.inventory.FluidManipulatorMenu;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.TankManager;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class FluidManipulatorBlockEntity extends ManipulatorBlockEntity
    implements ISidedInventory, INamedContainerProvider {

  protected static final int SLOT_INPUT = 0;
  protected static final int SLOT_PROCESSING = 1;
  protected static final int SLOT_OUTPUT = 2;
  protected static final int[] SLOTS = InvTools.buildSlotArray(0, 3);
  protected static final int CAPACITY = FluidTools.BUCKET_VOLUME * 32;

  protected final InventoryAdvanced fluidFilterInventory =
      new InventoryAdvanced(1).callbackInv(this).phantom();
  protected final TankManager tankManager = new TankManager();
  private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> this.tankManager);
  protected final StandardTank tank = new FilteredTank(CAPACITY);
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int fluidProcessingTimer;

  protected FluidManipulatorBlockEntity(TileEntityType<?> type) {
    super(type);
    this.setInventorySize(3);
    this.tankManager.add(this.tank);
    this.tank.setValidator(
        fluidStack -> this.getFilterFluid().map(fluidStack::isFluidEqual).orElse(true));
    this.tank.setUpdateCallback(__ -> this.syncToClient());
  }

  public TankManager getTankManager() {
    return this.tankManager;
  }

  public InventoryAdvanced getFluidFilter() {
    return this.fluidFilterInventory;
  }

  public Optional<FluidStack> getFilterFluid() {
    return this.fluidFilterInventory.getItem(0).isEmpty()
        ? Optional.empty()
        : FluidItemHelper.getFluidStackInContainer(this.fluidFilterInventory.getItem(0));
  }

  public FluidStack getFluidHandled() {
    return this.getFilterFluid().orElseGet(this.tank::getFluid);
  }

  @Nullable
  protected static IFluidHandler getFluidHandler(AbstractMinecartEntity cart, Direction direction) {
    return cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction)
        .orElse(null);
  }

  public boolean use(PlayerEntity player, Hand hand) {
    boolean success = FluidTools.interactWithFluidHandler(player, hand, this.tank);
    if (success && !this.level.isClientSide()) {
      this.syncToClient();
    }
    return success;
  }

  @Override
  public boolean canHandleCart(AbstractMinecartEntity cart) {
    return cart
        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
            this.getFacing().getOpposite())
        .isPresent()
        && super.canHandleCart(cart);
  }

  @Override
  protected void upkeep() {
    super.upkeep();

    InventoryIterator<IExtInvSlot> it = InventoryIterator.get(this);
    it.slot(SLOT_INPUT).validate(this.level, this.getBlockPos());
    it.slot(SLOT_PROCESSING).validate(this.level, this.getBlockPos(), FluidItemHelper::isContainer);
    it.slot(SLOT_OUTPUT).validate(this.level, this.getBlockPos(), FluidItemHelper::isContainer);

    if (this.fluidProcessingTimer++ >= FluidTools.BUCKET_FILL_TIME) {
      this.fluidProcessingTimer = 0;
      this.processState =
          FluidTools.processContainer(this, tank, this.getProcessType(), this.processState);
    }
  }

  protected abstract FluidTools.ProcessType getProcessType();

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    if (slot == SLOT_INPUT) {
      if (!FluidItemHelper.isContainer(stack)) {
        return false;
      }
      if (FluidItemHelper.isEmptyContainer(stack)) {
        return true;
      }
      return this.getFilterFluid().map(FluidStack::getFluid)
          .map(fluid -> FluidItemHelper.containsFluid(stack, fluid))
          .orElse(true);
    }
    return false;
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
    return this.canPlaceItem(index, itemStackIn);
  }

  @Override
  public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
    return index == SLOT_OUTPUT;
  }

  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
    return new FluidManipulatorMenu(this, id, inventory);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putString("processState", this.processState.getSerializedName());
    data.put("tankManager", this.tankManager.serializeNBT());
    data.put("invFilter", this.getFluidFilter().serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.processState = FluidTools.ProcessState.getByName(data.getString("processState"))
        .orElse(FluidTools.ProcessState.RESET);
    this.tankManager.deserializeNBT(data.getList("tankManager", Constants.NBT.TAG_COMPOUND));
    this.getFluidFilter().deserializeNBT(data.getList("invFilter", Constants.NBT.TAG_COMPOUND));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.tankManager.writePacketData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.tankManager.readPacketData(data);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
        ? this.fluidHandler.cast()
        : super.getCapability(capability, facing);
  }
}
