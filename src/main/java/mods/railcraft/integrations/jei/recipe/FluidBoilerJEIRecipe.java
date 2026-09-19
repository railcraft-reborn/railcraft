package mods.railcraft.integrations.jei.recipe;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public record FluidBoilerJEIRecipe(TagKey<Fluid> fuel, Fluid water,
                                   Fluid steam, int temperature) {
}
