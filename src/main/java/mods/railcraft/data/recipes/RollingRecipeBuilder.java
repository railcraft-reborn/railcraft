package mods.railcraft.data.recipes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class RollingRecipeBuilder {

  private final Item result;
  private final int count;
  private final int delay;
  private final List<String> rows = Lists.newArrayList();
  private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

  public RollingRecipeBuilder(ItemLike resultItem, int resultCount, int recipeDelay) {
    this.result = resultItem.asItem();
    this.count = resultCount;
    this.delay = recipeDelay;
  }

  public static RollingRecipeBuilder rolled(ItemLike resultItem) {
    return rolled(resultItem, 1, 100);
  }

  public static RollingRecipeBuilder rolled(ItemLike resultItem, int resultCount) {
    return rolled(resultItem, resultCount, 100);
  }

  public static RollingRecipeBuilder rolled(ItemLike resultItem,
      int resultCount, int recipeDelay) {
    return new RollingRecipeBuilder(resultItem, resultCount, recipeDelay);
  }

  public RollingRecipeBuilder define(Character key, TagKey<Item> itemTagValue) {
    return this.define(key, Ingredient.of(itemTagValue));
  }

  public RollingRecipeBuilder define(Character key, ItemLike itemValue) {
    return this.define(key, Ingredient.of(itemValue));
  }

  /**
   * Define a key for the recipie.
   *
   * @param key       A charachter used by the pattern
   * @param itemValue The ingredient, can be an {@link net.minecraft.tags.TagKey<Item> ItemTag}
   * @return this, for chaning functions.
   */
  public RollingRecipeBuilder define(Character key, Ingredient itemValue) {
    if (this.key.containsKey(key)) {
      throw new IllegalArgumentException("Symbol '" + key + "' is already defined!");
    } else if (key == ' ') {
      throw new IllegalArgumentException(
          "Symbol ' ' (whitespace) is reserved and cannot be defined");
    } else {
      this.key.put(key, itemValue);
      return this;
    }
  }

  /**
   * Defines a row of the recipie's pattern.
   */
  public RollingRecipeBuilder pattern(String pattern) {
    if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
      throw new IllegalArgumentException("Pattern must be the same width on every line!");
    } else {
      this.rows.add(pattern);
      return this;
    }
  }

  public void save(Consumer<FinishedRecipe> finishedRecipe) {
    this.save(finishedRecipe, ForgeRegistries.ITEMS.getKey(this.result));
  }

  /**
   * Saves the item for registration.
   *
   * @param key Custom filename for multiple recipies creating a single item
   */
  public void save(Consumer<FinishedRecipe> finishedRecipe, String key) {
    ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
    if ((new ResourceLocation(key)).equals(resourcelocation)) {
      throw new IllegalStateException("Shaped Recipe "
          + key + " should remove its 'save' argument");
    } else {
      this.save(finishedRecipe, new ResourceLocation(key));
    }
  }

  /**
   * Saves the item for registration.
   */
  public void save(Consumer<FinishedRecipe> finishedRecipe, ResourceLocation resourceLocation) {
    var path = resourceLocation.getPath();
    var customResourceLocation = new ResourceLocation(Railcraft.ID, "rolling/" + path);
    finishedRecipe.accept(
        new RollingRecipeBuilder.Result(customResourceLocation, this.result, this.count,
            this.delay, this.rows, this.key));
  }

  public class Result implements FinishedRecipe {

    private final ResourceLocation id;
    private final Item resultItem;
    private final int count;
    private final int delay;
    private final List<String> pattern;
    private final Map<Character, Ingredient> key;

    public Result(ResourceLocation resourceLocation, Item resultItem, int resultCount,
        int recipeDelay, List<String> recipePattern, Map<Character, Ingredient> ingredientMap) {
      this.id = resourceLocation;
      this.resultItem = resultItem;
      this.count = resultCount;
      this.delay = recipeDelay;
      this.pattern = recipePattern;
      this.key = ingredientMap;
    }

    public void serializeRecipeData(JsonObject jsonOut) {
      JsonArray jsonarray = new JsonArray();

      for (String s : this.pattern) {
        jsonarray.add(s);
      }

      jsonOut.add("pattern", jsonarray);
      JsonObject jsonobject = new JsonObject();

      for (Entry<Character, Ingredient> entry : this.key.entrySet()) {
        jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
      }

      jsonOut.add("key", jsonobject);
      JsonObject jsonobject1 = new JsonObject();
      jsonobject1.addProperty("item", ForgeRegistries.ITEMS.getKey(this.resultItem).toString());
      if (this.count > 1) {
        jsonobject1.addProperty("count", this.count);
      }

      jsonOut.add("result", jsonobject1);
      jsonOut.add("tickCost", new JsonPrimitive(delay));
    }

    public RecipeSerializer<?> getType() {
      return RailcraftRecipeSerializers.ROLLING.get();
    }

    public ResourceLocation getId() {
      return this.id;
    }

    @Override
    @Nullable
    public JsonObject serializeAdvancement() {
      return null;
    }

    @Override
    @Nullable
    public ResourceLocation getAdvancementId() {
      return null;
    }

  }
}
