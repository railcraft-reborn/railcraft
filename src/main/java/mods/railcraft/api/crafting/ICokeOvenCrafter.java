/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A manager for coke oven recipes.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ICokeOvenCrafter {
    int DEFAULT_COOK_TIME = 1800;

    /**
     * @param input An object that can be converted into an Ingredient. This includes,
     *              but is not limited to Ingredients, ItemStacks, Items, Blocks, and OreTag Strings.
     */
    default ICokeOvenRecipeBuilder newRecipe(Object input) {
        return new ICokeOvenRecipeBuilder() {};
    }

    /**
     * Gets the coke oven recipe that matches this input item stack.
     *
     * @param stack The input item stack
     * @return The matching recipe, may be {@code null}
     */
    default Optional<IRecipe> getRecipe(ItemStack stack) {
        return Optional.empty();
    }

    /**
     * Gets all the coke oven recipes registered in this manager.
     *
     * You can remove recipes from this list, but do not add them, it will throw an UnsupportedOperationException.
     *
     * @return The recipes registered
     */
    default List<IRecipe> getRecipes() {
        return Collections.emptyList();
    }

    /**
     * A coke oven recipe.
     */
    interface IRecipe extends ISimpleRecipe {

        /**
         * Gets the fluid output for this recipe.
         *
         * <p>Returns {@code null} if this recipe has no fluid products.</p>
         *
         * @return The fluid output
         */
        @Nullable
        FluidStack getFluidOutput();

        /**
         * Gets the item stack output for this recipe.
         *
         * @return The output item stack
         */
        ItemStack getOutput();
    }

    interface ICokeOvenRecipeBuilder extends
            IRecipeBuilder<ICokeOvenRecipeBuilder>,
            IRecipeBuilder.ISingleInputFeature,
            IRecipeBuilder.ISingleItemStackOutputFeature<ICokeOvenRecipeBuilder>,
            IRecipeBuilder.ITimeFeature<ICokeOvenRecipeBuilder> {

        default ICokeOvenRecipeBuilder fluid(@Nullable FluidStack outputFluid) {
            return this;
        }

        /**
         * Finalize and commit the recipe.
         */
        default void register() {}
    }
}
