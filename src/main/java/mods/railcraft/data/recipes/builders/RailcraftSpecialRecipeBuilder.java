package mods.railcraft.data.recipes.builders;

import java.util.function.Function;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;

public class RailcraftSpecialRecipeBuilder extends SpecialRecipeBuilder {

  private final Function<CraftingBookCategory, Recipe<?>> factory;

  public RailcraftSpecialRecipeBuilder(Function<CraftingBookCategory, Recipe<?>> factory) {
    super(factory);
    this.factory = factory;
  }

  public static RailcraftSpecialRecipeBuilder special(Function<CraftingBookCategory, Recipe<?>> factory) {
    return new RailcraftSpecialRecipeBuilder(factory);
  }

  @Override
  public void save(RecipeOutput recipeOutput, String id) {
    recipeOutput.accept(RailcraftConstants.rl(id),
        this.factory.apply(CraftingBookCategory.MISC), null);
  }
}
