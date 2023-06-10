/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import net.minecraft.world.item.DyeColor;

public interface Paintable {

  default float[] getPrimaryColor() {
    return getPrimaryDyeColor().getTextureDiffuseColors();
  }

  default float[] getSecondaryColor() {
    return getSecondaryDyeColor().getTextureDiffuseColors();
  }

  DyeColor getPrimaryDyeColor();

  DyeColor getSecondaryDyeColor();
}
