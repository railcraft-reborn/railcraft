package mods.railcraft.data;

import java.util.function.Consumer;

import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.RollingRecipeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;

public class RailcraftRecipiesProvider extends RecipeProvider {

  public RailcraftRecipiesProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void buildShapelessRecipes(Consumer<IFinishedRecipe> finishedRecipie) {
    /*
     * =====================================
     *        RAILCRAFT TOOLS
     * =====================================
     */
    /* === Crowbar === */
    this.crowbarFromMaterial(finishedRecipie, RailcraftItems.IRON_CROWBAR.get(), Tags.Items.INGOTS_IRON);
    this.crowbarFromMaterial(finishedRecipie, RailcraftItems.STEEL_CROWBAR.get(), RailcraftTags.Items.STEEL_INGOT);
    this.crowbarFromMaterial(finishedRecipie, RailcraftItems.DIAMOND_CROWBAR.get(), Tags.Items.GEMS_DIAMOND);

    ShapedRecipeBuilder.shaped(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get())
      .define('C', Items.COMPASS)
      .define('G', Tags.Items.GLASS_PANES)
      .define('B', Blocks.STONE_BUTTON)
      .define('R', Tags.Items.DUSTS_REDSTONE)
      .pattern(" C ")
      .pattern("BGB")
      .pattern(" R ")
      .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
      .save(finishedRecipie);

    ShapedRecipeBuilder.shaped(RailcraftItems.SIGNAL_TUNER.get())
      .define('T', Items.REDSTONE_TORCH)
      .define('C', RailcraftItems.RECEIVER_CIRCUIT.get())
      .define('B', Blocks.STONE_BUTTON)
      .pattern(" T ")
      .pattern("BCB")
      .pattern("   ")
      .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
      .unlockedBy("has_circuit", has(RailcraftItems.RECEIVER_CIRCUIT.get()))
      .save(finishedRecipie);

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
      .save(finishedRecipie);

    // this is a tool i think
    ShapedRecipeBuilder.shaped(RailcraftItems.REFINED_FIRESTONE.get())
      .define('L', Items.LAVA_BUCKET)
      .define('R', Items.REDSTONE_BLOCK)
      .define('S', RailcraftItems.CUT_FIRESTONE.get())
      .pattern("LRL")
      .pattern("RSR")
      .pattern("LRL")
      .unlockedBy("has_firestone", has(RailcraftItems.CUT_FIRESTONE.get()))
      .save(finishedRecipie, "firestone_lava_refinement");

    ShapedRecipeBuilder.shaped(RailcraftItems.REFINED_FIRESTONE.get())
      .define('L', Items.LAVA_BUCKET)
      .define('R', Items.REDSTONE_BLOCK)
      .define('S', RailcraftItems.CRACKED_FIRESTONE.get())
      .define('F', RailcraftItems.RAW_FIRESTONE.get())
      .pattern("LFL")
      .pattern("RSR")
      .pattern("LRL")
      .unlockedBy("has_firestone", has(RailcraftItems.CRACKED_FIRESTONE.get()))
      .save(finishedRecipie, "firestone_cracked_fixing");

    /*
     * =====================================
     *     RAILCRAFT CRAFTING COMPONENTS
     * =====================================
     */
    /* === Component recipies === */
    this.circuitFromMaterial(finishedRecipie, RailcraftItems.CONTROLLER_CIRCUIT.get(), Items.RED_WOOL);
    this.circuitFromMaterial(finishedRecipie, RailcraftItems.RECEIVER_CIRCUIT.get(), Items.GREEN_WOOL);
    this.circuitFromMaterial(finishedRecipie, RailcraftItems.SIGNAL_CIRCUIT.get(), Items.YELLOW_WOOL);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAIL.get(), 6)
      .requires(RailcraftItems.WOODEN_TIE.get())
      .requires(Tags.Items.INGOTS_IRON)
      .unlockedBy("has_wooden_tie", has(Items.RAIL))
      .save(finishedRecipie);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.STANDARD_RAIL.get())
      .requires(Items.RAIL, 8)
      .unlockedBy("has_rail", has(Items.RAIL))
      .save(finishedRecipie, "rail_deconstruction");

    RollingRecipeBuilder.rolled(RailcraftItems.STANDARD_RAIL.get(), 8)
      .define('I', Tags.Items.INGOTS_IRON)
      .pattern("I I")
      .pattern("I I")
      .pattern("I I")
      .save(finishedRecipie, "standard_rail_recipie");
    RollingRecipeBuilder.rolled(RailcraftItems.STANDARD_RAIL.get(), 16)
      .define('I', RailcraftTags.Items.STEEL_INGOT)
      .pattern("I I")
      .pattern("I I")
      .pattern("I I")
      .save(finishedRecipie, "steel_rail_recipie");
    RollingRecipeBuilder.rolled(RailcraftItems.ADVANCED_RAIL.get(), 8)
      .define('I', RailcraftItems.STANDARD_RAIL.get())
      .define('R', Tags.Items.DUSTS_REDSTONE)
      .define('G', Tags.Items.INGOTS_GOLD)
      .pattern("IRG")
      .pattern("IRG")
      .pattern("IRG")
      .save(finishedRecipie);
    RollingRecipeBuilder.rolled(RailcraftItems.HIGH_SPEED_RAIL.get(), 8)
      .define('S', RailcraftTags.Items.STEEL_INGOT)
      .define('O', Tags.Items.DUSTS_REDSTONE)
      .pattern("SOS")
      .pattern("SOS")
      .pattern("SOS")
      .save(finishedRecipie);
    RollingRecipeBuilder.rolled(RailcraftItems.ELECTRIC_RAIL.get(), 6)
      .define('S', RailcraftItems.STANDARD_RAIL.get())
      .define('C', RailcraftTags.Items.COPPER_INGOT)
      .pattern("SCS")
      .pattern("SCS")
      .pattern("SCS")
      .save(finishedRecipie);

    this.rebarFromMaterial(finishedRecipie, Tags.Items.INGOTS_IRON, 4, "rebar_iron");
    this.rebarFromMaterial(finishedRecipie, RailcraftTags.Items.BRONZE_INGOT, 4, "rebar_bronze");
    this.rebarFromMaterial(finishedRecipie, RailcraftTags.Items.STEEL_INGOT, 6, "rebar_steel");

    ShapedRecipeBuilder.shaped(RailcraftItems.STONE_TIE.get())
      .define('R', RailcraftItems.REBAR.get())
      .define('S', Items.STONE_SLAB)
      .pattern(" R ")
      .pattern("SSS")
      .pattern("   ")
      .unlockedBy("has_rebar", has(RailcraftItems.REBAR.get()))
      .save(finishedRecipie);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAILBED.get())
      .requires(RailcraftItems.WOODEN_TIE.get(), 4)
      .unlockedBy("has_tie", has(RailcraftItems.WOODEN_TIE.get()))
      .save(finishedRecipie);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.STONE_RAILBED.get())
      .requires(RailcraftItems.STONE_TIE.get(), 4)
      .unlockedBy("has_tie", has(RailcraftItems.WOODEN_TIE.get()))
      .save(finishedRecipie);

    this.railsFromMaterials(finishedRecipie, RailcraftItems.STRAP_IRON_FLEX_TRACK.get(),
      RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    this.railsFromMaterials(finishedRecipie,Items.RAIL,
      RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    this.railsFromMaterials(finishedRecipie, RailcraftItems.REINFORCED_FLEX_TRACK.get(),
      RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    this.railsFromMaterials(finishedRecipie, RailcraftItems.ELECTRIC_FLEX_TRACK.get(),
      RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    this.railsFromMaterials(finishedRecipie, RailcraftItems.HIGH_SPEED_FLEX_TRACK.get(),
      RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());


    /* === Item Compression === */
    this.compress(finishedRecipie, RailcraftItems.STEEL_INGOT.get(), RailcraftTags.Items.STEEL_NUGGET);
    this.compress(finishedRecipie, RailcraftItems.TIN_INGOT.get(), RailcraftTags.Items.TIN_NUGGET);
    this.compress(finishedRecipie, RailcraftItems.COPPER_INGOT.get(), RailcraftTags.Items.COPPER_NUGGET);
    this.compress(finishedRecipie, RailcraftItems.ZINC_INGOT.get(), RailcraftTags.Items.ZINC_NUGGET);
    this.compress(finishedRecipie, RailcraftItems.BRASS_INGOT.get(), RailcraftTags.Items.BRASS_NUGGET);
    this.compress(finishedRecipie, RailcraftItems.BRONZE_INGOT.get(), RailcraftTags.Items.BRONZE_NUGGET);

    /* === Item Decompression === */
    this.decompress(finishedRecipie, RailcraftItems.STEEL_NUGGET.get(), RailcraftTags.Items.STEEL_INGOT);
    this.decompress(finishedRecipie, RailcraftItems.TIN_NUGGET.get(), RailcraftTags.Items.TIN_INGOT);
    this.decompress(finishedRecipie, RailcraftItems.COPPER_NUGGET.get(), RailcraftTags.Items.COPPER_INGOT);
    this.decompress(finishedRecipie, RailcraftItems.ZINC_NUGGET.get(), RailcraftTags.Items.ZINC_INGOT);
    this.decompress(finishedRecipie, RailcraftItems.BRASS_NUGGET.get(), RailcraftTags.Items.BRASS_INGOT);
    this.decompress(finishedRecipie, RailcraftItems.BRONZE_NUGGET.get(), RailcraftTags.Items.BRONZE_INGOT);
  }

  private final void crowbarFromMaterial(Consumer<IFinishedRecipe> finishedRecipie, Item itemOut, ITag<Item> materialTag) {
    ShapedRecipeBuilder.shaped(itemOut)
      .define('I', materialTag)
      .define('D', Tags.Items.DYES_RED)
      .pattern(" DI")
      .pattern("DID")
      .pattern("ID ")
      .unlockedBy("has_rail", has(Blocks.RAIL))
      .save(finishedRecipie);
  }

  private final void circuitFromMaterial(Consumer<IFinishedRecipe> finishedRecipie, Item itemOut, Item woolItem) {
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

  private final void rebarFromMaterial(Consumer<IFinishedRecipe> finishedRecipie, ITag<Item> materialTag, int count, String fancyname) {
    RollingRecipeBuilder.rolled(RailcraftItems.REBAR.get(), count)
      .define('I', materialTag)
      .pattern("  I")
      .pattern(" I ")
      .pattern("I  ")
      .save(finishedRecipie, fancyname);
  }

  private final void railsFromMaterials(Consumer<IFinishedRecipe> finishedRecipie, Item itemOut, Item railType, Item railBedType) {
    ShapedRecipeBuilder.shaped(itemOut, 32)
      .define('I', railType)
      .define('B', railBedType)
      .pattern("I I")
      .pattern("IBI")
      .pattern("I I")
      .unlockedBy("has_rail", has(railType))
      .unlockedBy("has_railbed", has(railBedType))
      .save(finishedRecipie);
  }

  private final void compress(Consumer<IFinishedRecipe> finishedRecipie, Item itemOut, ITag<Item> materialTag) {
    ShapedRecipeBuilder.shaped(itemOut)
      .define('#', materialTag)
      .pattern("###")
      .pattern("###")
      .pattern("###")
      .unlockedBy("has_required_compression_material", has(materialTag))
      .save(finishedRecipie);
  }

  private final void decompress(Consumer<IFinishedRecipe> finishedRecipie, Item itemOut, ITag<Item> materialTag) {
    ShapelessRecipeBuilder.shapeless(itemOut, 9)
      .requires(materialTag)
      .unlockedBy("has_required_decompression_material", has(materialTag))
      .save(finishedRecipie);
  }
  @Override
  public String getName() {
    return "Railcraft Recipes";
  }
}
