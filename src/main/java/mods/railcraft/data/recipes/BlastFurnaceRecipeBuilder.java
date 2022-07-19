package mods.railcraft.data.recipes;

import com.google.gson.JsonObject;
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

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BlastFurnaceRecipeBuilder implements RecipeBuilder {

  public static final int DEFAULT_COOKING_TIME = 1280;

  private final Item result;
  private final int count;
  private final Ingredient ingredient;
  private final float experience;
  private final int cookingTime;
  private final int slagOutput;
  private final Advancement.Builder advancement = Advancement.Builder.advancement();
  @Nullable
  private String group;

  public BlastFurnaceRecipeBuilder(ItemLike result, int count, Ingredient ingredient,
      float experience, int cookingTime, int slagOutput) {
    this.result = result.asItem();
    this.count = count;
    this.ingredient = ingredient;
    this.experience = experience;
    this.cookingTime = cookingTime;
    this.slagOutput = slagOutput;
  }

  public static BlastFurnaceRecipeBuilder smelting(ItemLike result, Ingredient ingredient,
      int multiplier) {
    return new BlastFurnaceRecipeBuilder(result.asItem(), multiplier, ingredient,
        0.0F, DEFAULT_COOKING_TIME * multiplier, multiplier);
  }

  public static BlastFurnaceRecipeBuilder recycling(ItemLike result, Ingredient ingredient,
      int multiplier) {
    return new BlastFurnaceRecipeBuilder(result.asItem(),
        multiplier, ingredient, 0.0F, (DEFAULT_COOKING_TIME / 2) * multiplier, 0);
  }

  @Override
  public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
    consumer.accept(new BlastFurnaceRecipeBuilder.Result(id,
        this.group == null ? "" : this.group, this.result, this.count, this.ingredient,
        this.experience, this.cookingTime, this.slagOutput, this.advancement,
        new ResourceLocation(id.getNamespace(), "recipes/"
            + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath())));
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
    private final int slagOutput;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;

    public Result(ResourceLocation id, String group, Item result, int count, Ingredient ingredient,
        float experience, int cookingTime, int slagOutput, Advancement.Builder advancement,
        ResourceLocation advancementId) {
      this.id = id;
      this.group = group;
      this.result = result;
      this.count = count;
      this.ingredient = ingredient;
      this.experience = experience;
      this.cookingTime = cookingTime;
      this.slagOutput = slagOutput;
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
      json.addProperty("slagOutput", this.slagOutput);
    }

    @Override
    public RecipeSerializer<?> getType() {
      return RailcraftRecipeSerializers.BLASTING.get();
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
