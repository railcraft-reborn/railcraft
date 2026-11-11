package mods.railcraft.data.recipes.providers;

/*
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import forestry.api.ForestryConstants;
import forestry.core.content.machines.recipes.CarpenterRecipe;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.integrations.forestry.RailcraftBackpacks;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
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
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.fluids.FluidStack;

public class ForestryRecipeProvider extends RecipeProvider {

  private final HolderLookup.RegistryLookup<Item> items;

  private ForestryRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {
    super(registries, recipeOutput.withConditions(
        new ModLoadedCondition(ForestryConstants.MOD_ID)));
    this.items = registries.lookupOrThrow(Registries.ITEM);
  }

  public static class Runner extends RecipeProvider.Runner {

    public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
      super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries,
        RecipeOutput output) {
      return new ForestryRecipeProvider(registries, output);
    }

    @Override
    public String getName() {
      return "ForestryRecipeProvider";
    }
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

    this.backpack(RailcraftBackpacks.TRACKMAN_BACKPACK.get(),
        RailcraftBackpacks.WOVEN_TRACKMAN_BACKPACK.get(), Ingredient.of(Items.RAIL));
    this.backpack(RailcraftBackpacks.ICEMAN_BACKPACK.get(),
        RailcraftBackpacks.WOVEN_ICEMAN_BACKPACK.get(), Ingredient.of(Items.SNOW_BLOCK));
  }

  // Same recipes as Forestry's own backpacks: a crafting recipe for the normal backpack and a
  // carpenter recipe that weaves it with silk into the woven backpack.
  private void backpack(Item backpack, Item wovenBackpack, Ingredient material) {
    this.shaped(RecipeCategory.TOOLS, backpack)
        .pattern("X#X")
        .pattern("VYV")
        .pattern("X#X")
        .define('#', this.tag(ItemTags.WOOL))
        .define('V', material)
        .define('X', this.tag(Tags.Items.STRINGS))
        .define('Y', this.tag(Tags.Items.CHESTS_WOODEN))
        .unlockedBy("has_chest", this.has(Tags.Items.CHESTS_WOODEN))
        .save(this.output);

    var wovenSilk = this.items.getOrThrow(
        ResourceKey.create(Registries.ITEM, ForestryConstants.forestry("woven_silk")));
    carpenter(BuiltInRegistries.ITEM.getKey(wovenBackpack).getPath(), 200,
        new FluidStack(Fluids.WATER, 1000),
        new ItemStackTemplate(wovenBackpack),
        Map.of(
            'W', Ingredient.of(wovenSilk.value()),
            'X', this.tag(Tags.Items.GEMS_DIAMOND),
            'T', Ingredient.of(backpack)
        ), "WXW", "WTW", "WWW");
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
