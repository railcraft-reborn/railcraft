/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * This interface is the API facing wrapper for an internal helper class that makes it simple to
 * pass items and fluids around within a Train.
 * <p/>
 * The helper object can be accessed from CartTools and is aware of the IItemCart and IFluidCart
 * interfaces.
 * <p/>
 * Created by CovertJaguar on 5/11/2015.
 *
 * @see CartUtil
 * @see mods.railcraft.api.carts.IItemCart
 * @see mods.railcraft.api.carts.FluidCart
 */
public interface ITrainTransferHelper {
  // ***************************************************************************************************************************
  // Items
  // ***************************************************************************************************************************

  /**
   * Will attempt to push an ItemStack to the Train.
   *
   * @param requester the source AbstractMinecartEntity
   * @param stack the ItemStack to be pushed
   * @return the ItemStack that remains after any pushed items were removed, or null if it was fully
   *         pushed
   * @see mods.railcraft.api.carts.FluidCart
   */
  default ItemStack pushStack(AbstractMinecartEntity requester, ItemStack stack) {
    return stack;
  }

  /**
   * Will request an item from the Train.
   *
   * @param requester the source AbstractMinecartEntity
   * @param filter a Predicate<ItemStack> that defines the requested item
   * @return the ItemStack pulled from the Train, or null if the request cannot be met
   * @see mods.railcraft.api.carts.IItemCart
   */
  default ItemStack pullStack(AbstractMinecartEntity requester, Predicate<ItemStack> filter) {
    return ItemStack.EMPTY;
  }

  /**
   * Offers an item stack to the Train or drops it if no one wants it.
   *
   * @param requester the source AbstractMinecartEntity
   * @param stack the ItemStack to be offered
   */
  default void offerOrDropItem(AbstractMinecartEntity requester, ItemStack stack) {}

  /**
   * Returns an IItemHandlerModifiable with represents the entire train.
   *
   * @param cart a cart in the train
   */
  default Optional<IItemHandlerModifiable> getTrainItemHandler(AbstractMinecartEntity cart) {
    return Optional.empty();
  }


  // ***************************************************************************************************************************
  // Fluids
  // ***************************************************************************************************************************

  /**
   * Will attempt to push fluid to the Train.
   *
   * @param requester the source AbstractMinecartEntity
   * @param fluidStack the amount and type of Fluid to be pushed
   * @return the FluidStack that remains after any pushed Fluid was removed, or null if it was fully
   *         pushed
   * @see mods.railcraft.api.carts.FluidCart
   */
  default @Nullable FluidStack pushFluid(AbstractMinecartEntity requester, FluidStack fluidStack) {
    return fluidStack;
  }

  /**
   * Will request fluid from the Train.
   *
   * @param requester the source AbstractMinecartEntity
   * @param fluidStack the amount and type of Fluid requested
   * @return the FluidStack pulled from the Train, or null if the request cannot be met
   * @see mods.railcraft.api.carts.FluidCart
   */
  default @Nullable FluidStack pullFluid(AbstractMinecartEntity requester,
      @Nullable FluidStack fluidStack) {
    return null;
  }

  /**
   * Returns an IFluidHandler with represents the entire train.
   *
   * @param cart a cart in the train
   */
  default Optional<IFluidHandler> getTrainFluidHandler(AbstractMinecartEntity cart) {
    return Optional.empty();
  }
}
