package mods.railcraft.data.recipes.builders;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class CokeOvenRecipeBuilder extends AbstractCookingRecipeBuilder {

  public static final int DEFAULT_COOKING_TIME = 20 * 20; // 20 sec

  private final int creosoteOutput;

  private CokeOvenRecipeBuilder(ItemLike result, int count, Ingredient ingredient, float experience,
      int cookingTime, int creosoteOutput) {
    super(result, count, ingredient, experience, cookingTime);
    this.creosoteOutput = creosoteOutput;
  }

  public static CokeOvenRecipeBuilder coking(ItemLike result, Ingredient ingredient,
      float experience, int creosoteOutput) {
    return coking(result, ingredient, experience, DEFAULT_COOKING_TIME, creosoteOutput);
  }

  public static CokeOvenRecipeBuilder coking(ItemLike result, Ingredient ingredient,
      float experience, int cookingTime, int creosoteOutput) {
    return coking(result, 1, ingredient, experience, cookingTime, creosoteOutput);
  }

  public static CokeOvenRecipeBuilder coking(ItemLike result, int resultCount,
      Ingredient ingredient, float experience, int cookingTime, int creosoteOutput) {
    return new CokeOvenRecipeBuilder(result, resultCount, ingredient, experience, cookingTime,
        creosoteOutput);
  }

  @Override
  public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
    var path = resourceLocation.getPath();
    var customResourceLocation = RailcraftConstants.rl("coke_oven/" + path);

    var advancementId = customResourceLocation.withPrefix("recipes/");

    var builder = recipeOutput.advancement()
        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(customResourceLocation))
        .rewards(AdvancementRewards.Builder.recipe(customResourceLocation))
        .requirements(AdvancementRequirements.Strategy.OR);
    this.criteria.forEach(builder::addCriterion);

    var recipe = new CokeOvenRecipe(this.ingredient, new ItemStack(this.result, this.count),
        this.experience, this.cookingTime, this.creosoteOutput);
    recipeOutput.accept(customResourceLocation, recipe, builder.build(advancementId));
  }
}
