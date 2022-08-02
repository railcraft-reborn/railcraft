package mods.railcraft.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class CrusherRecipeBuilder {

  private static final int MAX_SLOTS = 9;

  private final Ingredient ingredient;
  private final List<Tuple<ItemStack, Double>> probabilityItems;
  private final int recipeDelay;

  private CrusherRecipeBuilder(Ingredient ingredient,
      List<Tuple<ItemStack, Double>> probabilityItems, int recipeDelay) {
    this.ingredient = ingredient;
    this.probabilityItems = probabilityItems;
    this.recipeDelay = recipeDelay;
  }

  public static CrusherRecipeBuilder crush(Ingredient ingredient, int recipeDelay) {
    return new CrusherRecipeBuilder(ingredient, new ArrayList<>(), recipeDelay);
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

  public void save(Consumer<FinishedRecipe> finishedRecipe) {
    var itemPath = Arrays.stream(ingredient.getItems())
        .filter(x -> !x.is(Items.BARRIER))
        .findFirst()
        .map(x -> ForgeRegistries.ITEMS.getKey(x.getItem()).getPath())
        .orElseThrow();
    save(finishedRecipe, itemPath);
  }

  public void save(Consumer<FinishedRecipe> finishedRecipe, String path) {
    var customResourceLocation = new ResourceLocation(Railcraft.ID, "crusher/crushing_" + path);

    finishedRecipe.accept(new Result(customResourceLocation, this.ingredient, this.probabilityItems,
        this.recipeDelay));
  }

  private static class Result implements FinishedRecipe {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final List<Tuple<ItemStack, Double>> probabilityItems;
    private final int recipeDelay;

    public Result(ResourceLocation resourceLocation, Ingredient ingredient,
        List<Tuple<ItemStack, Double>> probabilityItems,
        int recipeDelay) {
      this.id = resourceLocation;
      this.ingredient = ingredient;
      this.probabilityItems = probabilityItems;
      this.recipeDelay = recipeDelay;
    }

    public void serializeRecipeData(JsonObject jsonOut) {
      jsonOut.addProperty("tickCost", recipeDelay);
      jsonOut.add("ingredient", ingredient.toJson());

      JsonArray result = new JsonArray();
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
