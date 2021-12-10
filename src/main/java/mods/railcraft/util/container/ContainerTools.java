package mods.railcraft.util.container;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import mods.railcraft.api.item.Filter;
import mods.railcraft.api.item.InvToolsAPI;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public abstract class ContainerTools {

  public static final String TAG_SLOT = "Slot";

  public static void requiresNotEmpty(ItemStack stack) {
    if (stack.isEmpty())
      throw new IllegalStateException("Item cannot be empty.");
  }

  public static ItemStack setSize(ItemStack stack, int size) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    stack.setCount(size);
    return stack;
  }

  public static ItemStack incSize(@Nullable ItemStack stack, int size) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    stack.grow(size);
    return stack;
  }

  public static ItemStack decSize(@Nullable ItemStack stack, int size) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    stack.shrink(size);
    return stack;
  }

  public static ItemStack inc(@Nullable ItemStack stack) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    stack.grow(1);
    return stack;
  }

  public static ItemStack dec(@Nullable ItemStack stack) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    stack.shrink(1);
    return stack;
  }

  public static String toString(@Nullable ItemStack stack) {
    if (stack.isEmpty())
      return "ItemStack.EMPTY";
    return stack.toString();
  }

  public static ItemStack makeSafe(@Nullable ItemStack stack) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    return stack;
  }

  public static ItemStack copy(@Nullable ItemStack stack) {
    return stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
  }

  public static ItemStack copy(@Nullable ItemStack stack, int newSize) {
    ItemStack ret = copy(stack);
    if (!ret.isEmpty())
      ret.setCount(Math.min(newSize, ret.getMaxStackSize()));
    return ret;
  }

  public static ItemStack copyOne(ItemStack stack) {
    return copy(stack, 1);
  }

  public static boolean canMerge(ItemStack target, ItemStack source) {
    return target.isEmpty() || source.isEmpty() || (isItemEqual(target, source)
        && target.getCount() + source.getCount() <= target.getMaxStackSize());
  }

  public static int[] buildSlotArray(int start, int size) {
    return IntStream.range(0, size).map(i -> start + i).toArray();
  }

  // @Deprecated
  // public static boolean isSynthetic(ItemStack stack) {
  // CompoundNBT nbt = stack.getTag();
  // return nbt != null && nbt.hasKey("synthetic");
  // }
  //
  // @SuppressWarnings("unused")
  // public static void markItemSynthetic(ItemStack stack) {
  // CompoundNBT nbt = getItemData(stack);
  // nbt.setBoolean("synthetic", true);
  // CompoundNBT display = nbt.getCompound("display");
  // nbt.put("display", display);
  // ListNBT lore = display.getList("Lore", 8);
  // display.put("Lore", lore);
  // lore.add(StringNBT.valueOf("\u00a77\u00a7o" +
  // LocalizationPlugin.translate("item.synthetic")));
  // }

  public static void addItemToolTip(ItemStack stack, String msg) {
    CompoundTag nbt = getItemData(stack);
    CompoundTag display = nbt.getCompound("display");
    nbt.put("display", display);
    ListTag lore = display.getList("Lore", 8);
    display.put("Lore", lore);
    lore.add(StringTag.valueOf(msg));
  }

  /**
   * Use this for manipulating top level NBT data only.
   *
   * In most cases you should use {@link InvToolsAPI#getRailcraftData(ItemStack, boolean)}
   */
  public static CompoundTag getItemData(ItemStack stack) {
    CompoundTag nbt = stack.getTag();
    if (nbt == null) {
      nbt = new CompoundTag();
      stack.setTag(nbt);
    }
    return nbt;
  }

  public static ItemStack depleteItem(ItemStack stack) {
    if (stack.getCount() == 1)
      return stack.getItem().getContainerItem(stack);
    else {
      stack.split(1);
      return stack;
    }
  }

  public static void dropItem(ItemStack stack, Level world, BlockPos pos) {
    Containers.dropItemStack(
        world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
  }

  static int tryPut(List<ContainerSlot> slots, ItemStack stack, int injected, boolean simulate) {
    if (injected >= stack.getCount())
      return injected;
    for (ContainerSlot slot : slots) {
      int amountToInsert = stack.getCount() - injected;
      ItemStack remainder = slot.addToSlot(copy(stack, amountToInsert), simulate);
      if (remainder.isEmpty())
        return stack.getCount();
      injected += amountToInsert - remainder.getCount();
      if (injected >= stack.getCount())
        return injected;
    }
    return injected;
  }

  static boolean tryRemove(CompositeContainer comp, int amount, Predicate<ItemStack> filter,
      boolean simulate) {
    int amountNeeded = amount;
    for (ContainerAdaptor inv : comp.iterable()) {
      List<ItemStack> stacks = inv.extractItems(amountNeeded, filter, simulate);
      amountNeeded -= stacks.stream().mapToInt(ItemStack::getCount).sum();
      if (amountNeeded <= 0)
        return true;
    }
    return false;
  }

  public static boolean isItem(ItemStack stack, @Nullable Item item) {
    return !stack.isEmpty() && item != null && stack.getItem() == item;
  }

  public static boolean matchesFilter(ItemStack filter, ItemStack stack) {
    if (stack.isEmpty() || filter.isEmpty())
      return false;
    if (filter.getItem() instanceof Filter) {
      return ((Filter) filter.getItem()).matches(filter, stack);
    }
    return isItemEqual(stack, filter);
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

  /**
   * A more robust item comparison function.
   * <p/>
   * Does not compare stackSize.
   * <p/>
   * Two null stacks will return true, unlike the other functions.
   * <p/>
   * This function is primarily intended to be used to track changes to an ItemStack.
   *
   * @param a An ItemStack
   * @param b An ItemStack
   * @return True if equal
   */
  public static boolean isItemEqualSemiStrict(@Nullable ItemStack a, @Nullable ItemStack b) {
    if (a == null && b == null)
      return true;
    if (a == null || b == null)
      return false;
    if (a.getItem() != b.getItem())
      return false;
    if (a.getDamageValue() != b.getDamageValue())
      return false;
    return a.getTag() == null || b.getTag() == null
        || a.getTag().equals(b.getTag());
  }

  /**
   * Returns true if the item is equal to any one of several possible matches.
   *
   * @param stack the ItemStack to test
   * @param matches the ItemStacks to test against
   * @return true if a match is found
   */
  public static boolean isItemEqual(@Nullable ItemStack stack, ItemStack... matches) {
    return Arrays.stream(matches).anyMatch(match -> ItemStack.isSame(match, stack));
  }

  /**
   * Returns true if the item is equal to any one of several possible matches.
   *
   * @param stack the ItemStack to test
   * @param matches the ItemStacks to test against
   * @return true if a match is found
   */
  public static boolean isItemEqual(@Nullable ItemStack stack, Collection<ItemStack> matches) {
    return matches.stream().anyMatch(match -> ItemStack.isSame(stack, match));
  }

  public static boolean isItemGreaterOrEqualThan(@Nullable ItemStack stackA,
      @Nullable ItemStack stackB) {
    return ItemStack.isSame(stackA, stackB) && stackA.getCount() >= stackB.getCount();
  }

  public static boolean isItemLessThanOrEqualTo(@Nullable ItemStack stackA,
      @Nullable ItemStack stackB) {
    return ItemStack.isSame(stackA, stackB) && stackA.getCount() <= stackB.getCount();
  }

  public static ListTag writeInventory(Container inventory) {
    ListTag tag = new ListTag();
    for (byte i = 0; i < inventory.getContainerSize(); i++) {
      ItemStack itemStack = inventory.getItem(i);
      if (!itemStack.isEmpty()) {
        CompoundTag slotTag = new CompoundTag();
        slotTag.putByte("index", i);
        writeItem(itemStack, slotTag);
        tag.add(slotTag);
      }
    }
    return tag;
  }

  public static void readInventory(Container inventory, ListTag tag) {
    for (byte i = 0; i < tag.size(); i++) {
      CompoundTag slotTag = tag.getCompound(i);
      int slot = slotTag.getByte("index");
      if (slot >= 0 && slot < inventory.getContainerSize()) {
        ItemStack itemStack = ItemStack.of(slotTag);
        inventory.setItem(slot, itemStack);
      }
    }
  }

  public static void writeItem(ItemStack itemStack, CompoundTag tag) {
    if (itemStack.isEmpty()) {
      return;
    }
    if (itemStack.getCount() > 127) {
      setSize(itemStack, 127);
    }
    itemStack.save(tag);
  }

  public static boolean isStackEqualToBlock(ItemStack stack, @Nullable Block block) {
    return !(stack.isEmpty() || block == null) && stack.getItem() instanceof BlockItem
        && ((BlockItem) stack.getItem()).getBlock() == block;
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
    // noinspection deprecation
    return getBlockFromStack(stack).defaultBlockState();
  }

  public static @Nullable BlockState getBlockStateFromStack(ItemStack stack, Level world,
      BlockPos pos) {
    if (stack.isEmpty())
      return null;
    Item item = stack.getItem();
    if (item instanceof BlockItem) {
      return ((BlockItem) item).getBlock().getStateForPlacement(
          new BlockPlaceContext(world, null, InteractionHand.MAIN_HAND, stack, new BlockHitResult(
              new Vec3(0.5D, 0.5D, 0.5D), Direction.UP, pos.above(), false)));
    }
    return null;
  }

  public static double calculateFullness(ContainerManipulator manipulator) {
    return manipulator.streamSlots()
        .mapToDouble(slot -> slot.getStack().getCount() / (double) slot.getMaxStackSize()).average()
        .orElse(0.0);
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
