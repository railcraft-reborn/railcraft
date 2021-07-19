/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

/**
 * Created by CovertJaguar on 12/22/2018 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ISimpleRecipe {
    /**
     * Returns a resource location that describes the recipe.
     * It is only used for logging, so it doesn't need to be exact or unique.
     *
     * @return A resource location that describes the recipe.
     */
    ResourceLocation getName();

    /**
     * Gets the input for this recipe.
     *
     * @return The input matcher
     */
    Ingredient getInput();

    /**
     * Gets the cooking time/heat value/process time for this recipe, in ticks.
     *
     * @return The tick time
     */
    int getTickTime(ItemStack input);
}
