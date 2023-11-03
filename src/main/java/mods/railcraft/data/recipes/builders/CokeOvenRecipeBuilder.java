package mods.railcraft.data.recipes.builders;

import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
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
    var customResourceLocation = Railcraft.rl("coke_oven/" + path);

    var advancementId = customResourceLocation.withPrefix("recipes/");;

    var builder = recipeOutput.advancement()
        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(customResourceLocation))
        .rewards(AdvancementRewards.Builder.recipe(customResourceLocation))
        .requirements(AdvancementRequirements.Strategy.OR);
    this.criteria.forEach(builder::addCriterion);

    recipeOutput.accept(new Result(customResourceLocation, this.result, this.count, this.ingredient,
        this.experience, this.cookingTime, this.creosoteOutput, builder.build(advancementId)));
  }

  private static class Result extends AbstractCookingRecipeBuilder.AbstractResult {

    private final int creosoteOutput;

    public Result(ResourceLocation id, Item result, int count, Ingredient ingredient,
        float experience, int cookingTime, int creosoteOutput, AdvancementHolder advancement) {
      super(id, result, count, ingredient, experience, cookingTime, advancement);
      this.creosoteOutput = creosoteOutput;
    }

    @Override
    protected void addJsonProperty(JsonObject json) {
      json.addProperty("creosoteOutput", creosoteOutput);
    }

    @Override
    public RecipeSerializer<?> type() {
      return RailcraftRecipeSerializers.COKING.get();
    }
  }
}
