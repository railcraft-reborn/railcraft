package mods.railcraft.world.level.block.entity;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mods.railcraft.util.collections.StackKey;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.CompositeContainerAdaptor;
import mods.railcraft.util.container.ContainerAdaptor;
import mods.railcraft.util.container.ContainerManifest;
import mods.railcraft.util.container.ContainerManipulator;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.util.container.wrappers.ContainerMapper;
import mods.railcraft.world.inventory.ItemManipulatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class ItemManipulatorBlockEntity extends ManipulatorBlockEntity
    implements MenuProvider {

  protected static final Map<TransferMode, Predicate<ItemManipulatorBlockEntity>> modeHasWork =
      new EnumMap<>(TransferMode.class);
  protected static final int[] SLOTS = ContainerTools.buildSlotArray(0, 9);
  protected ContainerManipulator cart;

  static {
    modeHasWork.put(TransferMode.ALL, tile -> {
      ContainerManipulator dest = tile.getDestination();

      return tile.getSource().streamStacks()
          .filter(StackFilter.anyMatch(tile.getItemFilters()))
          .anyMatch(dest::willAccept);
    });

    modeHasWork.put(TransferMode.TRANSFER, tile -> {
      ContainerManifest filterManifest = ContainerManifest.create(tile.getItemFilters());
      ContainerManifest sourceManifest =
          ContainerManifest.create(tile.getSource(), filterManifest.keySet());
      ContainerManipulator dest = tile.getDestination();

      return sourceManifest.values().stream()
          .filter(entry -> dest.willAcceptAny(entry.stacks()))
          .anyMatch(entry -> tile.transferredItems.count(entry.key()) < filterManifest
              .count(entry.key()));
    });

    modeHasWork.put(TransferMode.STOCK, tile -> {
      ContainerManipulator dest = tile.getDestination();
      ContainerManifest filterManifest = ContainerManifest.create(tile.getItemFilters());
      ContainerManifest sourceManifest =
          ContainerManifest.create(tile.getSource(), filterManifest.keySet());
      ContainerManifest destManifest = ContainerManifest.create(dest, filterManifest.keySet());

      return sourceManifest.values().stream()
          .filter(entry -> dest.willAcceptAny(entry.stacks()))
          .anyMatch(entry -> destManifest.count(entry.key()) < filterManifest.count(entry.key()));
    });

    modeHasWork.put(TransferMode.EXCESS, tile -> {
      ContainerManipulator dest = tile.getDestination();
      ContainerManifest filterManifest = ContainerManifest.create(tile.getItemFilters());
      ContainerManifest sourceManifest =
          ContainerManifest.create(tile.getSource(), filterManifest.keySet());

      if (filterManifest.values().stream()
          .anyMatch(entry -> sourceManifest.count(entry.key()) > entry.count()))
        return true;

      ContainerManifest remainingManifest = ContainerManifest.create(tile.getSource());
      remainingManifest.keySet()
          .removeIf(stackKey -> StackFilter.anyMatch(tile.getItemFilters()).test(stackKey.get()));

      return remainingManifest.streamValueStacks().anyMatch(dest::willAccept);
    });
  }

  protected final CompositeContainerAdaptor chests = CompositeContainerAdaptor.create();
  protected final Multiset<StackKey> transferredItems = HashMultiset.create();
  protected final ContainerMapper bufferContainer;
  private final AdvancedContainer filtersContainer =
      new AdvancedContainer(9).callbackContainer(this).phantom();
  private TransferMode transferMode = TransferMode.ALL;

  protected ItemManipulatorBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
    this.setContainerSize(9);
    this.bufferContainer = ContainerMapper.make(this.getContainer()).ignoreItemChecks();
  }

  public abstract ContainerManipulator getSource();

  public abstract ContainerManipulator getDestination();

  public TransferMode getTransferMode() {
    return this.transferMode;
  }

  public void setTransferMode(TransferMode transferMode) {
    this.transferMode = transferMode;
  }

  public final AdvancedContainer getItemFilters() {
    return this.filtersContainer;
  }

  public abstract Slot getBufferSlot(int id, int x, int y);

  @Override
  protected void setPowered(boolean powered) {
    if (!this.isSendCartGateAction() && this.getRedstoneMode() == RedstoneMode.MANUAL) {
      super.setPowered(false);
      return;
    }
    super.setPowered(powered);
  }

  @Override
  protected void reset() {
    super.reset();
    this.transferredItems.clear();
  }

  @Override
  protected void processCart(AbstractMinecart cart) {
    this.chests.clear();
    this.chests.add(this.bufferContainer);
    this.chests.addAll(this.getAdjacentContainers());

    ContainerAdaptor cartInv = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
        this.getFacing().getOpposite()).map(ContainerAdaptor::of).orElse(null);
    if (cartInv == null) {
      sendCart(cart);
      return;
    }
    this.cart = cartInv;

    ContainerManifest filterManifest = ContainerManifest.create(getItemFilters());
    Stream<ContainerManifest.ManifestEntry> manifestStream = filterManifest.values().stream();
    switch (getTransferMode()) {
      case ALL: {
        if (filterManifest.isEmpty()) {
          ItemStack moved = getSource().moveOneItemTo(getDestination());
          itemMoved(moved);
        } else {
          moveItem(manifestStream);
        }
        break;
      }
      case TRANSFER: {
        moveItem(
            manifestStream.filter(entry -> transferredItems.count(entry.key()) < entry.count()));
        break;
      }
      case STOCK: {
        ContainerManifest destManifest =
            ContainerManifest.create(getDestination(), filterManifest.keySet());
        moveItem(manifestStream.filter(entry -> destManifest.count(entry.key()) < entry.count()));
        break;
      }
      case EXCESS: {
        ContainerManifest sourceManifest =
            ContainerManifest.create(getSource(), filterManifest.keySet());

        this.moveItem(
            manifestStream.filter(entry -> sourceManifest.count(entry.key()) > entry.count()));
        if (!isProcessing()) {
          Predicate<ItemStack> canMove = StackFilter.anyMatch(filterManifest.keyStacks()).negate();

          ItemStack moved = getSource().moveOneItemTo(this.getDestination(), canMove);
          this.itemMoved(moved);
        }
        break;
      }
    }
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    ContainerManipulator cartInv =
        cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
            this.getFacing().getOpposite()).map(ContainerAdaptor::of).orElse(null);
    if (cartInv == null || cartInv.slotCount() <= 0) {
      return false;
    }
    switch (this.getRedstoneMode()) {
      case IMMEDIATE:
        return false;
      case MANUAL:
        return true;
      case PARTIAL:
        if (cartInv.hasNoItems())
          return true;
      default:
        break;
    }
    this.cart = cartInv;
    return modeHasWork.get(this.getTransferMode()).test(this);
  }

  protected void moveItem(Stream<ContainerManifest.ManifestEntry> stream) {
    List<ItemStack> keys = stream.map(ContainerManifest.ManifestEntry::key)
        .map(StackKey::get)
        .collect(Collectors.toList());
    ItemStack moved =
        this.getSource().moveOneItemTo(this.getDestination(), StackFilter.anyMatch(keys));
    itemMoved(moved);
  }

  protected final void itemMoved(ItemStack moved) {
    if (!moved.isEmpty()) {
      this.setProcessing(true);
      this.transferredItems.add(StackKey.make(moved));
    }
  }

  @Override
  public boolean canHandleCart(AbstractMinecart cart) {
    return cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
        this.getFacing().getOpposite()).map(ContainerAdaptor::of)
        .map(inventory -> inventory.slotCount() > 0).orElse(false)
        && super.canHandleCart(cart);
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new ItemManipulatorMenu(id, inventory, this);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.transferMode);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.transferMode = data.readEnum(TransferMode.class);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("transferMode", this.transferMode.getSerializedName());
    tag.put("itemFilters", this.getItemFilters().serializeNBT());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.transferMode =
        TransferMode.getByName(tag.getString("transferMode")).orElse(TransferMode.ALL);
    this.getItemFilters().deserializeNBT(tag.getList("itemFilters", Tag.TAG_COMPOUND));
  }
}
