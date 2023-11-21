package mods.railcraft.data.recipes.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class CrusherRecipeBuilder {

  public static final int DEFAULT_PROCESSING_TIME = 10 * 20; // 10 sec
  private static final int MAX_SLOTS = 9;

  private final Ingredient ingredient;
  private final List<Tuple<ItemStack, Double>> probabilityItems;
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
    probabilityItems.add(new Tuple<>(itemStack, probability));
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
    recipeOutput.accept(new Result(
        Railcraft.rl("crusher/crushing_" + path),
        this.ingredient,
        this.probabilityItems,
        this.processTime));
  }

  private record Result(
      ResourceLocation id,
      Ingredient ingredient,
      List<Tuple<ItemStack, Double>> probabilityItems,
      int processTime) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(JsonObject jsonOut) {
      jsonOut.addProperty(RecipeJsonKeys.PROCESS_TIME, this.processTime);
      jsonOut.add(RecipeJsonKeys.INGREDIENT, this.ingredient.toJson(false));

      var result = new JsonArray();
      for (var item : this.probabilityItems) {
        var pattern = new JsonObject();

        var itemStackObject = new JsonObject();
        itemStackObject.addProperty(RecipeJsonKeys.ITEM,
            BuiltInRegistries.ITEM.getKey(item.getA().getItem()).toString());
        itemStackObject.addProperty(RecipeJsonKeys.COUNT, item.getA().getCount());
        pattern.add(RecipeJsonKeys.RESULT, itemStackObject);
        pattern.addProperty(RecipeJsonKeys.PROBABILITY, item.getB());
        result.add(pattern);
      }
      jsonOut.add(RecipeJsonKeys.OUTPUTS, result);
    }

    @Override
    public ResourceLocation id() {
      return this.id;
    }

    @Override
    public RecipeSerializer<?> type() {
      return RailcraftRecipeSerializers.CRUSHER.get();
    }

    @Nullable
    @Override
    public AdvancementHolder advancement() {
      return null;
    }
  }
}
