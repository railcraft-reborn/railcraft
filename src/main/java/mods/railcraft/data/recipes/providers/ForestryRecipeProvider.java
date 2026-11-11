package mods.railcraft.data.recipes.providers;

import java.util.Map;
import forestry.api.ForestryConstants;
import forestry.core.content.machines.recipes.CarpenterRecipe;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.integrations.forestry.RailcraftBackpacks;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.fluids.FluidStack;

public class ForestryRecipeProvider {

  private ForestryRecipeProvider() {
  }

  public static void genRecipes(RecipeOutput recipeOutput) {
    var output = recipeOutput.withConditions(new ModLoadedCondition(ForestryConstants.MOD_ID));

    carpenter(output, "wooden_tie", 40,
        new FluidStack(RailcraftFluids.CREOSOTE.get(), 750),
        RailcraftItems.WOODEN_TIE.toStack(3),
        Map.of('#', Ingredient.of(ItemTags.WOODEN_SLABS)),
        "###");

    carpenter(output, "stone_tie", 40,
        new FluidStack(Fluids.WATER, 750),
        RailcraftItems.STONE_TIE.toStack(),
        Map.of(
            '#', Ingredient.of(RailcraftItems.BAG_OF_CEMENT.get()),
            'r', Ingredient.of(RailcraftItems.REBAR.get())
        ), "#r#");

    carpenter(output, "creosote_torch", 10,
        new FluidStack(RailcraftFluids.CREOSOTE.get(), 1000),
        new ItemStack(Items.TORCH, 6),
        Map.of(
            '#', Ingredient.of(ItemTags.WOOL),
            '|', Ingredient.of(Items.STICK)
        ), "#", "|");

    backpacks(output);
  }

  private static void backpacks(RecipeOutput output) {
    backpack(output, RailcraftBackpacks.TRACKMAN_BACKPACK.get(),
        RailcraftBackpacks.WOVEN_TRACKMAN_BACKPACK.get(), Ingredient.of(Items.RAIL));
    backpack(output, RailcraftBackpacks.ICEMAN_BACKPACK.get(),
        RailcraftBackpacks.WOVEN_ICEMAN_BACKPACK.get(), Ingredient.of(Items.SNOW_BLOCK));
  }

  private static void backpack(RecipeOutput output, Item backpack, Item wovenBackpack,
      Ingredient material) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, backpack)
        .pattern("X#X")
        .pattern("VYV")
        .pattern("X#X")
        .define('#', ItemTags.WOOL)
        .define('V', material)
        .define('X', Tags.Items.STRINGS)
        .define('Y', Tags.Items.CHESTS_WOODEN)
        .unlockedBy("has_chest", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(Tags.Items.CHESTS_WOODEN)))
        .save(output);

    var wovenSilk = BuiltInRegistries.ITEM.getOptional(ForestryConstants.forestry("woven_silk"))
        .orElseThrow(() -> new IllegalStateException("Forestry woven silk is not registered"));
    carpenter(output, BuiltInRegistries.ITEM.getKey(wovenBackpack).getPath(), 200,
        new FluidStack(Fluids.WATER, 1000),
        new ItemStack(wovenBackpack),
        Map.of(
            'W', Ingredient.of(wovenSilk),
            'X', Ingredient.of(Tags.Items.GEMS_DIAMOND),
            'T', Ingredient.of(backpack)
        ), "WXW", "WTW", "WWW");
  }

  private static void carpenter(RecipeOutput output, String name, int packagingTime,
      FluidStack liquid, ItemStack result, Map<Character, Ingredient> key, String... pattern) {
    var id = RailcraftConstants.rl("carpenter/" + name);
    var craftingRecipe = new ShapedRecipe("", CraftingBookCategory.MISC, ShapedRecipePattern.of(key, pattern), result);
    output.accept(id, new CarpenterRecipe(id, packagingTime, liquid, Ingredient.EMPTY, craftingRecipe, null),
            null);
  }
}
