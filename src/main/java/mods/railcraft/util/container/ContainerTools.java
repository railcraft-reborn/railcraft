package mods.railcraft.util.container;

import java.util.function.Predicate;
import java.util.stream.IntStream;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.item.Filter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public abstract class ContainerTools {

  public static void requiresNotEmpty(ItemStack stack) {
    if (stack.isEmpty())
      throw new IllegalStateException("Item cannot be empty.");
  }

  public static boolean canMerge(ItemStack target, ItemStack source) {
    return target.isEmpty() || source.isEmpty() || (ItemStack.isSameItem(target, source)
        && target.getCount() + source.getCount() <= target.getMaxStackSize());
  }

  public static int[] buildSlotArray(int start, int size) {
    return IntStream.range(0, size).map(i -> start + i).toArray();
  }

  public static ItemStack depleteItem(ItemStack stack) {
    if (stack.getCount() == 1)
      return stack.getItem().getCraftingRemainingItem(stack);
    else {
      stack.split(1);
      return stack;
    }
  }

  public static void dropIfInvalid(Level level, BlockPos blockPos, Container container, int index) {
    drop(level, blockPos, container, index, item -> container.canPlaceItem(index, item));
  }

  public static void drop(Level level, BlockPos blockPos, Container container, int index,
      Predicate<ItemStack> predicate) {
    var item = container.getItem(index);
    if (!item.isEmpty() && !predicate.test(item)) {
      container.setItem(index, ItemStack.EMPTY);
      Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), item);
    }
  }

  public static boolean matchesFilter(ItemStack filter, ItemStack stack) {
    if (stack.isEmpty() || filter.isEmpty())
      return false;
    if (filter.getItem() instanceof Filter filterItem) {
      return filterItem.matches(filter, stack);
    }
    return ItemStack.isSameItem(stack, filter);
  }

  /**
   * A more robust item comparison function.
   * <p/>
   * Compares stackSize as well.
   * <p/>
   * Two null stacks will return true, unlike the other functions.
   * <p/>
   * This function is primarily intended to be used to track changes to an ItemStack.
   *
   * @param a An ItemStack
   * @param b An ItemStack
   * @return True if equal
   */
  public static boolean isItemEqualStrict(@Nullable ItemStack a, @Nullable ItemStack b) {
    if (a.isEmpty() && b.isEmpty())
      return true;
    if (a.isEmpty() || b.isEmpty())
      return false;
    if (a.getItem() != b.getItem())
      return false;
    if (a.getCount() != b.getCount())
      return false;
    if (a.getDamageValue() != b.getDamageValue())
      return false;
    return a.getTag() == null || b.getTag() == null
        || a.getTag().equals(b.getTag());
  }

  public static ListTag writeContainer(Container container) {
    var tag = new ListTag();
    for (byte i = 0; i < container.getContainerSize(); i++) {
      var itemStack = container.getItem(i);
      if (!itemStack.isEmpty()) {
        var slotTag = new CompoundTag();
        slotTag.putByte("index", i);
        itemStack.save(slotTag);
        tag.add(slotTag);
      }
    }
    return tag;
  }

  public static void readContainer(Container container, ListTag tag) {
    for (byte i = 0; i < tag.size(); i++) {
      var slotTag = tag.getCompound(i);
      int slot = slotTag.getByte("index");
      if (slot >= 0 && slot < container.getContainerSize()) {
        var itemStack = ItemStack.of(slotTag);
        container.setItem(slot, itemStack);
      }
    }
  }

  public static boolean isItemStackBlock(ItemStack itemStack, Block block) {
    return !itemStack.isEmpty()
        && itemStack.getItem() instanceof BlockItem item
        && item.getBlock() == block;
  }

  public static Block getBlockFromStack(ItemStack stack) {
    if (stack.isEmpty())
      return Blocks.AIR;
    Item item = stack.getItem();
    return item instanceof BlockItem ? ((BlockItem) item).getBlock() : Blocks.AIR;
  }

  public static BlockState getBlockStateFromStack(ItemStack stack) {
    if (stack.isEmpty())
      return Blocks.AIR.defaultBlockState();
    return getBlockFromStack(stack).defaultBlockState();
  }

  @Nullable
  public static BlockState getBlockStateFromStack(ItemStack stack, Level level,
      BlockPos pos) {
    if (stack.isEmpty()) {
      return null;
    }
    if (stack.getItem() instanceof BlockItem blockItem) {
      return blockItem.getBlock().getStateForPlacement(
          new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, stack,
              new BlockHitResult(new Vec3(0.5D, 0.5D, 0.5D), Direction.UP, pos.above(), false)));
    }
    return null;
  }

  /**
   * Checks if a stack can have more items filled in.
   *
   * <p>
   * Callers: Be warned that you need to check slot stack limit as well!
   *
   * @param stack the stack to check
   * @return whether the stack needs filling
   */
  public static boolean isStackFull(ItemStack stack) {
    return !stack.isEmpty() && stack.getCount() == stack.getMaxStackSize();
  }
}
