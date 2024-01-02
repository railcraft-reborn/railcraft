package mods.railcraft.data.recipes.builders;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class BlastFurnaceRecipeBuilder extends AbstractCookingRecipeBuilder {

  public static final int DEFAULT_COOKING_TIME = 20 * 20; // 20 sec

  private final int slagOutput;

  private BlastFurnaceRecipeBuilder(ItemLike result, int count, Ingredient ingredient,
      float experience, int cookingTime, int slagOutput) {
    super(result, count, ingredient, experience, cookingTime);
    this.slagOutput = slagOutput;
  }

  public static BlastFurnaceRecipeBuilder smelting(ItemLike result, int count,
      Ingredient ingredient, int multiplier, int slagOutput) {
    return new BlastFurnaceRecipeBuilder(result, count, ingredient, 0.0F,
        DEFAULT_COOKING_TIME * multiplier, slagOutput);
  }

  public static BlastFurnaceRecipeBuilder smelting(ItemLike result, Ingredient ingredient,
      int multiplier, int slagOutput) {
    return new BlastFurnaceRecipeBuilder(result.asItem(), multiplier, ingredient,
        0.0F, DEFAULT_COOKING_TIME * multiplier, slagOutput);
  }

  public static BlastFurnaceRecipeBuilder recycling(ItemLike result, Ingredient ingredient,
      int multiplier) {
    return new BlastFurnaceRecipeBuilder(result.asItem(),
        multiplier, ingredient, 0.0F, (DEFAULT_COOKING_TIME / 2) * multiplier, 0);
  }

  @Override
  public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
    var path = resourceLocation.getPath();
    var customResourceLocation = RailcraftConstants.rl("blast_furnace/" + path);

    var advancementId = customResourceLocation.withPrefix("recipes/");

    var builder = recipeOutput.advancement()
        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(customResourceLocation))
        .rewards(AdvancementRewards.Builder.recipe(customResourceLocation))
        .requirements(AdvancementRequirements.Strategy.OR);
    this.criteria.forEach(builder::addCriterion);

    var recipe = new BlastFurnaceRecipe(this.ingredient, new ItemStack(this.result),
        this.experience, this.cookingTime, this.slagOutput);
    recipeOutput.accept(customResourceLocation, recipe, builder.build(advancementId));
  }
}
