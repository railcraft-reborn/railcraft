/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import mods.railcraft.api.core.IOwnable;
import net.minecraft.item.ItemStack;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface IRoutableCart extends IOwnable {

    String getDestination();

    boolean setDestination(ItemStack ticket);

}
