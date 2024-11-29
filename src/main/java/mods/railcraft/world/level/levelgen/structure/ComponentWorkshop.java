package mods.railcraft.world.level.levelgen.structure;

import java.util.ArrayList;
import com.mojang.datafixers.util.Pair;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class ComponentWorkshop {

  private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY =
      ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty"));

  /**
   * Adds the building to the targeted pool. We will call this in addNewVillageBuilding method
   * further down to add to every village.
   * <p>
   * Note: This is an additive operation which means multiple mods can do this, and they stack with
   * each other safely.
   */
  private static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry,
      Registry<StructureProcessorList> processorListRegistry,
      ResourceLocation templatePoolName, ResourceLocation newStructureName, int frequency) {

    Holder<StructureProcessorList> emptyProcessorList = processorListRegistry
        .getOrThrow(EMPTY_PROCESSOR_LIST_KEY);

    var optionalPool = templatePoolRegistry.get(templatePoolName);
    if (optionalPool.isEmpty()) {
      return;
    }
    var pool = optionalPool.get().value();

    var piece = SinglePoolElement
        .legacy(newStructureName.toString(), emptyProcessorList)
        .apply(StructureTemplatePool.Projection.RIGID);

    for (int i = 0; i < frequency; i++) {
      pool.templates.add(piece);
    }

    var listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
    listOfPieceEntries.add(new Pair<>(piece, frequency));
    pool.rawTemplates = listOfPieceEntries;
  }

  public static void addVillageStructures(RegistryAccess.Frozen registryAccess) {
    var templatePoolRegistry = registryAccess.lookup(Registries.TEMPLATE_POOL).orElseThrow();
    var processorListRegistry = registryAccess.lookup(Registries.PROCESSOR_LIST).orElseThrow();

    addBuildingToPool(templatePoolRegistry, processorListRegistry,
        ResourceLocation.withDefaultNamespace("village/plains/houses"),
        RailcraftConstants.rl("component_workshop_cartman"), 4);

    addBuildingToPool(templatePoolRegistry, processorListRegistry,
        ResourceLocation.withDefaultNamespace("village/plains/houses"),
        RailcraftConstants.rl("component_workshop_trackman"), 6);
  }
}
