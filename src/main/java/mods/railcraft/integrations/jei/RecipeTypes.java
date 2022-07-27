package mods.railcraft.integrations.jei;

import mezz.jei.api.recipe.RecipeType;
import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.RollingRecipe;

public class RecipeTypes {
  public static final RecipeType<RollingRecipe> ROLLING_MACHINE = RecipeType.create(Railcraft.ID, "rolling_machine", RollingRecipe.class);
}
