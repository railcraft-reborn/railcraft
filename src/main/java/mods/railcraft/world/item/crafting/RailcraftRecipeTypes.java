package mods.railcraft.world.item.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.world.item.crafting.RecipeType;

public class RailcraftRecipeTypes {

  public static final RecipeType<RollingRecipe> ROLLING =
      RecipeType.register(Railcraft.ID + ":rolling");

  public static final RecipeType<CokeOvenRecipe> COKE_OVEN_COOKING =
      RecipeType.register(Railcraft.ID + ":coke_oven_cooking");
}
