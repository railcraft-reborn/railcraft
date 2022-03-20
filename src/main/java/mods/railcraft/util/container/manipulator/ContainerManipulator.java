package mods.railcraft.util.container.manipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.base.Predicates;
import mods.railcraft.util.collections.ItemStackKey;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.StackFilter;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * This interface defines the standard inventory operations.
 *
 * Other classes may expand on these, but these are foundations that everything else is built on.
 *
 * This interface exists mainly to enforce a consistent naming scheme on these functions.
 *
 * Created by CovertJaguar on 12/9/2018 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface ContainerManipulator<T extends SlotAccessor> {

  static ContainerManipulator<SlotAccessor> of(IItemHandler itemHandler) {
    return new ItemHandlerManipulator.Standard(itemHandler);
  }

  /**
   * Only use this on inventories we control.
   */
  static ContainerManipulator<ModifiableSlotAccessor> of(IItemHandlerModifiable itemHandler) {
    return new ItemHandlerManipulator.Modifiable(itemHandler);
  }

  /**
   * Returns the number of slots the inventory contains.
   */
  int slotCount();

  /**
   * Attempt to add the stack to the inventory returning the remainder.
   *
   * If the entire stack was accepted, it returns an empty stack.
   *
   * @return The remainder
   */
  default ItemStack addStack(ItemStack stack, boolean simulate) {
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }
    var newStack = stack.copy();
    List<SlotAccessor> filledSlots = new ArrayList<>();
    List<SlotAccessor> emptySlots = new ArrayList<>();
    this.stream().forEach(slot -> {
      if (slot.isValid(newStack)) {
        if (slot.getItem().isEmpty()) {
          emptySlots.add(slot);
        } else {
          filledSlots.add(slot);
        }
      }
    });

    int injected = 0;
    injected = ManipulatorUtil.tryPut(filledSlots, newStack, injected, simulate);
    injected = ManipulatorUtil.tryPut(emptySlots, newStack, injected, simulate);
    newStack.shrink(injected);
    return newStack.isEmpty() ? ItemStack.EMPTY : newStack;
  }

  /**
   * Removes up to maxAmount items in one slot matching the filter.
   */
  default ItemStack removeStack(int maxAmount, Predicate<ItemStack> filter, boolean simulate) {
    return this.stream()
        .filter(slot -> slot.hasItem() && slot.canRemoveItem() && slot.matches(filter))
        .map(slot -> slot.shrink(maxAmount, simulate))
        .findFirst()
        .orElse(ItemStack.EMPTY);
  }

  default List<ItemStack> extractItems(int maxAmount, Predicate<ItemStack> filter,
      boolean simulate) {
    var amountNeeded = new AtomicInteger(maxAmount);
    return this.stream()
        .takeWhile(__ -> amountNeeded.getPlain() > 0)
        .filter(slot -> slot.hasItem() && slot.canRemoveItem() && slot.matches(filter))
        .map(slot -> slot.shrink(amountNeeded.getPlain(), simulate))
        .filter(item -> !item.isEmpty())
        .peek(item -> amountNeeded.addAndGet(-item.getCount()))
        .toList();
  }

  default ItemStack moveOneItemTo(ContainerManipulator<?> dest, Predicate<ItemStack> filter) {
    return this.stream()
        .filter(slot -> slot.hasItem() && slot.canRemoveItem() && slot.matches(filter))
        .filter(slot -> dest.addStack(ContainerTools.copyOne(slot.getItem())).isEmpty())
        .map(T::shrink)
        .findFirst()
        .orElse(ItemStack.EMPTY);
  }

  Stream<T> stream();

  default Stream<ItemStack> streamItems() {
    return this.stream().filter(SlotAccessor::hasItem).map(SlotAccessor::getItem);
  }

  /**
   * Checks if inventory will accept the ItemStack.
   *
   * @param stack The ItemStack
   * @return true if room for stack
   */
  default boolean willAccept(ItemStack stack) {
    if (stack.isEmpty()) {
      return false;
    }
    var newStack = ContainerTools.copyOne(stack);
    return this.stream().anyMatch(slot -> slot.isValid(newStack));
  }

  /**
   * Checks if inventory will accept any item from the list.
   *
   * @param stacks The ItemStacks
   * @return true if room for stack
   */
  default boolean willAcceptAny(List<ItemStack> stacks) {
    return stacks.stream().anyMatch(this::willAccept);
  }

  /**
   * Checks if there is room for the ItemStack in the inventory.
   *
   * @param stack The ItemStack
   * @return true if room for stack
   */
  default boolean canFit(ItemStack stack) {
    return this.addStack(stack, true).isEmpty();
  }

  /**
   * Attempt to add the stack to the inventory returning the remainder.
   *
   * If the entire stack was accepted, it returns an empty stack.
   *
   * @return The remainder
   */
  default ItemStack addStack(ItemStack stack) {
    return addStack(stack, false);
  }

  /**
   * Removed an item matching the filter.
   */
  default ItemStack removeOneItem(Predicate<ItemStack> filter) {
    return removeStack(1, filter, false);
  }

  /**
   * Removes and returns a single item from the inventory.
   *
   * @return An ItemStack
   */
  default ItemStack removeOneItem() {
    return removeOneItem(StackFilter.ALL);
  }

  /**
   * Removes and returns a single item from the inventory that matches the filter.
   *
   * @param filter the filter to match against
   * @return An ItemStack
   */
  default ItemStack removeOneItem(ItemStack... filter) {
    return removeOneItem(StackFilter.anyOf(filter));
  }

  /**
   * Returns the item that would be returned if an item matching the filter was removed. Does not
   * modify the inventory.
   */
  default ItemStack findOne(Predicate<ItemStack> filter) {
    return removeStack(1, filter, true);
  }

  /**
   * Attempts to move a single item from one inventory to another.
   *
   * @param dest the destination inventory
   * @return null if nothing was moved, the stack moved otherwise
   */
  default ItemStack moveOneItemTo(ContainerManipulator<?> dest) {
    return this.moveOneItemTo(dest, Predicates.alwaysTrue());
  }

  /**
   * Returns true if the inventory contains the specified item.
   *
   * @param filter The ItemStack to look for
   * @return true is exists
   */
  default boolean contains(Predicate<ItemStack> filter) {
    return streamItems().anyMatch(filter);
  }

  default int countStacks() {
    return countStacks(StackFilter.ALL);
  }

  default int countStacks(Predicate<ItemStack> filter) {
    return (int) streamItems().filter(filter).count();
  }

  /**
   * Returns true if the inventory contains any of the specified items.
   *
   * @param items The ItemStack to look for
   * @return true is exists
   */
  default boolean contains(ItemStack... items) {
    return contains(StackFilter.anyOf(items));
  }

  /**
   * Counts the number of items that match the filter.
   *
   * @param filter the Predicate to match against
   * @return the number of items in the inventory
   */
  default int countItems(Predicate<ItemStack> filter) {
    return streamItems().filter(filter).mapToInt(ItemStack::getCount).sum();
  }

  /**
   * Counts the number of items.
   *
   * @return the number of items in the inventory
   */
  default int countItems() {
    return countItems(StackFilter.ALL);
  }

  /**
   * Counts the number of items that match the filter.
   *
   * @param filters the items to match against
   * @return the number of items in the inventory
   */
  default int countItems(ItemStack... filters) {
    return countItems(StackFilter.anyOf(filters));
  }

  default boolean hasItems() {
    return streamItems().findAny().isPresent();
  }

  default boolean hasNoItems() {
    return !hasItems();
  }

  default boolean isFull() {
    return this.stream().allMatch(SlotAccessor::hasItem);
  }

  default boolean hasEmptySlot() {
    return !isFull();
  }

  default int countMaxItemStackSize() {
    return streamItems().mapToInt(ItemStack::getMaxStackSize).sum();
  }

  /**
   * Returns all items from the inventory that match the filter, but does not remove them. The
   * resulting set will be populated with a single instance of each item type.
   *
   * @param filter EnumItemType to match against
   * @return A Set of ItemStacks
   */
  default Set<ItemStackKey> findAll(Predicate<ItemStack> filter) {
    return streamItems().filter(filter).map(ItemStackKey::make).collect(Collectors.toSet());
  }

  /**
   * @see Container#calcRedstoneFromInventory(IInventory)
   */
  default int calcRedstone() {
    double average = ContainerTools.calculateFullness(this);

    return Mth.floor(average * 14.0F) + (hasNoItems() ? 0 : 1);
  }
}
