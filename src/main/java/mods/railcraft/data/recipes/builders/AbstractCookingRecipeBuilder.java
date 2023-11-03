package mods.railcraft.data.recipes.builders;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonObject;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.ForgeRegistries;

public abstract class AbstractCookingRecipeBuilder implements RecipeBuilder {

  protected final Item result;
  protected final int count;
  protected final Ingredient ingredient;
  protected final float experience;
  protected final int cookingTime;
  protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
  @Nullable
  protected String group;

  public AbstractCookingRecipeBuilder(ItemLike result, int count, Ingredient ingredient,
      float experience, int cookingTime) {
    this.result = result.asItem();
    this.count = count;
    this.ingredient = ingredient;
    this.experience = experience;
    this.cookingTime = cookingTime;
  }

  @Override
  public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
    this.criteria.put(name, criterion);
    return this;
  }

  @Override
  public RecipeBuilder group(String group) {
    this.group = group;
    return this;
  }

  @Override
  public Item getResult() {
    return this.result;
  }

  public static abstract class AbstractResult implements FinishedRecipe {

    private final ResourceLocation id;
    private final Item result;
    private final int count;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final AdvancementHolder advancement;


    public AbstractResult(ResourceLocation id, Item result, int count,
        Ingredient ingredient, float experience, int cookingTime, AdvancementHolder advancement) {
      this.id = id;
      this.result = result;
      this.count = count;
      this.ingredient = ingredient;
      this.experience = experience;
      this.cookingTime = cookingTime;
      this.advancement = advancement;
    }

    @Override
    public final void serializeRecipeData(JsonObject json) {
      json.add("ingredient", ingredient.toJson(false));
      var resultJson = new JsonObject();
      resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
      if (count > 1) {
        resultJson.addProperty("count", count);
      }
      json.add("result", resultJson);
      json.addProperty("experience", experience);
      json.addProperty("cookingTime", cookingTime);
      addJsonProperty(json);
    }

    protected void addJsonProperty(JsonObject json) {
    }

    @Override
    public ResourceLocation id() {
      return this.id;
    }

    @Override
    @Nullable
    public AdvancementHolder advancement() {
      return this.advancement;
    }
  }
}
