/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import java.util.Optional;

/**
 * This interface should be implemented by any minecart that wishes to change the default linkage
 * behavior. It is NOT required to be able to link a cart, it merely gives you more control over the
 * process.
 */
public interface Linkable {

  default Optional<Side> disabledSide() {
    return Optional.empty();
  }

  /**
   * Check called when attempting to link carts.
   *
   * @param rollingStock The cart that we are attempting to link with.
   * @return True if we can link with this cart.
   */
  default boolean isLinkableWith(RollingStock rollingStock) {
    return true;
  }

  /**
   * Gets the distance at which this cart can be linked. This is called on both carts and added
   * together to determine how close two carts need to be for a successful link. Default =
   * LinkageManager.LINKAGE_DISTANCE
   *
   * @param rollingStock The cart that you are attempting to link with.
   * @return The linkage distance
   */
  default float getLinkageDistance(RollingStock rollingStock) {
    return RollingStock.MAX_LINK_DISTANCE;
  }

  /**
   * Gets the optimal distance between linked carts. This is called on both carts and added together
   * to determine the optimal rest distance between linked carts. The LinkageManager will attempt to
   * maintain this distance between linked carts at all times. Default =
   * LinkageManager.OPTIMAL_DISTANCE
   *
   * @param rollingStock The cart that you are linked with.
   * @return The optimal rest distance
   */
  default float getOptimalDistance(RollingStock rollingStock) {
    return RollingStock.OPTIMAL_LINK_DISTANCE;
  }

  /**
   * Return false if linked carts have no effect on the velocity of this cart. Use carefully, if you
   * link two carts that can't be adjusted, it will behave as if they are not linked.
   *
   * @param rollingStock - the rolling stock doing the adjusting.
   * @return Whether the rolling stock can have its velocity adjusted.
   */
  default boolean canBeAdjusted(RollingStock rollingStock) {
    return true;
  }

  /**
   * Called upon successful link creation.
   *
   * @param rollingStock - the rolling stock we linked with.
   */
  default void linked(RollingStock rollingStock) {}

  /**
   * Called when a link is broken (usually).
   *
   * @param rollingStock - the rolling stock we were linked with.
   */
  default void unlinked(RollingStock rollingStock) {}
}
