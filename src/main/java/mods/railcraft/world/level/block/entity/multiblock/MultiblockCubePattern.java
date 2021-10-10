package mods.railcraft.world.level.block.entity.multiblock;

import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mods.railcraft.world.level.block.RailcraftBlocks;

public class MultiblockCubePattern {

  public static final Logger MULTIBLOCK_LOGGER =
      LogManager.getLogger("Railcraft/MultiblockCubePattern");
  private Vector3i size;
  private Predicate<Vector3i> validator;
  private World world;

  MultiblockCubePattern(Vector3i size, World world, Predicate<Vector3i> validator) {
    this.size = size;
    this.validator = validator;
    this.world = world;
  }

  public boolean validate(BlockPos originPos, BlockPos normal) {
    // int i = Math.max(Math.max(this.width, this.height), this.depth);
    int box =  Math.max(Math.max(this.size.getX(), this.size.getY()), this.size.getZ()) - 1;
    // BlockPos offset = originPos;
    originPos = originPos.offset(
      (normal.getX() == 0) ? -1 : 0,
      0,
      (normal.getZ() == 0) ? -1 : 0);
    int i = 0;

    for (BlockPos blockpos : BlockPos.betweenClosed(
        // 3 - 1
        originPos, originPos.offset(
          (normal.getX() == 0) ? box : normal.getX() * box,
          2,
          (normal.getZ() == 0) ? box : normal.getZ() * box))) {
      i++;
      MULTIBLOCK_LOGGER.info(
          "SCANNING:"
          + " (" + blockpos.toShortString() + ") "
          + "WITH OFFSET: "
          + this.world.getBlockState(blockpos).getBlock().toString()
          + " ITER:" + i);

      Minecraft.getInstance().particleEngine.add(
          Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.LARGE_SMOKE,
          blockpos.getX() + 0.5d, blockpos.getY() + 0.5d, blockpos.getZ() + 0.5d,
          0, 0, 0));
      Block theBlock = this.world.getBlockState(blockpos).getBlock();
      if (i == 14 && !theBlock.is(Blocks.AIR)) {
        MULTIBLOCK_LOGGER.info("NOT AIR");
        return false;
      } else if (i != 14 && !theBlock.is(RailcraftBlocks.COKE_OVEN_BLOCK.get())) {
        return false;
      }
    }

    return true;
  }
}
