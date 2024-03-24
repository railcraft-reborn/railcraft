/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import net.minecraft.resources.ResourceLocation;

/**
 * This interface it used to define an item that can be used as a bore head for the Tunnel Bore.
 */
public interface TunnelBoreHead {

  /**
   * Return the texture file used for this bore head.
   *
   * @return The texture file path
   */
  ResourceLocation getTextureLocation();
}
