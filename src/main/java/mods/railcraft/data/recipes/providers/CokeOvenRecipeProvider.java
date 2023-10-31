package mods.railcraft.data.recipes.providers;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.data.recipes.builders.CokeOvenRecipeBuilder;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CokeOvenRecipeProvider extends RecipeProvider {

  private CokeOvenRecipeProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider);
  }

  @Override
  protected void buildRecipes(RecipeOutput recipeOutput) {
  }

  public static void genRecipes(RecipeOutput recipeOutput) {
    CokeOvenRecipeBuilder
        .coking(Items.CHARCOAL, Ingredient.of(ItemTags.LOGS), 0, 300, 250)
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(recipeOutput);

    CokeOvenRecipeBuilder
        .coking(RailcraftItems.COAL_COKE.get(), Ingredient.of(Items.COAL), 0, 500)
        .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
        .save(recipeOutput);

    CokeOvenRecipeBuilder
        .coking(RailcraftItems.COKE_BLOCK.get(), Ingredient.of(Items.COAL_BLOCK), 0,
            CokeOvenRecipeBuilder.DEFAULT_COOKING_TIME * 9, 5000)
        .unlockedBy(getHasName(Items.COAL_BLOCK), has(Items.COAL_BLOCK))
        .save(recipeOutput);
  }
}
