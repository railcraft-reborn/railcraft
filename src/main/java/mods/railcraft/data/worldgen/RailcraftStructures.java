package mods.railcraft.data.worldgen;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.levelgen.structure.GeodeStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

public class RailcraftStructures {

  public static final ResourceKey<Structure> GEODE = ResourceKey
      .create(Registries.STRUCTURE, RailcraftConstants.id("geode"));

  public static void bootstrap(BootstrapContext<Structure> context) {
    var holdergetter = context.lookup(Registries.BIOME);
    context.register(GEODE, new GeodeStructure(
        new Structure.StructureSettings.Builder(holdergetter.getOrThrow(BiomeTags.IS_DEEP_OCEAN))
            .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
            .terrainAdapation(TerrainAdjustment.NONE).build()));
  }
}
