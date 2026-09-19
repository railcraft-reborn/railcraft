package mods.railcraft.data.recipes.providers;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.recipes.builders.RollingRecipeBuilder;
import mods.railcraft.data.recipes.patterns.RollingRecipePattern;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class RollingRecipeProvider extends RecipeProvider {

  private final HolderLookup.RegistryLookup<Item> items;

  private RollingRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {
    super(registries, recipeOutput);
    this.items = registries.lookupOrThrow(Registries.ITEM);
  }

  public static class Runner extends RecipeProvider.Runner {

    public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
      super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
      return new RollingRecipeProvider(registries, output);
    }

    @Override
    public String getName() {
      return "RollingRecipeProvider";
    }
  }

  @Override
  protected void buildRecipes() {
    misc();
    buildChargeSpool();
    buildTrackParts();
    buildRebars();
    buildElectrodes();
    buildRails();
    buildPlates();
  }

  private void misc() {
    RollingRecipePattern.hForm(output, Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_IRON)),
        new ItemStackTemplate(RailcraftItems.POST.variantFor(DyeColor.BLACK), 16));

    RollingRecipePattern.diagonalLine(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_PLATE)),
        new ItemStackTemplate(RailcraftItems.TURBINE_BLADE), "steel_turbine_blade");
    RollingRecipePattern.diagonalLine(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.NICKEL_PLATE)),
        new ItemStackTemplate(RailcraftItems.TURBINE_BLADE), "nickel_turbine_blade");

    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.BRONZE_PLATE)),
        new ItemStackTemplate(RailcraftItems.BUSHING_GEAR, 4), "_bronze");
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.BRASS_PLATE)),
        new ItemStackTemplate(RailcraftItems.BUSHING_GEAR, 4), "_brass");
  }

  private void buildChargeSpool() {
    RollingRecipeBuilder.rolled(new ItemStackTemplate(RailcraftItems.CHARGE_SPOOL_LARGE))
        .pattern("a")
        .define(this.items, 'a', Tags.Items.STORAGE_BLOCKS_COPPER)
        .save(output);
    RollingRecipeBuilder.rolled(new ItemStackTemplate(RailcraftItems.CHARGE_SPOOL_SMALL))
        .pattern("a")
        .define(this.items, 'a', Tags.Items.INGOTS_COPPER)
        .save(output);
  }

  private void buildTrackParts() {
    final var trackParts = new ItemStackTemplate(RailcraftItems.TRACK_PARTS);
    var name = RecipeBuilder.getDefaultRecipeId(trackParts).identifier().getPath();
    RollingRecipeBuilder.rolled(trackParts)
        .pattern("aa ")
        .pattern("a  ")
        .define(this.items, 'a', RailcraftTags.Items.BRONZE_NUGGET)
        .save(output, RailcraftConstants.id(name + "_bronze_nugget"));
    RollingRecipeBuilder.rolled(trackParts)
        .pattern("aa")
        .define(this.items, 'a', Tags.Items.NUGGETS_IRON)
        .save(output, RailcraftConstants.id(name + "_iron_nugget"));
    RollingRecipeBuilder.rolled(trackParts)
        .pattern("a")
        .define(this.items, 'a', RailcraftTags.Items.STEEL_NUGGET)
        .save(output, RailcraftConstants.id(name + "_steel_nugget"));
  }

  private void buildRebars() {
    RollingRecipePattern.diagonalLine(output,
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_IRON)),
        new ItemStackTemplate(RailcraftItems.REBAR), "rebar_iron");
    RollingRecipePattern.diagonalLine(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.BRONZE_INGOT)),
        new ItemStackTemplate(RailcraftItems.REBAR), "rebar_bronze");
    RollingRecipePattern.diagonalLine(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.INVAR_INGOT)),
        new ItemStackTemplate(RailcraftItems.REBAR), "rebar_invar");
    RollingRecipePattern.diagonalLine(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_INGOT)),
        new ItemStackTemplate(RailcraftItems.REBAR), "rebar_steel");
  }

  private void buildElectrodes() {
    RollingRecipePattern.line(output, Ingredient.of(Items.COAL, Items.CHARCOAL),
        new ItemStackTemplate(RailcraftItems.CARBON_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.NICKEL_PLATE)),
        new ItemStackTemplate(RailcraftItems.NICKEL_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.IRON_PLATE)),
        new ItemStackTemplate(RailcraftItems.IRON_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.ZINC_PLATE)),
        new ItemStackTemplate(RailcraftItems.ZINC_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.SILVER_PLATE)),
        new ItemStackTemplate(RailcraftItems.SILVER_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_PLATE)),
        new ItemStackTemplate(RailcraftItems.STEEL_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.TIN_PLATE)),
        new ItemStackTemplate(RailcraftItems.TIN_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.GOLD_PLATE)),
        new ItemStackTemplate(RailcraftItems.GOLD_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.LEAD_PLATE)),
        new ItemStackTemplate(RailcraftItems.LEAD_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.BRASS_PLATE)),
        new ItemStackTemplate(RailcraftItems.BRASS_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.INVAR_PLATE)),
        new ItemStackTemplate(RailcraftItems.INVAR_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.BRONZE_PLATE)),
        new ItemStackTemplate(RailcraftItems.BRONZE_ELECTRODE));
    RollingRecipePattern.line(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.COPPER_PLATE)),
        new ItemStackTemplate(RailcraftItems.COPPER_ELECTRODE));
  }

  private void buildRails() {
    RollingRecipePattern.parallelLines(output,
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_IRON)),
        new ItemStackTemplate(RailcraftItems.STANDARD_RAIL, 8), "standard_rail");
    RollingRecipePattern.parallelLines(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.BRONZE_INGOT)),
        new ItemStackTemplate(RailcraftItems.STANDARD_RAIL, 8), "bronze_rail");
    RollingRecipePattern.parallelLines(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.INVAR_INGOT)),
        new ItemStackTemplate(RailcraftItems.STANDARD_RAIL, 12), "invar_rail");
    RollingRecipePattern.parallelLines(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_INGOT)),
        new ItemStackTemplate(RailcraftItems.STANDARD_RAIL, 16), "steel_rail");
    RollingRecipePattern.parallelLines(output,
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_COPPER)),
        new ItemStackTemplate(RailcraftItems.ELECTRIC_RAIL, 6), "copper_electric_rail");
    RollingRecipePattern.parallelLines(output,
        Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)),
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_GOLD)),
        new ItemStackTemplate(RailcraftItems.ADVANCED_RAIL, 8), "advanced_rail");
    RollingRecipePattern.parallelThreeLines(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_INGOT)),
        Ingredient.of(Items.BLAZE_POWDER),
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_GOLD)),
        new ItemStackTemplate(RailcraftItems.HIGH_SPEED_RAIL, 8), "standard_high_speed_rail");
    RollingRecipePattern.parallelThreeLines(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_INGOT)),
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.OBSIDIAN_DUST)),
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_INGOT)),
        new ItemStackTemplate(RailcraftItems.REINFORCED_RAIL, 8), "steel_reinforced_rail");
    RollingRecipePattern.parallelThreeLines(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.INVAR_INGOT)),
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.OBSIDIAN_DUST)),
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.INVAR_INGOT)),
        new ItemStackTemplate(RailcraftItems.REINFORCED_RAIL, 4), "invar_reinforced_rail");
    RollingRecipePattern.parallelThreeLines(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_INGOT)),
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_COPPER)),
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_INGOT)),
        new ItemStackTemplate(RailcraftItems.ELECTRIC_RAIL, 12));
  }

  private void buildPlates() {
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_IRON)),
        new ItemStackTemplate(RailcraftItems.IRON_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_GOLD)),
        new ItemStackTemplate(RailcraftItems.GOLD_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_COPPER)),
        new ItemStackTemplate(RailcraftItems.COPPER_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_INGOT)),
        new ItemStackTemplate(RailcraftItems.STEEL_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.NICKEL_INGOT)),
        new ItemStackTemplate(RailcraftItems.NICKEL_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.INVAR_INGOT)),
        new ItemStackTemplate(RailcraftItems.INVAR_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.BRASS_INGOT)),
        new ItemStackTemplate(RailcraftItems.BRASS_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.TIN_INGOT)),
        new ItemStackTemplate(RailcraftItems.TIN_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.BRONZE_INGOT)),
        new ItemStackTemplate(RailcraftItems.BRONZE_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.LEAD_INGOT)),
        new ItemStackTemplate(RailcraftItems.LEAD_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.SILVER_INGOT)),
        new ItemStackTemplate(RailcraftItems.SILVER_PLATE, 4));
    RollingRecipePattern.square2x2(output,
        Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.ZINC_INGOT)),
        new ItemStackTemplate(RailcraftItems.ZINC_PLATE, 4));
  }
}
