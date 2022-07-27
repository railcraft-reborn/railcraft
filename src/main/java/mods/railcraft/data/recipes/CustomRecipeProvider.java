package mods.railcraft.data.recipes;

import java.util.function.Consumer;
import mods.railcraft.Railcraft;
import mods.railcraft.util.VariantRegistrar;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public abstract class CustomRecipeProvider extends RecipeProvider {

  public CustomRecipeProvider(DataGenerator gen) {
    super(gen);
  }

  protected static void tankWall(Consumer<FinishedRecipe> consumer,
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

  protected static void tankValve(Consumer<FinishedRecipe> consumer,
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

  protected static void compress(Consumer<FinishedRecipe> finishedRecipe,
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

  protected static void decompress(Consumer<FinishedRecipe> finishedRecipe,
                                   Item itemOut,
                                   TagKey<Item> materialTag,
                                   String identifier) {
    ShapelessRecipeBuilder.shapeless(itemOut, 9)
      .requires(materialTag)
      .unlockedBy("has_material", has(materialTag))
      .save(finishedRecipe, new ResourceLocation(Railcraft.ID,
        RecipeBuilder.getDefaultRecipeId(itemOut).getPath() + "_" + identifier));
  }

  protected static void coloredBlockVariant(Consumer<FinishedRecipe> consumer,
                                          VariantRegistrar<DyeColor, BlockItem> colorItems,
                                          TagKey<Item> tagItem) {
    var base = colorItems.variantFor(DyeColor.WHITE).get();
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
