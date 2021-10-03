package mods.railcraft.data;

import java.util.function.Consumer;

import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
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
    /* === Crowbar === */
    this.crowbarFromMaterial(finishedRecipie, RailcraftItems.IRON_CROWBAR.get(), Tags.Items.INGOTS_IRON);
    this.crowbarFromMaterial(finishedRecipie, RailcraftItems.STEEL_CROWBAR.get(), RailcraftTags.Items.STEEL_INGOT);
    this.crowbarFromMaterial(finishedRecipie, RailcraftItems.DIAMOND_CROWBAR.get(), Tags.Items.GEMS_DIAMOND);

    /* === Component recipies === */
    this.circuitFromMaterial(finishedRecipie, RailcraftItems.CONTROLLER_CIRCUIT.get(), Items.RED_WOOL);
    this.circuitFromMaterial(finishedRecipie, RailcraftItems.RECEIVER_CIRCUIT.get(), Items.GREEN_WOOL);
    this.circuitFromMaterial(finishedRecipie, RailcraftItems.SIGNAL_CIRCUIT.get(), Items.YELLOW_WOOL);

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
    ShapedRecipeBuilder.shaped(itemOut, 1)
      .define('I', materialTag)
      .define('D', Tags.Items.DYES_RED)
      .pattern(" DI")
      .pattern("DID")
      .pattern("ID ")
      .unlockedBy("has_rail", has(Blocks.RAIL))
      .save(finishedRecipie);
  }

  private final void compress(Consumer<IFinishedRecipe> finishedRecipie, Item itemOut, ITag<Item> materialTag) {
    ShapedRecipeBuilder.shaped(itemOut, 1)
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

  private final void circuitFromMaterial(Consumer<IFinishedRecipe> finishedRecipie, Item itemOut, Item woolItem) {
    ShapedRecipeBuilder.shaped(itemOut, 1)
    .define('W', woolItem)
    .define('R', Items.REPEATER)
    .define('S', Tags.Items.DUSTS_REDSTONE)
    .define('G', Tags.Items.INGOTS_GOLD)
    .define('L', Tags.Items.GEMS_LAPIS)
    .define('B', Tags.Items.SLIMEBALLS)
    .pattern(" RW")
    .pattern("BGS")
    .pattern("WSL")
    .unlockedBy("has_redstone", has(Items.REDSTONE))
    .save(finishedRecipie);
  }

  @Override
  public String getName() {
    return "Railcraft Recipes";
  }
}
