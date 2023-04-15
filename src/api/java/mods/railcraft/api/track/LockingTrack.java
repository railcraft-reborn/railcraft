/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * Any rail tile entity that can completely halt all cart movement should implement this interface.
 * (Used in collision handling)
 */
public interface LockingTrack {

  boolean isCartLocked(AbstractMinecart cart);

  void releaseCart();
}
