package mods.railcraft.data.recipes;

import java.util.HashMap;
import java.util.function.Consumer;

import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class RecipeBlocks extends CustomRecipeProvider {

  private RecipeBlocks(DataGenerator datagen) {
    super(datagen);
  }

  public static void registerBlocks(Consumer<FinishedRecipe> consumer) {
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

    post(consumer, RailcraftItems.POST, RailcraftTags.Items.POST);

    //TODO: TANK_GAUGE I don't know the recipe
    strengthenedGlass(consumer);
  }

  public static void decompressItem(Consumer<FinishedRecipe> consumer) {
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
  }

  public static void compressItem(Consumer<FinishedRecipe> consumer) {
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
  }

  private static void strengthenedGlass(Consumer<FinishedRecipe> consumer) {
    var ingredients = new HashMap<String, TagKey<Item>>();
    ingredients.put("tin", RailcraftTags.Items.TIN_INGOT);
    ingredients.put("nickel", RailcraftTags.Items.NICKEL_INGOT);
    ingredients.put("invar", RailcraftTags.Items.INVAR_INGOT);
    ingredients.put("brass", RailcraftTags.Items.BRASS_INGOT);
    ingredients.put("iron", Tags.Items.INGOTS_IRON);

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
        .unlockedBy(getHasName(RailcraftItems.SALTPETER_DUST.get()), has(RailcraftItems.SALTPETER_DUST.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, recipeName));
    }

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

}
