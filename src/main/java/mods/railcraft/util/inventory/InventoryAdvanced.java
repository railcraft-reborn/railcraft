package mods.railcraft.util.inventory;

import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.util.Optionals;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;

/**
 * Creates a standalone instance of IInventory.
 * <p/>
 * Useful for hiding parts of an inventory from outsiders.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class InventoryAdvanced extends Inventory implements IInventoryComposite {

  public static final InventoryAdvanced ZERO_SIZE_INV = new InventoryAdvanced(0);

  private @Nullable Callback callback;
  private int maxStackSize = 64;

  public InventoryAdvanced(int size) {
    super(size);
  }

  public InventoryAdvanced callbackInv(IInventory callback) {
    return callback(new CallbackInv(callback));
  }

  public InventoryAdvanced callbackTile(RailcraftBlockEntity callback) {
    return callback(new CallbackTile(() -> callback));
  }

  public InventoryAdvanced callbackTile(Supplier<RailcraftBlockEntity> callback) {
    return callback(new CallbackTile(callback));
  }

  public InventoryAdvanced callback(Callback callback) {
    this.callback = callback;
    addListener(callback);
    return this;
  }

  public InventoryAdvanced callback(Object callback) {
    if (callback instanceof IInventory)
      return callbackInv((IInventory) callback);
    if (callback instanceof RailcraftBlockEntity)
      return callbackTile((RailcraftBlockEntity) callback);
    return this;
  }

  public InventoryAdvanced phantom() {
    maxStackSize = 127;
    return this;
  }

  @Override
  public int slotCount() {
    return getContainerSize();
  }

  public void setMaxStackSize(int maxStackSize) {
    this.maxStackSize = maxStackSize;
  }

  @Override
  public int getMaxStackSize() {
    return maxStackSize;
  }

  @Override
  public boolean stillValid(PlayerEntity PlayerEntity) {
    return callback == null || callback.stillValid(PlayerEntity);
  }

  @Override
  public void startOpen(PlayerEntity player) {
    if (callback != null) {
      callback.startOpen(player);
    }
  }

  @Override
  public void stopOpen(PlayerEntity player) {
    if (callback != null) {
      callback.stopOpen(player);
    }
  }

  public void writeToNBT(String tag, CompoundNBT data) {
    InvTools.writeInvToNBT(this, tag, data);
  }

  public void readFromNBT(String tag, CompoundNBT data) {
    ListNBT list = data.getList(tag, 10);
    for (byte entry = 0; entry < list.size(); entry++) {
      CompoundNBT itemTag = list.getCompound(entry);
      int slot = itemTag.getByte(InvTools.TAG_SLOT);
      if (slot >= 0 && slot < getContainerSize()) {
        ItemStack stack = ItemStack.of(itemTag);
        setItem(slot, stack);
      }
    }
  }

  public abstract static class Callback implements IInventoryChangedListener {

    public boolean stillValid(PlayerEntity player) {
      return true;
    }

    public void startOpen(PlayerEntity player) {}

    public void stopOpen(PlayerEntity player) {}

    public String getName() {
      return "Standalone";
    }

    public boolean hasCustomName() {
      return false;
    }
  }

  public static class CallbackInv extends Callback {

    private final IInventory inv;

    public CallbackInv(IInventory inv) {
      this.inv = inv;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
      return inv.stillValid(player);
    }

    @Override
    public void startOpen(PlayerEntity player) {
      inv.startOpen(player);
    }

    @Override
    public void stopOpen(PlayerEntity player) {
      inv.stopOpen(player);
    }

    @Override
    public void containerChanged(IInventory invBasic) {
      inv.setChanged();
    }
  }

  public static class CallbackTile extends Callback {

    private final Supplier<RailcraftBlockEntity> tile;

    public CallbackTile(Supplier<RailcraftBlockEntity> tile) {
      this.tile = tile;
    }

    public Optional<RailcraftBlockEntity> tile() {
      return Optional.ofNullable(tile.get());
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
      return Optionals.test(tile(), t -> RailcraftBlockEntity.isUsableByPlayerHelper(t, player));
    }

    @Override
    public void containerChanged(IInventory invBasic) {
      tile().ifPresent(TileEntity::setChanged);
    }
  }
}
