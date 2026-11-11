package mods.railcraft.data.recipes.providers;

import mods.railcraft.data.recipes.builders.CokeOvenRecipeBuilder;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class CokeOvenRecipeProvider extends RecipeProvider {

  private final HolderGetter<Item> items;

  private CokeOvenRecipeProvider(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
    super(recipes, advancements);
    this.items = recipes.lookup(Registries.ITEM);
  }

  public static void bootstrap(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
    new CokeOvenRecipeProvider(recipes, advancements).buildRecipes();
  }

  @Override
  protected void buildRecipes() {
    CokeOvenRecipeBuilder
        .coking(new ItemStackTemplate(Items.CHARCOAL),
            Ingredient.of(this.items.getOrThrow(ItemTags.LOGS)), 0, 300, 250)
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(output);

    CokeOvenRecipeBuilder
        .coking(new ItemStackTemplate(RailcraftItems.COAL_COKE),
            Ingredient.of(Items.COAL), 0, 500)
        .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
        .save(output);

    CokeOvenRecipeBuilder
        .coking(new ItemStackTemplate(RailcraftItems.COAL_COKE_BLOCK),
            Ingredient.of(Items.COAL_BLOCK), 0, CokeOvenRecipeBuilder.DEFAULT_COOKING_TIME * 9, 5000)
        .unlockedBy(getHasName(Items.COAL_BLOCK), has(Items.COAL_BLOCK))
        .save(output);
  }
}
