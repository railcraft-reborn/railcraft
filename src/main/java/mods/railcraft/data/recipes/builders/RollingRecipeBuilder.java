package mods.railcraft.data.recipes.builders;

import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mods.railcraft.world.item.crafting.RollingRecipe;
import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

public class RollingRecipeBuilder {

  public static final int DEFAULT_PROCESSING_TIME = SharedConstants.TICKS_PER_SECOND * 5;
  private final ItemStackTemplate result;
  private final int processTime;
  private final List<String> rows = Lists.newArrayList();
  private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

  private RollingRecipeBuilder(ItemStackTemplate result, int processTime) {
    this.result = result;
    this.processTime = processTime;
  }

  public static RollingRecipeBuilder rolled(ItemStackTemplate result) {
    return new RollingRecipeBuilder(result, DEFAULT_PROCESSING_TIME);
  }

  public RollingRecipeBuilder define(HolderLookup.RegistryLookup<Item> items,
      Character key, TagKey<Item> itemTagValue) {
    return this.define(key, Ingredient.of(items.getOrThrow(itemTagValue)));
  }

  public RollingRecipeBuilder define(Character key, ItemLike itemValue) {
    return this.define(key, Ingredient.of(itemValue));
  }

  public RollingRecipeBuilder define(Character key, Ingredient itemValue) {
    if (this.key.containsKey(key)) {
      throw new IllegalArgumentException("Symbol '" + key + "' is already defined!");
    } else if (key == ' ') {
      throw new IllegalArgumentException(
          "Symbol ' ' (whitespace) is reserved and cannot be defined");
    } else {
      this.key.put(key, itemValue);
      return this;
    }
  }

  public RollingRecipeBuilder pattern(String pattern) {
    if (!this.rows.isEmpty() && pattern.length() != this.rows.getFirst().length()) {
      throw new IllegalArgumentException("Pattern must be the same width on every line!");
    } else {
      this.rows.add(pattern);
      return this;
    }
  }

  public void save(RecipeOutput recipeOutput) {
    this.save(recipeOutput, BuiltInRegistries.ITEM.getKey(this.result.item().value()));
  }

  public void save(RecipeOutput recipeOutput, Identifier identifier) {
    var customIdentifier = identifier.withPrefix("rolling/");
    var pattern = ShapedRecipePattern.of(this.key, this.rows);
    var recipe = new RollingRecipe(pattern, this.result, this.processTime);
    recipeOutput.accept(ResourceKey.create(Registries.RECIPE, customIdentifier), recipe, null);
  }
}
