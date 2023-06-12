package mods.railcraft.world.entity.vehicle;

import java.util.function.Predicate;
import mods.railcraft.api.carts.FluidMinecart;
import mods.railcraft.api.carts.ItemTransferHandler;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.api.carts.TrainTransferService;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.util.container.manipulator.SlotAccessor;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Utility class for simplifying moving items and fluids through a train.
 *
 * Created by CovertJaguar on 5/9/2015.
 */
public enum TrainTransferServiceImpl implements TrainTransferService {

  INSTANCE;

  private static final int NUM_SLOTS = 8;
  private static final int TANK_CAPACITY = 8 * FluidType.BUCKET_VOLUME;

  /**
   * Offers an item stack to linked carts or drops it if no one wants it.
   */
  @Override
  public void offerOrDropItem(RollingStock cart, ItemStack stack) {
    stack = this.pushStack(cart, stack);
    if (!stack.isEmpty()) {
      var entity = cart.entity();
      Containers.dropItemStack(entity.level(),
          entity.getX(), entity.getY(), entity.getZ(), stack);
    }
  }

  // ==================================================
  // Items
  // ==================================================

  @Override
  public ItemStack pushStack(RollingStock requester, ItemStack itemStack) {
    for (var side : Side.values()) {
      Iterable<RollingStock> targets =
          () -> requester.traverseTrain(side).iterator();
      for (var target : targets) {
        var cart = target.entity();
        var adaptor = cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
            .map(ContainerManipulator::of)
            .orElse(null);
        if (adaptor != null && canAcceptPushedItem(requester.entity(), cart, itemStack)) {
          itemStack = adaptor.insert(itemStack);
        }

        if (itemStack.isEmpty() || blocksItemRequests(cart, itemStack)) {
          break;
        }
      }

      if (itemStack.isEmpty()) {
        return ItemStack.EMPTY;
      }
    }
    return itemStack;
  }

  @Override
  public ItemStack pullStack(RollingStock requester, Predicate<ItemStack> filter) {
    for (var side : Side.values()) {
      Iterable<RollingStock> targets =
          () -> requester.traverseTrain(side).iterator();
      RollingStock resultProvider = null;
      SlotAccessor result = null;
      for (var target : targets) {
        var cart = target.entity();
        var slot = cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
            .map(ContainerManipulator::of)
            .flatMap(manipulator -> manipulator.findFirstExtractable(
                filter.and(stack -> canProvidePulledItem(requester.entity(), cart, stack))))
            .orElse(null);
        if (slot != null) {
          resultProvider = target;
          result = slot;
        }
      }

      if (result == null) {
        continue;
      }

      for (var target : targets) {
        if (target == resultProvider) {
          break;
        }
        if (blocksItemRequests(target.entity(), result.item())) {
          result = null;
          break;
        }
      }

      if (result != null) {
        return result.extract();
      }
    }

    return ItemStack.EMPTY;
  }

  private static boolean canAcceptPushedItem(AbstractMinecart requester, AbstractMinecart cart,
      ItemStack stack) {
    return !(cart instanceof ItemTransferHandler handler)
        || handler.canAcceptPushedItem(requester, stack);
  }

  private static boolean canProvidePulledItem(AbstractMinecart requester,
      AbstractMinecart cart, ItemStack stack) {
    return !(cart instanceof ItemTransferHandler handler)
        || handler.canProvidePulledItem(requester, stack);
  }

  private static boolean blocksItemRequests(AbstractMinecart cart, ItemStack stack) {
    return cart instanceof ItemTransferHandler handler
        ? !handler.canPassItemRequests(stack)
        : cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
            .map(IItemHandler::getSlots)
            .orElse(0) < NUM_SLOTS;
  }

  // ==================================================
  // Fluids
  // ==================================================

  @Override
  public FluidStack pushFluid(RollingStock requester, FluidStack fluidStack) {
    var remainder = fluidStack.copy();
    for (var side : Side.values()) {
      Iterable<RollingStock> targets = () -> requester.traverseTrain(side).iterator();
      for (var target : targets) {
        var cart = target.entity();
        if (canAcceptPushedFluid(requester.entity(), cart, remainder)) {
          var fluidHandler = cart.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
          if (fluidHandler != null) {
            var filled = fluidHandler.fill(remainder, IFluidHandler.FluidAction.EXECUTE);
            remainder.setAmount(remainder.getAmount() - filled);
          }
        }
        if (remainder.isEmpty() || blocksFluidRequests(cart, remainder)) {
          break;
        }
      }

      if (remainder.isEmpty()) {
        return FluidStack.EMPTY;
      }
    }
    return remainder;
  }

  @Override
  public FluidStack pullFluid(RollingStock requester, FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      return FluidStack.EMPTY;
    }

    for (var side : Side.values()) {
      Iterable<RollingStock> targets = () -> requester.traverseTrain(side).iterator();
      for (var target : targets) {
        var cart = target.entity();
        if (canProvidePulledFluid(requester.entity(), cart, fluidStack)) {
          var fluidHandler = cart.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
          if (fluidHandler != null) {
            var drained = fluidHandler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            if (!drained.isEmpty()) {
              return drained;
            }
          }
        }

        if (blocksFluidRequests(cart, fluidStack)) {
          break;
        }
      }
    }


    return FluidStack.EMPTY;
  }

  private static boolean canAcceptPushedFluid(AbstractMinecart requester,
      AbstractMinecart cart, FluidStack fluid) {
    return !(cart instanceof FluidMinecart fluidMinecart)
        || fluidMinecart.canAcceptPushedFluid(requester, fluid);
  }

  private static boolean canProvidePulledFluid(AbstractMinecart requester,
      AbstractMinecart cart, FluidStack fluid) {
    return !(cart instanceof FluidMinecart fluidMinecart)
        || fluidMinecart.canProvidePulledFluid(requester, fluid);
  }

  private static boolean blocksFluidRequests(AbstractMinecart cart, FluidStack fluid) {
    return cart instanceof FluidMinecart fluidMinecart
        ? !fluidMinecart.canPassFluidRequests(fluid)
        : cart.getCapability(ForgeCapabilities.FLUID_HANDLER)
            .map(fluidHandler -> !hasMatchingTank(fluidHandler, fluid))
            .orElse(true);
  }

  private static boolean hasMatchingTank(IFluidHandler handler, FluidStack fluid) {
    for (int i = 0; i < handler.getTanks(); i++) {
      if (handler.getTankCapacity(i) >= TANK_CAPACITY) {
        var tankFluid = handler.getFluidInTank(i);
        if (tankFluid.isEmpty() || tankFluid.isFluidEqual(fluid)) {
          return true;
        }
      }
    }
    return false;
  }
}
