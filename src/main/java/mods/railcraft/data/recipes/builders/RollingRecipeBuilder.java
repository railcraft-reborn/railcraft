package mods.railcraft.data.recipes.builders;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class RollingRecipeBuilder {

  public static final int DEFAULT_PROCESSING_TIME = 100;
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

  public void save(RecipeOutput recipeOutput) {
    this.save(recipeOutput, BuiltInRegistries.ITEM.getKey(this.result));
  }

  public void save(RecipeOutput recipeOutput, String key) {
    var resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
    if (new ResourceLocation(key).equals(resourcelocation)) {
      throw new IllegalStateException(
          "Shaped Recipe %s should remove its 'save' argument".formatted(key));
    } else {
      this.save(recipeOutput, new ResourceLocation(key));
    }
  }

  public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
    var customResourceLocation = resourceLocation.withPrefix("rolling/");
    recipeOutput.accept(new Result(customResourceLocation, this.result, this.count,
        this.processTime, this.rows, this.key));
  }

  private record Result(
      ResourceLocation id,
      Item item,
      int count,
      int processTime,
      List<String> pattern,
      Map<Character, Ingredient> key) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(JsonObject jsonOut) {
      var patternJson = new JsonArray();
      for (var s : this.pattern) {
        patternJson.add(s);
      }
      jsonOut.add(RecipeJsonKeys.PATTERN, patternJson);

      var keyJson = new JsonObject();
      for (var entry : this.key.entrySet()) {
        keyJson.add(String.valueOf(entry.getKey()), entry.getValue().toJson(false));
      }
      jsonOut.add(RecipeJsonKeys.KEY, keyJson);

      var resultJson = new JsonObject();
      resultJson.addProperty(RecipeJsonKeys.ITEM,
          BuiltInRegistries.ITEM.getKey(this.item).toString());
      if (this.count > 1) {
        resultJson.addProperty(RecipeJsonKeys.COUNT, this.count);
      }
      jsonOut.add(RecipeJsonKeys.RESULT, resultJson);

      jsonOut.add(RecipeJsonKeys.PROCESS_TIME, new JsonPrimitive(this.processTime));
    }

    @Override
    public ResourceLocation id() {
      return this.id;
    }

    @Override
    public RecipeSerializer<?> type() {
      return RailcraftRecipeSerializers.ROLLING.get();
    }

    @Nullable
    @Override
    public AdvancementHolder advancement() {
      return null;
    }
  }
}
