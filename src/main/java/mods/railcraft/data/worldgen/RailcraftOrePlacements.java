package mods.railcraft.data.worldgen;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import mods.railcraft.Railcraft;
import mods.railcraft.data.worldgen.RailcraftConfigurableOre.PLACEMENT_TYPE;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;

public class RailcraftOrePlacements {

  public static final RailcraftConfigurableOre TIN_ORE = new RailcraftConfigurableOre(
      ResourceLocation.of(Railcraft.ID + "ore_tin", ':'), // has to use the static constructor for some reason
      List.of(
        Pair.of(OreFeatures.STONE_ORE_REPLACEABLES, RailcraftBlocks.TIN_ORE.getId()),
        Pair.of(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RailcraftBlocks.TIN_ORE.getId())
      ),
      PLACEMENT_TYPE.COMMON,
      9, 30, 136, 319);

}
