/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Created by CovertJaguar on 9/1/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IRailcraftRegistryEntry<T extends IForgeRegistryEntry<T>> extends IForgeRegistryEntry<T> {
    default ResourceLocation getRegistryName(IVariantEnum variant) {
        return new ResourceLocation(getRegistryName().getNamespace(),
                getRegistryName().getPath() + "." + variant.getResourcePathSuffix());
    }
}
