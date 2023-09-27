/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.item;

import net.minecraft.world.item.ItemStack;

public interface Filter {

    default boolean matches(ItemStack matcher, ItemStack target) {
        return false;
    }
}
