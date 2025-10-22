package mods.railcraft.data.worldgen;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftStructureSets {
  private static final DeferredRegister<StructureSet> STRUCTURE_SETS =
      DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, RailcraftConstants.ID);

  private static final RegistryObject<StructureSet> GEODE =
      STRUCTURE_SETS.register("geode", RailcraftStructureSets::createGeode);

  private static StructureSet createGeode() {
    return new StructureSet(RailcraftStructures.GEODE.getHolder().get(),
        new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.TRIANGULAR, 73927265));
  }
}
