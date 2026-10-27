package mods.railcraft.world.level.levelgen.structure;

import java.util.ArrayList;
import java.util.List;
import com.mojang.datafixers.util.Pair;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class ComponentWorkshop {

  private static final List<String> VILLAGES =
      List.of("plains", "desert", "savanna", "snowy", "taiga");

  private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY =
      ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.withDefaultNamespace("empty"));

  /**
   * Builds the workshop out of sandstone, to match the desert village houses.
   */
  public static final ResourceKey<StructureProcessorList> DESERT_WORKSHOP =
      ResourceKey.create(Registries.PROCESSOR_LIST, RailcraftConstants.id("desert_workshop"));

  /**
   * Adds the building to the targeted pool. We will call this in addNewVillageBuilding method
   * further down to add to every village.
   * <p>
   * Note: This is an additive operation which means multiple mods can do this, and they stack with
   * each other safely.
   */
  private static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry,
      Registry<StructureProcessorList> processorListRegistry,
      Identifier templatePoolName, Identifier newStructureName,
      ResourceKey<StructureProcessorList> processorListKey, int frequency) {

    Holder<StructureProcessorList> processorList = processorListRegistry
        .getOrThrow(processorListKey);

    var optionalPool = templatePoolRegistry.get(templatePoolName);
    if (optionalPool.isEmpty()) {
      return;
    }
    var pool = optionalPool.get().value();

    var piece = SinglePoolElement
        .legacy(newStructureName.toString(), processorList)
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

    for (var village : VILLAGES) {
      var houses = Identifier.withDefaultNamespace("village/" + village + "/houses");
      var processorList = village.equals("desert") ? DESERT_WORKSHOP : EMPTY_PROCESSOR_LIST_KEY;
      addBuildingToPool(templatePoolRegistry, processorListRegistry, houses,
          RailcraftConstants.id("component_workshop_cartman"), processorList, 3);
      addBuildingToPool(templatePoolRegistry, processorListRegistry, houses,
          RailcraftConstants.id("component_workshop_trackman"), processorList, 4);
    }
  }
}
