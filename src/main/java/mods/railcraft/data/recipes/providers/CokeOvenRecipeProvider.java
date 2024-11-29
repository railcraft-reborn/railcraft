package mods.railcraft.data.recipes.providers;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.data.recipes.builders.CokeOvenRecipeBuilder;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CokeOvenRecipeProvider extends RecipeProvider {

  private final HolderLookup.RegistryLookup<Item> items;

  private CokeOvenRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {
    super(registries, recipeOutput);
    this.items = registries.lookupOrThrow(Registries.ITEM);
  }

  public static class Runner extends RecipeProvider.Runner {

    public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
      super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
      return new CokeOvenRecipeProvider(registries, output);
    }

    @Override
    public String getName() {
      return "CokeOvenRecipeProvider";
    }
  }

  @Override
  protected void buildRecipes() {
    CokeOvenRecipeBuilder
        .coking(Items.CHARCOAL, Ingredient.of(this.items.getOrThrow(ItemTags.LOGS)), 0, 300, 250)
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(output);

    CokeOvenRecipeBuilder
        .coking(RailcraftItems.COAL_COKE.get(), Ingredient.of(Items.COAL), 0, 500)
        .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
        .save(output);

    CokeOvenRecipeBuilder
        .coking(RailcraftItems.COAL_COKE_BLOCK.get(), Ingredient.of(Items.COAL_BLOCK), 0,
            CokeOvenRecipeBuilder.DEFAULT_COOKING_TIME * 9, 5000)
        .unlockedBy(getHasName(Items.COAL_BLOCK), has(Items.COAL_BLOCK))
        .save(output);
  }
}
