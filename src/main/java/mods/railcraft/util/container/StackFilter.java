package mods.railcraft.util.container;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import mods.railcraft.world.item.CartItem;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.item.MinecartFactory;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.Predicates;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A collection of helper methods for creating {@code Predicate<ItemStack>} objects.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public enum StackFilter implements Predicate<ItemStack> {

  ALL(Predicates.alwaysTrue()),
  FUEL(itemStack -> ForgeHooks.getBurnTime(itemStack, null) > 0),
  TRACK(TrackUtil::isRail),
  MINECART(itemStack ->  {
    var item = itemStack.getItem();
    return item instanceof MinecartItem ||
        item instanceof MinecartFactory ||
        item instanceof CartItem;
  } ),
  @SuppressWarnings("deprecation")
  BALLAST(itemStack -> itemStack.getItem() instanceof BlockItem blockItem
      && blockItem.getBlock().builtInRegistryHolder().is(RailcraftTags.Blocks.BALLAST)),
  FLUID_CONTAINER(itemStack -> itemStack
      .getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
      .isPresent()),
  FEED(itemStack -> itemStack.getItem().getFoodProperties(itemStack, null) != null
      || itemStack.getItem() == Items.WHEAT
      || itemStack.getItem() instanceof BlockItem blockItem
          && blockItem.getBlock() instanceof StemBlock),
  CARGO(itemStack -> (RailcraftConfig.server.chestAllowFluids.get()
      || !FluidItemHelper.isContainer(itemStack))
      && !RailcraftConfig.server.cargoBlacklist.get()
          .contains(ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString())),
  RAW_METAL(itemStack -> itemStack.is(RailcraftTags.Items.METAL));

  private final Predicate<ItemStack> predicate;

  private StackFilter(Predicate<ItemStack> predicate) {
    this.predicate = predicate;
  }

  @Override
  public boolean test(ItemStack itemStack) {
    return !itemStack.isEmpty() && this.predicate.test(itemStack);
  }

  /**
   * Matches against the provided ItemStack.
   */
  public static Predicate<ItemStack> of(ItemStack itemStack) {
    return stack1 -> ContainerTools.isItemEqual(stack1, itemStack);
  }

  /**
   * Matches against the provided class/interface.
   */
  public static Predicate<ItemStack> of(final Class<?> itemClass) {
    return itemStack -> !itemStack.isEmpty()
        && itemClass.isAssignableFrom(itemStack.getItem().getClass());
  }

  /**
   * Matches against the provided Item.
   */
  public static Predicate<ItemStack> of(final Item item) {
    return itemStack -> !itemStack.isEmpty() && itemStack.is(item);
  }

  /**
   * Matches against the provided Item.
   */
  public static Predicate<ItemStack> of(final Block block) {
    return itemStack -> !itemStack.isEmpty() && itemStack.is(block.asItem());
  }

  /**
   * Matches against the provided ItemStacks. If the Item class extends IFilterItem then it will
   * pass the check to the item.
   */
  public static Predicate<ItemStack> anyMatch(final ItemStack... filters) {
    return anyMatch(Arrays.asList(filters));
  }

  /**
   * Matches against the provided ItemStacks. If the Item class extends IFilterItem then it will
   * pass the check to the item.
   */
  public static Predicate<ItemStack> anyMatch(final Collection<ItemStack> filters) {
    return itemStack -> filters.stream().anyMatch(f -> ContainerTools.matchesFilter(f, itemStack));
  }

  /**
   * Matches against the provided Inventory. If the Item class extends IFilterItem then it will pass
   * the check to the item.
   */
  public static Predicate<ItemStack> anyMatch(final ContainerManipulator<?> inv) {
    return itemStack -> inv.streamItems().anyMatch(f -> ContainerTools.matchesFilter(f, itemStack));
  }

  /**
   * Matches against the provided ItemStacks.
   *
   * <p>
   * If no ItemStacks are provided to match against, it returns true.
   */
  public static Predicate<ItemStack> anyOf(final ItemStack... stacks) {
    return anyOf(Arrays.asList(stacks));
  }

  /**
   * Matches against the provided ItemStacks.
   *
   * <p>
   * If no ItemStacks are provided to match against, it returns true.
   */
  public static Predicate<ItemStack> anyOf(final Collection<ItemStack> stacks) {
    return itemStack -> stacks.isEmpty() || stacks.stream().allMatch(ItemStack::isEmpty)
        || ContainerTools.isItemEqual(itemStack, stacks);
  }

  public static Predicate<ItemStack> none() {
    return itemStack -> false;
  }

  /**
   * Matches only if the given ItemStack does not match any of the provided ItemStacks.
   *
   * <p>
   * Returns false if the ItemStack being matched is null and true if the stacks to match against is
   * empty/nulled.
   */
  public static Predicate<ItemStack> noneOf(final ItemStack... stacks) {
    return noneOf(Arrays.asList(stacks));
  }

  /**
   * Matches only if the given ItemStack does not match any of the provided ItemStacks.
   *
   * <p>
   * Returns false if the ItemStack being matched is null and true if the stacks to match against is
   * empty/nulled.
   */
  public static Predicate<ItemStack> noneOf(final Collection<ItemStack> stacks) {
    return itemStack -> {
      if (itemStack.isEmpty()) {
        return false;
      }
      return stacks.stream().filter(toTest -> !toTest.isEmpty())
          .noneMatch(filter -> ContainerTools.isItemEqual(itemStack, filter));
    };
  }

  public static Predicate<ItemStack> ofSize(int size) {
    return itemStack -> itemStack.getCount() == size;
  }

  public static Predicate<ItemStack> singleton() {
    return itemStack -> itemStack.getCount() == 1;
  }

  public static Predicate<ItemStack> nonEmpty() {
    return itemStack -> !itemStack.isEmpty();
  }

  /**
   * Matches if the ItemStack matches the given cart.
   */
  public static Predicate<ItemStack> isCart(@Nullable final AbstractMinecart cart) {
    return itemStack -> {
      if (itemStack.isEmpty()) {
        return false;
      }
      if (cart == null) {
        return false;
      }

      ItemStack cartItem = cart.getPickResult();
      boolean matches = !itemStack.isEmpty() && cartItem.sameItem(itemStack);

      if (itemStack.hasCustomHoverName()) {
        return matches && itemStack.getDisplayName().getContents()
            .equals(cart.getPickResult().getDisplayName().getContents());
      }

      return matches;
    };
  }
}
