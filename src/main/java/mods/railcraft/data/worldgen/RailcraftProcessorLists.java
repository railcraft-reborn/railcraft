package mods.railcraft.data.worldgen;

import java.util.List;
import java.util.Map;
import mods.railcraft.world.level.levelgen.structure.BlockSwapProcessor;
import mods.railcraft.world.level.levelgen.structure.ComponentWorkshop;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class RailcraftProcessorLists {

  public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
    context.register(ComponentWorkshop.DESERT_WORKSHOP,
        new StructureProcessorList(List.of(new BlockSwapProcessor(Map.of(
            Blocks.BRICKS, Blocks.CUT_SANDSTONE,
            Blocks.STONE_BRICKS, Blocks.CUT_SANDSTONE,
            Blocks.MOSSY_STONE_BRICKS, Blocks.SANDSTONE,
            Blocks.CRACKED_STONE_BRICKS, Blocks.SANDSTONE,
            Blocks.SMOOTH_STONE, Blocks.SMOOTH_SANDSTONE,
            Blocks.BRICK_STAIRS, Blocks.SANDSTONE_STAIRS,
            Blocks.STONE_BRICK_STAIRS, Blocks.SANDSTONE_STAIRS)))));
  }
}
