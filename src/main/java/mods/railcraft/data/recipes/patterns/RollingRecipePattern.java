package mods.railcraft.data.recipes.patterns;

import mods.railcraft.Railcraft;
import mods.railcraft.data.recipes.builders.RollingRecipeBuilder;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class RollingRecipePattern {

  public static void line(RecipeOutput recipeOutput,
      Ingredient ingredient,
      ItemLike result,
      int count) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    line(recipeOutput, ingredient, result, count, name);
  }

  public static void line(RecipeOutput recipeOutput,
      Ingredient ingredient,
      ItemLike result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern(" a ")
        .pattern(" a ")
        .pattern(" a ")
        .define('a', ingredient)
        .save(recipeOutput, Railcraft.rl(customName));
  }

  public static void parallelLines(RecipeOutput recipeOutput,
      Ingredient ingred,
      ItemLike result,
      int count,
      String customName) {
    parallelLines(recipeOutput, ingred, ingred, result, count, customName);
  }

  public static void parallelLines(RecipeOutput recipeOutput,
      Ingredient ingred1,
      Ingredient ingred2,
      ItemLike result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("a b")
        .pattern("a b")
        .pattern("a b")
        .define('a', ingred1)
        .define('b', ingred2)
        .save(recipeOutput, Railcraft.rl(customName));
  }


  public static void parallelThreeLines(RecipeOutput recipeOutput,
      Ingredient ingred1,
      Ingredient ingred2,
      Ingredient ingred3,
      ItemLike result,
      int count) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    parallelThreeLines(recipeOutput, ingred1, ingred2, ingred3, result, count, name);
  }

  public static void parallelThreeLines(RecipeOutput recipeOutput,
      Ingredient ingred1,
      Ingredient ingred2,
      Ingredient ingred3,
      ItemLike result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("abc")
        .pattern("abc")
        .pattern("abc")
        .define('a', ingred1)
        .define('b', ingred2)
        .define('c', ingred3)
        .save(recipeOutput, Railcraft.rl(customName));
  }

  public static void diagonalLine(RecipeOutput recipeOutput,
      Ingredient materialTag,
      ItemLike result,
      int count,
      String customName) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("  a")
        .pattern(" a ")
        .pattern("a  ")
        .define('a', materialTag)
        .save(recipeOutput, Railcraft.rl(customName));
  }

  public static void square2x2(RecipeOutput recipeOutput,
      Ingredient materialTag,
      ItemLike result,
      int count) {
    square2x2(recipeOutput, materialTag, result, count, "");
  }

  public static void square2x2(RecipeOutput recipeOutput,
      Ingredient materialTag,
      ItemLike result,
      int count,
      String postfix) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    RollingRecipeBuilder.rolled(result, count)
        .pattern("aa")
        .pattern("aa")
        .define('a', materialTag)
        .save(recipeOutput, Railcraft.rl(name + postfix));
  }

  public static void hForm(RecipeOutput recipeOutput,
      Ingredient materialTag,
      ItemLike result,
      int count) {
    RollingRecipeBuilder.rolled(result, count)
        .pattern("a a")
        .pattern("aaa")
        .pattern("a a")
        .define('a', materialTag)
        .save(recipeOutput);
  }
}
