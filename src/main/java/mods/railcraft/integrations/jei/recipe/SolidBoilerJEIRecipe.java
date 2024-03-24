package mods.railcraft.integrations.jei.recipe;

import net.minecraftforge.fluids.FluidStack;

public record SolidBoilerJEIRecipe(FluidStack water, FluidStack steam, int temperature) {
}
