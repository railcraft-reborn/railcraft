package mods.railcraft.data.recipes;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.VariantRegistrar;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class RailcraftRecipeProvider extends RecipeProvider {

  public RailcraftRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
    /*
     * ===================================== RAILCRAFT TOOLS =====================================
     */
    /* === Crowbar === */
    this.crowbarFromMaterial(consumer, RailcraftItems.IRON_CROWBAR.get(),
        Tags.Items.INGOTS_IRON);
    this.crowbarFromMaterial(consumer, RailcraftItems.STEEL_CROWBAR.get(),
        RailcraftTags.Items.STEEL_INGOT);
    this.crowbarFromMaterial(consumer, RailcraftItems.DIAMOND_CROWBAR.get(),
        Tags.Items.GEMS_DIAMOND);

    ShapedRecipeBuilder.shaped(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get())
        .define('C', Items.COMPASS)
        .define('G', Tags.Items.GLASS_PANES)
        .define('B', Blocks.STONE_BUTTON)
        .define('R', Tags.Items.DUSTS_REDSTONE)
        .pattern(" C ")
        .pattern("BGB")
        .pattern(" R ")
        .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.SIGNAL_TUNER.get())
        .define('T', Items.REDSTONE_TORCH)
        .define('C', RailcraftItems.RECEIVER_CIRCUIT.get())
        .define('B', Blocks.STONE_BUTTON)
        .pattern(" T ")
        .pattern("BCB")
        .pattern("   ")
        .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .unlockedBy("has_circuit", has(RailcraftItems.RECEIVER_CIRCUIT.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.GOGGLES.get())
        .define('S', RailcraftTags.Items.STEEL_INGOT)
        .define('G', Tags.Items.GLASS_PANES)
        .define('C', RailcraftItems.RECEIVER_CIRCUIT.get())
        .define('L', Tags.Items.LEATHER)
        .pattern("GCG")
        .pattern("S S")
        .pattern("LLL")
        .unlockedBy("has_steel", has(RailcraftTags.Items.STEEL_INGOT))
        .unlockedBy("has_circuit", has(RailcraftItems.RECEIVER_CIRCUIT.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.WHISTLE_TUNER.get())
        .define('N', RailcraftTags.Items.STEEL_NUGGET)
        .pattern("N N")
        .pattern("NNN")
        .pattern(" N ")
        .unlockedBy("has_nugget", has(RailcraftTags.Items.STEEL_NUGGET))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.OVERALLS.get())
        .define('W', Items.CYAN_WOOL)
        .pattern("WWW")
        .pattern("W W")
        .pattern("W W")
        .unlockedBy("has_wool", has(Items.CYAN_WOOL))
        .save(consumer);

    // this is a tool i think
    ShapedRecipeBuilder.shaped(RailcraftItems.REFINED_FIRESTONE.get())
        .define('L', Items.LAVA_BUCKET)
        .define('R', Items.REDSTONE_BLOCK)
        .define('S', RailcraftItems.CUT_FIRESTONE.get())
        .pattern("LRL")
        .pattern("RSR")
        .pattern("LRL")
        .unlockedBy("has_firestone", has(RailcraftItems.CUT_FIRESTONE.get()))
        .save(consumer, Railcraft.ID + ":firestone_lava_refinement");

    ShapedRecipeBuilder.shaped(RailcraftItems.REFINED_FIRESTONE.get())
        .define('L', Items.LAVA_BUCKET)
        .define('R', Items.REDSTONE_BLOCK)
        .define('S', RailcraftItems.CRACKED_FIRESTONE.get())
        .define('F', RailcraftItems.RAW_FIRESTONE.get())
        .pattern("LFL")
        .pattern("RSR")
        .pattern("LRL")
        .unlockedBy("has_firestone", has(RailcraftItems.CRACKED_FIRESTONE.get()))
        .save(consumer, Railcraft.ID + ":firestone_cracked_fixing");

    /*
     * ===================================== RAILCRAFT BLOCKS =====================================
     */
    buildBlocks(consumer);
    buildCompressions(consumer);
    buildDecompressions(consumer);
    buildGears(consumer);
    buildKits(consumer);

    /*
     * ===================================== RAILCRAFT CRAFTING COMPONENTS =========================
     */
    this.circuitFromMaterial(consumer, RailcraftItems.CONTROLLER_CIRCUIT.get(),
        Items.RED_WOOL);
    this.circuitFromMaterial(consumer, RailcraftItems.RECEIVER_CIRCUIT.get(),
        Items.GREEN_WOOL);
    this.circuitFromMaterial(consumer, RailcraftItems.SIGNAL_CIRCUIT.get(),
        Items.YELLOW_WOOL);

    this.conversion(consumer, RailcraftItems.CHARGE_SPOOL_MEDIUM.get(),
        RailcraftItems.CHARGE_SPOOL_SMALL.get(), 3, "charge_spool_small_from_medium");
    this.conversion(consumer, RailcraftItems.CHARGE_SPOOL_LARGE.get(),
        RailcraftItems.CHARGE_SPOOL_MEDIUM.get(), 3, "charge_spool_medium_from_large");

    ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAIL.get(), 6)
        .requires(RailcraftItems.WOODEN_TIE.get())
        .requires(Tags.Items.INGOTS_IRON)
        .unlockedBy("has_wooden_tie", has(Items.RAIL))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.STANDARD_RAIL.get())
        .requires(Items.RAIL, 8)
        .unlockedBy("has_rail", has(Items.RAIL))
        .save(consumer, Railcraft.ID + ":rail_deconstruction");

    ShapedRecipeBuilder.shaped(RailcraftItems.STONE_TIE.get())
        .define('R', RailcraftItems.REBAR.get())
        .define('S', Items.STONE_SLAB)
        .pattern(" R ")
        .pattern("SSS")
        .pattern("   ")
        .unlockedBy(getHasName(RailcraftItems.REBAR.get()), has(RailcraftItems.REBAR.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAILBED.get())
        .requires(RailcraftItems.WOODEN_TIE.get(), 4)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.STONE_RAILBED.get())
        .requires(RailcraftItems.STONE_TIE.get(), 4)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);

    // this.railsFromMaterials(finishedRecipe, RailcraftItems.IRON_.get(),
    // RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    this.railsFromMaterials(consumer, Items.RAIL,
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    this.railsFromMaterials(consumer, RailcraftItems.REINFORCED_TRACK.get(),
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    this.railsFromMaterials(consumer, RailcraftItems.ELECTRIC_TRACK.get(),
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    this.railsFromMaterials(consumer, RailcraftItems.HIGH_SPEED_TRACK.get(),
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
  }

  private void crowbarFromMaterial(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut, TagKey<Item> materialTag) {
    ShapedRecipeBuilder.shaped(itemOut)
        .define('I', materialTag)
        .define('D', Tags.Items.DYES_RED)
        .pattern(" DI")
        .pattern("DID")
        .pattern("ID ")
        .unlockedBy(getHasName(Blocks.RAIL), has(Blocks.RAIL))
        .save(finishedRecipe);
  }

  private void conversion(Consumer<FinishedRecipe> finishedRecipe, ItemLike from, ItemLike to,
      int count, String optionalName) {
    ResourceLocation path;
    if (optionalName.isEmpty()) {
      path = RecipeBuilder.getDefaultRecipeId(to);
    } else {
      path = new ResourceLocation(Railcraft.ID, optionalName);
    }
    ShapelessRecipeBuilder.shapeless(to, count)
        .requires(from)
        .unlockedBy(getHasName(from), has(from))
        .save(finishedRecipe, path);
  }

  private void circuitFromMaterial(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut, Item woolItem) {
    ShapedRecipeBuilder.shaped(itemOut)
        .define('W', woolItem)
        .define('R', Items.REPEATER)
        .define('S', Tags.Items.DUSTS_REDSTONE)
        .define('G', Tags.Items.INGOTS_GOLD)
        .define('L', Tags.Items.GEMS_LAPIS)
        .define('B', Tags.Items.SLIMEBALLS)
        .pattern(" RW")
        .pattern("BGS")
        .pattern("WSL")
        .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
        .save(finishedRecipe);
  }

  private void railsFromMaterials(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut, Item railType, Item railBedType) {
    ShapedRecipeBuilder.shaped(itemOut, 32)
        .define('I', railType)
        .define('B', railBedType)
        .pattern("I I")
        .pattern("IBI")
        .pattern("I I")
        .unlockedBy(getHasName(railType), has(railType))
        .unlockedBy(getHasName(railBedType), has(railBedType))
        // this is deliberate as vanilla ones fail to properly build (rails.json already exists)
        .save(finishedRecipe,
            new ResourceLocation(Railcraft.ID,
                RecipeBuilder.getDefaultRecipeId(itemOut).getPath()));
  }

  @Override
  public String getName() {
    return "Railcraft Recipes";
  }

  private static void buildKits(Consumer<FinishedRecipe> consumer) {
    kits(consumer, RailcraftItems.ACTIVATOR_TRACK_KIT.get(), 8, List.of(
            new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1),
            new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
        ));
    kits(consumer, RailcraftItems.BOOSTER_TRACK_KIT.get(), 16, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), 2, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.INGOTS_IRON), 1),
        new Tuple<>(Ingredient.of(Tags.Items.INGOTS_IRON), 1)
    ));
    kits(consumer, RailcraftItems.CONTROL_TRACK_KIT.get(), 16, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.DETECTOR_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.DISEMBARKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.EMBARKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.ENDER_PEARL), 1),
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.GATED_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.FENCE_GATES), 1),
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.LOCKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.STICKY_PISTON), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.ONE_WAY_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.PISTON), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.LAUNCHER_TRACK_KIT.get(), 1, List.of(
        new Tuple<>(Ingredient.of(Items.PISTON), 1),
        new Tuple<>(Ingredient.of(RailcraftTags.Items.STEEL_BLOCK), 1),
        new Tuple<>(Ingredient.of(RailcraftTags.Items.STEEL_BLOCK), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.SIGNAL_LAMP.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.TRANSITION_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.COUPLER_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
  }

  private static void kits(Consumer<FinishedRecipe> finishedRecipe,
      Item result,
      int count,
      List<Tuple<Ingredient, Integer>> ingredients) {

      var builder = ShapelessRecipeBuilder.shapeless(result, count)
          .requires(Items.OAK_WOOD)
          .requires(RailcraftItems.TRACK_PARTS.get());

      for(var ingredient : ingredients) {
        builder = builder.requires(ingredient.getA(), ingredient.getB());
      }
      builder
          .unlockedBy(getHasName(RailcraftItems.TRACK_PARTS.get()),
              has(RailcraftItems.TRACK_PARTS.get()))
          .save(finishedRecipe);
  }

  private static void buildGears(Consumer<FinishedRecipe> consumer) {
    square2x2(consumer, RailcraftTags.Items.BRONZE_INGOT,
        RailcraftItems.BUSHING_GEAR.get(), 1, "_bronze");
    square2x2(consumer, RailcraftTags.Items.BRASS_INGOT,
        RailcraftItems.BUSHING_GEAR.get(), 1, "_brass");

    gear(consumer, RailcraftItems.IRON_GEAR.get(),
        Tags.Items.INGOTS_IRON);
    gear(consumer, RailcraftItems.COPPER_GEAR.get(),
        Tags.Items.INGOTS_COPPER);
    gear(consumer, RailcraftItems.GOLD_GEAR.get(),
        Tags.Items.INGOTS_GOLD);
    gear(consumer, RailcraftItems.STEEL_GEAR.get(),
        RailcraftTags.Items.STEEL_INGOT);
    gear(consumer, RailcraftItems.TIN_GEAR.get(),
        RailcraftTags.Items.TIN_INGOT);
    gear(consumer, RailcraftItems.ZINC_GEAR.get(),
        RailcraftTags.Items.ZINC_INGOT);
    gear(consumer, RailcraftItems.BRASS_GEAR.get(),
        RailcraftTags.Items.BRASS_INGOT);
    gear(consumer, RailcraftItems.BRONZE_GEAR.get(),
        RailcraftTags.Items.BRONZE_INGOT);
    gear(consumer, RailcraftItems.NICKEL_GEAR.get(),
        RailcraftTags.Items.NICKEL_INGOT);
    gear(consumer, RailcraftItems.INVAR_GEAR.get(),
        RailcraftTags.Items.INVAR_INGOT);
    gear(consumer, RailcraftItems.SILVER_GEAR.get(),
        RailcraftTags.Items.SILVER_INGOT);
    gear(consumer, RailcraftItems.LEAD_GEAR.get(),
        RailcraftTags.Items.LEAD_INGOT);
  }

  private static void gear(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut,
      TagKey<Item> materialTag) {
    ShapedRecipeBuilder.shaped(itemOut)
        .pattern(" a ")
        .pattern("aba")
        .pattern(" a ")
        .define('a', materialTag)
        .define('b', RailcraftItems.BUSHING_GEAR.get())
        .unlockedBy("has_material", has(materialTag))
        .save(finishedRecipe);
  }

  // ================================================================================
  // Blocks
  // ================================================================================

  private static void buildBlocks(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RailcraftItems.COKE_OVEN_BRICKS.get())
        .define('B', Items.BRICK)
        .define('S', Tags.Items.SAND)
        .pattern("SBS")
        .pattern("BSB")
        .pattern("SBS")
        .unlockedBy(getHasName(Items.BRICK), has(Items.BRICK))
        .unlockedBy(getHasName(Items.SAND), has(Tags.Items.SAND))
        .save(consumer);

    tankWall(consumer,
        RailcraftItems.IRON_PLATE.get(),
        RailcraftItems.IRON_TANK_WALL,
        RailcraftTags.Items.IRON_TANK_WALL);
    tankWall(consumer,
        RailcraftItems.STEEL_PLATE.get(),
        RailcraftItems.STEEL_TANK_WALL,
        RailcraftTags.Items.STEEL_TANK_WALL);
    tankValve(consumer,
        RailcraftItems.IRON_PLATE.get(),
        RailcraftItems.IRON_TANK_VALVE,
        RailcraftTags.Items.IRON_TANK_VALVE);
    tankValve(consumer,
        RailcraftItems.STEEL_PLATE.get(),
        RailcraftItems.STEEL_TANK_VALVE,
        RailcraftTags.Items.STEEL_TANK_VALVE);

    tankGauge(consumer,
        RailcraftItems.IRON_PLATE.get(),
        RailcraftItems.IRON_TANK_GAUGE,
        RailcraftTags.Items.IRON_TANK_GAUGE);
    tankGauge(consumer,
        RailcraftItems.STEEL_PLATE.get(),
        RailcraftItems.STEEL_TANK_GAUGE,
        RailcraftTags.Items.STEEL_TANK_GAUGE);

    post(consumer, RailcraftItems.POST, RailcraftTags.Items.POST);
    strengthenedGlass(consumer);
  }

  private static void buildDecompressions(Consumer<FinishedRecipe> consumer) {
    decompress(consumer, RailcraftItems.STEEL_NUGGET.get(),
        RailcraftTags.Items.STEEL_INGOT, "nugget");
    decompress(consumer, RailcraftItems.TIN_NUGGET.get(),
        RailcraftTags.Items.TIN_INGOT, "nugget");
    decompress(consumer, RailcraftItems.ZINC_NUGGET.get(),
        RailcraftTags.Items.ZINC_INGOT, "nugget");
    decompress(consumer, RailcraftItems.BRASS_NUGGET.get(),
        RailcraftTags.Items.BRASS_INGOT, "nugget");
    decompress(consumer, RailcraftItems.BRONZE_NUGGET.get(),
        RailcraftTags.Items.BRONZE_INGOT, "nugget");
    decompress(consumer, RailcraftItems.NICKEL_NUGGET.get(),
        RailcraftTags.Items.NICKEL_INGOT, "nugget");
    decompress(consumer, RailcraftItems.INVAR_NUGGET.get(),
        RailcraftTags.Items.INVAR_INGOT, "nugget");
    decompress(consumer, RailcraftItems.SILVER_NUGGET.get(),
        RailcraftTags.Items.SILVER_INGOT, "nugget");
    decompress(consumer, RailcraftItems.LEAD_NUGGET.get(),
        RailcraftTags.Items.LEAD_INGOT, "nugget");

    decompress(consumer, RailcraftItems.STEEL_INGOT.get(),
        RailcraftTags.Items.STEEL_BLOCK, "block_ingot");
    decompress(consumer, RailcraftItems.COAL_COKE.get(),
        RailcraftItems.COKE_BLOCK.get(), "block_coke");
  }

  private static void buildCompressions(Consumer<FinishedRecipe> consumer) {
    compress(consumer, RailcraftItems.STEEL_INGOT.get(),
        RailcraftTags.Items.STEEL_NUGGET, "ingot");
    compress(consumer, RailcraftItems.TIN_INGOT.get(),
        RailcraftTags.Items.TIN_NUGGET, "ingot");
    compress(consumer, RailcraftItems.ZINC_INGOT.get(),
        RailcraftTags.Items.ZINC_NUGGET, "ingot");
    compress(consumer, RailcraftItems.BRASS_INGOT.get(),
        RailcraftTags.Items.BRASS_NUGGET, "ingot");
    compress(consumer, RailcraftItems.BRONZE_INGOT.get(),
        RailcraftTags.Items.BRONZE_NUGGET, "ingot");
    compress(consumer, RailcraftItems.NICKEL_INGOT.get(),
        RailcraftTags.Items.NICKEL_NUGGET, "ingot");
    compress(consumer, RailcraftItems.INVAR_INGOT.get(),
        RailcraftTags.Items.INVAR_NUGGET, "ingot");
    compress(consumer, RailcraftItems.LEAD_INGOT.get(),
        RailcraftTags.Items.LEAD_NUGGET, "ingot");
    compress(consumer, RailcraftItems.SILVER_INGOT.get(),
        RailcraftTags.Items.SILVER_NUGGET, "ingot");

    compress(consumer, RailcraftItems.STEEL_BLOCK.get(),
        RailcraftTags.Items.STEEL_INGOT, "block");
    compress(consumer, RailcraftItems.COKE_BLOCK.get(),
        RailcraftItems.COAL_COKE.get(), "block");
  }

  private static void strengthenedGlass(Consumer<FinishedRecipe> consumer) {
    var ingredients = Map.of(
        "tin", RailcraftTags.Items.TIN_INGOT,
        "nickel", RailcraftTags.Items.NICKEL_INGOT,
        "invar", RailcraftTags.Items.INVAR_INGOT,
        "brass", RailcraftTags.Items.BRASS_INGOT,
        "iron", Tags.Items.INGOTS_IRON);

    var colorItems = RailcraftItems.STRENGTHENED_GLASS;
    var tagItem = RailcraftTags.Items.STRENGTHENED_GLASS;

    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();

    for (var ingredient : ingredients.entrySet()) {
      var recipeName = name.substring(name.indexOf('_') + 1) + "_" + ingredient.getKey();
      ShapedRecipeBuilder.shaped(result, 6)
          .pattern("aba")
          .pattern("aca")
          .pattern("ada")
          .define('a', Tags.Items.GLASS)
          .define('b', ingredient.getValue())
          .define('c', RailcraftItems.SALTPETER_DUST.get())
          .define('d', Items.WATER_BUCKET)
          .unlockedBy(getHasName(RailcraftItems.SALTPETER_DUST.get()),
              has(RailcraftItems.SALTPETER_DUST.get()))
          .save(consumer, new ResourceLocation(Railcraft.ID, recipeName));
    }

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private static void tankWall(Consumer<FinishedRecipe> consumer,
      ItemLike ingredient,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(result, 8)
        .pattern("aa")
        .pattern("aa")
        .define('a', ingredient)
        .unlockedBy(getHasName(ingredient), has(ingredient))
        .save(consumer, Railcraft.ID + ":" + name.substring(name.indexOf('_') + 1));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private static void tankValve(Consumer<FinishedRecipe> consumer,
      ItemLike ingredient,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(result, 8)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.IRON_BARS)
        .define('b', ingredient)
        .define('c', Items.LEVER)
        .unlockedBy(getHasName(ingredient), has(ingredient))
        .save(consumer, new ResourceLocation(Railcraft.ID, name.substring(name.indexOf('_') + 1)));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private static void tankGauge(Consumer<FinishedRecipe> consumer,
      ItemLike ingredient,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(result, 8)
        .pattern("aba")
        .pattern("bab")
        .pattern("aba")
        .define('a', Items.GLASS_PANE)
        .define('b', ingredient)
        .unlockedBy(getHasName(ingredient), has(ingredient))
        .save(consumer, new ResourceLocation(Railcraft.ID, name.substring(name.indexOf('_') + 1)));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private static void post(Consumer<FinishedRecipe> consumer,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    coloredBlockVariant(consumer, colorItems, tagItem, DyeColor.BLACK);
  }

  private static void compress(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut,
      TagKey<Item> materialTag,
      String identifier) {
    ShapedRecipeBuilder.shaped(itemOut)
        .pattern("###")
        .pattern("###")
        .pattern("###")
        .define('#', materialTag)
        .unlockedBy("has_material", has(materialTag))
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID,
            RecipeBuilder.getDefaultRecipeId(itemOut).getPath() + "_" + identifier));
  }

  private static void compress(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut,
      Item ingredient,
      String identifier) {
    ShapedRecipeBuilder.shaped(itemOut)
        .pattern("###")
        .pattern("###")
        .pattern("###")
        .define('#', ingredient)
        .unlockedBy(getHasName(ingredient), has(ingredient))
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID,
            RecipeBuilder.getDefaultRecipeId(itemOut).getPath() + "_" + identifier));
  }

  private static void decompress(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut,
      TagKey<Item> materialTag,
      String identifier) {
    ShapelessRecipeBuilder.shapeless(itemOut, 9)
        .requires(materialTag)
        .unlockedBy("has_material", has(materialTag))
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID,
            RecipeBuilder.getDefaultRecipeId(itemOut).getPath() + "_" + identifier));
  }

  private static void decompress(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut,
      Item ingredient,
      String identifier) {
    ShapelessRecipeBuilder.shapeless(itemOut, 9)
        .requires(ingredient)
        .unlockedBy("has_material", has(ingredient))
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID,
            RecipeBuilder.getDefaultRecipeId(itemOut).getPath() + "_" + identifier));
  }

  private static void square2x2(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> ingredient,
      Item result,
      int quantity,
      String suffix) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(result, quantity)
        .pattern("aa")
        .pattern("aa")
        .define('a', ingredient)
        .unlockedBy("has_material", has(ingredient))
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, name + suffix));
  }

  private static void coloredBlockVariant(Consumer<FinishedRecipe> consumer,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    coloredBlockVariant(consumer, colorItems, tagItem, DyeColor.WHITE);
  }

  private static void coloredBlockVariant(Consumer<FinishedRecipe> consumer,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem,
      DyeColor baseColor) {
    var base = colorItems.variantFor(baseColor).get();
    for (var dyeColor : DyeColor.values()) {
      ShapedRecipeBuilder.shaped(colorItems.variantFor(dyeColor).get(), 8)
          .pattern("aaa")
          .pattern("aba")
          .pattern("aaa")
          .define('a', tagItem)
          .define('b', DyeItem.byColor(dyeColor))
          .unlockedBy(getHasName(base), has(base))
          .save(consumer);
    }
  }
}
