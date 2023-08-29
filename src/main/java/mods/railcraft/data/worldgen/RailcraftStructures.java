package mods.railcraft.data.worldgen;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.levelgen.structure.GeodeStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Structures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

public class RailcraftStructures {

  public static final ResourceKey<Structure> GEODE = ResourceKey
      .create(Registries.STRUCTURE, new ResourceLocation(Railcraft.ID, "geode"));

  public static void bootstrap(BootstapContext<Structure> context) {
    var holdergetter = context.lookup(Registries.BIOME);

    context.register(GEODE, new GeodeStructure(Structures
        .structure(holdergetter.getOrThrow(BiomeTags.IS_DEEP_OCEAN),
            GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
  }
}
