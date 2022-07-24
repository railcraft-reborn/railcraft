package mods.railcraft.world.item.crafting;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
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
import net.minecraft.world.level.Level;

/**
 * Rolling recipe class.
 *
 * @author LetterN (https://github.com/LetterN)
 */
public class RollingRecipe implements Recipe<CraftingContainer> {

  private final ResourceLocation id;
  private final NonNullList<Ingredient> recipeItems;
  private final ItemStack result;
  private final int tickCost;

  /**
   * Creates a new recipie.
   *
   * @param resourceLocation -
   * @param tickCost - The time cost of the recipie
   * @param ingredients - Ingredients list of the object
   * @param resultItemStack - The result
   */
  public RollingRecipe(ResourceLocation resourceLocation, int tickCost,
      NonNullList<Ingredient> ingredients, ItemStack resultItemStack) {
    this.id = resourceLocation;
    this.recipeItems = ingredients;
    this.result = resultItemStack;
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
            ingredient = this.recipeItems.get(3 - k - 1 + l * 3);
          } else {
            ingredient = this.recipeItems.get(k + l * 3);
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
  public ResourceLocation getId() {
    return this.id;
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

    @Override
    public RollingRecipe fromJson(ResourceLocation resourceLoc, JsonObject jsonObject) {
      Map<String, Ingredient> map = keyFromJson(GsonHelper.getAsJsonObject(jsonObject, "key"));
      String[] astring = shrink(patternFromJson(GsonHelper.getAsJsonArray(jsonObject, "pattern")));

      int tickCost = GsonHelper.getAsInt(jsonObject, "tickCost", 100); // 5 seconds
      // I SAID, STRICT 3X3
      NonNullList<Ingredient> ingredients = dissolvePattern(astring, map, 3, 3);
      ItemStack resultItemStack = itemFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
      // 3x3 recipies only, attempting to register 4x4's will not work and we will never honor it.
      return new RollingRecipe(resourceLoc, tickCost, ingredients, resultItemStack);
    }

    @Override
    public RollingRecipe fromNetwork(ResourceLocation resourceLoc, FriendlyByteBuf packetBuffer) {
      NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
      int tickCost = packetBuffer.readVarInt();

      for (int k = 0; k < ingredients.size(); ++k) {
        ingredients.set(k, Ingredient.fromNetwork(packetBuffer));
      }

      ItemStack itemstack = packetBuffer.readItem();
      return new RollingRecipe(resourceLoc, tickCost, ingredients, itemstack);
    }

    @Override
    public void toNetwork(FriendlyByteBuf packetBuffer, RollingRecipe recipe) {
      packetBuffer.writeVarInt(recipe.tickCost);
      // format: [tickcost(int), ingredient, result]
      for (Ingredient ingredient : recipe.recipeItems) {
        ingredient.toNetwork(packetBuffer);
      }

      packetBuffer.writeItem(recipe.result);
    }

    private static String[] patternFromJson(JsonArray jsondat) {
      String[] astring = new String[jsondat.size()];
      if (astring.length > 3) {
        throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
      } else if (astring.length == 0) {
        throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
      } else {
        for (int i = 0; i < astring.length; ++i) {
          String s = GsonHelper.convertToString(jsondat.get(i), "pattern[" + i + "]");
          if (s.length() > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
          }
          if (i > 0 && astring[0].length() != s.length()) {
            throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
          }
          astring[i] = s;
        }
        return astring;
      }
    }

    /**
     * see vanilla crafting table.
     *
     * @param jsondat -
     * @return {@see net.minecraft.item.crafting.ShapedRecipe keyFromJson}
     */
    private static Map<String, Ingredient> keyFromJson(JsonObject jsondat) {
      Map<String, Ingredient> map = Maps.newHashMap();

      for (Entry<String, JsonElement> entry : jsondat.entrySet()) {
        if (entry.getKey().length() != 1) {
          throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey()
              + "' is an invalid symbol (must be 1 character only).");
        }
        if (" ".equals(entry.getKey())) {
          throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
        }
        map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
      }
      map.put(" ", Ingredient.EMPTY);
      return map;
    }

    public static ItemStack itemFromJson(JsonObject jsondat) {
      if (!jsondat.has("item")) {
        throw new JsonParseException("No item key found");
      }
      if (jsondat.has("data")) {
        throw new JsonParseException("Disallowed data tag found");
      } else {
        return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(jsondat, true);
      }
    }

    private static NonNullList<Ingredient> dissolvePattern(String[] patternArray,
        Map<String, Ingredient> ingreidentKeyMap, int x, int y) {
      NonNullList<Ingredient> nonnulllist = NonNullList.withSize(x * y, Ingredient.EMPTY);
      Set<String> set = Sets.newHashSet(ingreidentKeyMap.keySet());
      set.remove(" ");

      for (int i = 0; i < patternArray.length; ++i) {
        for (int j = 0; j < patternArray[i].length(); ++j) {
          String s = patternArray[i].substring(j, j + 1);
          Ingredient ingredient = ingreidentKeyMap.get(s);
          if (ingredient == null) {
            throw new JsonSyntaxException(
                "Pattern references symbol '" + s + "' but it's not defined in the key");
          }
          set.remove(s);
          nonnulllist.set(j + x * i, ingredient);
        }
      }

      if (!set.isEmpty()) {
        throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
      } else {
        return nonnulllist;
      }
    }

    static String[] shrink(String... patternArray) {
      int i = Integer.MAX_VALUE;
      int j = 0;
      int k = 0;
      int l = 0;

      for (int i1 = 0; i1 < patternArray.length; ++i1) {
        String s = patternArray[i1];
        i = Math.min(i, firstNonSpace(s));
        int j1 = lastNonSpace(s);
        j = Math.max(j, j1);
        if (j1 < 0) {
          if (k == i1) {
            ++k;
          }

          ++l;
        } else {
          l = 0;
        }
      }

      if (patternArray.length == l) {
        return new String[0];
      } else {
        String[] astring = new String[patternArray.length - l - k];
        for (int k1 = 0; k1 < astring.length; ++k1) {
          astring[k1] = patternArray[k1 + k].substring(i, j + 1);
        }
        return astring;
      }
    }

    private static final int firstNonSpace(String in) {
      int i;
      for (i = 0; i < in.length() && in.charAt(i) == ' '; ++i) {
        ;
      }
      return i;
    }

    private static final int lastNonSpace(String in) {
      int i;
      for (i = in.length() - 1; i >= 0 && in.charAt(i) == ' '; --i) {
        ;
      }
      return i;
    }
  }
}
