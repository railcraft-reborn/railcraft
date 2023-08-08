package mods.railcraft.util.container.manipulator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import com.google.common.base.Predicates;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.StackFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * This interface defines the standard inventory operations.
 * <p>
 * Other classes may expand on these, but these are foundations that everything else is built on.
 * <p>
 * This interface exists mainly to enforce a consistent naming scheme on these functions.
 */
public interface ContainerManipulator<T extends SlotAccessor> extends Iterable<T> {

  static <T extends SlotAccessor> ContainerManipulator<T> empty() {
    return Stream::empty;
  }

  static ContainerManipulator<ModifiableSlotAccessor> of(Container container) {
    var slots = ContainerSlotAccessor.createSlots(container).toList();
    return slots::stream;
  }

  static ContainerManipulator<ModifiableSlotAccessor> of(
      WorldlyContainer container, Direction face) {
    var slots = ContainerSlotAccessor.createSlots(container, face).toList();
    return slots::stream;
  }

  static ContainerManipulator<SlotAccessor> of(IItemHandler itemHandler) {
    var slots = ItemHandlerSlotAccessor.createSlots(itemHandler).toList();
    return slots::stream;
  }

  static ContainerManipulator<ModifiableSlotAccessor> of(IItemHandlerModifiable itemHandler) {
    var slots = ItemHandlerSlotAccessor.createSlots(itemHandler).toList();
    return slots::stream;
  }

  static ContainerManipulator<?> findAdjacent(Level level, BlockPos blockPos) {
    return findAdjacent(level, blockPos, Predicates.alwaysTrue());
  }

  static ContainerManipulator<?> findAdjacent(Level level, BlockPos blockPos,
      Predicate<BlockEntity> filter) {
    return () -> Arrays.stream(Direction.values())
        .flatMap(direction -> LevelUtil.getBlockEntity(level, blockPos.relative(direction))
            .filter(filter)
            .flatMap(blockEntity -> blockEntity
                .getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite())
                .map(ContainerManipulator::of))
            .stream())
        .flatMap(ContainerManipulator::stream);
  }

  @SafeVarargs
  static <T extends SlotAccessor> ContainerManipulator<T> of(
      ContainerManipulator<? extends T>... containers) {
    return () -> Arrays.stream(containers).flatMap(ContainerManipulator::stream);
  }

  static <T extends SlotAccessor> ContainerManipulator<T> of(
      Collection<? extends ContainerManipulator<? extends T>> containers) {
    return () -> containers.stream().flatMap(ContainerManipulator::stream);
  }

  Stream<T> stream();

  @Override
  default Iterator<T> iterator() {
    return this.stream().iterator();
  }

  default Stream<ItemStack> streamItems() {
    return this.stream().filter(SlotAccessor::hasItem).map(SlotAccessor::item);
  }

  /**
   * Attempt to add the stack to the inventory returning the remainder.
   *
   * If the entire stack was accepted, it returns an empty stack.
   *
   * @return The remainder
   */
  default ItemStack insert(ItemStack stack, boolean simulate) {
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    var it = this.stream()
        .sorted(Comparator.comparingInt(SlotAccessor::count).reversed())
        .iterator();
    var remainder = stack;

    while (it.hasNext()) {
      var slot = it.next();
      remainder = slot.insert(remainder, simulate);
      if (remainder.isEmpty()) {
        return ItemStack.EMPTY;
      }
    }

    return remainder;
  }

  /**
   * Removes up to maxAmount items in one slot matching the filter.
   */
  default ItemStack extract(int maxAmount, Predicate<ItemStack> filter, boolean simulate) {
    return this.stream()
        .filter(slot -> slot.matches(filter))
        .map(slot -> slot.extract(maxAmount, simulate))
        .filter(item -> !item.isEmpty())
        .findFirst()
        .orElse(ItemStack.EMPTY);
  }

  /**
   * Removed an item matching the filter.
   */
  default ItemStack extract(Predicate<ItemStack> filter) {
    return this.extract(1, filter, false);
  }

  /**
   * Removes and returns a single item from the inventory.
   *
   * @return An ItemStack
   */
  default ItemStack extract() {
    return this.extract(StackFilter.ALL);
  }

  /**
   * Removes and returns a single item from the inventory that matches the filter.
   *
   * @param filter the filter to match against
   * @return An ItemStack
   */
  default ItemStack extract(ItemStack... filter) {
    return this.extract(StackFilter.anyOf(filter));
  }

  default ItemStack moveOneItemTo(ContainerManipulator<?> dest, Predicate<ItemStack> filter) {
    return this.stream()
        .filter(slot -> slot.matches(filter))
        .filter(slot -> dest.insert(slot.simulateExtract()).isEmpty())
        .map(T::extract)
        .findFirst()
        .orElse(ItemStack.EMPTY);
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
    return this.insert(stack, true).isEmpty();
  }

  /**
   * Attempt to add the stack to the inventory returning the remainder.
   *
   * If the entire stack was accepted, it returns an empty stack.
   *
   * @return The remainder
   */
  default ItemStack insert(ItemStack stack) {
    return this.insert(stack, false);
  }

  default Optional<T> findFirstExtractable(Predicate<ItemStack> filter) {
    return this.stream()
        .filter(slot -> slot.matches(filter) && !slot.simulateExtract().isEmpty())
        .findFirst();
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
  default Stream<T> findAll(Predicate<ItemStack> filter) {
    return this.stream().filter(slot -> slot.matches(filter));
  }

  /**
   * @see Container#calcRedstoneFromInventory(IInventory)
   */
  default int calcRedstone() {
    double average = ContainerTools.calculateFullness(this);

    return Mth.floor(average * 14.0F) + (hasNoItems() ? 0 : 1);
  }
}
