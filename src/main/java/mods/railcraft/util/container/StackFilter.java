package mods.railcraft.util.container;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.item.MinecartFactory;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.CartItem;
import mods.railcraft.world.level.material.FluidItemHelper;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A collection of helper methods for creating {@code Predicate<ItemStack>} objects.
 */
public enum StackFilter implements Predicate<ItemStack> {

  FUEL(itemStack -> ForgeHooks.getBurnTime(itemStack, null) > 0),
  TRACK(TrackUtil::isRail),
  MINECART(itemStack -> {
    var item = itemStack.getItem();
    return item instanceof MinecartItem ||
        item instanceof MinecartFactory ||
        item instanceof CartItem;
  }),
  @SuppressWarnings("deprecation")
  BALLAST(itemStack -> itemStack.getItem() instanceof BlockItem blockItem
      && blockItem.getBlock().builtInRegistryHolder().is(RailcraftTags.Blocks.BALLAST)),
  FLUID_CONTAINER(itemStack -> itemStack
      .getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
      .isPresent()),
  FEED(itemStack -> itemStack.getItem().isEdible()
      || itemStack.is(Items.WHEAT)
      || itemStack.getItem() instanceof BlockItem blockItem
          && blockItem.getBlock() instanceof StemBlock),
  CARGO(itemStack -> (RailcraftConfig.SERVER.chestAllowFluids.get()
      || !FluidItemHelper.isContainer(itemStack))
      && !RailcraftConfig.SERVER.cargoBlacklist.get()
          .contains(ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString())),
  DYES(itemStack -> itemStack.is(Tags.Items.DYES)),
  RAW_METAL(itemStack -> itemStack.is(RailcraftTags.Items.METAL));

  private final Predicate<ItemStack> predicate;

  StackFilter(Predicate<ItemStack> predicate) {
    this.predicate = predicate;
  }

  @Override
  public boolean test(ItemStack itemStack) {
    return !itemStack.isEmpty() && this.predicate.test(itemStack);
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
          .noneMatch(filter -> ItemStack.isSameItem(itemStack, filter));
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

      var cartItem = cart.getPickResult();
      boolean matches = ItemStack.isSameItem(cartItem, itemStack);

      if (itemStack.hasCustomHoverName()) {
        return matches && itemStack.getDisplayName().getContents()
            .equals(cartItem.getDisplayName().getContents());
      }

      return matches;
    };
  }
}
