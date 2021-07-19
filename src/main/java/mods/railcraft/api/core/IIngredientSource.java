/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import javax.annotation.Nullable;

/**
 * An interface for containers that can provide ingredients.
 */
public interface IIngredientSource {

    default ItemStack getStack(int qty) {
        throw new UnsupportedOperationException();
    }

    default ItemStack getStack() {
        return getStack(1);
    }

    /**
     * Gets an ingredient for this container. If one is not available,
     * {@link Ingredient#EMPTY} is returned.
     *
     * @return The ingredient
     */
    Ingredient getIngredient();

    /**
     * Gets an ingredient for this container of a certain variant.
     *
     * @param variant The specified variant
     * @return The ingredient
     */
    default Ingredient getIngredient(@Nullable IVariantEnum variant) {
        throw new UnsupportedOperationException();
    }
}
