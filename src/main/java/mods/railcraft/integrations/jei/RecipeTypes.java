package mods.railcraft.integrations.jei;

import mezz.jei.api.recipe.RecipeType;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import mods.railcraft.world.item.crafting.RollingRecipe;

public class RecipeTypes {

  public static final RecipeType<RollingRecipe> ROLLING_MACHINE =
      RecipeType.create(RailcraftConstants.ID, "rolling_machine", RollingRecipe.class);

  public static final RecipeType<CokeOvenRecipe> COKE_OVEN =
      RecipeType.create(RailcraftConstants.ID, "coke_oven", CokeOvenRecipe.class);

  public static final RecipeType<BlastFurnaceRecipe> BLAST_FURNACE =
      RecipeType.create(RailcraftConstants.ID, "blast_furnace", BlastFurnaceRecipe.class);

  public static final RecipeType<CrusherRecipe> CRUSHER =
      RecipeType.create(RailcraftConstants.ID, "crusher", CrusherRecipe.class);
}
