package mods.railcraft.util.container.manipulator;

import java.util.function.Predicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * This interface represents an abstract container slot. It provides a unified interface for
 * interfacing with containers.
 */
public interface SlotAccessor {

  boolean isValid(ItemStack itemStack);

  default boolean isEmpty() {
    return this.item().isEmpty();
  }

  default boolean hasItem() {
    return !this.isEmpty();
  }

  default boolean is(Item item) {
    return this.item().is(item);
  }

  default boolean matches(Predicate<ItemStack> filter) {
    return filter.test(this.item());
  }

  /**
   * Removes a single item from an inventory slot and returns it in a new stack.
   */
  default ItemStack extract() {
    return this.extract(1, false);
  }

  default ItemStack simulateExtract() {
    return this.simulateExtract(1);
  }

  default ItemStack simulateExtract(int amount) {
    return this.extract(amount, true);
  }

  ItemStack extract(int amount, boolean simulate);

  default ItemStack insert(ItemStack itemStack) {
    return this.insert(itemStack, false);
  }

  default ItemStack simulateInsert(ItemStack itemStack) {
    return this.insert(itemStack, true);
  }

  /**
   * Add as much of the given ItemStack to the slot as possible.
   *
   * @return the remaining items that were not added
   */
  ItemStack insert(ItemStack stack, boolean simulate);

  /**
   * It is not legal to edit the stack returned from this function.
   */
  ItemStack item();

  default int count() {
    return this.item().getCount();
  }

  int maxStackSize();

  default boolean isFull() {
    return this.item().getCount() == this.maxStackSize();
  }
}
