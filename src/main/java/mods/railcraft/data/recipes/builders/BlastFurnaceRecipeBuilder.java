package mods.railcraft.data.recipes.builders;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
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

public class BlastFurnaceRecipeBuilder extends AbstractCookingRecipeBuilder {

  public static final int DEFAULT_COOKING_TIME = SharedConstants.TICKS_PER_SECOND * 20;

  private final int slagOutput;

  private BlastFurnaceRecipeBuilder(ItemInstance result, Ingredient ingredient,
      float experience, int cookingTime, int slagOutput) {
    super(result, ingredient, experience, cookingTime);
    this.slagOutput = slagOutput;
  }

  public static BlastFurnaceRecipeBuilder smelting(ItemInstance result, Ingredient ingredient,
      int multiplier, int slagOutput) {
    return new BlastFurnaceRecipeBuilder(new ItemStackTemplate(result.typeHolder(), multiplier),
        ingredient, 0, DEFAULT_COOKING_TIME * multiplier, slagOutput);
  }

  public static BlastFurnaceRecipeBuilder recycling(ItemInstance result, Ingredient ingredient,
      int multiplier) {
    return new BlastFurnaceRecipeBuilder(new ItemStackTemplate(result.typeHolder(), multiplier),
        ingredient, 0, (DEFAULT_COOKING_TIME / 2) * multiplier, 0);
  }

  @Override
  public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
    var path = resourceKey.identifier().getPath();
    var customIdentifier = RailcraftConstants.id("blast_furnace/" + path);
    var customResourceKey = ResourceKey.create(resourceKey.registryKey(), customIdentifier);

    var advancementId = customIdentifier.withPrefix("recipes/");

    var builder = recipeOutput.advancement()
        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(customResourceKey))
        .rewards(AdvancementRewards.Builder.recipe(customResourceKey))
        .requirements(AdvancementRequirements.Strategy.OR);
    this.criteria.forEach(builder::addCriterion);

    var recipe = new BlastFurnaceRecipe(this.ingredient,
        new ItemStackTemplate(this.result.typeHolder(), this.result.count()),
        this.experience, this.cookingTime, this.slagOutput);
    recipeOutput.accept(customResourceKey, recipe, builder.build(advancementId));
  }
}
