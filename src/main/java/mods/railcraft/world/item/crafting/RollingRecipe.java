package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

/**
 * Rolling recipe class.
 *
 * @author LetterN (https://github.com/LetterN)
 */
public class RollingRecipe implements Recipe<CraftingContainer> {

  private final ResourceLocation recipeId;
  private final NonNullList<Ingredient> ingredients;
  private final ItemStack result;
  private final int tickCost;

  /**
   * Creates a new recipe.
   *
   * @param recipeId        - The id of the recipe
   * @param ingredients     - Ingredients list of the object
   * @param result          - The result
   * @param tickCost        - The time cost of the recipe
   */
  public RollingRecipe(ResourceLocation recipeId, NonNullList<Ingredient> ingredients,
      ItemStack result, int tickCost) {
    this.recipeId = recipeId;
    this.ingredients = ingredients;
    this.result = result;
    this.tickCost = tickCost;
  }

  /**
   * Get how long the user should wait before this gets crafted.
   *
   * @return tick cost, in int.
   */
  public int getTickCost() {
    return this.tickCost;
  }

  @Override
  public boolean matches(CraftingContainer inventory, Level world) {
    for (int i = 0; i <= inventory.getWidth() - 3; ++i) {
      for (int j = 0; j <= inventory.getHeight() - 3; ++j) {
        if (this.matches(inventory, i, j, true)) {
          return true;
        }

        if (this.matches(inventory, i, j, false)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean matches(CraftingContainer inventory, int x, int y, boolean inverse) {
    for (int i = 0; i < inventory.getWidth(); ++i) {
      for (int j = 0; j < inventory.getHeight(); ++j) {
        int k = i - x;
        int l = j - y;
        Ingredient ingredient = Ingredient.EMPTY;
        if (k >= 0 && l >= 0 && k < 3 && l < 3) {
          if (inverse) {
            ingredient = this.ingredients.get(3 - k - 1 + l * 3);
          } else {
            ingredient = this.ingredients.get(k + l * 3);
          }
        }

        if (!ingredient.test(inventory.getItem(i + j * inventory.getWidth()))) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public ItemStack assemble(CraftingContainer inventory) {
    return this.getResultItem().copy();
  }

  @Override
  public boolean canCraftInDimensions(int x, int y) {
    return x >= 3 && y >= 3;
  }

  @Override
  public ItemStack getResultItem() {
    return this.result;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.ingredients;
  }

  @Override
  public ResourceLocation getId() {
    return this.recipeId;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.ROLLING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return RailcraftRecipeTypes.ROLLING.get();
  }

  public static class RollingRecipeSerializer implements RecipeSerializer<RollingRecipe> {
    static {
      ShapedRecipe.setCraftingSize(3, 3);
    }

    @Override
    public RollingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      var map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
      String[] astring =
          ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(json,
          "pattern")));

      int tickCost = GsonHelper.getAsInt(json, "tickCost", 100); // 5 seconds
      var ingredients = ShapedRecipe.dissolvePattern(astring, map, 3, 3);
      ItemStack resultItemStack = itemFromJson(GsonHelper.getAsJsonObject(json, "result"));
      return new RollingRecipe(recipeId, ingredients, resultItemStack, tickCost);
    }

    @Override
    public RollingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
      int tickCost = buffer.readVarInt();
      ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
      ItemStack result = buffer.readItem();

      return new RollingRecipe(recipeId, ingredients, result, tickCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RollingRecipe recipe) {
      buffer.writeVarInt(recipe.tickCost);
      for (Ingredient ingredient : recipe.ingredients) {
        ingredient.toNetwork(buffer);
      }
      buffer.writeItem(recipe.result);
    }

    private static ItemStack itemFromJson(JsonObject itemObject) {
      if (!itemObject.has("item")) {
        throw new JsonParseException("No item key found");
      }
      if (itemObject.has("data")) {
        throw new JsonParseException("Disallowed data tag found");
      } else {
        return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(itemObject, true);
      }
    }
  }
}
