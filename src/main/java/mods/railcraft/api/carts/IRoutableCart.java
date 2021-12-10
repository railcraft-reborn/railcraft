/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import mods.railcraft.api.core.Ownable;
import net.minecraft.world.item.ItemStack;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface IRoutableCart extends Ownable {

    String getDestination();

    boolean setDestination(ItemStack ticket);

}
