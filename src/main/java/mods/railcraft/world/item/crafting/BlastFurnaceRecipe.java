package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mods.railcraft.data.recipes.builders.BlastFurnaceRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

public class BlastFurnaceRecipe extends AbstractCookingRecipe {

  private final int slagOutput;

  public BlastFurnaceRecipe(ResourceLocation id, String group,
      Ingredient ingredient, ItemStack result, float experience, int cookingTime, int slagOutput) {
    super(RailcraftRecipeTypes.BLASTING.get(), id, group, CookingBookCategory.MISC,
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
      var group = GsonHelper.getAsString(json, "group", "");
      var cookingTime = GsonHelper.getAsInt(json, "cookingTime",
          BlastFurnaceRecipeBuilder.DEFAULT_COOKING_TIME);
      var slagOutput = GsonHelper.getAsInt(json, "slagOutput", 1);
      var ingredient = Ingredient.fromJson(json.get("ingredient"));
      var result = itemFromJson(GsonHelper.getAsJsonObject(json, "result"));
      var experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
      return new BlastFurnaceRecipe(recipeId, group, ingredient, result, experience, cookingTime,
          slagOutput);
    }

    @Override
    public BlastFurnaceRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      var group = buffer.readUtf();
      var slagOutput = buffer.readVarInt();
      var cookingTime = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var result = buffer.readItem();
      var experience = buffer.readFloat();
      return new BlastFurnaceRecipe(recipeId, group, ingredient, result, experience, cookingTime,
          slagOutput);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BlastFurnaceRecipe recipe) {
      buffer.writeUtf(recipe.group);
      buffer.writeVarInt(recipe.slagOutput);
      buffer.writeVarInt(recipe.cookingTime);
      recipe.ingredient.toNetwork(buffer);
      buffer.writeItem(recipe.result);
      buffer.writeFloat(recipe.experience);
    }

    public static ItemStack itemFromJson(JsonObject json) {
      if (!json.has("item")) {
        throw new JsonParseException("No item key found");
      }
      return CraftingHelper.getItemStack(json, true);
    }
  }
}
