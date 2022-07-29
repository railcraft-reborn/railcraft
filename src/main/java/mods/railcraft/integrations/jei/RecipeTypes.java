package mods.railcraft.integrations.jei;

import mezz.jei.api.recipe.RecipeType;
import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.RollingRecipe;

public class RecipeTypes {

  public static final RecipeType<RollingRecipe> ROLLING_MACHINE = RecipeType.create(Railcraft.ID,
      "rolling_machine", RollingRecipe.class);
  public static final RecipeType<CokeOvenRecipe> COKE_OVEN = RecipeType.create(Railcraft.ID,
      "coke_oven", CokeOvenRecipe.class);
  public static final RecipeType<BlastFurnaceRecipe> BLAST_FURNACE = RecipeType.create(Railcraft.ID,
      "blast_furnace", BlastFurnaceRecipe.class);
}
