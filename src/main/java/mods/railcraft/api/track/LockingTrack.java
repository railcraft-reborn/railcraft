/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

/**
 * Any rail tile entity that can completely halt all cart movement should implement this interface.
 * (Used in collision handling)
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface LockingTrack {

  boolean isCartLocked(AbstractMinecartEntity cart);

  void releaseCart();
}
