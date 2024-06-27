package mods.railcraft.integrations.jei.recipe;

import net.minecraftforge.fluids.FluidStack;

public record FluidBoilerJEIRecipe(FluidStack fuel, FluidStack water,
                                   FluidStack steam, int temperature) {
}
