package mods.railcraft.data.recipes.providers;

import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class CrusherRecipeProvider extends RecipeProvider {

  private CrusherRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(RecipeOutput recipeOutput) {
  }

  public static void genRecipes(RecipeOutput recipeOutput) {
    buildVanilla(recipeOutput);
    buildRailcraft(recipeOutput);
    buildConditionalRecipe(recipeOutput);
  }

  private static void buildVanilla(RecipeOutput recipeOutput) {
    CrusherRecipeBuilder.crush(Items.OBSIDIAN)
        .addResult(RailcraftItems.CRUSHED_OBSIDIAN, 1, 1)
        .addResult(RailcraftItems.OBSIDIAN_DUST, 1, 0.25)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.COBBLESTONE)
        .addResult(Items.GRAVEL, 1, 1)
        .addResult(Items.FLINT, 1, 0.1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.MOSSY_COBBLESTONE)
        .addResult(Items.GRAVEL, 1, 1)
        .addResult(Items.VINE, 1, 0.8)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.GRAVEL)
        .addResult(Items.SAND, 1, 1)
        .addResult(Items.GOLD_NUGGET, 1, 0.001)
        .addResult(Items.DIAMOND, 1, 0.00005)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.STONE)
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.SANDSTONE)
        .addResult(Items.SAND, 4, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.BRICKS)
        .addResult(Items.BRICK, 3, 1)
        .addResult(Items.BRICK, 1, 0.50)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.CLAY)
        .addResult(Items.CLAY_BALL, 4, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(ItemTags.STONE_BRICKS)
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.COBBLESTONE_STAIRS)
        .addResult(Items.GRAVEL, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.STONE_BRICK_STAIRS)
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.NETHER_BRICK_STAIRS)
        .addResult(Items.NETHER_BRICK, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.BRICK_STAIRS)
        .addResult(Items.BRICK, 4, 1)
        .addResult(Items.BRICK, 1, 0.5)
        .addResult(Items.BRICK, 1, 0.5)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.STONE_SLAB, Items.STONE_BRICK_SLAB))
        .addResult(Items.COBBLESTONE, 1, 0.45)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.SANDSTONE_SLAB)
        .addResult(Items.SAND, 1, 0.45)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.COBBLESTONE_SLAB)
        .addResult(Items.GRAVEL, 1, 0.45)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.BRICK_SLAB)
        .addResult(Items.BRICK, 1, 1)
        .addResult(Items.BRICK, 1, 0.75)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.ICE)
        .addResult(Items.SNOW_BLOCK, 1, 0.85)
        .addResult(Items.SNOWBALL, 1, 0.25)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.NETHER_BRICK_FENCE)
        .addResult(Items.NETHER_BRICK, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.GLOWSTONE)
        .addResult(Items.GLOWSTONE_DUST, 3, 1)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.75)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.REDSTONE_LAMP)
        .addResult(Items.GLOWSTONE_DUST, 3, 1)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.75)
        .addResult(Items.REDSTONE, 3, 1)
        .addResult(Items.REDSTONE, 1, 0.75)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.BONE)
        .addResult(Items.BONE_MEAL, 4, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.BLAZE_ROD)
        .addResult(Items.BLAZE_POWDER, 2, 1)
        .addResult(Items.BLAZE_POWDER, 1, 0.65)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.5)
        .addResult(Items.BLAZE_POWDER, 1, 0.25)
        .addResult(Items.BLAZE_POWDER, 1, 0.25)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_REDSTONE)
        .addResult(Items.REDSTONE, 6, 1)
        .addResult(Items.REDSTONE, 2, 0.85)
        .addResult(Items.REDSTONE, 1, 0.25)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_DIAMOND)
        .addResult(Items.DIAMOND, 1, 1)
        .addResult(Items.DIAMOND, 1, 0.85)
        .addResult(Items.DIAMOND, 1, 0.25)
        .addResult(Items.COAL, 1, 0.1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_EMERALD)
        .addResult(Items.EMERALD, 1, 1)
        .addResult(Items.EMERALD, 1, 0.85)
        .addResult(Items.EMERALD, 1, 0.25)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_LAPIS)
        .addResult(Items.LAPIS_LAZULI, 8, 1)
        .addResult(Items.LAPIS_LAZULI, 1, 0.85)
        .addResult(Items.LAPIS_LAZULI, 1, 0.35)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.2)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_COAL)
        .addResult(RailcraftItems.COAL_DUST, 2, 1)
        .addResult(RailcraftItems.COAL_DUST, 1, 0.65)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.15)
        .addResult(Items.COAL, 1, 0.15)
        .addResult(Items.DIAMOND, 1, 0.001)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.COAL)
        .addResult(RailcraftItems.COAL_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.CHARCOAL)
        .addResult(RailcraftItems.CHARCOAL_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.COAL_BLOCK)
        .addResult(RailcraftItems.COAL_DUST, 9, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.ENDER_PEARL)
        .addResult(RailcraftItems.ENDER_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_QUARTZ)
        .addResult(Items.QUARTZ, 3, 1)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.25)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.DARK_PRISMARINE)
        .addResult(Items.PRISMARINE_SHARD, 8, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.PRISMARINE_BRICKS)
        .addResult(Items.PRISMARINE_SHARD, 9, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.PRISMARINE)
        .addResult(Items.PRISMARINE_SHARD, 4, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.AMETHYST_BLOCK)
        .addResult(Items.AMETHYST_SHARD, 4, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.NETHER_WART_BLOCK)
        .addResult(Items.NETHER_WART, 9, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(ItemTags.WOOL)
        .addResult(Items.STRING, 4, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.STORAGE_BLOCKS_QUARTZ)
        .addResult(Items.QUARTZ, 4, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.RAW_IRON_BLOCK)
        .addResult(Items.RAW_IRON, 9, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.STORAGE_BLOCKS_RAW_COPPER)
        .addResult(Items.RAW_COPPER, 9, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.RAW_GOLD_BLOCK)
        .addResult(Items.RAW_GOLD, 9, 1)
        .save(recipeOutput);
  }

  private static void buildRailcraft(RecipeOutput recipeOutput) {
    CrusherRecipeBuilder.crush(RailcraftItems.COKE_OVEN_BRICKS)
        .addResult(Items.BRICK, 3, 1)
        .addResult(Items.BRICK, 1, 0.5)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftItems.BLAST_FURNACE_BRICKS)
        .addResult(Items.NETHER_BRICK, 1, 0.75)
        .addResult(Items.SOUL_SAND, 1, 0.75)
        .addResult(Items.BLAZE_POWDER, 1, 0.05)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftItems.CRUSHED_OBSIDIAN)
        .addResult(RailcraftItems.OBSIDIAN_DUST, 1, 1)
        .addResult(RailcraftItems.OBSIDIAN_DUST, 1, 0.25)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.SULFUR_ORE)
        .addResult(RailcraftItems.SULFUR_DUST, 5, 1)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.85)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.35)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.SALTPETER_ORE)
        .addResult(RailcraftItems.SALTPETER_DUST, 3, 1)
        .addResult(RailcraftItems.SALTPETER_DUST, 1, 0.85)
        .addResult(RailcraftItems.SALTPETER_DUST, 1, 0.35)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftItems.FIRESTONE_ORE)
        .addResult(RailcraftItems.RAW_FIRESTONE, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.QUARRIED)
        .addResult(RailcraftItems.QUARRIED_COBBLESTONE, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.QUARRIED_BRICK_STAIRS,
            RailcraftItems.QUARRIED_PAVER_STAIRS))
        .addResult(RailcraftItems.QUARRIED_COBBLESTONE, 1, 0.75)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.QUARRIED_BRICK_SLAB,
            RailcraftItems.QUARRIED_PAVER_SLAB))
        .addResult(RailcraftItems.QUARRIED_COBBLESTONE, 1, 0.5)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.ABYSSAL)
        .addResult(RailcraftItems.ABYSSAL_COBBLESTONE, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.ABYSSAL_BRICK_STAIRS,
            RailcraftItems.ABYSSAL_PAVER_STAIRS))
        .addResult(RailcraftItems.ABYSSAL_COBBLESTONE, 1, 0.75)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.ABYSSAL_BRICK_SLAB,
            RailcraftItems.ABYSSAL_PAVER_SLAB))
        .addResult(RailcraftItems.ABYSSAL_COBBLESTONE, 1, 0.5)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.ZINC_SILVER_BATTERY_EMPTY,
            RailcraftItems.ZINC_CARBON_BATTERY_EMPTY))
        .addResult(RailcraftItems.CHARGE_TERMINAL, 2, 1)
        .addResult(RailcraftItems.CHARGE_SPOOL_MEDIUM, 1, 1)
        .addResult(RailcraftItems.SLAG, 4, 1)
        .addResult(RailcraftItems.SLAG, 2, 0.5)
        .save(recipeOutput);
  }

  private static void buildConditionalRecipe(RecipeOutput recipeOutput) {
    CrusherRecipeBuilder.crush(Items.NETHERITE_INGOT)
        .addResult(RailcraftTags.Items.NETHERITE_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.BRONZE_INGOT)
        .addResult(RailcraftTags.Items.BRONZE_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Items.LAPIS_LAZULI)
        .addResult(RailcraftTags.Items.LAPIS_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.GEMS_QUARTZ)
        .addResult(RailcraftTags.Items.QUARTZ_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.GEMS_EMERALD)
        .addResult(RailcraftTags.Items.EMERALD_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.GEMS_DIAMOND)
        .addResult(RailcraftTags.Items.DIAMOND_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.STEEL_INGOT)
        .addResult(RailcraftTags.Items.STEEL_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.INGOTS_IRON)
        .addResult(RailcraftTags.Items.IRON_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.STORAGE_BLOCKS_RAW_IRON)
        .addResult(RailcraftTags.Items.IRON_DUST, 12, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_IRON)
        .addResult(RailcraftTags.Items.IRON_DUST, 2, 1)
        .addResult(RailcraftTags.Items.NICKEL_DUST, 1, 0.1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.RAW_MATERIALS_IRON)
        .addResult(RailcraftTags.Items.IRON_DUST, 1, 1)
        .addResult(RailcraftTags.Items.IRON_DUST, 1, 0.35)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.INGOTS_GOLD)
        .addResult(RailcraftTags.Items.GOLD_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_GOLD)
        .addResult(RailcraftTags.Items.GOLD_DUST, 2, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.STORAGE_BLOCKS_RAW_GOLD)
        .addResult(RailcraftTags.Items.GOLD_DUST, 12, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.INGOTS_COPPER)
        .addResult(RailcraftTags.Items.COPPER_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.RAW_MATERIALS_COPPER)
        .addResult(RailcraftTags.Items.COPPER_DUST, 1, 1)
        .addResult(RailcraftTags.Items.COPPER_DUST, 1, 0.35)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(Tags.Items.ORES_COPPER)
        .addResult(RailcraftTags.Items.COPPER_DUST, 2, 1)
        .addResult(RailcraftTags.Items.GOLD_DUST, 1, 0.1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.TIN_INGOT)
        .addResult(RailcraftTags.Items.TIN_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.LEAD_INGOT)
        .addResult(RailcraftTags.Items.LEAD_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.RAW_LEAD_BLOCK)
        .addResult(RailcraftTags.Items.LEAD_DUST, 12, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.LEAD_ORE)
        .addResult(RailcraftTags.Items.LEAD_DUST, 2, 1)
        .addResult(RailcraftTags.Items.SILVER_DUST, 1, 0.1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.COAL_COKE)
        .addResult(RailcraftTags.Items.COAL_COKE_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.SILVER_INGOT)
        .addResult(RailcraftTags.Items.SILVER_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.SILVER_ORE)
        .addResult(RailcraftTags.Items.SILVER_DUST, 2, 1)
        .addResult(RailcraftTags.Items.LEAD_DUST, 1, 0.1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.SILVER_RAW)
        .addResult(RailcraftTags.Items.SILVER_DUST, 1, 1)
        .addResult(RailcraftTags.Items.SILVER_DUST, 1, 0.35)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.RAW_SILVER_BLOCK)
        .addResult(RailcraftTags.Items.SILVER_DUST, 12, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.NICKEL_INGOT)
        .addResult(RailcraftTags.Items.NICKEL_DUST, 1, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.RAW_NICKEL_BLOCK)
        .addResult(RailcraftTags.Items.NICKEL_DUST, 12, 1)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.NICKEL_RAW)
        .addResult(RailcraftTags.Items.NICKEL_DUST, 1, 1)
        .addResult(RailcraftTags.Items.NICKEL_DUST, 1, 0.35)
        .save(recipeOutput);
    CrusherRecipeBuilder.crush(RailcraftTags.Items.NICKEL_ORE)
        .addResult(RailcraftTags.Items.NICKEL_DUST, 2, 1)
        .save(recipeOutput);
  }
}
