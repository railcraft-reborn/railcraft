package mods.railcraft.data.recipes.providers;

import java.util.function.Consumer;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.recipes.builders.BlastFurnaceRecipeBuilder;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class BlastFurnaceRecipeProvider extends RecipeProvider {

  private BlastFurnaceRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
  }

  public static void genRecipes(Consumer<FinishedRecipe> consumer) {
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftBlocks.STEEL_BLOCK.get(), 1,
            Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), 9, 9)
        .unlockedBy("has_iron_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
        .save(consumer, getRailcraftBlastingRecipeName(RailcraftItems.STEEL_BLOCK.get()));

    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(Tags.Items.INGOTS_IRON), 1, 1)
        .unlockedBy("has_iron_ingots", has(Tags.Items.INGOTS_IRON))
        .save(consumer, getRailcraftBlastingRecipeName(Items.IRON_INGOT));

    consumeIronSteelItems(consumer);
  }

  private static void consumeIronSteelItems(Consumer<FinishedRecipe> consumer) {
    blastFurnaceSmelting(consumer, Items.BUCKET, 3);
    blastFurnaceSmelting(consumer, Items.IRON_HELMET, 5);
    blastFurnaceSmelting(consumer, Items.IRON_CHESTPLATE, 8);
    blastFurnaceSmelting(consumer, Items.IRON_LEGGINGS, 7);
    blastFurnaceSmelting(consumer, Items.IRON_BOOTS, 4);
    blastFurnaceSmelting(consumer, Items.IRON_HORSE_ARMOR, 4);
    blastFurnaceSmelting(consumer, Items.IRON_SWORD, 2);
    blastFurnaceSmelting(consumer, Items.IRON_SHOVEL, 1);
    blastFurnaceSmelting(consumer, Items.IRON_PICKAXE, 3);
    blastFurnaceSmelting(consumer, Items.IRON_AXE, 3);
    blastFurnaceSmelting(consumer, Items.IRON_HOE, 2);
    blastFurnaceSmelting(consumer, Items.SHEARS, 2);
    blastFurnaceSmelting(consumer, Items.IRON_DOOR, 2);
    blastFurnaceSmelting(consumer, Items.IRON_TRAPDOOR, 4);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_HELMET.get(), 4);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_CHESTPLATE.get(), 7);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_LEGGINGS.get(), 6);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_BOOTS.get(), 3);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_SWORD.get(), 1);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_PICKAXE.get(), 2);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_HOE.get(), 1);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_AXE.get(), 2);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_SHEARS.get(), 1);
    blastFurnaceRecycling(consumer, RailcraftItems.IRON_CROWBAR.get(), 2);
  }

  private static void blastFurnaceSmelting(Consumer<FinishedRecipe> consumer, ItemLike item,
      int multiplier) {
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier, multiplier)
        .unlockedBy(getHasName(item), has(item))
        .save(consumer, getRailcraftBlastingRecipeName(item));
  }

  private static void blastFurnaceRecycling(Consumer<FinishedRecipe> consumer, ItemLike item,
      int multiplier) {
    BlastFurnaceRecipeBuilder
        .recycling(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier)
        .unlockedBy(getHasName(item), has(item))
        .save(consumer, getRailcraftBlastingRecipeName(item));
  }

  private static ResourceLocation getRailcraftBlastingRecipeName(ItemLike item) {
    var tag = ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    return RailcraftConstants.rl("blasting_" + tag);
  }
}
