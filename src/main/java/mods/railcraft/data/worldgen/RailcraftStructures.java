package mods.railcraft.data.worldgen;

import java.util.Map;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.levelgen.structure.GeodeStructure;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftStructures {
  private static final DeferredRegister<Structure> STRUCTURES =
      DeferredRegister.create(Registry.STRUCTURE_REGISTRY, RailcraftConstants.ID);

  public static final RegistryObject<Structure> GEODE =
      STRUCTURES.register("geode", RailcraftStructures::createGeode);

  private static Structure createGeode() {
    var biomes = BuiltinRegistries.BIOME.getOrCreateTag(BiomeTags.IS_DEEP_OCEAN);
    return new GeodeStructure(new Structure.StructureSettings(
        biomes,
        Map.of(),
        GenerationStep.Decoration.SURFACE_STRUCTURES,
        TerrainAdjustment.NONE
    ));
  }
}
