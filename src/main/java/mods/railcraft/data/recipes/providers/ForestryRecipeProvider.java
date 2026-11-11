package mods.railcraft.data.recipes.providers;

// TODO Forestry CE 26.1: uncomment when Forestry CE is released for 26.1.x
//  (see also build.gradle, Railcraft#gatherData and RailcraftDataMapProvider).
//  Ported from the 1.21.x branch without a Forestry jar to compile against: check the
//  CarpenterRecipe constructor (e.g. how an empty box is represented now that
//  Ingredient.EMPTY no longer exists) and that the recipe JSON format is unchanged.
/*
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import forestry.api.ForestryConstants;
import forestry.core.content.machines.recipes.CarpenterRecipe;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.fluids.FluidStack;

public class ForestryRecipeProvider extends RecipeProvider {

  private final HolderGetter<Item> items;

  private ForestryRecipeProvider(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
    super(registries, recipeOutput.withConditions(
        new ModLoadedCondition(ForestryConstants.MOD_ID)));
    this.items = recipes.lookup(Registries.ITEM);
  }

  public static void bootstrap(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
    new ForestryRecipeProvider(recipes, advancements).buildRecipes();
  }

  @Override
  protected void buildRecipes() {
    carpenter("wooden_tie", 40,
        new FluidStack(RailcraftFluids.CREOSOTE.get(), 750),
        new ItemStackTemplate(RailcraftItems.WOODEN_TIE.get(), 3),
        Map.of('#', Ingredient.of(this.items.getOrThrow(ItemTags.WOODEN_SLABS))),
        "###");

    carpenter("stone_tie", 40,
        new FluidStack(Fluids.WATER, 750),
        new ItemStackTemplate(RailcraftItems.STONE_TIE.get()),
        Map.of(
            '#', Ingredient.of(RailcraftItems.BAG_OF_CEMENT.get()),
            'r', Ingredient.of(RailcraftItems.REBAR.get())
        ), "#r#");

    carpenter("creosote_torch", 10,
        new FluidStack(RailcraftFluids.CREOSOTE.get(), 1000),
        new ItemStackTemplate(Items.TORCH, 6),
        Map.of(
            '#', Ingredient.of(this.items.getOrThrow(ItemTags.WOOL)),
            '|', Ingredient.of(Items.STICK)
        ), "#", "|");
  }

  private void carpenter(String name, int packagingTime, FluidStack liquid,
      ItemStackTemplate result, Map<Character, Ingredient> key, String... pattern) {
    var id = RailcraftConstants.rl("carpenter/" + name);
    var craftingRecipe = new ShapedRecipe(new Recipe.CommonInfo(false),
        new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, ""),
        ShapedRecipePattern.of(key, pattern), result);
    this.output.accept(ResourceKey.create(Registries.RECIPE, id),
        new CarpenterRecipe(id, packagingTime, liquid, Ingredient.EMPTY, craftingRecipe, null),
        null);
  }
}
*/
