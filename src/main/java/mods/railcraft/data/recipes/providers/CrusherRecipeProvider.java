package mods.railcraft.data.recipes.providers;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.DecorativeBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class CrusherRecipeProvider extends RecipeProvider {

  private final HolderLookup.RegistryLookup<Item> items;

  private CrusherRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {
    super(registries, recipeOutput);
    this.items = registries.lookupOrThrow(Registries.ITEM);
  }

  public static class Runner extends RecipeProvider.Runner {

    public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
      super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
      return new CrusherRecipeProvider(registries, output);
    }

    @Override
    public String getName() {
      return "CrusherRecipeProvider";
    }
  }

  @Override
  protected void buildRecipes() {
    buildVanilla();
    buildRailcraft();
    buildConditionalRecipe();
  }

  private void buildVanilla() {
    CrusherRecipeBuilder.crush(Items.OBSIDIAN)
        .addResult(RailcraftItems.CRUSHED_OBSIDIAN, 1, 1)
        .addResult(RailcraftItems.OBSIDIAN_DUST, 1, 0.25)
        .save(output);
    CrusherRecipeBuilder.crush(Items.COBBLESTONE)
        .addResult(Items.GRAVEL, 1, 1)
        .addResult(Items.FLINT, 1, 0.1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.MOSSY_COBBLESTONE)
        .addResult(Items.GRAVEL, 1, 1)
        .addResult(Items.VINE, 1, 0.8)
        .save(output);
    CrusherRecipeBuilder.crush(Items.GRAVEL)
        .addResult(Items.SAND, 1, 1)
        .addResult(Items.GOLD_NUGGET, 1, 0.001)
        .addResult(Items.DIAMOND, 1, 0.00005)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.STONES)
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.SANDSTONE_BLOCKS)
        .addResult(Items.SAND, 4, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.BRICKS)
        .addResult(Items.BRICK, 3, 1)
        .addResult(Items.BRICK, 1, 0.50)
        .save(output);
    CrusherRecipeBuilder.crush(Items.CLAY)
        .addResult(Items.CLAY_BALL, 4, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, ItemTags.STONE_BRICKS)
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.COBBLESTONE_STAIRS)
        .addResult(Items.GRAVEL, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.STONE_BRICK_STAIRS)
        .addResult(Items.COBBLESTONE, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.NETHER_BRICK_STAIRS)
        .addResult(Items.NETHER_BRICK, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.BRICK_STAIRS)
        .addResult(Items.BRICK, 4, 1)
        .addResult(Items.BRICK, 1, 0.5)
        .addResult(Items.BRICK, 1, 0.5)
        .save(output);
    CrusherRecipeBuilder.crush(Ingredient.of(Items.STONE_SLAB, Items.STONE_BRICK_SLAB))
        .addResult(Items.COBBLESTONE, 1, 0.45)
        .save(output);
    CrusherRecipeBuilder.crush(Items.SANDSTONE_SLAB)
        .addResult(Items.SAND, 1, 0.45)
        .save(output);
    CrusherRecipeBuilder.crush(Items.COBBLESTONE_SLAB)
        .addResult(Items.GRAVEL, 1, 0.45)
        .save(output);
    CrusherRecipeBuilder.crush(Items.BRICK_SLAB)
        .addResult(Items.BRICK, 1, 1)
        .addResult(Items.BRICK, 1, 0.75)
        .save(output);
    CrusherRecipeBuilder.crush(Items.ICE)
        .addResult(Items.SNOW_BLOCK, 1, 0.85)
        .addResult(Items.SNOWBALL, 1, 0.25)
        .save(output);
    CrusherRecipeBuilder.crush(Items.NETHER_BRICK_FENCE)
        .addResult(Items.NETHER_BRICK, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.GLOWSTONE)
        .addResult(Items.GLOWSTONE_DUST, 3, 1)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.75)
        .save(output);
    CrusherRecipeBuilder.crush(Items.REDSTONE_LAMP)
        .addResult(Items.GLOWSTONE_DUST, 3, 1)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.75)
        .addResult(Items.REDSTONE, 3, 1)
        .addResult(Items.REDSTONE, 1, 0.75)
        .save(output);
    CrusherRecipeBuilder.crush(Items.BONE)
        .addResult(Items.BONE_MEAL, 4, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.BLAZE_ROD)
        .addResult(Items.BLAZE_POWDER, 2, 1)
        .addResult(Items.BLAZE_POWDER, 1, 0.65)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.5)
        .addResult(Items.BLAZE_POWDER, 1, 0.25)
        .addResult(Items.BLAZE_POWDER, 1, 0.25)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_REDSTONE)
        .addResult(Items.REDSTONE, 6, 1)
        .addResult(Items.REDSTONE, 2, 0.85)
        .addResult(Items.REDSTONE, 1, 0.25)
        .addResult(Items.GLOWSTONE_DUST, 1, 0.1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_DIAMOND)
        .addResult(Items.DIAMOND, 1, 1)
        .addResult(Items.DIAMOND, 1, 0.85)
        .addResult(Items.DIAMOND, 1, 0.25)
        .addResult(Items.COAL, 1, 0.1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_EMERALD)
        .addResult(Items.EMERALD, 1, 1)
        .addResult(Items.EMERALD, 1, 0.85)
        .addResult(Items.EMERALD, 1, 0.25)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_LAPIS)
        .addResult(Items.LAPIS_LAZULI, 8, 1)
        .addResult(Items.LAPIS_LAZULI, 1, 0.85)
        .addResult(Items.LAPIS_LAZULI, 1, 0.35)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.2)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_COAL)
        .addResult(RailcraftItems.COAL_DUST, 2, 1)
        .addResult(RailcraftItems.COAL_DUST, 1, 0.65)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.15)
        .addResult(Items.COAL, 1, 0.15)
        .addResult(Items.DIAMOND, 1, 0.001)
        .save(output);
    CrusherRecipeBuilder.crush(Items.COAL)
        .addResult(RailcraftItems.COAL_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.CHARCOAL)
        .addResult(RailcraftItems.CHARCOAL_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.COAL_BLOCK)
        .addResult(RailcraftItems.COAL_DUST, 9, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.ENDER_PEARL)
        .addResult(RailcraftItems.ENDER_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_QUARTZ)
        .addResult(Items.QUARTZ, 3, 1)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.25)
        .save(output);
    CrusherRecipeBuilder.crush(Items.DARK_PRISMARINE)
        .addResult(Items.PRISMARINE_SHARD, 8, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.PRISMARINE_BRICKS)
        .addResult(Items.PRISMARINE_SHARD, 9, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.PRISMARINE)
        .addResult(Items.PRISMARINE_SHARD, 4, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.AMETHYST_BLOCK)
        .addResult(Items.AMETHYST_SHARD, 4, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.NETHER_WART_BLOCK)
        .addResult(Items.NETHER_WART, 9, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, ItemTags.WOOL)
        .addResult(Items.STRING, 4, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.QUARTZ_BLOCK)
        .addResult(Items.QUARTZ, 4, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.RAW_IRON_BLOCK)
        .addResult(Items.RAW_IRON, 9, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.STORAGE_BLOCKS_RAW_COPPER)
        .addResult(Items.RAW_COPPER, 9, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.RAW_GOLD_BLOCK)
        .addResult(Items.RAW_GOLD, 9, 1)
        .save(output);
  }

  private void buildRailcraft() {
    CrusherRecipeBuilder.crush(RailcraftItems.COKE_OVEN_BRICKS)
        .addResult(Items.BRICK, 3, 1)
        .addResult(Items.BRICK, 1, 0.5)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .addResult(Items.SAND, 1, 0.25)
        .save(output);
    CrusherRecipeBuilder.crush(RailcraftItems.BLAST_FURNACE_BRICKS)
        .addResult(Items.NETHER_BRICK, 1, 0.75)
        .addResult(Items.SOUL_SAND, 1, 0.75)
        .addResult(Items.BLAZE_POWDER, 1, 0.05)
        .save(output);
    CrusherRecipeBuilder.crush(RailcraftItems.CRUSHED_OBSIDIAN)
        .addResult(RailcraftItems.OBSIDIAN_DUST, 1, 1)
        .addResult(RailcraftItems.OBSIDIAN_DUST, 1, 0.25)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.SULFUR_ORE)
        .addResult(RailcraftItems.SULFUR_DUST, 5, 1)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.85)
        .addResult(RailcraftItems.SULFUR_DUST, 1, 0.35)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.SALTPETER_ORE)
        .addResult(RailcraftItems.SALTPETER_DUST, 3, 1)
        .addResult(RailcraftItems.SALTPETER_DUST, 1, 0.85)
        .addResult(RailcraftItems.SALTPETER_DUST, 1, 0.35)
        .save(output);
    CrusherRecipeBuilder.crush(RailcraftItems.FIRESTONE_ORE)
        .addResult(RailcraftItems.RAW_FIRESTONE, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.QUARRIED)
        .addResult(RailcraftItems.DECORATIVE_COBBLESTONE
            .variantFor(DecorativeBlock.QUARRIED).get(), 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.ABYSSAL)
        .addResult(RailcraftItems.DECORATIVE_COBBLESTONE
            .variantFor(DecorativeBlock.ABYSSAL).get(), 1, 1)
        .save(output);
    for (var type : DecorativeBlock.values()) {
      CrusherRecipeBuilder.crush(Ingredient.of(
              RailcraftItems.DECORATIVE_BRICK_STAIRS.variantFor(type).get(),
              RailcraftItems.DECORATIVE_PAVER_STAIRS.variantFor(type).get()))
          .addResult(RailcraftItems.DECORATIVE_COBBLESTONE.variantFor(type).get(), 1, 0.75)
          .save(output);
      CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.DECORATIVE_BRICK_SLAB.variantFor(type).get(),
              RailcraftItems.DECORATIVE_PAVER_SLAB.variantFor(type).get()))
          .addResult(RailcraftItems.DECORATIVE_COBBLESTONE.variantFor(type).get(), 1, 0.5)
          .save(output);
    }
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.ZINC_SILVER_BATTERY_EMPTY,
            RailcraftItems.ZINC_CARBON_BATTERY_EMPTY))
        .addResult(RailcraftItems.CHARGE_TERMINAL, 2, 1)
        .addResult(RailcraftItems.CHARGE_SPOOL_MEDIUM, 1, 1)
        .addResult(RailcraftItems.SLAG, 4, 1)
        .addResult(RailcraftItems.SLAG, 2, 0.5)
        .save(output);
    CrusherRecipeBuilder.crush(Ingredient.of(RailcraftItems.PERSONAL_WORLD_SPIKE,
        RailcraftItems.WORLD_SPIKE))
        .addResult(RailcraftItems.CRUSHED_OBSIDIAN.get(), 1, 1)
        .addResult(RailcraftItems.CRUSHED_OBSIDIAN.get(), 1, 0.5)
        .addResult(Items.OBSIDIAN, 1, 0.25)
        .addResult(RailcraftItems.OBSIDIAN_DUST.get(), 1, 0.25)
        .addResult(Items.GOLD_NUGGET, 16, 1)
        .addResult(Items.GOLD_NUGGET, 8, 0.5)
        .addResult(Items.GOLD_NUGGET, 8, 0.5)
        .addResult(Items.GOLD_NUGGET, 4, 0.5)
        .addResult(Items.EMERALD, 1, 0.5)
        .save(output);
  }

  private void buildConditionalRecipe() {
    CrusherRecipeBuilder.crush(Items.NETHERITE_INGOT)
        .addResult(this.items, RailcraftTags.Items.NETHERITE_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.BRONZE_INGOT)
        .addResult(this.items, RailcraftTags.Items.BRONZE_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(Items.LAPIS_LAZULI)
        .addResult(this.items, RailcraftTags.Items.LAPIS_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.GEMS_QUARTZ)
        .addResult(this.items, RailcraftTags.Items.QUARTZ_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.GEMS_EMERALD)
        .addResult(this.items, RailcraftTags.Items.EMERALD_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.GEMS_DIAMOND)
        .addResult(this.items, RailcraftTags.Items.DIAMOND_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.STEEL_INGOT)
        .addResult(this.items, RailcraftTags.Items.STEEL_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.INGOTS_IRON)
        .addResult(this.items, RailcraftTags.Items.IRON_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.STORAGE_BLOCKS_RAW_IRON)
        .addResult(this.items, RailcraftTags.Items.IRON_DUST, 12, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_IRON)
        .addResult(this.items, RailcraftTags.Items.IRON_DUST, 2, 1)
        .addResult(this.items, RailcraftTags.Items.NICKEL_DUST, 1, 0.1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.RAW_MATERIALS_IRON)
        .addResult(this.items, RailcraftTags.Items.IRON_DUST, 1, 1)
        .addResult(this.items, RailcraftTags.Items.IRON_DUST, 1, 0.35)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.INGOTS_GOLD)
        .addResult(this.items, RailcraftTags.Items.GOLD_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_GOLD)
        .addResult(this.items, RailcraftTags.Items.GOLD_DUST, 2, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.STORAGE_BLOCKS_RAW_GOLD)
        .addResult(this.items, RailcraftTags.Items.GOLD_DUST, 12, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.INGOTS_COPPER)
        .addResult(this.items, RailcraftTags.Items.COPPER_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.RAW_MATERIALS_COPPER)
        .addResult(this.items, RailcraftTags.Items.COPPER_DUST, 1, 1)
        .addResult(this.items, RailcraftTags.Items.COPPER_DUST, 1, 0.35)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, Tags.Items.ORES_COPPER)
        .addResult(this.items, RailcraftTags.Items.COPPER_DUST, 2, 1)
        .addResult(this.items, RailcraftTags.Items.GOLD_DUST, 1, 0.1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.TIN_INGOT)
        .addResult(this.items, RailcraftTags.Items.TIN_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.LEAD_INGOT)
        .addResult(this.items, RailcraftTags.Items.LEAD_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.RAW_LEAD_BLOCK)
        .addResult(this.items, RailcraftTags.Items.LEAD_DUST, 12, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.LEAD_ORE)
        .addResult(this.items, RailcraftTags.Items.LEAD_DUST, 2, 1)
        .addResult(this.items, RailcraftTags.Items.SILVER_DUST, 1, 0.1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.COAL_COKE)
        .addResult(this.items, RailcraftTags.Items.COAL_COKE_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.SILVER_INGOT)
        .addResult(this.items, RailcraftTags.Items.SILVER_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.SILVER_ORE)
        .addResult(this.items, RailcraftTags.Items.SILVER_DUST, 2, 1)
        .addResult(this.items, RailcraftTags.Items.LEAD_DUST, 1, 0.1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.SILVER_RAW)
        .addResult(this.items, RailcraftTags.Items.SILVER_DUST, 1, 1)
        .addResult(this.items, RailcraftTags.Items.SILVER_DUST, 1, 0.35)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.RAW_SILVER_BLOCK)
        .addResult(this.items, RailcraftTags.Items.SILVER_DUST, 12, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.NICKEL_INGOT)
        .addResult(this.items, RailcraftTags.Items.NICKEL_DUST, 1, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.RAW_NICKEL_BLOCK)
        .addResult(this.items, RailcraftTags.Items.NICKEL_DUST, 12, 1)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.NICKEL_RAW)
        .addResult(this.items, RailcraftTags.Items.NICKEL_DUST, 1, 1)
        .addResult(this.items, RailcraftTags.Items.NICKEL_DUST, 1, 0.35)
        .save(output);
    CrusherRecipeBuilder.crush(this.items, RailcraftTags.Items.NICKEL_ORE)
        .addResult(this.items, RailcraftTags.Items.NICKEL_DUST, 2, 1)
        .save(output);
  }
}
