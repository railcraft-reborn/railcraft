package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.CokeOvenRecipeBuilder;
import mods.railcraft.util.JsonUtil;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class CokeOvenRecipe extends AbstractCookingRecipe {

  private final FluidStack creosote;

  public CokeOvenRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result,
      float experience, int cookingTime, int creosoteOutput) {
    super(RailcraftRecipeTypes.COKING.get(), id, "", CookingBookCategory.MISC,
        ingredient, result, experience, cookingTime);
    this.creosote = new FluidStack(RailcraftFluids.CREOSOTE.get(), creosoteOutput);
  }

  public FluidStack getCreosote() {
    return this.creosote;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.COKING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.COKE_OVEN_BRICKS.get());
  }

  public static class Serializer implements RecipeSerializer<CokeOvenRecipe> {

    @Override
    public CokeOvenRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      var cookingTime = GsonHelper.getAsInt(json, RecipeJsonKeys.COOKING_TIME,
          CokeOvenRecipeBuilder.DEFAULT_COOKING_TIME);
      var creosoteOutput = GsonHelper.getAsInt(json, RecipeJsonKeys.CREOSOTE_OUTPUT, 1000); // 1 bucket
      var ingredient = Ingredient.fromJson(json.get(RecipeJsonKeys.INGREDIENT));
      var result = JsonUtil.itemFromJson(GsonHelper.getAsJsonObject(json, RecipeJsonKeys.RESULT));
      var experience = GsonHelper.getAsFloat(json, RecipeJsonKeys.EXPERIENCE, 0);
      return new CokeOvenRecipe(recipeId, ingredient, result, experience, cookingTime,
          creosoteOutput);
    }

    @Override
    public CokeOvenRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      var creosoteOutput = buffer.readVarInt();
      var cookingTime = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var result = buffer.readItem();
      var experience = buffer.readFloat();
      return new CokeOvenRecipe(recipeId, ingredient, result, experience, cookingTime,
          creosoteOutput);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CokeOvenRecipe recipe) {
      buffer.writeVarInt(recipe.creosote.getAmount());
      buffer.writeVarInt(recipe.cookingTime);
      recipe.ingredient.toNetwork(buffer);
      buffer.writeItem(recipe.result);
      buffer.writeFloat(recipe.experience);
    }
  }
}
