package mods.railcraft.world.item.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.item.crafting.IRecipeType;

public class RailcraftRecipeTypes {

  public static final IRecipeType<RollingRecipe> ROLLING =
      IRecipeType.register(Railcraft.ID + ":rolling");

  public static final IRecipeType<CokeOvenRecipe> COKEING =
      IRecipeType.register(Railcraft.ID + ":cokeing");
}
