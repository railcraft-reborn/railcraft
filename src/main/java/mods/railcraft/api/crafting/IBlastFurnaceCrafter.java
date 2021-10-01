/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.crafting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.item.ItemStack;

public interface IBlastFurnaceCrafter {
  /**
   * The default number of ticks it takes to turn an ingot of iron into an ingot of steel.
   */
  int SMELT_TIME = 1280;

  /**
   * Add a fuel source. It uses the standard Furnace cookTime for the heat value.
   *
   * By default, it will look up the heat value the vanilla Furnace uses.
   *
   * @param input An object that can be converted into an Ingredient. This includes, but is not
   *        limited to Ingredients, ItemStacks, Items, Blocks, and OreTag Strings.
   */
  default IFuelBuilder newFuel(Object input) {
    return new IFuelBuilder() {};
  }

  /**
   * Begins the definition of a Blast Furnace recipe.
   *
   * @param input An object that can be converted into an Ingredient. This includes, but is not
   *        limited to Ingredients, ItemStacks, Items, Blocks, and OreTag Strings.
   */
  default IBlastFurnaceRecipeBuilder newRecipe(Object input) {
    return new IBlastFurnaceRecipeBuilder() {};
  }

  /**
   * You can remove fuels from this list, but do not add them, it will throw an
   * UnsupportedOperationException.
   */
  default List<ISimpleRecipe> getFuels() {
    return Collections.emptyList();
  }

  /**
   * You can remove recipes from this list, but do not add them, it will throw an
   * UnsupportedOperationException.
   */
  default List<IRecipe> getRecipes() {
    return Collections.emptyList();
  }

  default Optional<ISimpleRecipe> getFuel(ItemStack stack) {
    return Optional.empty();
  }

  default Optional<IRecipe> getRecipe(ItemStack stack) {
    return Optional.empty();
  }

  /**
   * Represents a blast furnace recipe.
   */
  interface IRecipe extends ISimpleRecipe {

    /**
     * Gets the output for this recipe.
     *
     * @return The output, safe to modify
     */
    ItemStack getOutput();

    /**
     * Gets the slag output for this recipe.
     */
    int getSlagOutput();
  }

  interface IBlastFurnaceRecipeBuilder extends
      IRecipeBuilder<IBlastFurnaceRecipeBuilder>,
      IRecipeBuilder.ISingleInputFeature,
      IRecipeBuilder.ISingleItemStackOutputFeature<IBlastFurnaceRecipeBuilder>,
      IRecipeBuilder.ITimeFeature<IBlastFurnaceRecipeBuilder> {

    /**
     * Sets the slag output of a furnace recipe.
     */
    default IBlastFurnaceRecipeBuilder slagOutput(int num) {
      return this;
    }

    /**
     * Finalize and commit the recipe.
     */
    default void register() {}
  }

  interface IFuelBuilder extends
      IRecipeBuilder<IFuelBuilder>,
      IRecipeBuilder.ISingleInputFeature,
      IRecipeBuilder.ITimeFeature<IFuelBuilder> {

    /**
     * Finalize and commit the recipe.
     */
    default void register() {}
  }
}
