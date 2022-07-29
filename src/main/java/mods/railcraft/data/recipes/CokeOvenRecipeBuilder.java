package mods.railcraft.data.recipes;

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class CokeOvenRecipeBuilder implements RecipeBuilder {

  public static final int DEFAULT_COOKING_TIME = 1800; // 90 sec

  private final Item result;
  private final int count;
  private final Ingredient ingredient;
  private final float experience;
  private final int cookingTime;
  private final int creosoteOutput;
  private final Advancement.Builder advancement = Advancement.Builder.advancement();
  @Nullable
  private String group;

  public CokeOvenRecipeBuilder(ItemLike result, int count, Ingredient ingredient, float experience,
      int cookingTime, int creosoteOutput) {
    this.result = result.asItem();
    this.count = count;
    this.ingredient = ingredient;
    this.experience = experience;
    this.cookingTime = cookingTime;
    this.creosoteOutput = creosoteOutput;
  }

  public static CokeOvenRecipeBuilder coking(ItemLike result, Ingredient ingredient,
      float experience, int creosoteOutput) {
    return coking(result, ingredient, experience, DEFAULT_COOKING_TIME, creosoteOutput);
  }
  public static CokeOvenRecipeBuilder coking(ItemLike result, Ingredient ingredient,
      float experience, int cookingTime, int creosoteOutput) {
    return coking(result, 1, ingredient, experience, cookingTime, creosoteOutput);
  }

  public static CokeOvenRecipeBuilder coking(ItemLike result, int resultCount,
      Ingredient ingredient, float experience, int cookingTime, int creosoteOutput) {
    return new CokeOvenRecipeBuilder(result, resultCount, ingredient, experience, cookingTime,
        creosoteOutput);
  }

  @Override
  public void save(Consumer<FinishedRecipe> finishedRecipe, ResourceLocation resourceLocation) {
    var path = resourceLocation.getPath();
    var customResourceLocation = new ResourceLocation(Railcraft.ID, "coke_oven/" + path);

    var advancementId = new ResourceLocation(Railcraft.ID,
        String.format("recipes/%s/%s", this.result.getItemCategory().getRecipeFolderName(),
        customResourceLocation.getPath()));

    finishedRecipe.accept(new CokeOvenRecipeBuilder.Result(customResourceLocation,
        this.group == null ? "" : this.group, this.result, this.count, this.ingredient,
        this.experience, this.cookingTime, this.creosoteOutput, this.advancement, advancementId));
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

  public class Result implements FinishedRecipe {

    private final ResourceLocation id;
    private final String group;
    private final Item result;
    private final int count;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final int creosoteOutput;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;

    public Result(ResourceLocation id, String group, Item result, int count, Ingredient ingredient,
        float experience, int cookingTime, int creosoteOutput, Advancement.Builder advancement,
        ResourceLocation advancementId) {
      this.id = id;
      this.group = group;
      this.result = result;
      this.count = count;
      this.ingredient = ingredient;
      this.experience = experience;
      this.cookingTime = cookingTime;
      this.creosoteOutput = creosoteOutput;
      this.advancement = advancement;
      this.advancementId = advancementId;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
      if (!this.group.isEmpty()) {
        json.addProperty("group", this.group);
      }

      json.add("ingredient", this.ingredient.toJson());
      var resultJson = new JsonObject();
      resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
      if (this.count > 1) {
        resultJson.addProperty("count", this.count);
      }
      json.add("result", resultJson);
      json.addProperty("experience", this.experience);
      json.addProperty("cookingTime", this.cookingTime);
      json.addProperty("creosoteOutput", this.creosoteOutput);
    }

    @Override
    public RecipeSerializer<?> getType() {
      return RailcraftRecipeSerializers.COKING.get();
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
