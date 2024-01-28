package mods.railcraft.data.recipes.builders;

import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.api.core.RecipeJsonKeys;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractCookingRecipeBuilder implements RecipeBuilder {

  protected final Item result;
  protected final int count;
  protected final Ingredient ingredient;
  protected final float experience;
  protected final int cookingTime;
  protected final Advancement.Builder advancement = Advancement.Builder.advancement();
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
  public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance trigger) {
    this.advancement.addCriterion(name, trigger);
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
    private final String group;
    private final Item result;
    private final int count;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;

    public AbstractResult(ResourceLocation id, String group, Item result, int count,
        Ingredient ingredient, float experience, int cookingTime, Advancement.Builder advancement,
        ResourceLocation advancementId) {
      this.id = id;
      this.group = group;
      this.result = result;
      this.count = count;
      this.ingredient = ingredient;
      this.experience = experience;
      this.cookingTime = cookingTime;
      this.advancement = advancement;
      this.advancementId = advancementId;
    }

    @Override
    public final void serializeRecipeData(JsonObject json) {
      if (!group.isEmpty()) {
        json.addProperty(RecipeJsonKeys.GROUP, group);
      }

      json.add(RecipeJsonKeys.INGREDIENT, ingredient.toJson());
      var resultJson = new JsonObject();
      resultJson.addProperty(RecipeJsonKeys.ITEM, ForgeRegistries.ITEMS.getKey(result).toString());
      if (count > 1) {
        resultJson.addProperty(RecipeJsonKeys.COUNT, count);
      }
      json.add(RecipeJsonKeys.RESULT, resultJson);
      json.addProperty(RecipeJsonKeys.EXPERIENCE, experience);
      json.addProperty(RecipeJsonKeys.COOKING_TIME, cookingTime);
      addJsonProperty(json);
    }

    protected void addJsonProperty(JsonObject json) {
    }

    @Override
    public ResourceLocation getId() {
      return this.id;
    }

    @Override
    @Nullable
    public JsonObject serializeAdvancement() {
      return this.advancement.serializeToJson();
    }

    @Override
    @Nullable
    public ResourceLocation getAdvancementId() {
      return this.advancementId;
    }
  }
}
