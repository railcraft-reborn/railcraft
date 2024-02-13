package mods.railcraft.data.recipes.builders;

import java.util.function.Consumer;
import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class CokeOvenRecipeBuilder extends AbstractCookingRecipeBuilder {

  public static final int DEFAULT_COOKING_TIME = SharedConstants.TICKS_PER_SECOND * 20;

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
  public void save(Consumer<FinishedRecipe> finishedRecipe, ResourceLocation resourceLocation) {
    var path = resourceLocation.getPath();
    var customResourceLocation = RailcraftConstants.rl("coke_oven/" + path);

    var advancementId = customResourceLocation.withPrefix("recipes/");

    finishedRecipe.accept(new Result(customResourceLocation,
        this.result, this.count, this.ingredient,
        this.experience, this.cookingTime, this.creosoteOutput, this.advancement, advancementId));
  }

  private static class Result extends AbstractCookingRecipeBuilder.AbstractResult {

    private final int creosoteOutput;

    public Result(ResourceLocation id, Item result, int count, Ingredient ingredient,
        float experience, int cookingTime, int creosoteOutput, Advancement.Builder advancement,
        ResourceLocation advancementId) {
      super(id, result, count, ingredient, experience, cookingTime, advancement, advancementId);
      this.creosoteOutput = creosoteOutput;
    }

    @Override
    protected void addJsonProperty(JsonObject json) {
      json.addProperty(RecipeJsonKeys.CREOSOTE_OUTPUT, creosoteOutput);
    }

    @Override
    public RecipeSerializer<?> getType() {
      return RailcraftRecipeSerializers.COKING.get();
    }
  }
}
