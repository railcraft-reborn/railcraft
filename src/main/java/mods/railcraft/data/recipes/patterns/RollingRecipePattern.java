package mods.railcraft.data.recipes.patterns;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.recipes.builders.RollingRecipeBuilder;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;

public class RollingRecipePattern {

  public static void line(RecipeOutput recipeOutput,
      Ingredient ingredient,
      ItemStackTemplate result) {
    var name = RecipeBuilder.getDefaultRecipeId(result).identifier().getPath();
    line(recipeOutput, ingredient, result, name);
  }

  public static void line(RecipeOutput recipeOutput,
      Ingredient ingredient,
      ItemStackTemplate result,
      String customName) {
    RollingRecipeBuilder.rolled(result)
        .pattern(" a ")
        .pattern(" a ")
        .pattern(" a ")
        .define('a', ingredient)
        .save(recipeOutput, RailcraftConstants.id(customName));
  }

  public static void parallelLines(RecipeOutput recipeOutput,
      Ingredient ingred,
      ItemStackTemplate result,
      String customName) {
    parallelLines(recipeOutput, ingred, ingred, result, customName);
  }

  public static void parallelLines(RecipeOutput recipeOutput,
      Ingredient ingred1,
      Ingredient ingred2,
      ItemStackTemplate result,
      String customName) {
    RollingRecipeBuilder.rolled(result)
        .pattern("a b")
        .pattern("a b")
        .pattern("a b")
        .define('a', ingred1)
        .define('b', ingred2)
        .save(recipeOutput, RailcraftConstants.id(customName));
  }


  public static void parallelThreeLines(RecipeOutput recipeOutput,
      Ingredient ingred1,
      Ingredient ingred2,
      Ingredient ingred3,
      ItemStackTemplate result) {
    var name = RecipeBuilder.getDefaultRecipeId(result).identifier().getPath();
    parallelThreeLines(recipeOutput, ingred1, ingred2, ingred3, result, name);
  }

  public static void parallelThreeLines(RecipeOutput recipeOutput,
      Ingredient ingred1,
      Ingredient ingred2,
      Ingredient ingred3,
      ItemStackTemplate result,
      String customName) {
    RollingRecipeBuilder.rolled(result)
        .pattern("abc")
        .pattern("abc")
        .pattern("abc")
        .define('a', ingred1)
        .define('b', ingred2)
        .define('c', ingred3)
        .save(recipeOutput, RailcraftConstants.id(customName));
  }

  public static void diagonalLine(RecipeOutput recipeOutput,
      Ingredient materialTag,
      ItemStackTemplate result,
      String customName) {
    RollingRecipeBuilder.rolled(result)
        .pattern("  a")
        .pattern(" a ")
        .pattern("a  ")
        .define('a', materialTag)
        .save(recipeOutput, RailcraftConstants.id(customName));
  }

  public static void square2x2(RecipeOutput recipeOutput, Ingredient materialTag, ItemStackTemplate result) {
    square2x2(recipeOutput, materialTag, result, "");
  }

  public static void square2x2(RecipeOutput recipeOutput,
      Ingredient materialTag,
      ItemStackTemplate result,
      String postfix) {
    var name = RecipeBuilder.getDefaultRecipeId(result).identifier().getPath();
    RollingRecipeBuilder.rolled(result)
        .pattern("aa")
        .pattern("aa")
        .define('a', materialTag)
        .save(recipeOutput, RailcraftConstants.id(name + postfix));
  }

  public static void hForm(RecipeOutput recipeOutput,
      Ingredient materialTag,
      ItemStackTemplate result) {
    RollingRecipeBuilder.rolled(result)
        .pattern("a a")
        .pattern("aaa")
        .pattern("a a")
        .define('a', materialTag)
        .save(recipeOutput);
  }
}
