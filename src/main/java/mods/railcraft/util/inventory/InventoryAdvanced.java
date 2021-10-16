package mods.railcraft.util.inventory;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.google.common.collect.Iterators;
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
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Creates a standalone instance of IInventory.
 * <p/>
 * Useful for hiding parts of an inventory from outsiders.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class InventoryAdvanced extends Inventory
    implements IInventoryComposite, INBTSerializable<ListNBT> {

  public static final InventoryAdvanced EMPTY = new InventoryAdvanced(0);

  @Nullable
  private Callback callback;
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
    this.addListener(callback);
    return this;
  }

  public InventoryAdvanced phantom() {
    this.maxStackSize = 127;
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
    return this.maxStackSize;
  }

  @Override
  public boolean stillValid(PlayerEntity PlayerEntity) {
    return this.callback == null || callback.stillValid(PlayerEntity);
  }

  @Override
  public void startOpen(PlayerEntity player) {
    if (this.callback != null) {
      this.callback.startOpen(player);
    }
  }

  @Override
  public void stopOpen(PlayerEntity player) {
    if (this.callback != null) {
      this.callback.stopOpen(player);
    }
  }

  @Override
  public ListNBT serializeNBT() {
    return InvTools.writeInventory(this);
  }

  @Override
  public void deserializeNBT(ListNBT tag) {
    for (byte i = 0; i < tag.size(); i++) {
      CompoundNBT slotTag = tag.getCompound(i);
      int slot = slotTag.getByte(InvTools.TAG_SLOT);
      if (slot >= 0 && slot < this.getContainerSize()) {
        this.setItem(slot, ItemStack.of(slotTag));
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
      return Optionals.test(tile(), t -> RailcraftBlockEntity.stillValid(t, player));
    }

    @Override
    public void containerChanged(IInventory invBasic) {
      tile().ifPresent(TileEntity::setChanged);
    }
  }

  @Override
  public Iterator<InventoryAdaptor> adaptors() {
    return Iterators.singletonIterator(InventoryAdaptor.of(this));
  }
}
