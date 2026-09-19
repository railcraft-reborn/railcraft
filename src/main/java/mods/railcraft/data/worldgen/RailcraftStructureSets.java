package mods.railcraft.data.worldgen;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class RailcraftStructureSets {

  private static final ResourceKey<StructureSet> GEODE = ResourceKey
      .create(Registries.STRUCTURE_SET, RailcraftConstants.id("geode"));

  public static void bootstrap(BootstrapContext<StructureSet> context) {
    var holdergetter = context.lookup(Registries.STRUCTURE);
    context.register(GEODE, new StructureSet(holdergetter.getOrThrow(RailcraftStructures.GEODE),
        new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.TRIANGULAR, 73927265)));
  }
}
