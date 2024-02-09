package mods.railcraft.world.item.crafting;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.util.RecipeUtil;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CrusherRecipe implements Recipe<Container> {
  private final ResourceLocation recipeId;
  private final Ingredient ingredient;
  private final List<CrusherOutput> probabilityOutputs;
  private final int processTime;

  public CrusherRecipe(ResourceLocation recipeId, Ingredient ingredient,
      List<CrusherOutput> probabilityOutputs, int processTime) {
    this.recipeId = recipeId;
    this.ingredient = ingredient;
    this.probabilityOutputs = probabilityOutputs;
    this.processTime = processTime;
  }

  public int getProcessTime() {
    return this.processTime;
  }

  @Override
  public boolean matches(Container inventory, Level level) {
    return this.ingredient.test(inventory.getItem(0));
  }

  @Override
  public ItemStack assemble(Container inventory, RegistryAccess registryAccess) {
    return this.getResultItem(registryAccess).copy();
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }


  /**
   * Use {@link #getProbabilityOutputs()} since we have more output
   */
  @Override
  @Deprecated()
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return ItemStack.EMPTY;
  }

  public List<CrusherOutput> getProbabilityOutputs() {
    return probabilityOutputs;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return NonNullList.of(Ingredient.EMPTY, ingredient);
  }

  @Override
  public ResourceLocation getId() {
    return this.recipeId;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.CRUSHER.get();
  }

  @Override
  public RecipeType<?> getType() {
    return RailcraftRecipeTypes.CRUSHING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.CRUSHER.get());
  }

  public record CrusherOutput(Ingredient output, int quantity, double probability) {
    public ItemStack getOutput() {
      return RecipeUtil.getPreferredStackbyMod(output.getItems()).copyWithCount(quantity);
    }
  }

  public static class Serializer implements RecipeSerializer<CrusherRecipe> {

    @Override
    public CrusherRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      int processTime = GsonHelper
          .getAsInt(json, RecipeJsonKeys.PROCESS_TIME, CrusherRecipeBuilder.DEFAULT_PROCESSING_TIME);
      var ingredient = Ingredient.fromJson(json.get(RecipeJsonKeys.INGREDIENT));
      var probabilityItems = new ArrayList<CrusherOutput>();

      var outputs = GsonHelper.getAsJsonArray(json, RecipeJsonKeys.OUTPUTS);
      for (var output : outputs) {
        var outputObj = output.getAsJsonObject();
        var probability = GsonHelper.getAsDouble(outputObj, RecipeJsonKeys.PROBABILITY, 1);
        probability = Mth.clamp(probability, 0, 1); //[0,1]
        var count = GsonHelper.getAsInt(outputObj, RecipeJsonKeys.COUNT);
        var outputIngredient = Ingredient.fromJson(outputObj.get(RecipeJsonKeys.RESULT));
        probabilityItems.add(new CrusherOutput(outputIngredient, count, probability));
      }
      return new CrusherRecipe(recipeId, ingredient, probabilityItems, processTime);
    }

    @Override
    public CrusherRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      var tickCost = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var probabilityOutputs = buffer.readList(buf ->
          new CrusherOutput(Ingredient.fromNetwork(buf), buf.readVarInt(), buf.readDouble()));
      return new CrusherRecipe(recipeId, ingredient, probabilityOutputs, tickCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CrusherRecipe recipe) {
      buffer.writeVarInt(recipe.processTime);
      recipe.ingredient.toNetwork(buffer);
      buffer.writeCollection(recipe.probabilityOutputs, (buf, item) -> {
        item.output.toNetwork(buf);
        buf.writeVarInt(item.quantity);
        buf.writeDouble(item.probability);
      });
    }
  }
}
