package mods.railcraft.data.recipes.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mojang.datafixers.util.Pair;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class CrusherRecipeBuilder {

  public static final int DEFAULT_PROCESSING_TIME = 10 * 20; // 10 sec
  private static final int MAX_SLOTS = 9;

  private final Ingredient ingredient;
  private final List<Pair<ItemStack, Double>> probabilityItems;
  private final int processTime;

  private CrusherRecipeBuilder(Ingredient ingredient, int processTime) {
    this.ingredient = ingredient;
    this.probabilityItems = new ArrayList<>();
    this.processTime = processTime;
  }

  public static CrusherRecipeBuilder crush(Ingredient ingredient) {
    return crush(ingredient, DEFAULT_PROCESSING_TIME);
  }

  public static CrusherRecipeBuilder crush(Ingredient ingredient, int processTime) {
    return new CrusherRecipeBuilder(ingredient, processTime);
  }

  public CrusherRecipeBuilder addResult(ItemLike item, int quantity, double probability) {
    if (probabilityItems.size() >= MAX_SLOTS) {
      throw new IllegalStateException("Reached the maximum number of available slots as a result: "
          + MAX_SLOTS);
    }
    if (probability < 0 || probability > 1) {
      throw new IllegalStateException("The probability must be between 0 and 1! You have inserted: "
          + probability);
    }

    var itemStack = new ItemStack(item, quantity);
    probabilityItems.add(new Pair<>(itemStack, probability));
    return this;
  }

  public void save(RecipeOutput recipeOutput) {
    var itemPath = Arrays.stream(this.ingredient.getItems())
        .filter(x -> !x.is(Items.BARRIER))
        .findFirst()
        .map(x -> BuiltInRegistries.ITEM.getKey(x.getItem()).getPath())
        .orElseThrow();
    this.save(recipeOutput, itemPath);
  }

  public void save(RecipeOutput recipeOutput, String path) {
    var recipe = new CrusherRecipe(this.ingredient, this.probabilityItems, this.processTime);
    recipeOutput.accept(RailcraftConstants.rl("crusher/crushing_" + path), recipe, null);
  }
}
