package mods.railcraft.world.item.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.world.item.crafting.RecipeType;

public class RailcraftRecipeTypes {

  public static final RecipeType<RollingRecipe> ROLLING =
      RecipeType.register(Railcraft.ID + ":rolling");

  public static final RecipeType<CokeOvenRecipe> COKING =
      RecipeType.register(Railcraft.ID + ":coking");

  public static final RecipeType<BlastFurnaceRecipe> BLASTING =
      RecipeType.register(Railcraft.ID + ":blasting");

  // Dummy method to force this class to be class loaded.
  public static void init() {}
}
