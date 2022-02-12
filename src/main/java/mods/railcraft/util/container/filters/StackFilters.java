package mods.railcraft.util.container.filters;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.item.MinecartFactory;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.TrackTools;
import mods.railcraft.util.container.CompositeContainer;
import mods.railcraft.util.container.ContainerManipulator;
import mods.railcraft.util.container.ContainerTools;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

/**
 * A collection of helper methods for creating {@code Predicate<ItemStack>} objects.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public enum StackFilters implements Predicate<ItemStack> {

  ALL,
  FUEL {
    @Override
    protected boolean testType(ItemStack stack) {
      return ForgeHooks.getBurnTime(stack, null) > 0;
    }

  },
  TRACK {
    @Override
    protected boolean testType(ItemStack stack) {
      return TrackTools.isRail(stack);
    }

  },
  MINECART {
    @Override
    protected boolean testType(ItemStack stack) {
      return stack.getItem() instanceof MinecartItem || stack.getItem() instanceof MinecartFactory;
    }

  },
  BALLAST {
    @Override
    protected boolean testType(ItemStack stack) {
      return stack.getItem() instanceof BlockItem
          && RailcraftTags.Blocks.BALLAST.contains(((BlockItem) stack.getItem()).getBlock());
    }
  },
  // EMPTY_BUCKET {
  // @Override
  // protected boolean testType(ItemStack stack) {
  // if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
  // return true;
  // if (InvTools.isItem(stack, Items.BUCKET))
  // return true;
  // UniversalBucket uBucket = ForgeModContainer.getInstance().universalBucket;
  // FluidStack fluidStack;
  // return uBucket != null && of(UniversalBucket.class).test(stack) && (fluidStack =
  // uBucket.getFluid(stack)) != null && fluidStack.amount <= 0;
  // }
  //
  // },
  FLUID_CONTAINER {
    @Override
    protected boolean testType(
        ItemStack stack) {
      return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }
  },
  FEED {
    @Override
    protected boolean testType(
        ItemStack stack) {
      return stack.getItem().getFoodProperties() != null || stack.getItem() == Items.WHEAT
          || stack.getItem() instanceof BlockItem
              && ((BlockItem) stack.getItem()).getBlock() instanceof StemBlock;
    }

  },
  CARGO {
    @Override
    protected boolean testType(
        ItemStack stack) {
      return (RailcraftConfig.server.chestAllowFluids.get() || !FluidItemHelper.isContainer(stack))
          && !RailcraftConfig.server.cargoBlacklist.get()
              .contains(stack.getItem().getRegistryName().toString());
    }

  },
  RAW_METAL {
    @Override
    protected boolean testType(
        ItemStack stack) {
      return RailcraftTags.Items.METAL.contains(stack.getItem());
    }
  };

  /**
   * Matches against the provided ItemStack.
   */
  public static Predicate<ItemStack> of(final ItemStack stack) {
    return stack1 -> ContainerTools.isItemEqual(stack1, stack);
  }

  /**
   * Matches against the provided class/interface.
   */
  public static Predicate<ItemStack> of(final Class<?> itemClass) {
    return stack -> !stack.isEmpty()
        && itemClass.isAssignableFrom(stack.getItem().getClass());
  }

  /**
   * Matches against the provided Item.
   */
  public static Predicate<ItemStack> of(final Item item) {
    return stack -> !stack.isEmpty() && stack.getItem() == item;
  }

  /**
   * Matches against the provided Item.
   */
  public static Predicate<ItemStack> of(final Block block) {
    return stack -> !stack.isEmpty() && stack.getItem() == block.asItem();
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
    return stack -> filters.stream().anyMatch(f -> ContainerTools.matchesFilter(f, stack));
  }

  /**
   * Matches against the provided Inventory. If the Item class extends IFilterItem then it will pass
   * the check to the item.
   */
  public static Predicate<ItemStack> anyMatch(final ContainerManipulator inv) {
    return stack -> inv.streamStacks().anyMatch(f -> ContainerTools.matchesFilter(f, stack));
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
    return stack -> stacks.isEmpty() || stacks.stream().allMatch(ItemStack::isEmpty)
        || ContainerTools.isItemEqual(stack, stacks);
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
    return stack -> {
      if (stack.isEmpty()) {
        return false;
      }
      return stacks.stream().filter(toTest -> !toTest.isEmpty())
          .noneMatch(filter -> ContainerTools.isItemEqual(stack, filter));
    };
  }

  public static Predicate<ItemStack> ofSize(final int size) {
    return stack -> stack.getCount() == size;
  }

  public static Predicate<ItemStack> singleton() {
    return stack -> stack.getCount() == 1;
  }

  public static Predicate<ItemStack> nonEmpty() {
    return stack -> !stack.isEmpty();
  }

  /**
   * Matches if the Inventory contains the given ItemStack.
   */
  public static Predicate<ItemStack> containedIn(final CompositeContainer inv) {
    return inv::contains;
  }

  /**
   * Matches if the Inventory has room and accepts the given ItemStack.
   */
  public static Predicate<ItemStack> roomIn(final CompositeContainer inv) {
    return inv::canFit;
  }

  /**
   * Matches if the ItemStack matches the given cart.
   */
  public static Predicate<ItemStack> isCart(@Nullable final AbstractMinecart cart) {
    return stack -> {
      if (stack.isEmpty()) {
        return false;
      }
      if (cart == null) {
        return false;
      }

      ItemStack cartItem = cart.getPickResult();
      boolean matches = !stack.isEmpty() && cartItem.sameItem(stack);

      if (stack.hasCustomHoverName()) {
        return matches && stack.getDisplayName().getContents()
            .equals(cart.getPickResult().getDisplayName().getContents());
      }

      return matches;
    };
  }

  protected boolean testType(ItemStack stack) {
    return true;
  }

  @Override
  public boolean test(ItemStack stack) {
    return !stack.isEmpty() && this.testType(stack);
  }
}
