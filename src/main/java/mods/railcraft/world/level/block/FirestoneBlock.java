package mods.railcraft.world.level.block;

import mods.railcraft.client.ClientEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class FirestoneBlock extends Block {

  public FirestoneBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource rand) {
    super.animateTick(blockState, level, pos, rand);
    BlockPos start = new BlockPos(pos.getX() - 10 + rand.nextInt(20), pos.getY(),
        pos.getZ() - 10 + rand.nextInt(20));
    Vec3 startPosition = new Vec3(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.8, 0.5);
    Vec3 endPosition =
        new Vec3(start.getX(), start.getY(), start.getZ()).add(0.5, 0.5, 0.5);
    ClientEffects.INSTANCE.fireSparkEffect(level, startPosition, endPosition);
    this.spawnBurningFaceParticles(level, pos);
  }

  private void spawnBurningFaceParticles(Level level, BlockPos pos) {
    var random = level.getRandom();
    double pixel = 0.0625D;

    BlockState state = level.getBlockState(pos);

    for (Direction facing : Direction.values()) {
      if (!Block.shouldRenderFace(state, level, pos, facing, pos.relative(facing))) {
        continue;
      }

      double px = pos.getX();
      double py = pos.getY();
      double pz = pos.getZ();
      double positiveAxis =
          (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? 1.0 : 0.0;

      if (facing.getAxis() == Direction.Axis.X)
        px += pixel * facing.getStepX() + positiveAxis;
      else
        px += random.nextFloat();

      if (facing.getAxis() == Direction.Axis.Y)
        py += pixel * facing.getStepY() + positiveAxis;
      else
        py += random.nextFloat();

      if (facing.getAxis() == Direction.Axis.Z)
        pz += pixel * facing.getStepZ() + positiveAxis;
      else
        pz += random.nextFloat();

      Minecraft mc = Minecraft.getInstance();
      ParticleEngine particleEngine = mc.particleEngine;
      // flame particle
      particleEngine.add(particleEngine.createParticle(
          ParticleTypes.FLAME, px, py, pz, 0.0D, 0.0D, 0.0D));
      // smoke_normal particle
      particleEngine.add(particleEngine.createParticle(
          ParticleTypes.SMOKE, px, py, pz, 0.0D, 0.0D, 0.0D));
    }
  }
}
