package mods.railcraft.data.recipes.providers;

import java.util.function.Consumer;
import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

public class CrusherRecipeProvider extends RecipeProvider {

  private CrusherRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
  }

  public static void genRecipes(Consumer<FinishedRecipe> consumer) {
    buildVanilla(consumer);
    buildRailcraft(consumer);
  }

  private static void buildVanilla(Consumer<FinishedRecipe> consumer) {
    CrusherRecipeBuilder.crush(Ingredient.of(Items.OBSIDIAN))
        .addResult(RailcraftItems.CRUSHED_OBSIDIAN.get(), 1, 1)
        .addResult(RailcraftItems.OBSIDIAN_DUST.get(), 1, 0.25)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.COBBLESTONE))
        .addResult(Items.GRAVEL, 1, 1)
        .addResult(Items.FLINT, 1, 0.1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.MOSSY_COBBLESTONE))
        .addResult(Items.GRAVEL, 1, 1)
        .addResult(Items.VINE, 1, 0.8)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.GRAVEL))
        .addResult(Items.SAND, 1, 1)
        .addResult(Items.GOLD_NUGGET, 1, 0.001)
        .addResult(Items.DIAMOND, 1, 0.00005)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Tags.Items.STONE))
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(consumer, "tags_stone");
    CrusherRecipeBuilder.crush(Ingredient.of(Tags.Items.SANDSTONE))
        .addResult(Items.SAND, 4, 1)
        .save(consumer, "tags_sandstone");
    CrusherRecipeBuilder.crush(Ingredient.of(Items.BRICKS))
        .addResult(Items.BRICK, 3, 1)
        .addResult(Items.BRICK, 1, 0.50)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.CLAY))
        .addResult(Items.CLAY_BALL, 4, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(ItemTags.STONE_BRICKS))
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(consumer, "tags_stone_bricks");
    CrusherRecipeBuilder.crush(Ingredient.of(Items.COBBLESTONE_STAIRS))
        .addResult(Items.GRAVEL, 1, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.STONE_BRICK_STAIRS))
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.NETHER_BRICK_STAIRS))
        .addResult(Items.NETHER_BRICK, 1, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.BRICK_STAIRS))
        .addResult(Items.BRICK, 4, 1)
        .addResult(Items.BRICK, 1, 0.5)
        .addResult(Items.BRICK, 1, 0.5)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.STONE_SLAB, Items.STONE_BRICK_SLAB))
        .addResult(Items.COBBLESTONE, 1, 0.45)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.SANDSTONE_SLAB))
        .addResult(Items.SAND, 1, 0.45)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.COBBLESTONE_SLAB))
        .addResult(Items.GRAVEL, 1, 0.45)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.BRICK_SLAB))
        .addResult(Items.BRICK, 1, 1)
        .addResult(Items.BRICK, 1, 0.75)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.ICE))
        .addResult(Items.SNOW_BLOCK, 1, 0.85)
        .addResult(Items.SNOWBALL, 1, 0.25)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.NETHER_BRICK_FENCE))
        .addResult(Items.NETHER_BRICK, 1, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.GLOWSTONE))
        .addResult(Items.GLOWSTONE_DUST, 3, 1)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.75)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.REDSTONE_LAMP))
        .addResult(Items.GLOWSTONE_DUST, 3, 1)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.75)
        .addResult(Items.REDSTONE, 3, 1)
        .addResult(Items.REDSTONE, 1, 0.75)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.BONE))
        .addResult(Items.BONE_MEAL, 4, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.BLAZE_ROD))
        .addResult(Items.BLAZE_POWDER, 2, 1)
        .addResult(Items.BLAZE_POWDER, 1, 0.65)
        .addResult(RailcraftItems.SULFUR_DUST.get(), 1, 0.5)
        .addResult(Items.BLAZE_POWDER, 1, 0.25)
        .addResult(Items.BLAZE_POWDER, 1, 0.25)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Tags.Items.ORES_REDSTONE))
        .addResult(Items.REDSTONE, 6, 1)
        .addResult(Items.REDSTONE, 2, 0.85)
        .addResult(Items.REDSTONE, 1, 0.25)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.1)
        .save(consumer, "tags_ores_redstone");
    CrusherRecipeBuilder.crush(Ingredient.of(Tags.Items.ORES_DIAMOND))
        .addResult(Items.DIAMOND, 1, 1)
        .addResult(Items.DIAMOND, 1, 0.85)
        .addResult(Items.DIAMOND, 1, 0.25)
        .addResult(Items.COAL, 1, 0.1)
        .save(consumer, "tags_ores_diamond");
    CrusherRecipeBuilder.crush(Ingredient.of(Tags.Items.ORES_EMERALD))
        .addResult(Items.EMERALD, 1, 1)
        .addResult(Items.EMERALD, 1, 0.85)
        .addResult(Items.EMERALD, 1, 0.25)
        .save(consumer, "tags_ores_emerald");
    CrusherRecipeBuilder.crush(Ingredient.of(Tags.Items.ORES_LAPIS))
        .addResult(Items.LAPIS_LAZULI, 8, 1)
        .addResult(Items.LAPIS_LAZULI, 1, 0.85)
        .addResult(Items.LAPIS_LAZULI, 1, 0.35)
        .save(consumer, "tags_ores_lapis");
    CrusherRecipeBuilder.crush(Ingredient.of(Tags.Items.ORES_COAL))
        .addResult(RailcraftItems.COAL_DUST.get(), 2, 1)
        .addResult(RailcraftItems.COAL_DUST.get(), 1, 0.65)
        .addResult(Items.COAL, 1, 0.15)
        .addResult(Items.DIAMOND, 1, 0.001)
        .save(consumer, "tags_ores_coal");
    CrusherRecipeBuilder.crush(Ingredient.of(Items.COAL))
        .addResult(RailcraftItems.COAL_DUST.get(), 1, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.CHARCOAL))
        .addResult(RailcraftItems.CHARCOAL_DUST.get(), 1, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.COAL_BLOCK))
        .addResult(RailcraftItems.COAL_DUST.get(), 9, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.ENDER_PEARL))
        .addResult(RailcraftItems.ENDER_DUST.get(), 1, 1)
        .save(consumer);
  }

  private static void buildRailcraft(Consumer<FinishedRecipe> consumer) {
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.COKE_OVEN_BRICKS.get()))
        .addResult(Items.BRICK, 3, 1)
        .addResult(Items.BRICK, 1, 0.5)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.BLAST_FURNACE_BRICKS.get()))
        .addResult(Items.NETHER_BRICK, 1, 0.75)
        .addResult(Items.SOUL_SAND, 1, 0.75)
        .addResult(Items.BLAZE_POWDER, 1, 0.05)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.CRUSHED_OBSIDIAN.get()))
        .addResult(RailcraftItems.OBSIDIAN_DUST.get(), 1, 1)
        .addResult(RailcraftItems.OBSIDIAN_DUST.get(), 1, 0.25)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftTags.Items.SULFUR_ORE))
        .addResult(RailcraftItems.SULFUR_DUST.get(), 5, 1)
        .addResult(RailcraftItems.SULFUR_DUST.get(), 1, 0.85)
        .addResult(RailcraftItems.SULFUR_DUST.get(), 1, 0.35)
        .save(consumer, "tags_ores_sulfur");
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftTags.Items.SALTPETER_ORE))
        .addResult(RailcraftItems.SALTPETER_DUST.get(), 3, 1)
        .addResult(RailcraftItems.SALTPETER_DUST.get(), 1, 0.85)
        .addResult(RailcraftItems.SALTPETER_DUST.get(), 1, 0.35)
        .save(consumer, "tags_ores_saltpeter");
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.FIRESTONE_ORE.get()))
        .addResult(RailcraftItems.RAW_FIRESTONE.get(), 1, 1)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftTags.Items.QUARRIED))
        .addResult(RailcraftItems.QUARRIED_COBBLESTONE.get(), 1, 1)
        .save(consumer, "tags_quarried");
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.QUARRIED_BRICK_STAIRS.get(),
            RailcraftItems.QUARRIED_PAVER_STAIRS.get()))
        .addResult(RailcraftItems.QUARRIED_COBBLESTONE.get(), 1, 0.75)
        .save(consumer);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.QUARRIED_BRICK_SLAB.get(),
            RailcraftItems.QUARRIED_PAVER_SLAB.get()))
        .addResult(RailcraftItems.QUARRIED_COBBLESTONE.get(), 1, 0.5)
        .save(consumer);
  }
}
