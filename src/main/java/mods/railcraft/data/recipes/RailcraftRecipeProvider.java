package mods.railcraft.data.recipes;

import java.util.function.Consumer;
import mods.railcraft.Railcraft;
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
     * =============== RAILCRAFT TOOLS ===============
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
     * ================== RAILCRAFT BLOCKS ==================
     */
    ShapedRecipeBuilder.shaped(RailcraftItems.COKE_OVEN_BRICKS.get())
        .define('B', Items.BRICK)
        .define('S', Tags.Items.SAND)
        .pattern("SBS")
        .pattern("BSB")
        .pattern("SBS")
        .unlockedBy("has_brick", has(Items.BRICK))
        .unlockedBy("has_snad", has(Tags.Items.SAND)) // intentional
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_ANVIL.get())
        .define('B', RailcraftTags.Items.STEEL_BLOCK)
        .define('I', RailcraftTags.Items.STEEL_INGOT)
        .pattern("BBB")
        .pattern(" I ")
        .pattern("III")
        .unlockedBy("has_steel", has(RailcraftTags.Items.STEEL_INGOT))
        .unlockedBy("has_steel_block", has(RailcraftTags.Items.STEEL_BLOCK))
        .save(consumer);

    // iron = light grey
    ShapedRecipeBuilder.shaped(RailcraftItems.LIGHT_GRAY_IRON_TANK_WALL.get(), 8)
        .define('P', RailcraftTags.Items.IRON_PLATES)
        .pattern("PP ")
        .pattern("PP ")
        .pattern("   ")
        .unlockedBy("has_steel_plate", has(RailcraftTags.Items.IRON_PLATES))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.LIGHT_GRAY_IRON_TANK_VALVE.get(), 8)
        .define('P', RailcraftTags.Items.IRON_PLATES)
        .define('B', Items.IRON_BARS)
        .define('L', Items.LEVER)
        .pattern("BPB")
        .pattern("PLP")
        .pattern("BPB")
        .unlockedBy("has_steel_plate", has(RailcraftTags.Items.IRON_PLATES))
        .save(consumer);
    // this.tankGaugeFromMaterial(consumer, RailcraftItems.LIGHT_GRAY_IRON_TANK_GAUGE.get())

    // steel = grey
    ShapedRecipeBuilder.shaped(RailcraftItems.GRAY_STEEL_TANK_WALL.get(), 8)
        .define('P', RailcraftTags.Items.STEEL_PLATES)
        .pattern("PP ")
        .pattern("PP ")
        .pattern("   ")
        .unlockedBy("has_steel_plate", has(RailcraftTags.Items.STEEL_PLATES))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.GRAY_STEEL_TANK_VALVE.get(), 8)
        .define('P', RailcraftTags.Items.STEEL_PLATES)
        .define('B', Items.IRON_BARS)
        .define('L', Items.LEVER)
        .pattern("BPB")
        .pattern("PLP")
        .pattern("BPB")
        .unlockedBy("has_steel_plate", has(RailcraftTags.Items.STEEL_PLATES))
        .save(consumer);

    // this.railsFromMaterials(finishedRecipie, RailcraftItems.IRON_.get(),
    // RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    this.railsFromMaterials(consumer, Items.RAIL,
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    this.railsFromMaterials(consumer, RailcraftItems.REINFORCED_TRACK.get(),
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    this.railsFromMaterials(consumer, RailcraftItems.ELECTRIC_TRACK.get(),
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    this.railsFromMaterials(consumer, RailcraftItems.HIGH_SPEED_TRACK.get(),
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());

    /*
     * =============== RAILCRAFT CRAFTING COMPONENTS ===============
     */

    /* == Misc == */
    RollingRecipeBuilder.rolled(RailcraftItems.STEEL_PLATE.get(), 4)
        .define('P', RailcraftTags.Items.STEEL_PLATES)
        .pattern("PP ")
        .pattern("PP ")
        .pattern("   ")
        .save(consumer);

    /* == Circuits == */
    {
      this.circuitFromMaterial(consumer, RailcraftItems.CONTROLLER_CIRCUIT.get(),
        Items.RED_WOOL);
      this.circuitFromMaterial(consumer, RailcraftItems.RECEIVER_CIRCUIT.get(),
        Items.GREEN_WOOL);
      this.circuitFromMaterial(consumer, RailcraftItems.SIGNAL_CIRCUIT.get(),
        Items.YELLOW_WOOL);
    }

    /* == Rails == */
    {
      ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAIL.get(), 6)
          .requires(RailcraftItems.WOODEN_TIE.get())
          .requires(Tags.Items.INGOTS_IRON)
          .unlockedBy("has_wooden_tie", has(Items.RAIL))
          .save(consumer);

      ShapelessRecipeBuilder.shapeless(RailcraftItems.STANDARD_RAIL.get())
          .requires(Items.RAIL, 8)
          .unlockedBy("has_rail", has(Items.RAIL))
          .save(consumer, Railcraft.ID + ":rail_deconstruction");

      RollingRecipeBuilder.rolled(RailcraftItems.STANDARD_RAIL.get(), 8)
          .define('I', Tags.Items.INGOTS_IRON)
          .pattern("I I")
          .pattern("I I")
          .pattern("I I")
          .save(consumer, Railcraft.ID + ":standard_rail_recipie");
      RollingRecipeBuilder.rolled(RailcraftItems.STANDARD_RAIL.get(), 16)
          .define('I', RailcraftTags.Items.STEEL_INGOT)
          .pattern("I I")
          .pattern("I I")
          .pattern("I I")
          .save(consumer, Railcraft.ID + ":steel_rail_recipie");
      RollingRecipeBuilder.rolled(RailcraftItems.ADVANCED_RAIL.get(), 8)
          .define('I', RailcraftItems.STANDARD_RAIL.get())
          .define('R', Tags.Items.DUSTS_REDSTONE)
          .define('G', Tags.Items.INGOTS_GOLD)
          .pattern("IRG")
          .pattern("IRG")
          .pattern("IRG")
          .save(consumer);
      RollingRecipeBuilder.rolled(RailcraftItems.HIGH_SPEED_RAIL.get(), 8)
          .define('S', RailcraftTags.Items.STEEL_INGOT)
          .define('O', Tags.Items.DUSTS_REDSTONE)
          .pattern("SOS")
          .pattern("SOS")
          .pattern("SOS")
          .save(consumer);
      RollingRecipeBuilder.rolled(RailcraftItems.ELECTRIC_RAIL.get(), 6)
          .define('S', RailcraftItems.STANDARD_RAIL.get())
          .define('C', RailcraftTags.Items.COPPER_INGOT)
          .pattern("SCS")
          .pattern("SCS")
          .pattern("SCS")
          .save(consumer);

    this.rebarFromMaterial(consumer, Tags.Items.INGOTS_IRON, 4,
        Railcraft.ID + ":rebar_iron");
    this.rebarFromMaterial(consumer, RailcraftTags.Items.BRONZE_INGOT, 4,
        Railcraft.ID + ":rebar_bronze");
    this.rebarFromMaterial(consumer, RailcraftTags.Items.STEEL_INGOT, 6,
        Railcraft.ID + ":rebar_steel");

    ShapedRecipeBuilder.shaped(RailcraftItems.STONE_TIE.get())
        .define('R', RailcraftItems.REBAR.get())
        .define('S', Items.STONE_SLAB)
        .pattern(" R ")
        .pattern("SSS")
        .pattern("   ")
        .unlockedBy("has_rebar", has(RailcraftItems.REBAR.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAILBED.get())
        .requires(RailcraftItems.WOODEN_TIE.get(), 4)
        .unlockedBy("has_tie", has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.STONE_RAILBED.get())
        .requires(RailcraftItems.STONE_TIE.get(), 4)
        .unlockedBy("has_tie", has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);
    }

    /* === Item Compression === */
    this.compress(consumer, RailcraftItems.STEEL_INGOT.get(),
        RailcraftTags.Items.STEEL_NUGGET, "ingot");
    this.compress(consumer, RailcraftItems.TIN_INGOT.get(),
        RailcraftTags.Items.TIN_NUGGET, "ingot");
    this.compress(consumer, RailcraftItems.ZINC_INGOT.get(),
        RailcraftTags.Items.ZINC_NUGGET, "ingot");
    this.compress(consumer, RailcraftItems.BRASS_INGOT.get(),
        RailcraftTags.Items.BRASS_NUGGET, "ingot");
    this.compress(consumer, RailcraftItems.BRONZE_INGOT.get(),
        RailcraftTags.Items.BRONZE_NUGGET, "ingot");

    this.compress(consumer, RailcraftItems.STEEL_BLOCK.get(),
        RailcraftTags.Items.STEEL_INGOT, "block");

    /* === Item Decompression === */
    this.decompress(consumer, RailcraftItems.STEEL_NUGGET.get(),
        RailcraftTags.Items.STEEL_INGOT, "nugget");
    this.decompress(consumer, RailcraftItems.TIN_NUGGET.get(),
        RailcraftTags.Items.TIN_INGOT, "nugget");
    this.decompress(consumer, RailcraftItems.ZINC_NUGGET.get(),
        RailcraftTags.Items.ZINC_INGOT, "nugget");
    this.decompress(consumer, RailcraftItems.BRASS_NUGGET.get(),
        RailcraftTags.Items.BRASS_INGOT, "nugget");
    this.decompress(consumer, RailcraftItems.BRONZE_NUGGET.get(),
        RailcraftTags.Items.BRONZE_INGOT, "nugget");

    this.decompress(consumer, RailcraftItems.STEEL_INGOT.get(),
        RailcraftTags.Items.STEEL_BLOCK, "block_ingot");

    CokeOvenRecipeBuilder
        .coking(Items.COAL, Ingredient.of(ItemTags.LOGS), 0.0F, 250)
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(consumer);

    CokeOvenRecipeBuilder
        .coking(RailcraftItems.COAL_COKE.get(), Ingredient.of(Items.COAL), 0.0F, 500)
        .unlockedBy("has_coal", has(Items.COAL))
        .save(consumer);

    new BlastFurnaceRecipeBuilder(RailcraftBlocks.STEEL_BLOCK.get(), 1,
        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), 0.0F,
        BlastFurnaceRecipeBuilder.DEFAULT_COOKING_TIME * 9,
        9)
            .unlockedBy("has_iron_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
            .save(consumer, getBlastingRecipeName("steel_block"));
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(Tags.Items.INGOTS_IRON), 1)
        .unlockedBy("has_iron_ingots", has(Tags.Items.INGOTS_IRON))
        .save(consumer, getBlastingRecipeName("iron_ingots"));

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
    var name = item.asItem().getRegistryName().getPath();
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier)
        .unlockedBy("has_" + name, has(item))
        .save(consumer, getBlastingRecipeName(name));
  }

  private static void blastFurnaceRecycling(Consumer<FinishedRecipe> consumer, ItemLike item,
      int multiplier) {
    var name = item.asItem().getRegistryName().getPath();
    BlastFurnaceRecipeBuilder
        .recycling(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier)
        .unlockedBy("has_" + name, has(item))
        .save(consumer, getBlastingRecipeName(name));
  }

  private static ResourceLocation getBlastingRecipeName(String tag) {
    return new ResourceLocation(Railcraft.ID, "blasting_" + tag);
  }

  private final void tankGaugeFromMaterial(Consumer<FinishedRecipe> finishedRecipie,
    Item itemOut, TagKey<Item> windowMaterial, TagKey<Item> plateMaterial) {
    ShapedRecipeBuilder.shaped(itemOut, 8)
      .define('P', plateMaterial)
      .define('W', windowMaterial)
      .pattern("BWB")
      .pattern("W W")
      .pattern("BWB")
      .unlockedBy("has_whatever_we_want", has(plateMaterial))
      .save(finishedRecipie);
  }

  private final void crowbarFromMaterial(Consumer<FinishedRecipe> finishedRecipie,
      Item itemOut, TagKey<Item> materialTag) {
    ShapedRecipeBuilder.shaped(itemOut)
        .define('I', materialTag)
        .define('D', Tags.Items.DYES_RED)
        .pattern(" DI")
        .pattern("DID")
        .pattern("ID ")
        .unlockedBy("has_rail", has(Blocks.RAIL))
        .save(finishedRecipie);
  }

  private final void circuitFromMaterial(Consumer<FinishedRecipe> finishedRecipie,
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
        .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .save(finishedRecipie);
  }

  private final void rebarFromMaterial(Consumer<FinishedRecipe> finishedRecipie,
      TagKey<Item> materialTag, int count, String fancyname) {
    RollingRecipeBuilder.rolled(RailcraftItems.REBAR.get(), count)
        .define('I', materialTag)
        .pattern("  I")
        .pattern(" I ")
        .pattern("I  ")
        .save(finishedRecipie, fancyname);
  }

  private final void railsFromMaterials(Consumer<FinishedRecipe> finishedRecipie,
      Item itemOut, Item railType, Item railBedType) {
    ShapedRecipeBuilder.shaped(itemOut, 32)
        .define('I', railType)
        .define('B', railBedType)
        .pattern("I I")
        .pattern("IBI")
        .pattern("I I")
        .unlockedBy("has_rail", has(railType))
        .unlockedBy("has_railbed", has(railBedType))
        // this is deliberate as vanilla ones fail to properly register (rails.json already exists)
        .save(finishedRecipie,
            new ResourceLocation(Railcraft.ID, ForgeRegistries.ITEMS.getKey(itemOut).getPath()));
  }

  private final void compress(Consumer<FinishedRecipe> finishedRecipie,
      Item itemOut, TagKey<Item> materialTag, String identifier) {
    ShapedRecipeBuilder.shaped(itemOut)
        .define('#', materialTag)
        .pattern("###")
        .pattern("###")
        .pattern("###")
        .unlockedBy("has_required_compression_material", has(materialTag))
        .save(finishedRecipie, new ResourceLocation(Railcraft.ID,
            RecipeBuilder.getDefaultRecipeId(itemOut).getPath() + "_" + identifier));
  }

  private final void decompress(Consumer<FinishedRecipe> finishedRecipie,
      Item itemOut, TagKey<Item> materialTag, String identifier) {
    ShapelessRecipeBuilder.shapeless(itemOut, 9)
        .requires(materialTag)
        .unlockedBy("has_required_decompression_material", has(materialTag))
        .save(finishedRecipie, new ResourceLocation(Railcraft.ID,
            RecipeBuilder.getDefaultRecipeId(itemOut).getPath() + "_" + identifier));
  }

  @Override
  public String getName() {
    return "Railcraft Recipes";
  }
}
