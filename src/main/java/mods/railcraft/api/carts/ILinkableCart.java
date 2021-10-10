/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

/**
 * This interface should be implemented by any minecart that wishes to change the default linkage
 * behavior. It is NOT required to be able to link a cart, it merely gives you more control over the
 * process.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface ILinkableCart {

  /**
   * To disable linking altogether, return false here.
   *
   * @return True if this cart is linkable.
   */
  default boolean isLinkable() {
    return true;
  }

  /**
   * Check called when attempting to link carts.
   *
   * @param cart The cart that we are attempting to link with.
   * @return True if we can link with this cart.
   */
  default boolean canLink(AbstractMinecartEntity cart) {
    return isLinkable();
  }

  /**
   * Returns true if this cart has two links or false if it can only link with one cart.
   *
   * <p>
   * If {@link #isLinkable()} returns false, this method must return false, too.
   * </p>
   *
   * @return True if two links
   */
  default boolean hasTwoLinks() {
    return isLinkable();
  }

  /**
   * Gets the distance at which this cart can be linked. This is called on both carts and added
   * together to determine how close two carts need to be for a successful link. Default =
   * LinkageManager.LINKAGE_DISTANCE
   *
   * @param cart The cart that you are attempting to link with.
   * @return The linkage distance
   */
  default float getLinkageDistance(AbstractMinecartEntity cart) {
    return LinkageManager.LINKAGE_DISTANCE;
  }

  /**
   * Gets the optimal distance between linked carts. This is called on both carts and added together
   * to determine the optimal rest distance between linked carts. The LinkageManager will attempt to
   * maintain this distance between linked carts at all times. Default =
   * LinkageManager.OPTIMAL_DISTANCE
   *
   * @param cart The cart that you are linked with.
   * @return The optimal rest distance
   */
  default float getOptimalDistance(AbstractMinecartEntity cart) {
    return LinkageManager.OPTIMAL_DISTANCE;
  }

  /**
   * Return false if linked carts have no effect on the velocity of this cart. Use carefully, if you
   * link two carts that can't be adjusted, it will behave as if they are not linked.
   *
   * @param cart The cart doing the adjusting.
   * @return Whether the cart can have its velocity adjusted.
   */
  default boolean canBeAdjusted(AbstractMinecartEntity cart) {
    return isLinkable();
  }

  /**
   * Called upon successful link creation.
   *
   * @param cart The cart we linked with.
   */
  default void onLinkCreated(AbstractMinecartEntity cart) {}

  /**
   * Called when a link is broken (usually).
   *
   * @param cart The cart we were linked with.
   */
  default void onLinkBroken(AbstractMinecartEntity cart) {}
}
