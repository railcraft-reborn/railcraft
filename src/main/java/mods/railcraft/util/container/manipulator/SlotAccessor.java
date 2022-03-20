package mods.railcraft.util.container.manipulator;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * This interface represents an abstract container slot. It provides a unified interface for
 * interfacing with containers.
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface SlotAccessor {

  boolean isValid(ItemStack itemStack);

  boolean canRemoveItem();

  default boolean hasItem() {
    return !this.getItem().isEmpty();
  }

  default boolean is(Item item) {
    return this.getItem().is(item);
  }

  default boolean matches(Predicate<ItemStack> filter) {
    return filter.test(this.getItem());
  }

  /**
   * Removes a single item from an inventory slot and returns it in a new stack.
   */
  ItemStack shrink();

  ItemStack shrink(int amount, boolean simulate);

  /**
   * Add as much of the given ItemStack to the slot as possible.
   *
   * @return the remaining items that were not added
   */
  ItemStack grow(ItemStack stack, boolean simulate);

  /**
   * It is not legal to edit the stack returned from this function.
   */
  ItemStack getItem();

  int maxStackSize();
}
