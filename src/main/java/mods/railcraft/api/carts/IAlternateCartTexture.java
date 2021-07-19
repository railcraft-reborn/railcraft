/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import net.minecraft.util.ResourceLocation;

/**
 * Used to render a cart with a custom texture using Railcraft's cart renderer.
 * You could always write your own renderer, of course.
 *
 * <p>This texture is the texture of the cart body.</p>
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IAlternateCartTexture {

    /**
     * The texture to give the cart. If you return null, the default is used.
     *
     * @return the texture file
     */
    ResourceLocation getTextureFile();
}
