package mods.railcraft.data.recipes.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.ForgeRegistries;

public class CrusherRecipeBuilder {

  public static final int DEFAULT_PROCESSING_TIME = SharedConstants.TICKS_PER_SECOND * 10;
  private static final int MAX_SLOTS = 9;

  private final Ingredient ingredient;
  private final List<CrusherRecipe.CrusherOutput> probabilityOutputs;
  private final int processTime;
  private final List<ICondition> conditions;

  private CrusherRecipeBuilder(Ingredient ingredient, int processTime) {
    this.ingredient = ingredient;
    this.probabilityOutputs = new ArrayList<>();
    this.processTime = processTime;
    this.conditions = new ArrayList<>();
  }

  public static CrusherRecipeBuilder crush(ItemLike ingredient) {
    return crush(Ingredient.of(ingredient));
  }

  public static CrusherRecipeBuilder crush(TagKey<Item> ingredient) {
    var builder = crush(Ingredient.of(ingredient));
    builder.conditions.add(new NotCondition(new TagEmptyCondition(ingredient.location())));
    return builder;
  }

  public static CrusherRecipeBuilder crush(Ingredient ingredient) {
    return crush(ingredient, DEFAULT_PROCESSING_TIME);
  }

  public static CrusherRecipeBuilder crush(Ingredient ingredient, int processTime) {
    return new CrusherRecipeBuilder(ingredient, processTime);
  }

  public CrusherRecipeBuilder addResult(ItemLike output, int quantity, double probability) {
    return addResult(Ingredient.of(output), quantity, probability);
  }

  public CrusherRecipeBuilder addResult(TagKey<Item> output, int quantity, double probability) {
    this.conditions.add(new NotCondition(new TagEmptyCondition(output.location())));
    return addResult(Ingredient.of(output), quantity, probability);
  }

  public CrusherRecipeBuilder addResult(Ingredient output, int quantity, double probability) {
    if (probabilityOutputs.size() >= MAX_SLOTS) {
      throw new IllegalStateException(
          "Reached the maximum number of available slots as a result: " + MAX_SLOTS);
    }
    if (probability < 0 || probability > 1) {
      throw new IllegalStateException(
          "The probability must be between 0 and 1! You have inserted: " + probability);
    }

    probabilityOutputs.add(new CrusherRecipe.CrusherOutput(output, quantity, probability));
    return this;
  }

  public void save(Consumer<FinishedRecipe> finishedRecipe) {
    String itemPath;
    if (this.ingredient.values[0] instanceof Ingredient.TagValue tagValue) {
      var location = tagValue.tag.location().getPath().replace("/", "_");
      itemPath = "tags_" + location;
    } else {
      itemPath = Arrays.stream(this.ingredient.getItems())
          .filter(x -> !x.is(Items.BARRIER))
          .findFirst()
          .map(x -> ForgeRegistries.ITEMS.getKey(x.getItem()).getPath())
          .orElseThrow();
    }
    this.save(finishedRecipe, itemPath);
  }

  private void save(Consumer<FinishedRecipe> finishedRecipe, String path) {
    var customResourceLocation = RailcraftConstants.rl("crusher/crushing_" + path);

    finishedRecipe.accept(new Result(customResourceLocation, this.ingredient,
        this.probabilityOutputs, this.processTime, this.conditions));
  }

  private static class Result implements FinishedRecipe {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final List<CrusherRecipe.CrusherOutput> probabilityOutputs;
    private final int processTime;
    private final List<ICondition> conditions;

    public Result(ResourceLocation resourceLocation, Ingredient ingredient,
        List<CrusherRecipe.CrusherOutput> probabilityOutputs, int processTime,
        List<ICondition> conditions) {
      this.id = resourceLocation;
      this.ingredient = ingredient;
      this.probabilityOutputs = probabilityOutputs;
      this.processTime = processTime;
      this.conditions = conditions;
    }

    public void serializeRecipeData(JsonObject jsonOut) {
      if (!conditions.isEmpty()) {
        var conditionArray = new JsonArray();
        for(var condition : conditions) {
          conditionArray.add(CraftingHelper.serialize(condition));
        }
        jsonOut.add(RecipeJsonKeys.CONDITIONS, conditionArray);
      }
      jsonOut.addProperty(RecipeJsonKeys.PROCESS_TIME, processTime);
      jsonOut.add(RecipeJsonKeys.INGREDIENT, ingredient.toJson());

      var result = new JsonArray();
      for (var item : probabilityOutputs) {
        var pattern = new JsonObject();

        pattern.add(RecipeJsonKeys.RESULT, item.output().toJson());
        if (item.quantity() != 1) {
          pattern.addProperty(RecipeJsonKeys.COUNT, item.quantity());
        }
        pattern.addProperty(RecipeJsonKeys.PROBABILITY, item.probability());
        result.add(pattern);
      }
      jsonOut.add(RecipeJsonKeys.OUTPUTS, result);
    }

    public RecipeSerializer<?> getType() {
      return RailcraftRecipeSerializers.CRUSHER.get();
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
