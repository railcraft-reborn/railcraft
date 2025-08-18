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
  private static final BlockFamily ABYSSAL_DECORATIVE_BRICKS;
  private static final BlockFamily QUARRIED_DECORATIVE_BRICKS;
  private static final BlockFamily ABYSSAL_DECORATIVE_PAVER;
  private static final BlockFamily QUARRIED_DECORATIVE_PAVER;

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
    ABYSSAL_DECORATIVE_BRICKS =
        familyBuilder(RailcraftBlocks.DECORATIVE_BRICKS.variantFor(DecorativeBlock.ABYSSAL).get())
            .stairs(RailcraftBlocks.DECORATIVE_BRICK_STAIRS.variantFor(DecorativeBlock.ABYSSAL).get())
            .slab(RailcraftBlocks.DECORATIVE_BRICK_SLAB.variantFor(DecorativeBlock.ABYSSAL).get())
            .getFamily();

    QUARRIED_DECORATIVE_BRICKS =
        familyBuilder(RailcraftBlocks.DECORATIVE_BRICKS.variantFor(DecorativeBlock.QUARRIED).get())
            .stairs(RailcraftBlocks.DECORATIVE_BRICK_STAIRS.variantFor(DecorativeBlock.QUARRIED).get())
            .slab(RailcraftBlocks.DECORATIVE_BRICK_SLAB.variantFor(DecorativeBlock.QUARRIED).get())
            .getFamily();


    ABYSSAL_DECORATIVE_PAVER =
        familyBuilder(RailcraftBlocks.DECORATIVE_PAVER.variantFor(DecorativeBlock.ABYSSAL).get())
            .stairs(RailcraftBlocks.DECORATIVE_PAVER_STAIRS.variantFor(DecorativeBlock.ABYSSAL).get())
            .slab(RailcraftBlocks.DECORATIVE_PAVER_SLAB.variantFor(DecorativeBlock.ABYSSAL).get())
            .getFamily();

    QUARRIED_DECORATIVE_PAVER =
        familyBuilder(RailcraftBlocks.DECORATIVE_PAVER.variantFor(DecorativeBlock.QUARRIED).get())
            .stairs(RailcraftBlocks.DECORATIVE_PAVER_STAIRS.variantFor(DecorativeBlock.QUARRIED).get())
            .slab(RailcraftBlocks.DECORATIVE_PAVER_SLAB.variantFor(DecorativeBlock.QUARRIED).get())
            .getFamily();
  }
}
