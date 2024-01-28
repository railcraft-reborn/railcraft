package mods.railcraft.data.recipes.builders;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.SharedConstants;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class RollingRecipeBuilder {

  public static final int DEFAULT_PROCESSING_TIME = SharedConstants.TICKS_PER_SECOND * 5;
  private final Item result;
  private final int count;
  private final int processTime;
  private final List<String> rows = Lists.newArrayList();
  private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

  private RollingRecipeBuilder(ItemLike result, int count, int processTime) {
    this.result = result.asItem();
    this.count = count;
    this.processTime = processTime;
  }

  public static RollingRecipeBuilder rolled(ItemLike result) {
    return rolled(result, 1);
  }

  public static RollingRecipeBuilder rolled(ItemLike result, int count) {
    return rolled(result, count, DEFAULT_PROCESSING_TIME);
  }

  public static RollingRecipeBuilder rolled(ItemLike result, int count, int processTime) {
    return new RollingRecipeBuilder(result, count, processTime);
  }

  public RollingRecipeBuilder define(Character key, TagKey<Item> itemTagValue) {
    return this.define(key, Ingredient.of(itemTagValue));
  }

  public RollingRecipeBuilder define(Character key, ItemLike itemValue) {
    return this.define(key, Ingredient.of(itemValue));
  }

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

  public void save(Consumer<FinishedRecipe> finishedRecipe, String key) {
    var resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
    if ((new ResourceLocation(key)).equals(resourcelocation)) {
      throw new IllegalStateException(
          "Shaped Recipe %s should remove its 'save' argument".formatted(key));
    } else {
      this.save(finishedRecipe, new ResourceLocation(key));
    }
  }

  public void save(Consumer<FinishedRecipe> finishedRecipe, ResourceLocation resourceLocation) {
    var path = resourceLocation.getPath();
    var customResourceLocation = RailcraftConstants.rl("rolling/" + path);
    finishedRecipe.accept(
        new Result(customResourceLocation, this.result, this.count,
            this.processTime, this.rows, this.key));
  }

  private static class Result implements FinishedRecipe {

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
      var jsonarray = new JsonArray();

      for (var s : this.pattern) {
        jsonarray.add(s);
      }

      jsonOut.add(RecipeJsonKeys.PATTERN, jsonarray);
      var jsonobject = new JsonObject();

      for (var entry : this.key.entrySet()) {
        jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
      }

      jsonOut.add(RecipeJsonKeys.KEY, jsonobject);
      var result = new JsonObject();
      result.addProperty(RecipeJsonKeys.ITEM, ForgeRegistries.ITEMS.getKey(this.resultItem).toString());
      if (this.count > 1) {
        result.addProperty(RecipeJsonKeys.COUNT, this.count);
      }

      jsonOut.add(RecipeJsonKeys.RESULT, result);
      jsonOut.add(RecipeJsonKeys.PROCESS_TIME, new JsonPrimitive(delay));
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
