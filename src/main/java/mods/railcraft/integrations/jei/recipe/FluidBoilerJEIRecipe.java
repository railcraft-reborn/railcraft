package mods.railcraft.integrations.jei.recipe;

import net.minecraft.world.level.material.Fluid;

public record FluidBoilerJEIRecipe(Fluid fuel, float heatValuePerBucket, Fluid water,
                                   Fluid steam, int temperature) {
}
