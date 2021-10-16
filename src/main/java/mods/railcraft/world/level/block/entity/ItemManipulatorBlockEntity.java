package mods.railcraft.world.level.block.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mods.railcraft.util.collections.StackKey;
import mods.railcraft.util.inventory.IInventoryManipulator;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.InventoryAdaptor;
import mods.railcraft.util.inventory.InventoryAdvanced;
import mods.railcraft.util.inventory.InventoryComposite;
import mods.railcraft.util.inventory.InventoryManifest;
import mods.railcraft.util.inventory.filters.StackFilters;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.inventory.ItemManipulatorMenu;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class ItemManipulatorBlockEntity extends ManipulatorBlockEntity
    implements INamedContainerProvider {

  protected static final Map<TransferMode, Predicate<ItemManipulatorBlockEntity>> modeHasWork =
      new EnumMap<>(TransferMode.class);
  protected static final int[] SLOTS = InvTools.buildSlotArray(0, 9);
  protected IInventoryManipulator cart;

  static {
    modeHasWork.put(TransferMode.ALL, tile -> {
      IInventoryManipulator dest = tile.getDestination();

      return tile.getSource().streamStacks()
          .filter(StackFilters.anyMatch(tile.getItemFilters()))
          .anyMatch(dest::willAccept);
    });

    modeHasWork.put(TransferMode.TRANSFER, tile -> {
      InventoryManifest filterManifest = InventoryManifest.create(tile.getItemFilters());
      InventoryManifest sourceManifest =
          InventoryManifest.create(tile.getSource(), filterManifest.keySet());
      IInventoryManipulator dest = tile.getDestination();

      return sourceManifest.values().stream()
          .filter(entry -> dest.willAcceptAny(entry.stacks()))
          .anyMatch(entry -> tile.transferredItems.count(entry.key()) < filterManifest
              .count(entry.key()));
    });

    modeHasWork.put(TransferMode.STOCK, tile -> {
      IInventoryManipulator dest = tile.getDestination();
      InventoryManifest filterManifest = InventoryManifest.create(tile.getItemFilters());
      InventoryManifest sourceManifest =
          InventoryManifest.create(tile.getSource(), filterManifest.keySet());
      InventoryManifest destManifest = InventoryManifest.create(dest, filterManifest.keySet());

      return sourceManifest.values().stream()
          .filter(entry -> dest.willAcceptAny(entry.stacks()))
          .anyMatch(entry -> destManifest.count(entry.key()) < filterManifest.count(entry.key()));
    });

    modeHasWork.put(TransferMode.EXCESS, tile -> {
      IInventoryManipulator dest = tile.getDestination();
      InventoryManifest filterManifest = InventoryManifest.create(tile.getItemFilters());
      InventoryManifest sourceManifest =
          InventoryManifest.create(tile.getSource(), filterManifest.keySet());

      if (filterManifest.values().stream()
          .anyMatch(entry -> sourceManifest.count(entry.key()) > entry.count()))
        return true;

      InventoryManifest remainingManifest = InventoryManifest.create(tile.getSource());
      remainingManifest.keySet()
          .removeIf(stackKey -> StackFilters.anyMatch(tile.getItemFilters()).test(stackKey.get()));

      return remainingManifest.streamValueStacks().anyMatch(dest::willAccept);
    });
  }

  protected final InventoryComposite chests = InventoryComposite.create();
  protected final Multiset<StackKey> transferredItems = HashMultiset.create();
  protected final InventoryMapper bufferInventory;
  private final InventoryAdvanced invFilters = new InventoryAdvanced(9).callbackInv(this).phantom();
  private TransferMode transferMode = TransferMode.ALL;

  protected ItemManipulatorBlockEntity(TileEntityType<?> type) {
    super(type);
    setInventorySize(9);
    bufferInventory = InventoryMapper.make(getInventory()).ignoreItemChecks();
  }

  public abstract IInventoryManipulator getSource();

  public abstract IInventoryManipulator getDestination();

  public TransferMode getTransferMode() {
    return this.transferMode;
  }

  public void setTransferMode(TransferMode transferMode) {
    this.transferMode = transferMode;
  }

  public final InventoryAdvanced getItemFilters() {
    return this.invFilters;
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

  protected Collection<InventoryAdaptor> getAdjacentInventories() {
    List<InventoryAdaptor> containers = new ArrayList<>();
    for (Direction direction : Direction.values()) {
      TileEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity != null) {
        blockEntity
            .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite())
            .map(InventoryAdaptor::of)
            .ifPresent(containers::add);
      }
    }
    return containers;
  }

  @Override
  protected void processCart(AbstractMinecartEntity cart) {
    this.chests.clear();
    this.chests.add(this.bufferInventory);
    this.chests.addAll(this.getAdjacentInventories());

    InventoryAdaptor cartInv = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
        this.getFacing().getOpposite()).map(InventoryAdaptor::of).orElse(null);
    if (cartInv == null) {
      sendCart(cart);
      return;
    }
    this.cart = cartInv;

    InventoryManifest filterManifest = InventoryManifest.create(getItemFilters());
    Stream<InventoryManifest.ManifestEntry> manifestStream = filterManifest.values().stream();
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
        InventoryManifest destManifest =
            InventoryManifest.create(getDestination(), filterManifest.keySet());
        moveItem(manifestStream.filter(entry -> destManifest.count(entry.key()) < entry.count()));
        break;
      }
      case EXCESS: {
        InventoryManifest sourceManifest =
            InventoryManifest.create(getSource(), filterManifest.keySet());

        this.moveItem(
            manifestStream.filter(entry -> sourceManifest.count(entry.key()) > entry.count()));
        if (!isProcessing()) {
          Predicate<ItemStack> canMove = StackFilters.anyMatch(filterManifest.keyStacks()).negate();

          ItemStack moved = getSource().moveOneItemTo(this.getDestination(), canMove);
          this.itemMoved(moved);
        }
        break;
      }
    }
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecartEntity cart) {
    IInventoryManipulator cartInv =
        cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
            this.getFacing().getOpposite()).map(InventoryAdaptor::of).orElse(null);
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

  protected void moveItem(Stream<InventoryManifest.ManifestEntry> stream) {
    List<ItemStack> keys = stream.map(InventoryManifest.ManifestEntry::key)
        .map(StackKey::get)
        .collect(Collectors.toList());
    ItemStack moved =
        this.getSource().moveOneItemTo(this.getDestination(), StackFilters.anyMatch(keys));
    itemMoved(moved);
  }

  protected final void itemMoved(ItemStack moved) {
    if (!moved.isEmpty()) {
      this.setProcessing(true);
      this.transferredItems.add(StackKey.make(moved));
    }
  }

  @Override
  public boolean canHandleCart(AbstractMinecartEntity cart) {
    return cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
        this.getFacing().getOpposite()).map(InventoryAdaptor::of)
        .map(inventory -> inventory.slotCount() > 0).orElse(false)
        && super.canHandleCart(cart);
  }

  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
    return new ItemManipulatorMenu(this, id, inventory);
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.transferMode);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.transferMode = data.readEnum(TransferMode.class);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putString("transferMode", this.transferMode.getSerializedName());
    data.put("itemFilters", this.getItemFilters().serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.transferMode =
        TransferMode.getByName(data.getString("transferMode")).orElse(TransferMode.ALL);
    this.getItemFilters().deserializeNBT(data.getList("itemFilters", Constants.NBT.TAG_COMPOUND));
  }
}
