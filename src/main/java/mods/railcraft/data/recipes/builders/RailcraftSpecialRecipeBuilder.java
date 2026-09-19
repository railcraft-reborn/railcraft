package mods.railcraft.data.recipes.builders;

import java.util.function.Supplier;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

public class RailcraftSpecialRecipeBuilder extends SpecialRecipeBuilder {

  private final Supplier<Recipe<?>> factory;

  private RailcraftSpecialRecipeBuilder(Supplier<Recipe<?>> factory) {
    super(factory);
    this.factory = factory;
  }

  public static RailcraftSpecialRecipeBuilder special(Supplier<Recipe<?>> factory) {
    return new RailcraftSpecialRecipeBuilder(factory);
  }

  @Override
  public void save(RecipeOutput recipeOutput, String id) {
    var resourceKey = ResourceKey.create(Registries.RECIPE, RailcraftConstants.id(id));
    recipeOutput.accept(resourceKey, this.factory.get(), null);
  }
}
