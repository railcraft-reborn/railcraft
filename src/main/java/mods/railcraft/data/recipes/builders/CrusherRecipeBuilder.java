package mods.railcraft.data.recipes.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

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
    builder.conditions.add(new NotCondition(new TagEmptyCondition(ingredient)));
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
    this.conditions.add(new NotCondition(new TagEmptyCondition(output)));
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

  public void save(RecipeOutput recipeOutput) {
    String itemPath;
    if (this.ingredient.values[0] instanceof Ingredient.TagValue tagValue) {
      var location = tagValue.tag().location().getPath().replace("/", "_");
      itemPath = "tags_" + location;
    } else {
      itemPath = Arrays.stream(this.ingredient.getItems())
          .filter(x -> !x.is(Items.BARRIER))
          .findFirst()
          .map(x -> BuiltInRegistries.ITEM.getKey(x.getItem()).getPath())
          .orElseThrow();
    }
    this.save(recipeOutput, itemPath);
  }

  public void save(RecipeOutput recipeOutput, String path) {
    var recipe = new CrusherRecipe(this.ingredient, this.probabilityOutputs, this.processTime);
    recipeOutput
        .withConditions(conditions.toArray(ICondition[]::new))
        .accept(RailcraftConstants.rl("crusher/crushing_" + path), recipe, null);
  }
}
