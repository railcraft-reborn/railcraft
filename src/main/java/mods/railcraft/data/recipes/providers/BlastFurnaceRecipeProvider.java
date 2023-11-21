package mods.railcraft.data.recipes.providers;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.Railcraft;
import mods.railcraft.data.recipes.builders.BlastFurnaceRecipeBuilder;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

public class BlastFurnaceRecipeProvider extends RecipeProvider {

  private BlastFurnaceRecipeProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider);
  }

  @Override
  protected void buildRecipes(RecipeOutput recipeOutput) {}

  public static void genRecipes(RecipeOutput recipeOutput) {
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftBlocks.STEEL_BLOCK.get(), 1,
            Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), 9, 9)
        .unlockedBy("has_iron_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
        .save(recipeOutput, getRailcraftBlastingRecipeName(RailcraftItems.STEEL_BLOCK.get()));

    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(Tags.Items.INGOTS_IRON), 1, 1)
        .unlockedBy("has_iron_ingots", has(Tags.Items.INGOTS_IRON))
        .save(recipeOutput, getRailcraftBlastingRecipeName(Items.IRON_INGOT));

    consumeIronSteelItems(recipeOutput);
  }

  private static void consumeIronSteelItems(RecipeOutput recipeOutput) {
    blastFurnaceSmelting(recipeOutput, Items.BUCKET, 3);
    blastFurnaceSmelting(recipeOutput, Items.IRON_HELMET, 5);
    blastFurnaceSmelting(recipeOutput, Items.IRON_CHESTPLATE, 8);
    blastFurnaceSmelting(recipeOutput, Items.IRON_LEGGINGS, 7);
    blastFurnaceSmelting(recipeOutput, Items.IRON_BOOTS, 4);
    blastFurnaceSmelting(recipeOutput, Items.IRON_HORSE_ARMOR, 4);
    blastFurnaceSmelting(recipeOutput, Items.IRON_SWORD, 2);
    blastFurnaceSmelting(recipeOutput, Items.IRON_SHOVEL, 1);
    blastFurnaceSmelting(recipeOutput, Items.IRON_PICKAXE, 3);
    blastFurnaceSmelting(recipeOutput, Items.IRON_AXE, 3);
    blastFurnaceSmelting(recipeOutput, Items.IRON_HOE, 2);
    blastFurnaceSmelting(recipeOutput, Items.SHEARS, 2);
    blastFurnaceSmelting(recipeOutput, Items.IRON_DOOR, 2);
    blastFurnaceSmelting(recipeOutput, Items.IRON_TRAPDOOR, 4);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_HELMET.get(), 4);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_CHESTPLATE.get(), 7);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_LEGGINGS.get(), 6);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_BOOTS.get(), 3);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_SWORD.get(), 1);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_PICKAXE.get(), 2);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_HOE.get(), 1);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_AXE.get(), 2);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.STEEL_SHEARS.get(), 1);
    blastFurnaceRecycling(recipeOutput, RailcraftItems.IRON_CROWBAR.get(), 2);
  }

  private static void blastFurnaceSmelting(RecipeOutput recipeOutput, ItemLike item,
      int multiplier) {
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier, multiplier)
        .unlockedBy(getHasName(item), has(item))
        .save(recipeOutput, getRailcraftBlastingRecipeName(item));
  }

  private static void blastFurnaceRecycling(RecipeOutput recipeOutput, ItemLike item,
      int multiplier) {
    BlastFurnaceRecipeBuilder
        .recycling(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier)
        .unlockedBy(getHasName(item), has(item))
        .save(recipeOutput, getRailcraftBlastingRecipeName(item));
  }

  private static ResourceLocation getRailcraftBlastingRecipeName(ItemLike item) {
    var tag = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    return Railcraft.rl("blasting_" + tag);
  }
}
