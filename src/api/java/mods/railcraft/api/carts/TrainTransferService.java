/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * This interface is the API facing wrapper for an internal helper class that makes it simple to
 * pass items and fluids around within a Train.
 * <p/>
 * The helper object can be accessed from CartTools and is aware of the IItemCart and IFluidCart
 * interfaces.
 * <p/>
 *
 * @see CartUtil
 * @see mods.railcraft.api.carts.ItemTransferHandler
 * @see mods.railcraft.api.carts.FluidMinecart
 */
public interface TrainTransferService {

  // ==================================================
  // Items
  // ==================================================

  /**
   * Will attempt to push an ItemStack to the Train.
   *
   * @param requester the source AbstractMinecartEntity
   * @param stack the ItemStack to be pushed
   * @return the ItemStack that remains after any pushed items were removed, or null if it was fully
   *         pushed
   * @see mods.railcraft.api.carts.FluidMinecart
   */
  default ItemStack pushStack(RollingStock requester, ItemStack stack) {
    return stack;
  }

  /**
   * Will request an item from the Train.
   *
   * @param requester the source AbstractMinecartEntity
   * @param filter a Predicate<ItemStack> that defines the requested item
   * @return the ItemStack pulled from the Train, or null if the request cannot be met
   * @see mods.railcraft.api.carts.ItemTransferHandler
   */
  default ItemStack pullStack(RollingStock requester, Predicate<ItemStack> filter) {
    return ItemStack.EMPTY;
  }

  /**
   * Offers an item stack to the Train or drops it if no one wants it.
   *
   * @param requester the source AbstractMinecartEntity
   * @param stack the ItemStack to be offered
   */
  default void offerOrDropItem(RollingStock requester, ItemStack stack) {}

  // ==================================================
  // Fluids
  // ==================================================

  /**
   * Will attempt to push fluid to the Train.
   *
   * @param requester the source AbstractMinecartEntity
   * @param fluidStack the amount and type of Fluid to be pushed
   * @return the FluidStack that remains after any pushed Fluid was removed, or empty if it was
   *         fully pushed
   * @see mods.railcraft.api.carts.FluidMinecart
   */
  default FluidStack pushFluid(RollingStock requester, FluidStack fluidStack) {
    return fluidStack;
  }

  /**
   * Will request fluid from the Train.
   *
   * @param requester the source AbstractMinecartEntity
   * @param fluidStack the amount and type of Fluid requested
   * @return the FluidStack pulled from the Train, or empty if the request cannot be met
   * @see mods.railcraft.api.carts.FluidMinecart
   */
  default FluidStack pullFluid(RollingStock requester, FluidStack fluidStack) {
    return null;
  }
}
