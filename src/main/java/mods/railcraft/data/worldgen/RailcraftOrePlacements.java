package mods.railcraft.data.worldgen;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import mods.railcraft.data.worldgen.RailcraftConfigurableOre.PLACEMENT_TYPE;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.worldgen.features.OreFeatures;

public class RailcraftOrePlacements {

  public static final RailcraftConfigurableOre TIN_ORE = new RailcraftConfigurableOre(
      "ore_tin",
      List.of(
        Pair.of(OreFeatures.STONE_ORE_REPLACEABLES, RailcraftBlocks.TIN_ORE.getId()),
        Pair.of(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RailcraftBlocks.TIN_ORE.getId())
      ),
      PLACEMENT_TYPE.COMMON,
      9, 30, 136, 319);

}
