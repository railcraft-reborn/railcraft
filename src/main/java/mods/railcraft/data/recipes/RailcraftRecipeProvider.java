package mods.railcraft.data.recipes;

import java.util.function.Consumer;
import mods.railcraft.Railcraft;
import mods.railcraft.data.recipes.helpers.RollingRecipePattern;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

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
    RecipeBlocks.registerBlocks(consumer);
    RecipeBlocks.compressItem(consumer);
    RecipeBlocks.decompressItem(consumer);
    RecipeBlocks.gearItem(consumer);

    /*
     * ===================================== RAILCRAFT CRAFTING COMPONENTS =========================
     */
    this.circuitFromMaterial(consumer, RailcraftItems.CONTROLLER_CIRCUIT.get(),
        Items.RED_WOOL);
    this.circuitFromMaterial(consumer, RailcraftItems.RECEIVER_CIRCUIT.get(),
        Items.GREEN_WOOL);
    this.circuitFromMaterial(consumer, RailcraftItems.SIGNAL_CIRCUIT.get(),
        Items.YELLOW_WOOL);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAIL.get(), 6)
        .requires(RailcraftItems.WOODEN_TIE.get())
        .requires(Tags.Items.INGOTS_IRON)
        .unlockedBy("has_wooden_tie", has(Items.RAIL))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.STANDARD_RAIL.get())
        .requires(Items.RAIL, 8)
        .unlockedBy("has_rail", has(Items.RAIL))
        .save(consumer, Railcraft.ID + ":rail_deconstruction");

    //NEW
    RollingRecipePattern.parallelLines(consumer, Tags.Items.INGOTS_IRON,
        RailcraftItems.STANDARD_RAIL.get(), 8, "standard_rail");
    RollingRecipePattern.parallelLines(consumer, RailcraftTags.Items.BRONZE_INGOT,
        RailcraftItems.STANDARD_RAIL.get(), 8, "bronze_rail");
    RollingRecipePattern.parallelLines(consumer, RailcraftTags.Items.INVAR_INGOT,
        RailcraftItems.STANDARD_RAIL.get(), 12, "invar_rail");
    RollingRecipePattern.parallelLines(consumer, RailcraftTags.Items.STEEL_INGOT,
        RailcraftItems.STANDARD_RAIL.get(), 16, "steel_rail");
    RollingRecipePattern.parallelLines(consumer, Tags.Items.INGOTS_COPPER,
        RailcraftItems.ELECTRIC_RAIL.get(), 6, "copper_electric_rail");
    RollingRecipePattern.parallelLines(consumer, Tags.Items.DUSTS_REDSTONE, Tags.Items.INGOTS_GOLD,
        RailcraftItems.ADVANCED_RAIL.get(), 8, "advanced_rail");
    RollingRecipePattern.parallelThreeLines(consumer, RailcraftTags.Items.STEEL_INGOT,
        Items.BLAZE_POWDER,
        Tags.Items.INGOTS_GOLD, RailcraftItems.HIGH_SPEED_RAIL.get(), 8,
        "standard_high_speed_rail");
    RollingRecipePattern.parallelThreeLines(consumer, RailcraftTags.Items.STEEL_INGOT,
        RailcraftTags.Items.OBSIDIAN_DUST,
        RailcraftTags.Items.STEEL_INGOT, RailcraftItems.REINFORCED_RAIL.get(), 8,
        "steel_reinforced_rail");
    RollingRecipePattern.parallelThreeLines(consumer, RailcraftTags.Items.INVAR_INGOT,
        RailcraftTags.Items.OBSIDIAN_DUST,
        RailcraftTags.Items.INVAR_INGOT, RailcraftItems.REINFORCED_RAIL.get(), 4,
        "invar_reinforced_rail");

    RollingRecipePattern.diagonalLine(consumer, Tags.Items.INGOTS_IRON,
        RailcraftItems.REBAR.get(), 4, "rebar_iron");
    RollingRecipePattern.diagonalLine(consumer, RailcraftTags.Items.BRONZE_INGOT,
        RailcraftItems.REBAR.get(), 4, "rebar_bronze");
    RollingRecipePattern.diagonalLine(consumer, RailcraftTags.Items.INVAR_INGOT,
        RailcraftItems.REBAR.get(), 6, "rebar_invar");
    RollingRecipePattern.diagonalLine(consumer, RailcraftTags.Items.STEEL_INGOT,
        RailcraftItems.REBAR.get(), 8, "rebar_steel");

    RollingRecipePattern.parallelThreeLines(consumer, RailcraftTags.Items.STEEL_INGOT,
        Tags.Items.INGOTS_COPPER, RailcraftTags.Items.STEEL_INGOT,
        RailcraftItems.ELECTRIC_RAIL.get(), 12);
    RollingRecipePattern.hForm(consumer, Tags.Items.INGOTS_IRON,
        RailcraftItems.POST.variantFor(DyeColor.BLACK).get(), 16);

    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.BRONZE_PLATE,
        RailcraftItems.BUSHING_GEAR.get(), 4, "_bronze");
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.BRASS_PLATE,
        RailcraftItems.BUSHING_GEAR.get(), 4, "_brass");

    RollingRecipePattern.square2x2(consumer, Tags.Items.INGOTS_IRON,
        RailcraftItems.IRON_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, Tags.Items.INGOTS_GOLD,
        RailcraftItems.GOLD_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, Tags.Items.INGOTS_COPPER,
        RailcraftItems.COPPER_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.STEEL_INGOT,
        RailcraftItems.STEEL_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.NICKEL_INGOT,
        RailcraftItems.NICKEL_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.INVAR_INGOT,
        RailcraftItems.INVAR_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.BRASS_INGOT,
        RailcraftItems.BRASS_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.TIN_INGOT,
        RailcraftItems.TIN_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.BRONZE_INGOT,
        RailcraftItems.BRONZE_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.LEAD_INGOT,
        RailcraftItems.LEAD_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.SILVER_INGOT,
        RailcraftItems.SILVER_PLATE.get(), 4);
    RollingRecipePattern.square2x2(consumer, RailcraftTags.Items.ZINC_INGOT,
        RailcraftItems.ZINC_PLATE.get(), 4);

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

    CokeOvenRecipeBuilder
        .coking(Items.COAL, Ingredient.of(ItemTags.LOGS), 0.0F, 250)
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(consumer);

    CokeOvenRecipeBuilder
        .coking(RailcraftItems.COAL_COKE.get(), Ingredient.of(Items.COAL), 0.0F, 500)
        .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
        .save(consumer);

    new BlastFurnaceRecipeBuilder(RailcraftBlocks.STEEL_BLOCK.get(), 1,
        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), 0.0F,
        BlastFurnaceRecipeBuilder.DEFAULT_COOKING_TIME * 9,
        9)
        .unlockedBy("has_iron_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
        .save(consumer, getRailcraftBlastingRecipeName(RailcraftItems.STEEL_BLOCK.get()));
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(Tags.Items.INGOTS_IRON), 1)
        .unlockedBy("has_iron_ingots", has(Tags.Items.INGOTS_IRON))
        .save(consumer, getRailcraftBlastingRecipeName(Items.IRON_INGOT));

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
    blastFurnaceSmelting(consumer, RailcraftItems.IRON_CROWBAR.get(), 3);
    blastFurnaceSmelting(consumer, Items.IRON_DOOR, 6);
    blastFurnaceSmelting(consumer, Items.IRON_TRAPDOOR, 6);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_HELMET.get(), 4);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_CHESTPLATE.get(), 6);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_LEGGINGS.get(), 5);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_BOOTS.get(), 3);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_SWORD.get(), 1);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_PICKAXE.get(), 2);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_HOE.get(), 1);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_AXE.get(), 2);
    blastFurnaceRecycling(consumer, RailcraftItems.STEEL_SHEARS.get(), 1);
  }

  private static void blastFurnaceSmelting(Consumer<FinishedRecipe> consumer, ItemLike item,
      int multiplier) {
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier)
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
    return new ResourceLocation(Railcraft.ID, "blasting_" + tag);
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
        // this is deliberate as vanilla ones fail to properly register (rails.json already exists)
        .save(finishedRecipe,
            new ResourceLocation(Railcraft.ID,
                RecipeBuilder.getDefaultRecipeId(itemOut).getPath()));
  }


  @Override
  public String getName() {
    return "Railcraft Recipes";
  }
}
