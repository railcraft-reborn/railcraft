package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mods.railcraft.data.recipes.BlastFurnaceRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

public class BlastFurnaceRecipe extends AbstractCookingRecipe {

  private final int slagOutput;

  public BlastFurnaceRecipe(ResourceLocation id, String group,
      Ingredient ingredient, ItemStack result, float experience, int cookingTime, int slagOutput) {
    super(RailcraftRecipeTypes.BLASTING.get(), id, group, ingredient, result, experience,
        cookingTime);
    this.slagOutput = slagOutput;
  }

  public int getSlagOutput() {
    return this.slagOutput;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.BLASTING.get();
  }

  public static class Serializer implements RecipeSerializer<BlastFurnaceRecipe> {

    @Override
    public BlastFurnaceRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
      var group = GsonHelper.getAsString(jsonObject, "group", "");
      var cookingTime = GsonHelper.getAsInt(jsonObject, "cookingTime",
          BlastFurnaceRecipeBuilder.DEFAULT_COOKING_TIME);
      var slagOutput = GsonHelper.getAsInt(jsonObject, "slagOutput", 1000); // 1 bucket
      var ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
      var result = itemFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
      var experience = GsonHelper.getAsFloat(jsonObject, "experience", 0.0F);
      return new BlastFurnaceRecipe(id, group, ingredient, result, experience, cookingTime,
          slagOutput);
    }

    @Override
    public BlastFurnaceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf in) {
      var group = in.readUtf();
      var slagOutput = in.readVarInt();
      var cookingTime = in.readVarInt();
      var ingredient = Ingredient.fromNetwork(in);
      var result = in.readItem();
      var experience = in.readFloat();
      return new BlastFurnaceRecipe(id, group, ingredient, result, experience, cookingTime,
          slagOutput);
    }

    @Override
    public void toNetwork(FriendlyByteBuf out, BlastFurnaceRecipe recipe) {
      out.writeUtf(recipe.group);
      out.writeVarInt(recipe.slagOutput);
      out.writeVarInt(recipe.cookingTime);
      recipe.ingredient.toNetwork(out);
      out.writeItem(recipe.result);
      out.writeFloat(recipe.experience);
    }

    public static ItemStack itemFromJson(JsonObject json) {
      if (!json.has("item")) {
        throw new JsonParseException("No item key found");
      }
      return CraftingHelper.getItemStack(json, true);
    }
  }
}
