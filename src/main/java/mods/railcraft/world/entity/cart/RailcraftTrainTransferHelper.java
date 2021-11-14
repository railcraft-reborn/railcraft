package mods.railcraft.world.entity.cart;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.api.carts.FluidMinecart;
import mods.railcraft.api.carts.IItemCart;
import mods.railcraft.api.carts.TrainTransferHelper;
import mods.railcraft.util.collections.StackKey;
import mods.railcraft.util.inventory.InventoryAdaptor;
import mods.railcraft.util.inventory.filters.StackFilters;
import mods.railcraft.world.level.material.fluid.FluidTools;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Utility class for simplifying moving items and fluids through a train.
 *
 * Created by CovertJaguar on 5/9/2015.
 */
public enum RailcraftTrainTransferHelper implements TrainTransferHelper {

  INSTANCE;

  private static final int NUM_SLOTS = 8;
  private static final int TANK_CAPACITY = 8 * FluidTools.BUCKET_VOLUME;

  /**
   * Offers an item stack to linked carts or drops it if no one wants it.
   */
  @Override
  public void offerOrDropItem(AbstractMinecartEntity cart, ItemStack stack) {
    stack = this.pushStack(cart, stack);
    if (!stack.isEmpty()) {
      InventoryHelper.dropItemStack(cart.level, cart.getX(), cart.getY(), cart.getZ(), stack);
    }
  }

