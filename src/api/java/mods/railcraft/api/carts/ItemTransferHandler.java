/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import net.minecraft.world.item.ItemStack;

/**
 * Allows minecarts to control item transfer behaviour through a train.
 */
public interface ItemTransferHandler {
  /**
   * This function controls whether a cart can pass push or pull requests. This function is only
   * called if the cart cannot fulfill the request itself.
   * <p/>
   * If this interface is not implemented, a default value will be inferred based on the size of the
   * inventory of the Minecart. Anything with eight or more slots will be assumed to allow passage.
   *
   * @return true if can pass push and pull requests
   */
  boolean canPassItemRequests(ItemStack stack);

  /**
   * This function controls whether a cart will accept a pushed Item. Even if this function returns
   * true, there still must be a slot that accepts the item in question before it can be added to
   * the cart.
   * <p/>
   * If this interface is not implemented, it is assumed to be true.
   *
   * @param requester - the {@link RollingStock} that initiated the action
   * @param stack - the {@link ItemStack}
   * @return true if the cart can accept the item
   */
  boolean canAcceptPushedItem(RollingStock requester, ItemStack stack);

  /**
   * This function controls whether a cart will fulfill a pull request for a specific item. Even if
   * this function returns true, there still must be a slot that can extract the item in question
   * before it can be removed from the cart.
   * <p/>
   * If this interface is not implemented, it is assumed to be true.
   *
   * @param requester - the {@link RollingStock} that initiated the action
   * @param stack - the {@link ItemStack}
   * @return true if the cart can provide the item
   */
  boolean canProvidePulledItem(RollingStock requester, ItemStack stack);
}
