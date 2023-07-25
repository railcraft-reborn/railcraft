package mods.railcraft.util.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ForwardingContainer extends Container {

  /**
   * Gets the container to delegate to.
   *
   * @return the delegate
   */
  Container container();

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
  boolean stillValid(Player player);

  @Override
  default int getContainerSize() {
    return this.container().getContainerSize();
  }

  @Override
  default boolean isEmpty() {
    return this.container().isEmpty();
  }

  @Override
  default ItemStack getItem(int index) {
    return this.container().getItem(index);
  }

  @Override
  default ItemStack removeItem(int index, int count) {
    return this.container().removeItem(index, count);
  }

  @Override
  default ItemStack removeItemNoUpdate(int index) {
    return this.container().removeItemNoUpdate(index);
  }

  @Override
  default void setItem(int index, ItemStack stack) {
    this.container().setItem(index, stack);
  }

  @Override
  default int getMaxStackSize() {
    return this.container().getMaxStackSize();
  }

  @Override
  default void setChanged() {}

  @Override
  default void startOpen(Player player) {}

  @Override
  default void stopOpen(Player player) {}

  @Override
  default boolean canPlaceItem(int index, ItemStack stack) {
    return this.container().canPlaceItem(index, stack);
  }

  @Override
  default void clearContent() {
    this.container().clearContent();
  }
}
