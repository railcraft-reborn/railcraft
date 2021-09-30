package mods.railcraft.crafting;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RollingRecipe implements IRecipe<CraftingInventory> {

  private final ResourceLocation id;
  private final NonNullList<Ingredient> recipeItems;
  private final ItemStack result;
  private final int tickCost;

  public RollingRecipe(ResourceLocation resourceLocation, int tickCost, NonNullList<Ingredient> ingredients, ItemStack resultItemStack) {
    this.id = resourceLocation;
    this.recipeItems = ingredients;
    this.result = resultItemStack;
    this.tickCost = tickCost;
  }

  /**
   * Get how long the user should wait before this gets crafted.
   * @return
   */
  public int getTickCost() {
    return this.tickCost;
  }

  @Override
  public String getGroup() {
    return "ROLLING_MACHINE";
  }

  @Override
  public boolean matches(CraftingInventory inventory, World world) {
    for(int i = 0; i <= inventory.getWidth() - 3; ++i) {
      for(int j = 0; j <= inventory.getHeight() - 3; ++j) {
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

  private boolean matches(CraftingInventory inventory, int x, int y, boolean something) {
    for(int i = 0; i < inventory.getWidth(); ++i) {
      for(int j = 0; j < inventory.getHeight(); ++j) {
        int k = i - x;
        int l = j - y;
        Ingredient ingredient = Ingredient.EMPTY;
        if (k >= 0 && l >= 0 && k < 3 && l < 3) {
          if (something) {
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
  public ItemStack assemble(CraftingInventory inventory) {
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
  public IRecipeSerializer<?> getSerializer() {
    return RailcraftRecipies.ROLLING.get();
  }

  @Override
  public IRecipeType<?> getType() {
    return RailcraftRecipies.ROLLING_RECIPIE;
  }

  public static class RollingRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
    implements IRecipeSerializer<RollingRecipe> {

    @Override
    public RollingRecipe fromJson(ResourceLocation resourceLoc, JsonObject jsonObject) {
      Map<String, Ingredient> map = keyFromJson(JSONUtils.getAsJsonObject(jsonObject, "key"));
      String[] astring = shrink(patternFromJson(JSONUtils.getAsJsonArray(jsonObject, "pattern")));

      int tickCost = JSONUtils.getAsInt(jsonObject, "tickCost", 100); //5 seconds
      NonNullList<Ingredient> ingredients = dissolvePattern(astring, map, astring[0].length(), astring.length);
      ItemStack resultItemStack = itemFromJson(JSONUtils.getAsJsonObject(jsonObject, "result"));
      // 3x3 recipies only, attempting to register 4x4's will not work and we will never honor it.
      return new RollingRecipe(resourceLoc, tickCost, ingredients, resultItemStack);
    }

    @Override
    public RollingRecipe fromNetwork(ResourceLocation resourceLoc, PacketBuffer packetBuffer) {
      NonNullList<Ingredient> ingredients= NonNullList.withSize(9, Ingredient.EMPTY);
      int tickCost = packetBuffer.readVarInt();

      for(int k = 0; k < ingredients.size(); ++k) {
        ingredients.set(k, Ingredient.fromNetwork(packetBuffer));
      }

      ItemStack itemstack = packetBuffer.readItem();
      return new RollingRecipe(resourceLoc, tickCost, ingredients, itemstack);
    }

    @Override
    public void toNetwork(PacketBuffer packetBuffer, RollingRecipe recipe) {
      packetBuffer.writeVarInt(recipe.tickCost);
      // format: [tickcost(int), ingredient, result]
      for(Ingredient ingredient : recipe.recipeItems) {
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
        for(int i = 0; i < astring.length; ++i) {
          String s = JSONUtils.convertToString(jsondat.get(i), "pattern[" + i + "]");
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
     * @param jsondat
     * @return
     * @see net.minecraft.item.crafting.ShapedRecipe keyFromJson
     */
    private static Map<String, Ingredient> keyFromJson(JsonObject jsondat) {
      Map<String, Ingredient> map = Maps.newHashMap();

      for(Entry<String, JsonElement> entry : jsondat.entrySet()) {
        if (entry.getKey().length() != 1) {
          throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
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

    private static NonNullList<Ingredient> dissolvePattern(String[] patternArray, Map<String, Ingredient> ingreidentKeyMap, int x, int y) {
      NonNullList<Ingredient> nonnulllist = NonNullList.withSize(x * y, Ingredient.EMPTY);
      Set<String> set = Sets.newHashSet(ingreidentKeyMap.keySet());
      set.remove(" ");

      for(int i = 0; i < patternArray.length; ++i) {
        for(int j = 0; j < patternArray[i].length(); ++j) {
          String s = patternArray[i].substring(j, j + 1);
          Ingredient ingredient = ingreidentKeyMap.get(s);
          if (ingredient == null) {
            throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
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

      for(int i1 = 0; i1 < patternArray.length; ++i1) {
        String s = patternArray[i1];
        i = Math.min(i, Math.max(s.indexOf(' '), 0));
        int j1 = Math.max(s.lastIndexOf(' '), 0);
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
        for(int k1 = 0; k1 < astring.length; ++k1) {
          astring[k1] = patternArray[k1 + k].substring(i, j + 1);
        }
        return astring;
      }
    }

  }
}
