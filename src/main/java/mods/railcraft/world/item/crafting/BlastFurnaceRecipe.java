package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.BlastFurnaceRecipeBuilder;
import mods.railcraft.util.JsonUtil;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BlastFurnaceRecipe extends AbstractCookingRecipe {

  private final int slagOutput;

  public BlastFurnaceRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result,
      float experience, int cookingTime, int slagOutput) {
    super(RailcraftRecipeTypes.BLASTING.get(), id, "", CookingBookCategory.MISC,
        ingredient, result, experience, cookingTime);
    this.slagOutput = slagOutput;
  }

  public int getSlagOutput() {
    return this.slagOutput;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.BLASTING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }
  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
  }

  public static class Serializer implements RecipeSerializer<BlastFurnaceRecipe> {

    @Override
    public BlastFurnaceRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      var cookingTime = GsonHelper.getAsInt(json, RecipeJsonKeys.COOKING_TIME,
          BlastFurnaceRecipeBuilder.DEFAULT_COOKING_TIME);
      var slagOutput = GsonHelper.getAsInt(json, RecipeJsonKeys.SLAG_OUTPUT, 1);
      var ingredient = Ingredient.fromJson(json.get(RecipeJsonKeys.INGREDIENT));
      var result = JsonUtil.itemFromJson(GsonHelper.getAsJsonObject(json, RecipeJsonKeys.RESULT));
      var experience = GsonHelper.getAsFloat(json, RecipeJsonKeys.EXPERIENCE, 0);
      return new BlastFurnaceRecipe(recipeId, ingredient, result, experience, cookingTime,
          slagOutput);
    }

    @Override
    public BlastFurnaceRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      var slagOutput = buffer.readVarInt();
      var cookingTime = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var result = buffer.readItem();
      var experience = buffer.readFloat();
      return new BlastFurnaceRecipe(recipeId, ingredient, result, experience, cookingTime,
          slagOutput);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BlastFurnaceRecipe recipe) {
      buffer.writeVarInt(recipe.slagOutput);
      buffer.writeVarInt(recipe.cookingTime);
      recipe.ingredient.toNetwork(buffer);
      buffer.writeItem(recipe.result);
      buffer.writeFloat(recipe.experience);
    }
  }
}
