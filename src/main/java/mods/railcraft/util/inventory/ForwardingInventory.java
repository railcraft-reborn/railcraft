package mods.railcraft.util.inventory;

import java.util.Iterator;
import com.google.common.collect.Iterators;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by CovertJaguar on 11/18/2018 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ForwardingInventory extends IInventory, IInventoryComposite {

  /**
   * Gets the standalone inventory that backs this implementor.
   *
   * @return the delegate
   */
  IInventory getInventory();

  @Override
  default Iterator<InventoryAdaptor> adaptors() {
    return Iterators.singletonIterator(InventoryAdaptor.of(this));
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * This method must be implemented without delegation as it is determined by factors outside of
   * the standalone inventory; standalone inventories call the callback (usually the objects that
   * hold these inventories) for this method.
   *
   * @param player the player to check
   * @return true if the player can use this inventory
   */
  @Override
  boolean stillValid(PlayerEntity player);

  @Override
  default int getContainerSize() {
    return this.getInventory().getContainerSize();
  }

  @Override
  default boolean isEmpty() {
    return this.getInventory().isEmpty();
  }

  @Override
  default ItemStack getItem(int index) {
    return this.getInventory().getItem(index);
  }

  @Override
  default ItemStack removeItem(int index, int count) {
    return this.getInventory().removeItem(index, count);
  }

  @Override
  default ItemStack removeItemNoUpdate(int index) {
    return this.getInventory().removeItemNoUpdate(index);
  }

  @Override
  default void setItem(int index, ItemStack stack) {
    this.getInventory().setItem(index, stack);
  }

  @Override
  default int getMaxStackSize() {
    return this.getInventory().getMaxStackSize();
  }

  @Override
  default void setChanged() {}

  @Override
  default void startOpen(PlayerEntity player) {}

  @Override
  default void stopOpen(PlayerEntity player) {}

  @Override
  default boolean canPlaceItem(int index, ItemStack stack) {
    return this.getInventory().canPlaceItem(index, stack);
  }

  @Override
  default void clearContent() {
    this.getInventory().clearContent();
  }
}
