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
import net.minecraftforge.registries.ForgeRegistryEntry;

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

  public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
      implements RecipeSerializer<CokeOvenRecipe> {

    @Override
    public CokeOvenRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
      var group = GsonHelper.getAsString(jsonObject, "group", "");
      var cookingTime = GsonHelper.getAsInt(jsonObject, "cookingTime",
          CokeOvenRecipeBuilder.DEFAULT_COOKING_TIME);
      var creosoteOutput = GsonHelper.getAsInt(jsonObject, "creosoteOutput", 1000); // 1 bucket
      var ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
      var result = itemFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
      var experience = GsonHelper.getAsFloat(jsonObject, "experience", 0.0F);
      return new CokeOvenRecipe(id, group, ingredient, result, experience, cookingTime,
          creosoteOutput);
    }

    @Override
    public CokeOvenRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf in) {
      var group = in.readUtf();
      var creosoteOutput = in.readVarInt();
      var cookingTime = in.readVarInt();
      var ingredient = Ingredient.fromNetwork(in);
      var result = in.readItem();
      var experience = in.readFloat();
      return new CokeOvenRecipe(id, group, ingredient, result, experience, cookingTime,
          creosoteOutput);
    }

    @Override
    public void toNetwork(FriendlyByteBuf out, CokeOvenRecipe recipe) {
      out.writeUtf(recipe.group);
      out.writeVarInt(recipe.creosote.getAmount());
      out.writeVarInt(recipe.cookingTime);
      recipe.ingredient.toNetwork(out);
      out.writeItem(recipe.result);
      out.writeFloat(recipe.experience);
    }

    public static final ItemStack itemFromJson(JsonObject json) {
      if (!json.has("item")) {
        throw new JsonParseException("No item key found");
      }
      return CraftingHelper.getItemStack(json, true);
    }
  }
}
