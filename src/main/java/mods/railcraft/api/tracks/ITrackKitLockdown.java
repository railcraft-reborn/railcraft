/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

/**
 * Any rail tile entity that can completely halt
 * all cart movement should implement this interface.
 * (Used in collision handling)
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ITrackKitLockdown extends ITrackKitInstance {

    boolean isCartLockedDown(AbstractMinecartEntity cart);

    void releaseCart();
}
