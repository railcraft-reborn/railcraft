/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import mods.railcraft.api.core.Ownable;
import net.minecraft.world.item.ItemStack;

public interface Routable extends Ownable {

  String getDestination();

  boolean setDestination(ItemStack ticket);
}
