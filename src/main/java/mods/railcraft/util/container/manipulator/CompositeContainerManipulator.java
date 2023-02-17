package mods.railcraft.util.container.manipulator;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.Predicates;
import mods.railcraft.util.container.StackFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

/**
 * Primary interface for item containers of all types.
 *
 * Supports treating multiple containers as a single object, enabling one-to-one, many-to-many,
 * many-to-one, and one-to-many interactions between inventories.
 */
@FunctionalInterface
public interface CompositeContainerManipulator<T extends SlotAccessor>
    extends ContainerManipulator<T> {

  static CompositeContainerManipulator<?> findAdjacent(Level level, BlockPos blockPos) {
    return findAdjacent(level, blockPos, Predicates.alwaysTrue());
  }

  static CompositeContainerManipulator<?> findAdjacent(Level level, BlockPos blockPos,
      Predicate<BlockEntity> filter) {
    return () -> Arrays.stream(Direction.values())
        .flatMap(direction -> LevelUtil.getBlockEntity(level, blockPos.relative(direction))
            .filter(filter)
            .flatMap(blockEntity -> blockEntity.getCapability(
                ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).resolve())
            .stream())
        .map(ContainerManipulator::of);
  }

  @SafeVarargs
  static <T extends SlotAccessor> CompositeContainerManipulator<T> of(
      ContainerManipulator<? extends T>... containers) {
    return () -> Arrays.stream(containers);
  }

  static <T extends SlotAccessor> CompositeContainerManipulator<T> of(
      Collection<? extends ContainerManipulator<? extends T>> containers) {
    return containers::stream;
  }

  Stream<? extends ContainerManipulator<? extends T>> streamContainers();

  @Override
  default Stream<T> stream() {
    return this.streamContainers().flatMap(ContainerManipulator::stream);
  }

  @Override
  default int slotCount() {
    return this.streamContainers().mapToInt(ContainerManipulator::slotCount).sum();
  }

  /**
   * Removes a specified number of items matching the filter, but only if the operation can be
   * completed. If the function returns false, the inventory will not be modified.
   *
   * @param amount the amount of items to remove
   * @param filter the filter to match against
   * @return true if there are enough items that can be removed, false otherwise.
   */
  default boolean removeItems(int amount, ItemStack... filter) {
    return removeItems(amount, StackFilter.anyOf(filter));
  }

  /**
   * Removes a specified number of items matching the filter, but only if the operation can be
   * completed. If the function returns false, the inventory will not be modified.
   *
   * @param amount the amount of items to remove
   * @param filter the filter to match against
   * @return true if there are enough items that can be removed, false otherwise.
   */
  default boolean removeItems(int amount, Predicate<ItemStack> filter) {
    return ManipulatorUtil.tryRemove(this, amount, filter, true)
        && ManipulatorUtil.tryRemove(this, amount, filter, false);
  }
}
