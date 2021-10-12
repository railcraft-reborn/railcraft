package mods.railcraft.world.item.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.item.crafting.IRecipeType;

public class RailcraftRecipeTypes {

  public static final IRecipeType<RollingRecipe> ROLLING =
      IRecipeType.register(Railcraft.ID + ":rolling");

  public static final IRecipeType<CokeOvenRecipe> COKE_OVEN_COOKING =
      IRecipeType.register(Railcraft.ID + ":coke_oven_cooking");
}
