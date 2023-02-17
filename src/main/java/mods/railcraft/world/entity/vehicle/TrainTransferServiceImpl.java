package mods.railcraft.world.entity.vehicle;

import java.util.function.Predicate;
import java.util.stream.Stream;
import mods.railcraft.api.carts.FluidMinecart;
import mods.railcraft.api.carts.ItemTransferHandler;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.api.carts.TrainTransferService;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import net.minecraft.core.Direction;
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
      Containers.dropItemStack(entity.getLevel(),
          entity.getX(), entity.getY(), entity.getZ(), stack);
    }
  }

  // ==================================================
  // Items
  // ==================================================

  @Override
  public ItemStack pushStack(RollingStock requester, ItemStack stack) {
    Iterable<RollingStock> carts =
        () -> Stream.concat(
            requester.traverseTrain(Side.BACK),
            requester.traverseTrain(Side.FRONT)).iterator();
    for (var extension : carts) {
      var cart = extension.entity();
      var adaptor = cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
          .map(ContainerManipulator::of)
          .orElse(null);
      if (adaptor != null && this.canAcceptPushedItem(requester.entity(), cart, stack)) {
        stack = adaptor.addStack(stack);
      }

      if (stack.isEmpty() || this.blocksItemRequests(cart, stack)) {
        break;
      }
    }
    return stack;
  }

  @Override
  public ItemStack pullStack(RollingStock rollingStock, Predicate<ItemStack> filter) {
    ItemStack result = ItemStack.EMPTY;
    AbstractMinecart upTo = null;
    ContainerManipulator<?> targetInv = null;
    Iterable<RollingStock> carts =
        () -> Stream.concat(
            rollingStock.traverseTrain(Side.FRONT),
            rollingStock.traverseTrain(Side.BACK)).iterator();
    for (var extension : carts) {
      var cart = extension.entity();
      var inv = cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
          .map(ContainerManipulator::of)
          .orElse(null);
      if (inv == null) {
        continue;
      }

      for (var stackKey : inv.findAll(filter)) {
        var stack = stackKey.copyStack();
        if (!this.canProvidePulledItem(rollingStock.entity(), cart, stack)) {
          continue;
        }

        var toRemove = inv.findOne(stack::sameItem);
        if (toRemove.isEmpty()) {
          continue;
        }

        result = toRemove;
        upTo = cart;
        targetInv = inv;
        break;
      }
    }

    if (result.isEmpty()) {
      return ItemStack.EMPTY;
    }

    for (var extension : carts) {
      var entity = extension.entity();
      if (entity == upTo) {
        break;
      }
      if (this.blocksItemRequests(entity, result)) {
        return ItemStack.EMPTY;
      }
    }

    if (targetInv != null) {
      return targetInv.removeOneItem(result);
    }

    return ItemStack.EMPTY;
  }

  private boolean canAcceptPushedItem(AbstractMinecart requester, AbstractMinecart cart,
      ItemStack stack) {
    return !(cart instanceof ItemTransferHandler) || ((ItemTransferHandler) cart).canAcceptPushedItem(requester, stack);
  }

  private boolean canProvidePulledItem(AbstractMinecart requester,
      AbstractMinecart cart,
      ItemStack stack) {
    return !(cart instanceof ItemTransferHandler)
        || ((ItemTransferHandler) cart).canProvidePulledItem(requester, stack);
  }

  private boolean blocksItemRequests(AbstractMinecart cart, ItemStack stack) {
    return cart instanceof ItemTransferHandler itemCart
        ? !itemCart.canPassItemRequests(stack)
        : cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
            .map(IItemHandler::getSlots)
            .orElse(0) < NUM_SLOTS;
  }

  // ==================================================
  // Fluids
  // ==================================================

  @Override
  public FluidStack pushFluid(RollingStock requester, FluidStack fluidStack) {
    Iterable<RollingStock> carts =
        () -> Stream.concat(
            requester.traverseTrain(Side.BACK),
            requester.traverseTrain(Side.FRONT)).iterator();
    for (var extension : carts) {
      var cart = extension.entity();
      if (this.canAcceptPushedFluid(requester.entity(), cart, fluidStack)) {
        cart.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.UP)
            .ifPresent(fluidHandler -> fluidStack.setAmount(
                fluidStack.getAmount()
                    - fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE)));
      }
      if (fluidStack.getAmount() <= 0 || this.blocksFluidRequests(cart, fluidStack)) {
        break;
      }
    }
    return fluidStack;
  }

  @Override
  public FluidStack pullFluid(RollingStock requester, FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      return FluidStack.EMPTY;
    }
    Iterable<RollingStock> carts =
        () -> Stream.concat(
            requester.traverseTrain(Side.BACK),
            requester.traverseTrain(Side.FRONT)).iterator();
    for (var extension : carts) {
      var cart = extension.entity();
      if (this.canProvidePulledFluid(requester.entity(), cart, fluidStack)) {
        var fluidHandler = cart.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN)
            .orElse(null);
        if (fluidHandler != null) {
          var drained = fluidHandler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
          if (!drained.isEmpty()) {
            return drained;
          }
        }
      }

      if (this.blocksFluidRequests(cart, fluidStack)) {
        break;
      }
    }
    return FluidStack.EMPTY;
  }

  private boolean canAcceptPushedFluid(AbstractMinecart requester,
      AbstractMinecart cart, FluidStack fluid) {
    var fluidHandler = cart.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.UP)
        .orElse(null);
    if (fluidHandler == null) {
      return false;
    }
    if (cart instanceof FluidMinecart fluidMinecart) {
      return fluidMinecart.canAcceptPushedFluid(requester, fluid);
    }
    return fluidHandler.fill(new FluidStack(fluid, 1), IFluidHandler.FluidAction.SIMULATE) > 0;
  }

  private boolean canProvidePulledFluid(AbstractMinecart requester,
      AbstractMinecart cart, FluidStack fluid) {
    var fluidHandler = cart.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN)
        .orElse(null);
    if (fluidHandler == null) {
      return false;
    }
    if (cart instanceof FluidMinecart fluidMinecart) {
      return fluidMinecart.canProvidePulledFluid(requester, fluid);
    }
    return !fluidHandler.drain(new FluidStack(fluid, 1), IFluidHandler.FluidAction.SIMULATE)
        .isEmpty();
  }

  private boolean blocksFluidRequests(AbstractMinecart cart, FluidStack fluid) {
    return cart instanceof FluidMinecart fluidMinecart
        ? !fluidMinecart.canPassFluidRequests(fluid)
        : cart.getCapability(ForgeCapabilities.FLUID_HANDLER)
            .map(fluidHandler -> !this.hasMatchingTank(fluidHandler, fluid))
            .orElse(true);
  }

  private boolean hasMatchingTank(IFluidHandler handler, FluidStack fluid) {
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
