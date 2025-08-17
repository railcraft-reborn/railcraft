package mods.railcraft.integrations.jei.recipe;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public record FluidBoilerJEIRecipe(FluidIngredient fuel, Fluid water,
                                   Fluid steam, int temperature) {
}
