package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mods.railcraft.data.recipes.CokeOvenRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;

public class CokeOvenRecipe extends AbstractCookingRecipe {

  private final FluidStack creosote;

  public CokeOvenRecipe(ResourceLocation id, String group,
      Ingredient ingredient, ItemStack result, float experience, int cookingTime,
      int creosoteOutput) {
    super(RailcraftRecipeTypes.COKING.get(), id, group, ingredient, result, experience,
        cookingTime);
    this.creosote = new FluidStack(RailcraftFluids.CREOSOTE.get(), creosoteOutput);
  }

  public FluidStack getCreosote() {
    return this.creosote;
  }

  public FluidStack assembleFluid() {
    return this.getCreosote().copy();
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.COKE_OVEN_BRICKS.get());
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.COKING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  public static class Serializer implements RecipeSerializer<CokeOvenRecipe> {

    @Override
    public CokeOvenRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      var group = GsonHelper.getAsString(json, "group", "");
      var cookingTime = GsonHelper.getAsInt(json, "cookingTime",
          CokeOvenRecipeBuilder.DEFAULT_COOKING_TIME);
      var creosoteOutput = GsonHelper.getAsInt(json, "creosoteOutput", 1000); // 1 bucket
      var ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
      var result = itemFromJson(GsonHelper.getAsJsonObject(json, "result"));
      var experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
      return new CokeOvenRecipe(recipeId, group, ingredient, result, experience, cookingTime,
          creosoteOutput);
    }

    @Override
    public CokeOvenRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      var group = buffer.readUtf();
      var creosoteOutput = buffer.readVarInt();
      var cookingTime = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var result = buffer.readItem();
      var experience = buffer.readFloat();
      return new CokeOvenRecipe(recipeId, group, ingredient, result, experience, cookingTime,
          creosoteOutput);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CokeOvenRecipe recipe) {
      buffer.writeUtf(recipe.group);
      buffer.writeVarInt(recipe.creosote.getAmount());
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
