package mods.railcraft.world.level.block.entity.manipulator;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.SlotAccessor;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.ItemStackKey;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerManifest;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.inventory.ItemManipulatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public abstract class ItemManipulatorBlockEntity extends ManipulatorBlockEntity
    implements MenuProvider {

  protected static final Map<TransferMode, Predicate<ItemManipulatorBlockEntity>> modeHasWork =
      new EnumMap<>(TransferMode.class);
  protected ContainerManipulator<?> cart;

  static {
    modeHasWork.put(TransferMode.ALL, tile -> {
      var dest = tile.getDestination();

      return tile.getSource().streamItems()
          .filter(StackFilter.anyMatch(tile.getItemFilters()))
          .anyMatch(dest::willAccept);
    });

    modeHasWork.put(TransferMode.TRANSFER, tile -> {
      var filterManifest = ContainerManifest.create(tile.getItemFilters());
      var sourceManifest = ContainerManifest.create(tile.getSource(), filterManifest.keySet());
      var dest = tile.getDestination();

      return sourceManifest.values().stream()
          .filter(entry -> dest.willAcceptAny(entry.stacks()))
          .anyMatch(entry -> tile.transferredItems.count(entry.key()) < filterManifest
              .count(entry.key()));
    });

    modeHasWork.put(TransferMode.STOCK, tile -> {
      var dest = tile.getDestination();
      var filterManifest = ContainerManifest.create(tile.getItemFilters());
      var sourceManifest = ContainerManifest.create(tile.getSource(), filterManifest.keySet());
      var destManifest = ContainerManifest.create(dest, filterManifest.keySet());

      return sourceManifest.values().stream()
          .filter(entry -> dest.willAcceptAny(entry.stacks()))
          .anyMatch(entry -> destManifest.count(entry.key()) < filterManifest.count(entry.key()));
    });

    modeHasWork.put(TransferMode.EXCESS, tile -> {
      var dest = tile.getDestination();
      var filterManifest = ContainerManifest.create(tile.getItemFilters());
      var sourceManifest = ContainerManifest.create(tile.getSource(), filterManifest.keySet());

      if (filterManifest.values().stream()
          .anyMatch(entry -> sourceManifest.count(entry.key()) > entry.count())) {
        return true;
      }

      var remainingManifest = ContainerManifest.create(tile.getSource());
      remainingManifest.keySet()
          .removeIf(stackKey -> StackFilter.anyMatch(tile.getItemFilters()).test(stackKey.itemStack()));

      return remainingManifest.streamValueStacks().anyMatch(dest::willAccept);
    });
  }

  protected ContainerManipulator<? extends SlotAccessor> chests = ContainerManipulator.empty();
  protected final Multiset<ItemStackKey> transferredItems = HashMultiset.create();
  protected final ContainerMapper bufferContainer;
  private final AdvancedContainer filtersContainer =
      new AdvancedContainer(9).listener((Container) this).phantom();
  private TransferMode transferMode = TransferMode.ALL;

  protected ItemManipulatorBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
    this.setContainerSize(9);
    this.bufferContainer = ContainerMapper.make(this.container()).ignoreItemChecks();
  }

  public abstract ContainerManipulator<?> getSource();

  public abstract ContainerManipulator<?> getDestination();

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

  @Nullable
  protected static IItemHandler getCartItemHandler(AbstractMinecart cart, Direction direction) {
    return cart.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, direction);
  }

  @Override
  protected void processCart(AbstractMinecart cart) {
    this.chests = ContainerManipulator.of(this.bufferContainer, this.findAdjacentContainers());

    var cartInv = getCartItemHandler(cart, this.getFacing().getOpposite());
    if (cartInv == null) {
      sendCart(cart);
      return;
    }
    this.cart = ContainerManipulator.of(cartInv);

    var filterManifest = ContainerManifest.create(this.getItemFilters());
    var manifestStream = filterManifest.values().stream();
    switch (this.getTransferMode()) {
      case ALL: {
        if (filterManifest.isEmpty()) {
          ItemStack moved = this.getSource().moveOneItemTo(this.getDestination());
          itemMoved(moved);
        } else {
          this.moveItem(manifestStream);
        }
        break;
      }
      case TRANSFER: {
        this.moveItem(
            manifestStream.filter(entry -> transferredItems.count(entry.key()) < entry.count()));
        break;
      }
      case STOCK: {
        var destManifest = ContainerManifest.create(this.getDestination(), filterManifest.keySet());
        this.moveItem(manifestStream.filter(entry -> destManifest.count(entry.key()) < entry.count()));
        break;
      }
      case EXCESS: {
        var sourceManifest = ContainerManifest.create(this.getSource(), filterManifest.keySet());

        this.moveItem(
            manifestStream.filter(entry -> sourceManifest.count(entry.key()) > entry.count()));
        if (!this.isProcessing()) {
          Predicate<ItemStack> canMove = StackFilter.anyMatch(filterManifest.keyStacks()).negate();

          ItemStack moved = this.getSource().moveOneItemTo(this.getDestination(), canMove);
          this.itemMoved(moved);
        }
        break;
      }
    }
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    var itemHandler = getCartItemHandler(cart, this.getFacing().getOpposite());
    if (itemHandler == null) {
      return false;
    }
    var cartInv = ContainerManipulator.of(itemHandler);
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
    var keys = stream.map(ContainerManifest.ManifestEntry::key)
        .map(ItemStackKey::copyStack)
        .toList();
    var moved = this.getSource().moveOneItemTo(this.getDestination(), StackFilter.anyMatch(keys));
    this.itemMoved(moved);
  }

  protected final void itemMoved(ItemStack moved) {
    if (!moved.isEmpty()) {
      this.setProcessing(true);
      this.transferredItems.add(ItemStackKey.make(moved));
    }
  }

  @Override
  public boolean canHandleCart(AbstractMinecart cart) {
    return Optional.ofNullable(getCartItemHandler(cart, this.getFacing().getOpposite()))
        .map(inventory -> inventory.getSlots() > 0).orElse(false)
        && super.canHandleCart(cart);
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new ItemManipulatorMenu(id, inventory, this);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.transferMode);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.transferMode = data.readEnum(TransferMode.class);
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.store(CompoundTagKeys.TRANSFER_MODE, TransferMode.CODEC, this.transferMode);
    tag.put(CompoundTagKeys.ITEM_FILTERS, this.getItemFilters().createTag(provider));
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.transferMode =
        tag.read(CompoundTagKeys.TRANSFER_MODE, TransferMode.CODEC).orElse(TransferMode.ALL);
    tag.getList(CompoundTagKeys.ITEM_FILTERS).ifPresent(listTag -> {
      this.getItemFilters().fromTag(listTag, provider);
    });
  }
}