  // ***************************************************************************************************************************
  // Items
  // ***************************************************************************************************************************
  @Override
  public ItemStack pushStack(AbstractMinecartEntity requester, ItemStack stack) {
    Iterable<AbstractMinecartEntity> carts =
        RailcraftLinkageManager.INSTANCE.linkIterator(requester,
            RailcraftLinkageManager.LinkType.LINK_A);
    stack = _pushStack(requester, carts, stack);
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }
    if (RailcraftLinkageManager.INSTANCE.hasLink(requester,
        RailcraftLinkageManager.LinkType.LINK_B)) {
      carts =
          RailcraftLinkageManager.INSTANCE.linkIterator(requester,
              RailcraftLinkageManager.LinkType.LINK_B);
      stack = _pushStack(requester, carts, stack);
    }
    return stack;
  }

  private ItemStack _pushStack(AbstractMinecartEntity requester,
      Iterable<AbstractMinecartEntity> carts, ItemStack stack) {
    for (AbstractMinecartEntity cart : carts) {
      InventoryAdaptor adaptor = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
          .map(InventoryAdaptor::of)
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
  public ItemStack pullStack(AbstractMinecartEntity requester, Predicate<ItemStack> filter) {
    Iterable<AbstractMinecartEntity> carts =
        RailcraftLinkageManager.INSTANCE.linkIterator(requester,
            RailcraftLinkageManager.LinkType.LINK_A);
    ItemStack stack = this._pullStack(requester, carts, filter);
    if (!stack.isEmpty()) {
      return stack;
    }
    carts = RailcraftLinkageManager.INSTANCE.linkIterator(requester,
        RailcraftLinkageManager.LinkType.LINK_B);
    return this._pullStack(requester, carts, filter);
  }

  private ItemStack _pullStack(AbstractMinecartEntity requester,
      Iterable<AbstractMinecartEntity> carts, Predicate<ItemStack> filter) {
    ItemStack result = ItemStack.EMPTY;
    AbstractMinecartEntity upTo = null;
    InventoryAdaptor targetInv = null;
    for (AbstractMinecartEntity cart : carts) {
      InventoryAdaptor inv = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
          .map(InventoryAdaptor::of)
          .orElse(null);
      if (inv != null) {
        Set<StackKey> items = inv.findAll(filter);
        for (StackKey stackKey : items) {
          ItemStack stack = stackKey.get();
          if (this.canProvidePulledItem(requester, cart, stack)) {
            ItemStack toRemove = inv.findOne(StackFilters.of(stack));
            if (!toRemove.isEmpty()) {
              result = toRemove;
              upTo = cart;
              targetInv = inv;
              break;
            }
          }
        }
      }
    }

    if (result.isEmpty()) {
      return ItemStack.EMPTY;
    }

    for (AbstractMinecartEntity cart : carts) {
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

  private boolean canAcceptPushedItem(AbstractMinecartEntity requester, AbstractMinecartEntity cart,
      ItemStack stack) {
    return !(cart instanceof IItemCart) || ((IItemCart) cart).canAcceptPushedItem(requester, stack);
  }

  private boolean canProvidePulledItem(AbstractMinecartEntity requester,
      AbstractMinecartEntity cart,
      ItemStack stack) {
    return !(cart instanceof IItemCart)
        || ((IItemCart) cart).canProvidePulledItem(requester, stack);
  }

  private boolean blocksItemRequests(AbstractMinecartEntity cart, ItemStack stack) {
    return cart instanceof IItemCart
        ? !((IItemCart) cart).canPassItemRequests(stack)
        : cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            .map(IItemHandler::getSlots)
            .orElse(0) < NUM_SLOTS;
  }

  @Override
  public Optional<IItemHandlerModifiable> getTrainItemHandler(AbstractMinecartEntity cart) {
    return Train.get(cart).flatMap(Train::getItemHandler);
  }

  // ***************************************************************************************************************************
  // Fluids
  // ***************************************************************************************************************************
  @Override
  public FluidStack pushFluid(AbstractMinecartEntity requester, FluidStack fluidStack) {
    Iterable<AbstractMinecartEntity> carts =
        RailcraftLinkageManager.INSTANCE.linkIterator(requester,
            RailcraftLinkageManager.LinkType.LINK_A);
    fluidStack = this._pushFluid(requester, carts, fluidStack);
    if (fluidStack == null)
      return null;
    if (RailcraftLinkageManager.INSTANCE.hasLink(requester,
        RailcraftLinkageManager.LinkType.LINK_B)) {
      carts =
          RailcraftLinkageManager.INSTANCE.linkIterator(requester,
              RailcraftLinkageManager.LinkType.LINK_B);
      fluidStack = this._pushFluid(requester, carts, fluidStack);
    }
    return fluidStack;
  }

  private @Nullable FluidStack _pushFluid(AbstractMinecartEntity requester,
      Iterable<AbstractMinecartEntity> carts,
      FluidStack fluidStack) {
    for (AbstractMinecartEntity cart : carts) {
      if (canAcceptPushedFluid(requester, cart, fluidStack)) {
        cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP)
            .ifPresent(fluidHandler -> fluidStack.setAmount(
                fluidStack.getAmount()
                    - fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE)));
      }
      if (fluidStack.getAmount() <= 0 || this.blocksFluidRequests(cart, fluidStack)) {
        break;
      }
    }
    if (fluidStack.getAmount() <= 0) {
      return null;
    }
    return fluidStack;
  }

  @Override
  public FluidStack pullFluid(AbstractMinecartEntity requester, FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      return FluidStack.EMPTY;
    }
    Iterable<AbstractMinecartEntity> carts =
        RailcraftLinkageManager.INSTANCE.linkIterator(requester,
            RailcraftLinkageManager.LinkType.LINK_A);
    FluidStack pulled = this._pullFluid(requester, carts, fluidStack);
    if (!pulled.isEmpty()) {
      return pulled;
    }
    carts = RailcraftLinkageManager.INSTANCE.linkIterator(requester,
        RailcraftLinkageManager.LinkType.LINK_B);
    return this._pullFluid(requester, carts, fluidStack);
  }

  private FluidStack _pullFluid(AbstractMinecartEntity requester,
      Iterable<AbstractMinecartEntity> carts,
      FluidStack fluidStack) {
    for (AbstractMinecartEntity cart : carts) {
      if (canProvidePulledFluid(requester, cart, fluidStack)) {
        IFluidHandler fluidHandler =
            cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN)
                .orElse(null);
        if (fluidHandler != null) {
          FluidStack drained = fluidHandler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
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

  private boolean canAcceptPushedFluid(AbstractMinecartEntity requester,
      AbstractMinecartEntity cart,
      FluidStack fluid) {
    IFluidHandler fluidHandler = cart
        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).orElse(null);
    if (fluidHandler == null) {
      return false;
    }
    if (cart instanceof FluidMinecart) {
      return ((FluidMinecart) cart).canAcceptPushedFluid(requester, fluid);
    }
    return fluidHandler.fill(new FluidStack(fluid, 1), IFluidHandler.FluidAction.SIMULATE) > 0;
  }

  private boolean canProvidePulledFluid(AbstractMinecartEntity requester,
      AbstractMinecartEntity cart, FluidStack fluid) {
    IFluidHandler fluidHandler =
        cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN)
            .orElse(null);
    if (fluidHandler == null) {
      return false;
    }
    if (cart instanceof FluidMinecart) {
      return ((FluidMinecart) cart).canProvidePulledFluid(requester, fluid);
    }
    return !fluidHandler.drain(new FluidStack(fluid, 1), IFluidHandler.FluidAction.SIMULATE)
        .isEmpty();
  }

  private boolean blocksFluidRequests(AbstractMinecartEntity cart, FluidStack fluid) {
    return cart instanceof FluidMinecart
        ? !((FluidMinecart) cart).canPassFluidRequests(fluid)
        : cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            .map(fluidHandler -> !this.hasMatchingTank(fluidHandler, fluid))
            .orElse(true);
  }

  private boolean hasMatchingTank(IFluidHandler handler, FluidStack fluid) {
    for (int i = 0; i < handler.getTanks(); i++) {
      if (handler.getTankCapacity(i) >= TANK_CAPACITY) {
        FluidStack tankFluid = handler.getFluidInTank(i);
        if (tankFluid.isEmpty() || tankFluid.isFluidEqual(fluid)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Optional<IFluidHandler> getTrainFluidHandler(AbstractMinecartEntity cart) {
    return Train.get(cart).flatMap(Train::getFluidHandler);
  }
}
