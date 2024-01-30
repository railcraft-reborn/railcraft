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

public class BlastFurnaceRecipeBuilder extends AbstractCookingRecipeBuilder {

  public static final int DEFAULT_COOKING_TIME = SharedConstants.TICKS_PER_SECOND * 20;

  private final int slagOutput;

  private BlastFurnaceRecipeBuilder(ItemLike result, int count, Ingredient ingredient,
      float experience, int cookingTime, int slagOutput) {
    super(result, count, ingredient, experience, cookingTime);
    this.slagOutput = slagOutput;
  }

  public static BlastFurnaceRecipeBuilder smelting(ItemLike result, int count,
      Ingredient ingredient, int multiplier, int slagOutput) {
    return new BlastFurnaceRecipeBuilder(result, count, ingredient, 0,
        DEFAULT_COOKING_TIME * multiplier, slagOutput);
  }

  public static BlastFurnaceRecipeBuilder smelting(ItemLike result, Ingredient ingredient,
      int multiplier, int slagOutput) {
    return new BlastFurnaceRecipeBuilder(result.asItem(), multiplier, ingredient,
        0, DEFAULT_COOKING_TIME * multiplier, slagOutput);
  }

  public static BlastFurnaceRecipeBuilder recycling(ItemLike result, Ingredient ingredient,
      int multiplier) {
    return new BlastFurnaceRecipeBuilder(result.asItem(),
        multiplier, ingredient, 0, (DEFAULT_COOKING_TIME / 2) * multiplier, 0);
  }

  @Override
  public void save(Consumer<FinishedRecipe> finishedRecipe, ResourceLocation resourceLocation) {
    var path = resourceLocation.getPath();
    var customResourceLocation = RailcraftConstants.rl("blast_furnace/" + path);

    var advancementId = customResourceLocation.withPrefix("recipes/");

    finishedRecipe.accept(new Result(customResourceLocation,
        this.result, this.count, this.ingredient,
        this.experience, this.cookingTime, this.slagOutput, this.advancement, advancementId));
  }

  private static class Result extends AbstractCookingRecipeBuilder.AbstractResult {

    private final int slagOutput;

    public Result(ResourceLocation id, Item result, int count, Ingredient ingredient,
        float experience, int cookingTime, int slagOutput, Advancement.Builder advancement,
        ResourceLocation advancementId) {
      super(id, result, count, ingredient, experience, cookingTime, advancement,
          advancementId);
      this.slagOutput = slagOutput;
    }

    @Override
    protected void addJsonProperty(JsonObject json) {
      json.addProperty(RecipeJsonKeys.SLAG_OUTPUT, this.slagOutput);
    }

    @Override
    public RecipeSerializer<?> getType() {
      return RailcraftRecipeSerializers.BLASTING.get();
    }
  }
}
