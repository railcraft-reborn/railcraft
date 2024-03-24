package mods.railcraft.integrations.jei.recipe;

import net.neoforged.neoforge.fluids.FluidStack;

public record SolidBoilerJEIRecipe(FluidStack water, FluidStack steam, int temperature) {
}
