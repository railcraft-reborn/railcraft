package mods.railcraft.world.entity.vehicle;

import java.util.Optional;
import java.util.function.Predicate;
import mods.railcraft.api.carts.FluidMinecart;
import mods.railcraft.api.carts.IItemCart;
import mods.railcraft.api.carts.Link;
import mods.railcraft.api.carts.LinkageManager;
import mods.railcraft.api.carts.TrainTransferHelper;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.world.level.material.fluid.FluidTools;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Utility class for simplifying moving items and fluids through a train.
 *
 * Created by CovertJaguar on 5/9/2015.
 */
public enum TrainTransferHelperImpl implements TrainTransferHelper {

  INSTANCE;

  private static final int NUM_SLOTS = 8;
  private static final int TANK_CAPACITY = 8 * FluidTools.BUCKET_VOLUME;

  /**
   * Offers an item stack to linked carts or drops it if no one wants it.
   */
  @Override
  public void offerOrDropItem(AbstractMinecart cart, ItemStack stack) {
    stack = this.pushStack(cart, stack);
    if (!stack.isEmpty()) {
      Containers.dropItemStack(cart.level, cart.getX(), cart.getY(), cart.getZ(), stack);
    }
  }

  // ***************************************************************************************************************************
  // Items
  // ***************************************************************************************************************************
  @Override
  public ItemStack pushStack(AbstractMinecart requester, ItemStack stack) {
    Iterable<AbstractMinecart> carts =
        LinkageManagerImpl.INSTANCE.linkIterator(requester,
            Link.FRONT);
    stack = _pushStack(requester, carts, stack);
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }
    if (LinkageManager.hasLink(requester, Link.BACK)) {
      carts = LinkageManagerImpl.INSTANCE.linkIterator(requester, Link.BACK);
      stack = _pushStack(requester, carts, stack);
    }
    return stack;
  }

  private ItemStack _pushStack(AbstractMinecart requester,
      Iterable<AbstractMinecart> carts, ItemStack stack) {
    for (AbstractMinecart cart : carts) {
      var adaptor = cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
          .map(ContainerManipulator::of)
          .orElse(null);
      if (adaptor != null && this.canAcceptPushedItem(requester, cart, stack)) {
        stack = adaptor.addStack(stack);
      }

      if (stack.isEmpty() || this.blocksItemRequests(cart, stack)) {
        break;
      }
    }
    return stack;
  }

  @Override
  public ItemStack pullStack(AbstractMinecart requester, Predicate<ItemStack> filter) {
    Iterable<AbstractMinecart> carts =
        LinkageManagerImpl.INSTANCE.linkIterator(requester,
            Link.FRONT);
    ItemStack stack = this._pullStack(requester, carts, filter);
    if (!stack.isEmpty()) {
      return stack;
    }
    carts = LinkageManagerImpl.INSTANCE.linkIterator(requester,
        Link.BACK);
    return this._pullStack(requester, carts, filter);
  }

  private ItemStack _pullStack(AbstractMinecart requester,
      Iterable<AbstractMinecart> carts, Predicate<ItemStack> filter) {
    ItemStack result = ItemStack.EMPTY;
    AbstractMinecart upTo = null;
    ContainerManipulator<?> targetInv = null;
    for (var cart : carts) {
      var inv = cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
          .map(ContainerManipulator::of)
          .orElse(null);
      if (inv == null) {
        continue;
      }

      for (var stackKey : inv.findAll(filter)) {
        var stack = stackKey.copyStack();
        if (!this.canProvidePulledItem(requester, cart, stack)) {
          continue;
        }

        var toRemove = inv.findOne(StackFilter.of(stack));
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

    for (AbstractMinecart cart : carts) {
      if (cart == upTo) {
        break;
      }
      if (blocksItemRequests(cart, result)) {
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
    return !(cart instanceof IItemCart) || ((IItemCart) cart).canAcceptPushedItem(requester, stack);
  }

  private boolean canProvidePulledItem(AbstractMinecart requester,
      AbstractMinecart cart,
      ItemStack stack) {
    return !(cart instanceof IItemCart)
        || ((IItemCart) cart).canProvidePulledItem(requester, stack);
  }

  private boolean blocksItemRequests(AbstractMinecart cart, ItemStack stack) {
    return cart instanceof IItemCart
        ? !((IItemCart) cart).canPassItemRequests(stack)
        : cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
            .map(IItemHandler::getSlots)
            .orElse(0) < NUM_SLOTS;
  }

  @Override
  public Optional<IItemHandlerModifiable> getTrainItemHandler(AbstractMinecart cart) {
    return Train.get(cart).flatMap(Train::getItemHandler);
  }

  // ***************************************************************************************************************************
  // Fluids
  // ***************************************************************************************************************************
  @Override
  public FluidStack pushFluid(AbstractMinecart requester, FluidStack fluidStack) {
    var carts = LinkageManagerImpl.INSTANCE.linkIterator(requester, Link.FRONT);
    fluidStack = this._pushFluid(requester, carts, fluidStack);
    if (fluidStack.isEmpty()) {
      return FluidStack.EMPTY;
    }
    if (LinkageManager.hasLink(requester, Link.BACK)) {
      carts = LinkageManagerImpl.INSTANCE.linkIterator(requester, Link.BACK);
      fluidStack = this._pushFluid(requester, carts, fluidStack);
    }
    return fluidStack;
  }

  private FluidStack _pushFluid(AbstractMinecart requester,
      Iterable<AbstractMinecart> carts, FluidStack fluidStack) {
    for (var cart : carts) {
      if (this.canAcceptPushedFluid(requester, cart, fluidStack)) {
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
  public FluidStack pullFluid(AbstractMinecart requester, FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      return FluidStack.EMPTY;
    }
    var carts = LinkageManagerImpl.INSTANCE.linkIterator(requester, Link.FRONT);
    FluidStack pulled = this._pullFluid(requester, carts, fluidStack);
    if (!pulled.isEmpty()) {
      return pulled;
    }
    carts = LinkageManagerImpl.INSTANCE.linkIterator(requester, Link.BACK);
    return this._pullFluid(requester, carts, fluidStack);
  }

  private FluidStack _pullFluid(AbstractMinecart requester,
      Iterable<AbstractMinecart> carts, FluidStack fluidStack) {
    for (var cart : carts) {
      if (this.canProvidePulledFluid(requester, cart, fluidStack)) {
        var fluidHandler =
            cart.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN)
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

  @Override
  public Optional<IFluidHandler> getTrainFluidHandler(AbstractMinecart cart) {
    return Train.get(cart).flatMap(Train::getFluidHandler);
  }
}
