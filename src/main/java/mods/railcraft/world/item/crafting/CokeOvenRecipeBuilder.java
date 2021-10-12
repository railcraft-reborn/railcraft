package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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

public class CokeOvenRecipeBuilder {

  private final Item result;
  private final Ingredient ingredient;
  private final int count;
  private final int delay;
  private final int creosoteOut;

  public CokeOvenRecipeBuilder(IItemProvider resultItem, Ingredient ingredient,
      int resultCount, int recipieDelay, int creosoteOut) {
    this.result = resultItem.asItem();
    this.ingredient = ingredient;
    this.count = resultCount;
    this.delay = recipieDelay;
    this.creosoteOut = creosoteOut;
  }

  public static CokeOvenRecipeBuilder baked(IItemProvider resultItem, IItemProvider ingredient) {
    return baked(resultItem, ingredient, 1, 100);
  }

  public static CokeOvenRecipeBuilder baked(IItemProvider resultItem, IItemProvider ingredient,
      int resultCount) {
    return baked(resultItem, ingredient, resultCount, 100, 100);
  }

  public static CokeOvenRecipeBuilder baked(IItemProvider resultItem, IItemProvider ingredient,
      int resultCount, int recipieDelay) {
    return baked(resultItem, ingredient, resultCount, recipieDelay, 100);
  }

  public static CokeOvenRecipeBuilder baked(IItemProvider resultItem, IItemProvider ingredient,
      int resultCount, int recipieDelay, int creosoteOut) {
    return new CokeOvenRecipeBuilder(resultItem, Ingredient.of(ingredient),
      resultCount, recipieDelay, creosoteOut);
  }

  public static CokeOvenRecipeBuilder baked(IItemProvider resultItem, ITag<Item> ingredient) {
    return baked(resultItem, ingredient, 1, 100);
  }

  public static CokeOvenRecipeBuilder baked(IItemProvider resultItem, ITag<Item> ingredient,
      int resultCount) {
    return baked(resultItem, ingredient, resultCount, 100, 100);
  }

  public static CokeOvenRecipeBuilder baked(IItemProvider resultItem, ITag<Item> ingredient,
      int resultCount, int recipieDelay) {
    return baked(resultItem, ingredient, resultCount, recipieDelay, 100);
  }

  public static CokeOvenRecipeBuilder baked(IItemProvider resultItem, ITag<Item> ingredient,
      int resultCount, int recipieDelay, int creosoteOut) {
    return new CokeOvenRecipeBuilder(resultItem, Ingredient.of(ingredient),
      resultCount, recipieDelay, creosoteOut);
  }

  public void save(Consumer<IFinishedRecipe> finishedRecipie) {
    this.save(finishedRecipie, ForgeRegistries.ITEMS.getKey(this.result));
  }

  /**
   * Saves the item for registration.
   * @param key Custom filename for multiple recipies creating a single item
   */
  public void save(Consumer<IFinishedRecipe> finishedRecipie, String key) {
    ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
    if ((new ResourceLocation(key)).equals(resourcelocation)) {
      throw new IllegalStateException("Shaped Recipe "
        + key + " should remove its 'save' argument");
    } else {
      this.save(finishedRecipie, new ResourceLocation(key));
    }
  }

  /**
   * Saves the item for registration.
   */
  public void save(Consumer<IFinishedRecipe> finishedRecipie, ResourceLocation resourceLocation) {
    finishedRecipie.accept(
        new CokeOvenRecipeBuilder.Result(resourceLocation, this.result, this.ingredient,
            this.count, this.delay, this.creosoteOut));
  }

  public class Result implements IFinishedRecipe {

    private final ResourceLocation id;
    private final Item result;
    private final Ingredient ingredient;
    private final int count;
    private final int delay;
    private final int creosoteOut;

    public Result(ResourceLocation resourceLocation, Item resultItem, Ingredient ingredient,
        int resultCount, int recipieDelay, int creosoteOut) {
      this.id = resourceLocation;
      this.result = resultItem.asItem();
      this.ingredient = ingredient;
      this.count = resultCount;
      this.delay = recipieDelay;
      this.creosoteOut = creosoteOut;
    }

    public void serializeRecipeData(JsonObject jsonOut) {
      JsonObject resultObject = new JsonObject();
      resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
      if (this.count > 1) {
        resultObject.addProperty("count", this.count);
      }

      jsonOut.add("ingredient", this.ingredient.toJson());
      jsonOut.add("result", resultObject);
      jsonOut.add("tickCost", new JsonPrimitive(this.delay));
      jsonOut.add("creosoteOut", new JsonPrimitive(this.creosoteOut));
    }

    public IRecipeSerializer<?> getType() {
      return RailcraftRecipeSerializers.COKE_OVEN_COOKING.get();
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
