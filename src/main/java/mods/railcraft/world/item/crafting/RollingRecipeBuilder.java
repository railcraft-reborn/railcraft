package mods.railcraft.world.item.crafting;

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

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class RollingRecipeBuilder {

  private final Item result;
  private final int count;
  private final int delay;
  private final List<String> rows = Lists.newArrayList();
  private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

  public RollingRecipeBuilder(IItemProvider resultItem, int resultCount, int recipieDelay) {
    this.result = resultItem.asItem();
    this.count = resultCount;
    this.delay = recipieDelay;
  }

  public static RollingRecipeBuilder rolled(IItemProvider resultItem) {
    return rolled(resultItem, 1, 100);
  }

  public static RollingRecipeBuilder rolled(IItemProvider resultItem, int resultCount) {
    return rolled(resultItem, resultCount, 100);
  }

  public static RollingRecipeBuilder rolled(IItemProvider resultItem,
      int resultCount, int recipieDelay) {
    return new RollingRecipeBuilder(resultItem, resultCount, recipieDelay);
  }

  public RollingRecipeBuilder define(Character key, ITag<Item> itemTagValue) {
    return this.define(key, Ingredient.of(itemTagValue));
  }

  public RollingRecipeBuilder define(Character key, IItemProvider itemValue) {
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
    if (!this.rows.isEmpty() && pattern.length() != 3) {
      throw new IllegalArgumentException("Pattern must be the same width on every line!");
    } else {
      this.rows.add(pattern);
      return this;
    }
  }

  public void save(Consumer<IFinishedRecipe> finishedRecipie) {
    this.save(finishedRecipie, ForgeRegistries.ITEMS.getKey(this.result));
  }

  public void save(Consumer<IFinishedRecipe> finishedRecipie, String key) {
    ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
    if ((new ResourceLocation(key)).equals(resourcelocation)) {
      throw new IllegalStateException("Shaped Recipe "
        + key + " should remove its 'save' argument");
    } else {
      this.save(finishedRecipie, new ResourceLocation(key));
    }
  }

  public void save(Consumer<IFinishedRecipe> finishedRecipie, ResourceLocation resourceLocation) {
    finishedRecipie.accept(
        new RollingRecipeBuilder.Result(resourceLocation, this.result, this.count,
          this.delay, this.rows, this.key));
  }
  public class Result implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Item resultItem;
    private final int count;
    private final int delay;
    private final List<String> pattern;
    private final Map<Character, Ingredient> key;

    public Result(ResourceLocation resourceLocation, Item resultItem, int resultCount,
        int recipieDelay, List<String> recipiePattern, Map<Character, Ingredient> ingredientMap) {
      this.id = resourceLocation;
      this.resultItem = resultItem;
      this.count = resultCount;
      this.delay = recipieDelay;
      this.pattern = recipiePattern;
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

    public IRecipeSerializer<?> getType() {
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
