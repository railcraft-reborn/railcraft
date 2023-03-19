package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
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

public class RollingRecipe implements Recipe<CraftingContainer> {
  private final ResourceLocation recipeId;
  private final int width, height;
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
  public RollingRecipe(ResourceLocation recipeId, int width, int height,
      NonNullList<Ingredient> ingredients, ItemStack result, int tickCost) {
    this.recipeId = recipeId;
    this.width = width;
    this.height = height;
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

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  @Override
  public boolean matches(CraftingContainer inventory, Level level) {
    for (int i = 0; i <= inventory.getWidth() - this.width; ++i) {
      for (int j = 0; j <= inventory.getHeight() - this.height; ++j) {
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
        if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
          if (inverse) {
            ingredient = this.ingredients.get(this.width - k - 1 + l * this.width);
          } else {
            ingredient = this.ingredients.get(k + l * this.width);
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
  public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryAccess) {
    return this.getResultItem(registryAccess).copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= this.width && height >= this.height;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess registryAccess) {
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

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());
  }

  public static class Serializer implements RecipeSerializer<RollingRecipe> {

    @Override
    public RollingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      var map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
      var patterns = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(json,"pattern")));
      int width = patterns[0].length();
      int height = patterns.length;

      int tickCost = GsonHelper.getAsInt(json, "tickCost", 100); // 5 seconds
      var ingredients = ShapedRecipe.dissolvePattern(patterns, map, width, height);
      var resultItemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"result"));
      return new RollingRecipe(recipeId, width, height, ingredients, resultItemStack, tickCost);
    }

    @Override
    public RollingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      int width = buffer.readVarInt();
      int height = buffer.readVarInt();
      int tickCost = buffer.readVarInt();
      var ingredients =
          buffer.readCollection(NonNullList::createWithCapacity, Ingredient::fromNetwork);
      var result = buffer.readItem();

      return new RollingRecipe(recipeId, width, height, ingredients, result, tickCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RollingRecipe recipe) {
      buffer.writeVarInt(recipe.width);
      buffer.writeVarInt(recipe.height);
      buffer.writeVarInt(recipe.tickCost);
      buffer.writeCollection(recipe.ingredients, (buf, ingredient) -> ingredient.toNetwork(buffer));
      buffer.writeItem(recipe.result);
    }
  }
}
