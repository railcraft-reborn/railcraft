package mods.railcraft.integrations.jei;

import mezz.jei.api.recipe.RecipeType;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.integrations.jei.recipe.FluidBoilerJEIRecipe;
import mods.railcraft.integrations.jei.recipe.SolidBoilerJEIRecipe;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.crafting.RollingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RecipeTypes {

  public static final RecipeType<RecipeHolder<RollingRecipe>> ROLLING_MACHINE =
      RecipeType.createFromVanilla(RailcraftRecipeTypes.ROLLING.get());

  public static final RecipeType<RecipeHolder<CokeOvenRecipe>> COKE_OVEN =
      RecipeType.createFromVanilla(RailcraftRecipeTypes.COKING.get());

  public static final RecipeType<RecipeHolder<BlastFurnaceRecipe>> BLAST_FURNACE =
      RecipeType.createFromVanilla(RailcraftRecipeTypes.BLASTING.get());

  public static final RecipeType<RecipeHolder<CrusherRecipe>> CRUSHER =
      RecipeType.createFromVanilla(RailcraftRecipeTypes.CRUSHING.get());

  public static final RecipeType<SolidBoilerJEIRecipe> SOLID_BOILER =
      RecipeType.create(RailcraftConstants.ID, "solid_boiler", SolidBoilerJEIRecipe.class);

  public static final RecipeType<FluidBoilerJEIRecipe> FLUID_BOILER =
      RecipeType.create(RailcraftConstants.ID, "fluid_boiler", FluidBoilerJEIRecipe.class);
}
