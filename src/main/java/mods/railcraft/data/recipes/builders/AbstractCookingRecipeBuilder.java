package mods.railcraft.data.recipes.builders;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public abstract class AbstractCookingRecipeBuilder implements RecipeBuilder {

  protected final Item result;
  protected final int count;
  protected final Ingredient ingredient;
  protected final float experience;
  protected final int cookingTime;
  protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

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
    throw new IllegalStateException("Group not allow");
  }

  @Override
  public Item getResult() {
    return this.result;
  }
}
