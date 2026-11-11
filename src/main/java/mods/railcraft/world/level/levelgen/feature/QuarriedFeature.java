package mods.railcraft.world.level.levelgen.feature;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.levelgen.feature.configuration.QuarriedConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public class QuarriedFeature implements Feature {

  private static final int DISTANCE_OUTER_SQ = 8 * 8;

  public static final MapCodec<QuarriedFeature> CODEC = QuarriedConfiguration.CODEC.fieldOf("config")
      .xmap(QuarriedFeature::new, feature -> feature.config);

  private final QuarriedConfiguration config;

  public QuarriedFeature(QuarriedConfiguration config) {
    this.config = config;
  }

  @Override
  public MapCodec<QuarriedFeature> codec() {
    return CODEC;
  }

  @Override
  public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos position) {
    var origin = level
        .getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position)
        .below(3);
    if (!level.getBlockState(origin).is(BlockTags.DIRT))
      return false;

    final var currentPos = new BlockPos.MutableBlockPos();

    boolean clearTop = true;
    for (int x = -8; x < 8; x++) {
      for (int z = -8; z < 8; z++) {
        for (int y = 1; y < 4 && y + origin.getY() < level.getHeight() - 1; y++) {
          final int distSq = x * x + z * z;
          if (distSq > DISTANCE_OUTER_SQ)
            continue;

          currentPos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
          if (level.getBlockState(currentPos).getBlock() instanceof LiquidBlock)
            clearTop = false;
        }
      }
    }

    if (clearTop) {
      for (int x = -8; x < 8; x++) {
        for (int z = -8; z < 8; z++) {
          for (int y = 1; y < 4 && y + origin.getY() < level.getHeight() - 1; y++) {
            final int distSq = x * x + z * z;
            if (distSq > DISTANCE_OUTER_SQ)
              continue;

            currentPos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
            final BlockState existingState = level.getBlockState(currentPos);
            if (!placeAir(existingState, level, currentPos))
              break;
          }
        }
      }
    }

    for (int x = -8; x < 8; x++) {
      for (int z = -8; z < 8; z++) {
        for (int y = -8; y < 1 && y + origin.getY() < level.getHeight() - 1; y++) {
          final int distSq = x * x + z * z + y * y;
          if (distSq > DISTANCE_OUTER_SQ)
            continue;

          currentPos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
          final BlockState existingState = level.getBlockState(currentPos);
          placeState(existingState, level, currentPos, random, this.config);
        }
      }
    }
    return true;
  }

  private boolean placeAir(BlockState existingState, WorldGenLevel level, BlockPos blockPos) {
    var above = blockPos.above();
    if (!level.isEmptyBlock(above) || (existingState.getBlock() instanceof LiquidBlock))
      return false;

    for (var direction : Direction.Plane.HORIZONTAL) {
      var target = above.relative(direction);

      if (!level.isEmptyBlock(target))
        return false;
    }

    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
    return true;
  }

  private void placeState(BlockState existingState, WorldGenLevel level, BlockPos blockPos,
      RandomSource random, QuarriedConfiguration config) {
    var above = blockPos.above();
    if (level.getBlockState(above).is(Blocks.SHORT_GRASS))
      level.setBlock(above, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);

    if (config.targetProvider().test(existingState, blockPos, random))
      level.setBlock(blockPos, config.stateProvider().getState(level, random, blockPos),
          Block.UPDATE_CLIENTS);
  }
}
