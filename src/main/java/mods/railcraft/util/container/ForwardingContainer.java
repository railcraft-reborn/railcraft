package mods.railcraft.util.container;

import mods.railcraft.util.container.manipulator.VanillaContainerManipulator;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Created by CovertJaguar on 11/18/2018 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ForwardingContainer extends Container, VanillaContainerManipulator {

  /**
   * Gets the container to delegate to.
   *
   * @return the delegate
   */
  @Override
  Container getContainer();

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
    return this.getContainer().getContainerSize();
  }

  @Override
  default boolean isEmpty() {
    return this.getContainer().isEmpty();
  }

  @Override
  default ItemStack getItem(int index) {
    return this.getContainer().getItem(index);
  }

  @Override
  default ItemStack removeItem(int index, int count) {
    return this.getContainer().removeItem(index, count);
  }

  @Override
  default ItemStack removeItemNoUpdate(int index) {
    return this.getContainer().removeItemNoUpdate(index);
  }

  @Override
  default void setItem(int index, ItemStack stack) {
    this.getContainer().setItem(index, stack);
  }

  @Override
  default int getMaxStackSize() {
    return this.getContainer().getMaxStackSize();
  }

  @Override
  default void setChanged() {}

  @Override
  default void startOpen(Player player) {}

  @Override
  default void stopOpen(Player player) {}

  @Override
  default boolean canPlaceItem(int index, ItemStack stack) {
    return this.getContainer().canPlaceItem(index, stack);
  }

  @Override
  default void clearContent() {
    this.getContainer().clearContent();
  }
}
