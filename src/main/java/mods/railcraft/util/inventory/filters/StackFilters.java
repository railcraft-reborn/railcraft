package mods.railcraft.util.inventory.filters;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.IMinecart;
import mods.railcraft.api.item.MinecartPlacer;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.BallastRegistry;
import mods.railcraft.util.FuelUtil;
import mods.railcraft.util.TrackTools;
import mods.railcraft.util.inventory.IInventoryComposite;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.StemBlock;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MinecartItem;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

/**
 * A collection of helper methods for creating {@code Predicate<ItemStack>} objects.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public enum StackFilters implements Predicate<ItemStack> {

  ALL,
  FUEL {
    @Override
    protected boolean testType(ItemStack stack) {
      return FuelUtil.getBurnTime(stack) > 0;
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
    protected boolean testType(
        ItemStack stack) {
      return stack.getItem() instanceof MinecartItem || stack.getItem() instanceof MinecartPlacer;
    }

  },
  BALLAST {
    @Override
    protected boolean testType(ItemStack stack) {
      return BallastRegistry.isItemBallast(stack);
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
    protected boolean testType(ItemStack stack) {
      return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }
  },
  FEED {
    @Override
    protected boolean testType(ItemStack stack) {
      return stack.getItem().getFoodProperties() != null
          || stack.getItem() == Items.WHEAT
          || stack.getItem() instanceof BlockItem
              && ((BlockItem) stack.getItem()).getBlock() instanceof StemBlock;
    }

  },
  CARGO {
    @Override
    protected boolean testType(
        ItemStack stack) {
      return (Railcraft.serverConfig.chestAllowFluids.get() || !FluidItemHelper.isContainer(stack))
          && !Railcraft.serverConfig.cargoBlacklist.get()
              .contains(stack.getItem().getRegistryName().toString());
    }

  },
  RAW_METAL {
    @Override
    protected boolean testType(ItemStack stack) {
      return RailcraftTags.Items.METAL.contains(stack.getItem());
    }
  };

  /**
   * Matches against the provided ItemStack.
   */
  public static Predicate<ItemStack> of(final ItemStack stack) {
    return stack1 -> InvTools.isItemEqual(stack1, stack);
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
    return stack -> filters.stream().anyMatch(f -> InvTools.matchesFilter(f, stack));
  }

  /**
   * Matches against the provided Inventory. If the Item class extends IFilterItem then it will pass
   * the check to the item.
   */
  public static Predicate<ItemStack> anyMatch(final IInventoryComposite inv) {
    return stack -> inv.streamStacks().anyMatch(f -> InvTools.matchesFilter(f, stack));
  }

  /**
   * Matches against the provided ItemStacks.
   *
   * If no ItemStacks are provided to match against, it returns true.
   */
  public static Predicate<ItemStack> anyOf(final ItemStack... stacks) {
    return anyOf(Arrays.asList(stacks));
  }

  /**
   * Matches against the provided ItemStacks.
   *
   * If no ItemStacks are provided to match against, it returns true.
   */
  public static Predicate<ItemStack> anyOf(final Collection<ItemStack> stacks) {
    return stack -> stacks.isEmpty() || stacks.stream().allMatch(ItemStack::isEmpty)
        || InvTools.isItemEqual(stack, stacks);
  }

  public static Predicate<ItemStack> none() {
    return itemStack -> false;
  }

  /**
   * Matches only if the given ItemStack does not match any of the provided ItemStacks.
   *
   * Returns false if the ItemStack being matched is null and true if the stacks to match against is
   * empty/nulled.
   */
  public static Predicate<ItemStack> noneOf(final ItemStack... stacks) {
    return noneOf(Arrays.asList(stacks));
  }

  /**
   * Matches only if the given ItemStack does not match any of the provided ItemStacks.
   *
   * Returns false if the ItemStack being matched is null and true if the stacks to match against is
   * empty/nulled.
   */
  public static Predicate<ItemStack> noneOf(final Collection<ItemStack> stacks) {
    return stack -> {
      if (stack.isEmpty())
        return false;
      return stacks.stream()
          .filter(toTest -> !toTest.isEmpty())
          .noneMatch(filter -> InvTools.isItemEqual(stack, filter));
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
  public static Predicate<ItemStack> containedIn(final IInventoryComposite inv) {
    return inv::contains;
  }

  /**
   * Matches if the Inventory has room and accepts the given ItemStack
   */
  public static Predicate<ItemStack> roomIn(final IInventoryComposite inv) {
    return inv::canFit;
  }

  /**
   * Matches if the ItemStack matches the given cart.
   */
  public static Predicate<ItemStack> isCart(@Nullable final MinecartEntity cart) {
    return stack -> {
      if (stack.isEmpty())
        return false;
      if (cart == null)
        return false;
      if (cart instanceof IMinecart) {
        if (stack.hasCustomHoverName())
          return ((IMinecart) cart).doesCartMatchFilter(stack)
              && stack.getDisplayName().equals(cart.getCartItem().getDisplayName());
        return ((IMinecart) cart).doesCartMatchFilter(stack);
      }
      ItemStack cartItem = cart.getCartItem();
      return !stack.isEmpty() && cartItem.sameItem(stack);
    };
  }

  protected boolean testType(ItemStack stack) {
    return true;
  }

  @Override
  public boolean test(ItemStack stack) {
    return !stack.isEmpty() && testType(stack);
  }
}
