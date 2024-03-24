package mods.railcraft.world.level.block.entity.manipulator;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.inventory.FluidManipulatorMenu;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.TankManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


public abstract class FluidManipulatorBlockEntity extends ManipulatorBlockEntity
    implements WorldlyContainer, MenuProvider {

  protected static final int SLOT_INPUT = 0;
  protected static final int SLOT_PROCESSING = 1;
  protected static final int SLOT_OUTPUT = 2;
  protected static final int[] SLOTS = ContainerTools.buildSlotArray(0, 3);

  protected final AdvancedContainer fluidFilterContainer =
      new AdvancedContainer(1).listener((Container) this).phantom();
  protected final TankManager tankManager = new TankManager();
  protected final StandardTank tank = StandardTank.ofBuckets(32);
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int fluidProcessingTimer;

  protected FluidManipulatorBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
    this.setContainerSize(3);
    this.tankManager.add(this.tank);
    this.tank.setValidator(
        fluidStack -> this.getFilterFluid().map(fluidStack::isFluidEqual).orElse(true));
    this.tank.changeCallback(this::tankChanged);
  }

  protected void tankChanged() {
    this.syncToClient();
    this.setChanged();
  }

  public TankManager getTankManager() {
    return this.tankManager;
  }

  public AdvancedContainer getFluidFilter() {
    return this.fluidFilterContainer;
  }

  public Optional<FluidStack> getFilterFluid() {
    return FluidUtil.getFluidContained(this.fluidFilterContainer.getItem(0));
  }

  public FluidStack getFluidHandled() {
    return this.getFilterFluid().orElseGet(this.tank::getFluid);
  }

  @Nullable
  protected static IFluidHandler getFluidHandler(AbstractMinecart cart, Direction direction) {
    return cart.getCapability(Capabilities.FluidHandler.ENTITY, direction);
  }

  public boolean use(Player player, InteractionHand hand) {
    return FluidTools.interactWithFluidHandler(player, hand, this.tank);
  }

  @Override
  public boolean canHandleCart(AbstractMinecart cart) {
    return cart
        .getCapability(Capabilities.FluidHandler.ENTITY, this.getFacing().getOpposite()) != null
        && super.canHandleCart(cart);
  }

  @Override
  protected void upkeep() {
    super.upkeep();

    ContainerTools.dropIfInvalid(this.level, this.getBlockPos(), this, SLOT_INPUT);
    ContainerTools.drop(this.level, this.getBlockPos(), this, SLOT_PROCESSING,
        FluidTools::isFluidHandler);
    ContainerTools.drop(this.level, this.getBlockPos(), this, SLOT_OUTPUT,
        FluidTools::isFluidHandler);

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
      if (!FluidTools.isFluidHandler(stack)) {
        return false;
      }
      if (FluidTools.isEmptyContainer(stack)) {
        return true;
      }
      return this.getFilterFluid().map(FluidStack::getFluid)
          .map(fluid -> FluidTools.containsFluid(stack, fluid))
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
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new FluidManipulatorMenu(id, inventory, this);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString(CompoundTagKeys.PROCESS_STATE, this.processState.getSerializedName());
    tag.put(CompoundTagKeys.TANK_MANAGER, this.tankManager.serializeNBT());
    tag.put(CompoundTagKeys.INV_FILTER, this.getFluidFilter().createTag());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.processState = FluidTools.ProcessState.fromTag(tag);
    this.tankManager.deserializeNBT(tag.getList(CompoundTagKeys.TANK_MANAGER, Tag.TAG_COMPOUND));
    this.getFluidFilter().fromTag(tag.getList(CompoundTagKeys.INV_FILTER, Tag.TAG_COMPOUND));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.tankManager.writePacketData(data);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.tankManager.readPacketData(data);
  }

  public IFluidHandler getFluidCap(Direction side) {
    return this.tankManager;
  }
}
