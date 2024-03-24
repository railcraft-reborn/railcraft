package mods.railcraft.integrations.jei.recipe;

import net.neoforged.neoforge.fluids.FluidStack;

public record FluidBoilerJEIRecipe(FluidStack fuel, FluidStack water,
                                   FluidStack steam, int temperature) {
}
