package mods.railcraft.data.recipes.patterns;

import java.util.function.Consumer;
import mods.railcraft.Railcraft;
import mods.railcraft.data.recipes.RollingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class RollingRecipePattern {

  public static void line(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> ingredient,
      ItemLike result,
      int count) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    RollingRecipeBuilder.rolled(result, count)
        .pattern(" a ")
        .pattern(" a ")
        .pattern(" a ")
        .define('a', ingredient)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + name));
  }

  public static void line(Consumer<FinishedRecipe> finishedRecipe,
      ItemLike ingredient,
      ItemLike result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern(" a ")
        .pattern(" a ")
        .pattern(" a ")
        .define('a', ingredient)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + customName));
  }

  public static void parallelLines(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> ingred1,
      ItemLike result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("a a")
        .pattern("a a")
        .pattern("a a")
        .define('a', ingred1)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + customName));
  }

  public static void parallelLines(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> ingred1,
      TagKey<Item> ingred2,
      ItemLike result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("a b")
        .pattern("a b")
        .pattern("a b")
        .define('a', ingred1)
        .define('b', ingred2)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + customName));
  }

  public static void parallelThreeLines(Consumer<FinishedRecipe> finishedRecipe,
      ItemLike ingred1,
      TagKey<Item> ingred2,
      ItemLike ingred3,
      ItemLike result,
      int count) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    RollingRecipeBuilder.rolled(result, count)
        .pattern("abc")
        .pattern("abc")
        .pattern("abc")
        .define('a', ingred1)
        .define('b', ingred2)
        .define('c', ingred3)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + name));
  }

  public static void parallelThreeLines(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> ingred1,
      TagKey<Item> ingred2,
      TagKey<Item> ingred3,
      ItemLike result,
      int count) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    RollingRecipeBuilder.rolled(result, count)
        .pattern("abc")
        .pattern("abc")
        .pattern("abc")
        .define('a', ingred1)
        .define('b', ingred2)
        .define('c', ingred3)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + name));
  }

  public static void parallelThreeLines(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> ingred1,
      TagKey<Item> ingred2,
      TagKey<Item> ingred3,
      Item result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("abc")
        .pattern("abc")
        .pattern("abc")
        .define('a', ingred1)
        .define('b', ingred2)
        .define('c', ingred3)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + customName));
  }

  public static void parallelThreeLines(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> ingred1,
      ItemLike ingred2,
      TagKey<Item> ingred3,
      Item result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("abc")
        .pattern("abc")
        .pattern("abc")
        .define('a', ingred1)
        .define('b', ingred2)
        .define('c', ingred3)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + customName));
  }

  public static void diagonalLine(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> materialTag,
      ItemLike result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("  a")
        .pattern(" a ")
        .pattern("a  ")
        .define('a', materialTag)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + customName));
  }

  public static void square2x2(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> materialTag,
      ItemLike result,
      int count) {
    square2x2(finishedRecipe, materialTag, result, count, "");
  }

  public static void square2x2(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> materialTag,
      ItemLike result,
      int count,
      String postfix) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    RollingRecipeBuilder.rolled(result, count)
        .pattern("aa")
        .pattern("aa")
        .define('a', materialTag)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + name + postfix));
  }

  public static void hForm(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> materialTag,
      ItemLike result,
      int count) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    RollingRecipeBuilder.rolled(result, count)
        .pattern("a a")
        .pattern("aaa")
        .pattern("a a")
        .define('a', materialTag)
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, "rolling/" + name));
  }
}
