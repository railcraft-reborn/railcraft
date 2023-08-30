package mods.railcraft.world.level.levelgen.structure;

import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class GeodeStructurePiece extends StructurePiece {

  private static final int DISTANCE_OUTER_SQ = 8 * 8;
  private static final int DISTANCE_ORE_SQ = 5 * 5;
  private static final int DISTANCE_INNER_SQ = 4 * 4;

  public GeodeStructurePiece(int minX, int y, int minZ) {
    super(RailcraftStructurePieces.GEODE.get(), 0, createBoundingBox(minX, y, minZ));
    this.setOrientation(null);
  }

  public GeodeStructurePiece(CompoundTag tag) {
    super(RailcraftStructurePieces.GEODE.get(), tag);
  }

  private static BoundingBox createBoundingBox(int minX, int y, int minZ) {
    return new BoundingBox(minX, y, minZ, minX + 16, y + 16, minZ + 16);
  }

  @Override
  protected void addAdditionalSaveData(StructurePieceSerializationContext context,
      CompoundTag tag) {
  }

  @Override
  public void postProcess(WorldGenLevel level, StructureManager structureManager,
      ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos,
      BlockPos pos) {
    for (int x = -8; x < 8; x++) {
      for (int y = -8; y < 8; y++) {
        for (int z = -8; z < 8; z++) {
          int distSq = x * x + y * y + z * z;
          var targetPos = pos.offset(x, 9 + y, z);
          if (distSq <= DISTANCE_INNER_SQ) {
            placeAir(level, targetPos);
          } else if (distSq <= DISTANCE_OUTER_SQ) {
            placeStone(level, targetPos, random);
          }
          var existingState = level.getBlockState(targetPos);
          if (distSq > DISTANCE_INNER_SQ && distSq <= DISTANCE_ORE_SQ) {
            placeOre(level, existingState, random, targetPos);
          }
        }
      }
    }
  }

  private void placeAir(WorldGenLevel level, BlockPos pos) {
    this.placeBlock(level, Blocks.AIR.defaultBlockState(), pos.getX(), pos.getY(), pos.getZ(),
        this.getBoundingBox());
  }

  private void placeStone(WorldGenLevel level, BlockPos pos, RandomSource random) {
    var state = random.nextDouble() < 0.2
        ? random.nextDouble() < 0.5
          ? Blocks.DEEPSLATE.defaultBlockState()
          : Blocks.SMOOTH_BASALT.defaultBlockState()
        : RailcraftBlocks.ABYSSAL_STONE.get().defaultBlockState();
    this.placeBlock(level, state, pos.getX(), pos.getY(), pos.getZ(), this.getBoundingBox());
  }

  private void placeOre(WorldGenLevel level, BlockState existingState, RandomSource random,
      BlockPos pos) {
    if (existingState.is(RailcraftBlocks.ABYSSAL_STONE.get())) {
      double chance = random.nextDouble();
      BlockState oreState = null;
      if (chance <= 0.004) {
        oreState = Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState();
      } else if (chance <= 0.008) {
        oreState = Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState();
      } else if (chance <= 0.02) {
        oreState = Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState();
      }
      if (oreState != null) {
        this.placeBlock(level, oreState, pos.getX(), pos.getY(), pos.getZ(), this.getBoundingBox());
      }
    }
  }
}
