package mods.railcraft.data.recipes.builders;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class CokeOvenRecipeBuilder extends AbstractCookingRecipeBuilder {

  public static final int DEFAULT_COOKING_TIME = SharedConstants.TICKS_PER_SECOND * 20;

  private final int creosoteOutput;

  private CokeOvenRecipeBuilder(ItemInstance result, Ingredient ingredient, float experience,
      int cookingTime, int creosoteOutput) {
    super(result, ingredient, experience, cookingTime);
    this.creosoteOutput = creosoteOutput;
  }

  public static CokeOvenRecipeBuilder coking(ItemInstance result, Ingredient ingredient,
      float experience, int creosoteOutput) {
    return coking(result, ingredient, experience, DEFAULT_COOKING_TIME, creosoteOutput);
  }

  public static CokeOvenRecipeBuilder coking(ItemInstance result, Ingredient ingredient,
      float experience, int cookingTime, int creosoteOutput) {
    return new CokeOvenRecipeBuilder(result, ingredient, experience, cookingTime, creosoteOutput);
  }

  @Override
  public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
    var path = resourceKey.identifier().getPath();
    var customIdentifier = RailcraftConstants.id("coke_oven/" + path);
    var customResourceKey = ResourceKey.create(resourceKey.registryKey(), customIdentifier);

    var advancementId = customIdentifier.withPrefix("recipes/");

    var builder = recipeOutput.advancement()
        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(customResourceKey))
        .rewards(AdvancementRewards.Builder.recipe(customResourceKey))
        .requirements(AdvancementRequirements.Strategy.OR);
    this.criteria.forEach(builder::addCriterion);

    var recipe = new CokeOvenRecipe(this.ingredient,
        new ItemStackTemplate(this.result.typeHolder(), this.result.count()),
        this.experience, this.cookingTime, this.creosoteOutput);
    recipeOutput.accept(customResourceKey, recipe, builder.build(advancementId));
  }
}
