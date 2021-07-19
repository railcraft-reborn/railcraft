/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.crafting;

import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IRockCrusherCrafter {
    int PROCESS_TIME = 100;

    /**
     * Begins the definition of a Rock Crusher recipe.
     *
     * @param input An object that can be converted into an Ingredient. This includes,
     *              but is not limited to Ingredients, ItemStacks, Items, Blocks, and OreTag Strings.
     */
    default IRockCrusherRecipeBuilder makeRecipe(Object input) {
        return new IRockCrusherRecipeBuilder() {};
    }

    /**
     * This function will locate the highest priority recipe that successfully matches against the given ItemStack.
     */
    default Optional<IRecipe> getRecipe(ItemStack input) {
        return Optional.empty();
    }

    /**
     * You can remove recipes from this list, but do not add them, it will throw an UnsupportedOperationException.
     */
    default List<IRecipe> getRecipes() {
        return Collections.emptyList();
    }

    interface IRockCrusherRecipeBuilder extends
            IRecipeBuilder<IRockCrusherRecipeBuilder>,
            IRecipeBuilder.ISingleInputFeature,
            IRecipeBuilder.ITimeFeature<IRockCrusherRecipeBuilder> {

        default IRockCrusherRecipeBuilder addOutput(IOutputEntry entry) {
            return this;
        }

        default IRockCrusherRecipeBuilder addOutput(ItemStack output, IGenRule rule) {
            return this;
        }

        default IRockCrusherRecipeBuilder addOutput(ItemStack output, float chance) {
            return this;
        }

        default IRockCrusherRecipeBuilder addOutput(ItemStack output) {
            return addOutput(output, 1);
        }

        /**
         * Finalize and commit the recipe.
         */
        default void register() {}
    }

    /**
     * A Rock Crusher Recipe
     */
    interface IRecipe extends ISimpleRecipe {

        /**
         * Returns a list containing each output entry.
         */
        List<IOutputEntry> getOutputs();

        /**
         * Returns a list of outputs after it has passed through the predicate processor.
         */
        default List<ItemStack> pollOutputs(Random random) {
            return getOutputs().stream()
                    .filter(entry -> entry.getGenRule().test(random))
                    .map(IOutputEntry::getOutput)
                    .collect(Collectors.toList());
        }

    }
}
