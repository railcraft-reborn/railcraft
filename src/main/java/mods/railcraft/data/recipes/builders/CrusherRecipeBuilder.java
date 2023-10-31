package mods.railcraft.data.recipes.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.ForgeRegistries;

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
    var itemPath = Arrays.stream(ingredient.getItems())
        .filter(x -> !x.is(Items.BARRIER))
        .findFirst()
        .map(x -> ForgeRegistries.ITEMS.getKey(x.getItem()).getPath())
        .orElseThrow();
    save(recipeOutput, itemPath);
  }

  public void save(RecipeOutput recipeOutput, String path) {
    var customResourceLocation = Railcraft.rl("crusher/crushing_" + path);

    recipeOutput.accept(new Result(customResourceLocation, this.ingredient, this.probabilityItems,
        this.processTime));
  }

  private static class Result implements FinishedRecipe {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final List<Tuple<ItemStack, Double>> probabilityItems;
    private final int processTime;

    public Result(ResourceLocation resourceLocation, Ingredient ingredient,
        List<Tuple<ItemStack, Double>> probabilityItems,
        int processTime) {
      this.id = resourceLocation;
      this.ingredient = ingredient;
      this.probabilityItems = probabilityItems;
      this.processTime = processTime;
    }

    public void serializeRecipeData(JsonObject jsonOut) {
      jsonOut.addProperty("processTime", processTime);
      jsonOut.add("ingredient", ingredient.toJson());

      var result = new JsonArray();
      for (var item : probabilityItems) {
        var pattern = new JsonObject();

        var itemStackObject = new JsonObject();
        itemStackObject.addProperty("item",
            ForgeRegistries.ITEMS.getKey(item.getA().getItem()).toString());
        itemStackObject.addProperty("count", item.getA().getCount());
        pattern.add("result", itemStackObject);
        pattern.addProperty("probability", item.getB());
        result.add(pattern);
      }
      jsonOut.add("output", result);
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
