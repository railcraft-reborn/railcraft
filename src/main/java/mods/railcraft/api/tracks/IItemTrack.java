/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import net.minecraft.item.ItemStack;

public interface IItemTrack {
    TrackType getTrackType(ItemStack stack);
}
