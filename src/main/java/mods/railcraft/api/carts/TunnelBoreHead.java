/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import net.minecraft.resources.ResourceLocation;

/**
 * This interface it used to define an item that can be used as a bore head for the Tunnel Bore.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface TunnelBoreHead {

  /**
   * Return the texture file used for this bore head.
   *
   * @return The texture file path
   */
  ResourceLocation getTextureLocation();

  /**
   * Return the dig speed modifier of this bore head.
   *
   * This value controls how much faster or slow this bore head mines each layer compared to the
   * default time.
   *
   * @return The dig speed modifier
   */
  double getDigModifier();
}
