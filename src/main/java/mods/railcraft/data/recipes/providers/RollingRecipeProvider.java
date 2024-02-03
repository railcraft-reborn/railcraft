package mods.railcraft.data.recipes.providers;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.recipes.builders.RollingRecipeBuilder;
import mods.railcraft.data.recipes.patterns.RollingRecipePattern;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class RollingRecipeProvider extends RecipeProvider {

  private RollingRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(RecipeOutput recipeOutput) {
  }

  public static void genRecipes(RecipeOutput recipeOutput) {
    misc(recipeOutput);
    buildChargeSpool(recipeOutput);
    buildTrackParts(recipeOutput);
    buildRebars(recipeOutput);
    buildElectrodes(recipeOutput);
    buildRails(recipeOutput);
    buildPlates(recipeOutput);
  }

  private static void misc(RecipeOutput recipeOutput) {
    RollingRecipePattern.hForm(recipeOutput, Ingredient.of(Tags.Items.INGOTS_IRON),
        RailcraftItems.POST.variantFor(DyeColor.BLACK).get(), 16);

    RollingRecipePattern.diagonalLine(recipeOutput, Ingredient.of(RailcraftTags.Items.STEEL_PLATE),
        RailcraftItems.TURBINE_BLADE.get(), 1, "steel_turbine_blade");
    RollingRecipePattern.diagonalLine(recipeOutput, Ingredient.of(RailcraftTags.Items.NICKEL_PLATE),
        RailcraftItems.TURBINE_BLADE.get(), 1, "nickel_turbine_blade");

    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.BRONZE_PLATE),
        RailcraftItems.BUSHING_GEAR.get(), 4, "_bronze");
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.BRASS_PLATE),
        RailcraftItems.BUSHING_GEAR.get(), 4, "_brass");
  }

  private static void buildChargeSpool(RecipeOutput recipeOutput) {
    RollingRecipeBuilder.rolled(RailcraftItems.CHARGE_SPOOL_LARGE.get())
        .pattern("a")
        .define('a', Tags.Items.STORAGE_BLOCKS_COPPER)
        .save(recipeOutput);
    RollingRecipeBuilder.rolled(RailcraftItems.CHARGE_SPOOL_SMALL.get())
        .pattern("a")
        .define('a', Tags.Items.INGOTS_COPPER)
        .save(recipeOutput);
  }

  private static void buildTrackParts(RecipeOutput recipeOutput) {
    final var trackParts = RailcraftItems.TRACK_PARTS.get();
    var name = RecipeBuilder.getDefaultRecipeId(trackParts).getPath();
    RollingRecipeBuilder.rolled(trackParts)
        .pattern("aa ")
        .pattern("a  ")
        .define('a', RailcraftTags.Items.BRONZE_NUGGET)
        .save(recipeOutput, RailcraftConstants.rl(name + "_bronze_nugget"));
    RollingRecipeBuilder.rolled(trackParts)
        .pattern("aa")
        .define('a', Tags.Items.NUGGETS_IRON)
        .save(recipeOutput, RailcraftConstants.rl(name + "_iron_nugget"));
    RollingRecipeBuilder.rolled(trackParts)
        .pattern("a")
        .define('a', RailcraftTags.Items.STEEL_NUGGET)
        .save(recipeOutput, RailcraftConstants.rl(name + "_steel_nugget"));
  }

  private static void buildRebars(RecipeOutput recipeOutput) {
    RollingRecipePattern.diagonalLine(recipeOutput, Ingredient.of(Tags.Items.INGOTS_IRON),
        RailcraftItems.REBAR.get(), 4, "rebar_iron");
    RollingRecipePattern.diagonalLine(recipeOutput, Ingredient.of(RailcraftTags.Items.BRONZE_INGOT),
        RailcraftItems.REBAR.get(), 4, "rebar_bronze");
    RollingRecipePattern.diagonalLine(recipeOutput, Ingredient.of(RailcraftTags.Items.INVAR_INGOT),
        RailcraftItems.REBAR.get(), 6, "rebar_invar");
    RollingRecipePattern.diagonalLine(recipeOutput, Ingredient.of(RailcraftTags.Items.STEEL_INGOT),
        RailcraftItems.REBAR.get(), 8, "rebar_steel");
  }

  private static void buildElectrodes(RecipeOutput recipeOutput) {
    RollingRecipePattern.line(recipeOutput, Ingredient.of(Items.COAL, Items.CHARCOAL),
        RailcraftItems.CARBON_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.NICKEL_PLATE),
        RailcraftItems.NICKEL_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.IRON_PLATE),
        RailcraftItems.IRON_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.ZINC_PLATE),
        RailcraftItems.ZINC_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.SILVER_PLATE),
        RailcraftItems.SILVER_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.STEEL_PLATE),
        RailcraftItems.STEEL_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.TIN_PLATE),
        RailcraftItems.TIN_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.GOLD_PLATE),
        RailcraftItems.GOLD_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.LEAD_PLATE),
        RailcraftItems.LEAD_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.BRASS_PLATE),
        RailcraftItems.BRASS_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.INVAR_PLATE),
        RailcraftItems.INVAR_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.BRONZE_PLATE),
        RailcraftItems.BRONZE_ELECTRODE.get(), 1);
    RollingRecipePattern.line(recipeOutput, Ingredient.of(RailcraftTags.Items.COPPER_PLATE),
        RailcraftItems.COPPER_ELECTRODE.get(), 1);
  }

  private static void buildRails(RecipeOutput recipeOutput) {
    RollingRecipePattern.parallelLines(recipeOutput, Ingredient.of(Tags.Items.INGOTS_IRON),
        RailcraftItems.STANDARD_RAIL.get(), 8, "standard_rail");
    RollingRecipePattern.parallelLines(recipeOutput, Ingredient.of(RailcraftTags.Items.BRONZE_INGOT),
        RailcraftItems.STANDARD_RAIL.get(), 8, "bronze_rail");
    RollingRecipePattern.parallelLines(recipeOutput, Ingredient.of(RailcraftTags.Items.INVAR_INGOT),
        RailcraftItems.STANDARD_RAIL.get(), 12, "invar_rail");
    RollingRecipePattern.parallelLines(recipeOutput, Ingredient.of(RailcraftTags.Items.STEEL_INGOT),
        RailcraftItems.STANDARD_RAIL.get(), 16, "steel_rail");
    RollingRecipePattern.parallelLines(recipeOutput, Ingredient.of(Tags.Items.INGOTS_COPPER),
        RailcraftItems.ELECTRIC_RAIL.get(), 6, "copper_electric_rail");
    RollingRecipePattern.parallelLines(recipeOutput, Ingredient.of(Tags.Items.DUSTS_REDSTONE),
        Ingredient.of(Tags.Items.INGOTS_GOLD), RailcraftItems.ADVANCED_RAIL.get(), 8,
        "advanced_rail");
    RollingRecipePattern.parallelThreeLines(recipeOutput,
        Ingredient.of(RailcraftTags.Items.STEEL_INGOT),
        Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Tags.Items.INGOTS_GOLD),
        RailcraftItems.HIGH_SPEED_RAIL.get(), 8, "standard_high_speed_rail");
    RollingRecipePattern.parallelThreeLines(recipeOutput,
        Ingredient.of(RailcraftTags.Items.STEEL_INGOT),
        Ingredient.of(RailcraftTags.Items.OBSIDIAN_DUST),
        Ingredient.of(RailcraftTags.Items.STEEL_INGOT), RailcraftItems.REINFORCED_RAIL.get(), 8,
        "steel_reinforced_rail");
    RollingRecipePattern.parallelThreeLines(recipeOutput,
        Ingredient.of(RailcraftTags.Items.INVAR_INGOT),
        Ingredient.of(RailcraftTags.Items.OBSIDIAN_DUST),
        Ingredient.of(RailcraftTags.Items.INVAR_INGOT), RailcraftItems.REINFORCED_RAIL.get(), 4,
        "invar_reinforced_rail");
    RollingRecipePattern.parallelThreeLines(recipeOutput,
        Ingredient.of(RailcraftTags.Items.STEEL_INGOT), Ingredient.of(Tags.Items.INGOTS_COPPER),
        Ingredient.of(RailcraftTags.Items.STEEL_INGOT), RailcraftItems.ELECTRIC_RAIL.get(), 12);
  }

  private static void buildPlates(RecipeOutput recipeOutput) {
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(Tags.Items.INGOTS_IRON),
        RailcraftItems.IRON_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(Tags.Items.INGOTS_GOLD),
        RailcraftItems.GOLD_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(Tags.Items.INGOTS_COPPER),
        RailcraftItems.COPPER_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.STEEL_INGOT),
        RailcraftItems.STEEL_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.NICKEL_INGOT),
        RailcraftItems.NICKEL_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.INVAR_INGOT),
        RailcraftItems.INVAR_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.BRASS_INGOT),
        RailcraftItems.BRASS_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.TIN_INGOT),
        RailcraftItems.TIN_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.BRONZE_INGOT),
        RailcraftItems.BRONZE_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.LEAD_INGOT),
        RailcraftItems.LEAD_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.SILVER_INGOT),
        RailcraftItems.SILVER_PLATE.get(), 4);
    RollingRecipePattern.square2x2(recipeOutput, Ingredient.of(RailcraftTags.Items.ZINC_INGOT),
        RailcraftItems.ZINC_PLATE.get(), 4);
  }
}
