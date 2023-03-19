package mods.railcraft.world.item.crafting;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

public class CrusherRecipe implements Recipe<Container> {
  private final ResourceLocation recipeId;
  private final Ingredient ingredient;
  private final List<Tuple<ItemStack, Double>> probabilityItems;
  private final int tickCost;

  /**
   * Creates a new recipe.
   *
   * @param recipeId         - The id of the recipe
   * @param ingredient       - Ingredient of the object
   * @param probabilityItems - A list represents ItemStack - Probability
   * @param tickCost         - The time cost of the recipe
   */
  public CrusherRecipe(ResourceLocation recipeId, Ingredient ingredient,
      List<Tuple<ItemStack, Double>> probabilityItems, int tickCost) {
    this.recipeId = recipeId;
    this.ingredient = ingredient;
    this.probabilityItems = probabilityItems;
    this.tickCost = tickCost;
  }

  public int getTickCost() {
    return this.tickCost;
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
   * Use {@link #getProbabilityItems()} since we have more output
   */
  @Override
  @Deprecated(forRemoval = false)
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return ItemStack.EMPTY;
  }

  public List<Tuple<ItemStack, Double>> getProbabilityItems() {
    return probabilityItems;
  }

  public List<ItemStack> pollOutputs(RandomSource random) {
    var result = new ArrayList<ItemStack>();
    for(var item : probabilityItems) {
      if(random.nextDouble() < item.getB()) {
        result.add(item.getA().copy());
      }
    }
    return result;
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

  public static class Serializer implements RecipeSerializer<CrusherRecipe> {

    @Override
    public CrusherRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      int tickCost = GsonHelper
          .getAsInt(json, "tickCost", CrusherRecipeBuilder.DEFAULT_PROCESSING_TIME);
      var ingredient = Ingredient.fromJson(json.get("ingredient"));
      var probabilityItems = new ArrayList<Tuple<ItemStack, Double>>();

      var outputs = GsonHelper.getAsJsonArray(json, "output");
      for (var output : outputs) {
        var outputObj = output.getAsJsonObject();
        var probability = GsonHelper.getAsDouble(outputObj, "probability", 1);
        probability = Mth.clamp(probability, 0, 1); //[0,1]
        var result = itemFromJson(GsonHelper.getAsJsonObject(outputObj, "result"));
        probabilityItems.add(new Tuple<>(result, probability));
      }
      return new CrusherRecipe(recipeId, ingredient, probabilityItems, tickCost);
    }

    @Override
    public CrusherRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      var tickCost = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var probabilityItems = buffer.readList(buf -> {
        var result = buf.readItem();
        var probability = buf.readDouble();
        return new Tuple<>(result, probability);
      });
      return new CrusherRecipe(recipeId, ingredient, probabilityItems, tickCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CrusherRecipe recipe) {
      buffer.writeVarInt(recipe.tickCost);
      recipe.ingredient.toNetwork(buffer);
      buffer.writeCollection(recipe.probabilityItems, (buf, item) -> {
        buf.writeItem(item.getA());
        buf.writeDouble(item.getB());
      });
    }

    public static ItemStack itemFromJson(JsonObject json) {
      if (!json.has("item")) {
        throw new JsonParseException("No item key found");
      }
      return CraftingHelper.getItemStack(json, true);
    }
  }
}
