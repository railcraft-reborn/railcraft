package mods.railcraft.data.recipes.providers;

import java.util.function.Consumer;
import mods.railcraft.data.recipes.builders.CokeOvenRecipeBuilder;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CokeOvenRecipeProvider extends RecipeProvider {

  private CokeOvenRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
  }

  public static void genRecipes(Consumer<FinishedRecipe> consumer) {
    CokeOvenRecipeBuilder
        .coking(Items.CHARCOAL, Ingredient.of(ItemTags.LOGS), 0.0F, 250)
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(consumer);

    CokeOvenRecipeBuilder
        .coking(RailcraftItems.COAL_COKE.get(), Ingredient.of(Items.COAL), 0.0F, 500)
        .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
        .save(consumer);

    CokeOvenRecipeBuilder
        .coking(Items.COAL_BLOCK, Ingredient.of(RailcraftItems.COKE_BLOCK.get()), 0.0F, 16200, 4500)
        .unlockedBy(getHasName(Items.COAL_BLOCK), has(Items.COAL_BLOCK))
        .save(consumer);
  }
}
