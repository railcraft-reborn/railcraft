package mods.railcraft.data;

import java.util.Map;
import java.util.stream.Stream;
import com.google.common.collect.Maps;
import mods.railcraft.world.level.block.DecorativeBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

public final class RailcraftBlockFamilies {

  private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();

  private static BlockFamily.Builder familyBuilder(Block baseBlock) {
    BlockFamily.Builder blockfamily$builder = new BlockFamily.Builder(baseBlock);
    BlockFamily blockfamily = MAP.put(baseBlock, blockfamily$builder.getFamily());
    if (blockfamily != null) {
      throw new IllegalStateException("Duplicate family definition for "
          + BuiltInRegistries.BLOCK.getKey(baseBlock));
    } else {
      return blockfamily$builder;
    }
  }

  public static Stream<BlockFamily> getAllFamilies() {
    return MAP.values().stream();
  }

  static {
    for (var type : DecorativeBlock.values()) {
      familyBuilder(RailcraftBlocks.DECORATIVE_BRICKS.pick(type).get())
          .stairs(RailcraftBlocks.DECORATIVE_BRICK_STAIRS.pick(type).get())
          .slab(RailcraftBlocks.DECORATIVE_BRICK_SLAB.pick(type).get());
      familyBuilder(RailcraftBlocks.DECORATIVE_PAVER.pick(type).get())
          .stairs(RailcraftBlocks.DECORATIVE_PAVER_STAIRS.pick(type).get())
          .slab(RailcraftBlocks.DECORATIVE_PAVER_SLAB.pick(type).get());
    }
  }
}
