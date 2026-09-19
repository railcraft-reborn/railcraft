package mods.railcraft.integrations.jei;

import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.integrations.jei.recipe.FluidBoilerJEIRecipe;
import mods.railcraft.integrations.jei.recipe.SolidBoilerJEIRecipe;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.crafting.RollingRecipe;

public class RecipeTypes {

  public static final IRecipeHolderType<RollingRecipe> ROLLING_MACHINE =
      IRecipeType.create(RailcraftRecipeTypes.ROLLING.get());

  public static final IRecipeHolderType<CokeOvenRecipe> COKE_OVEN =
      IRecipeType.create(RailcraftRecipeTypes.COKING.get());

  public static final IRecipeHolderType<BlastFurnaceRecipe> BLAST_FURNACE =
      IRecipeType.create(RailcraftRecipeTypes.BLASTING.get());

  public static final IRecipeHolderType<CrusherRecipe> CRUSHER =
      IRecipeType.create(RailcraftRecipeTypes.CRUSHING.get());

  public static final IRecipeType<SolidBoilerJEIRecipe> SOLID_BOILER =
      IRecipeType.create(RailcraftConstants.ID, "solid_boiler", SolidBoilerJEIRecipe.class);

  public static final IRecipeType<FluidBoilerJEIRecipe> FLUID_BOILER =
      IRecipeType.create(RailcraftConstants.ID, "fluid_boiler", FluidBoilerJEIRecipe.class);
}
