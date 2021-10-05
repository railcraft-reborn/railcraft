package mods.railcraft.world.level.block;

import java.util.Random;
import mods.railcraft.client.ClientEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class FirestoneBlock extends Block {

  public FirestoneBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void animateTick(BlockState blockState, World level, BlockPos pos, Random rand) {
    super.animateTick(blockState, level, pos, rand);
    BlockPos start = new BlockPos(pos.getX() - 10 + rand.nextInt(20), pos.getY(),
        pos.getZ() - 10 + rand.nextInt(20));
    Vector3d startPosition = new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.8, 0.5);
    Vector3d endPosition =
        new Vector3d(start.getX(), start.getY(), start.getZ()).add(0.5, 0.5, 0.5);
    ClientEffects.INSTANCE.fireSparkEffect(level, startPosition, endPosition);
    this.spawnBurningFaceParticles(level, pos);
  }

  private void spawnBurningFaceParticles(World level, BlockPos pos) {
    Random random = level.getRandom();
    double pixel = 0.0625D;

    BlockState state = level.getBlockState(pos);

    for (Direction facing : Direction.values()) {
      if (!Block.shouldRenderFace(state, level, pos, facing))
        continue;

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
      ParticleManager particleEngine = mc.particleEngine;
      // flame particle
      particleEngine.add(particleEngine.createParticle(
          ParticleTypes.FLAME, px, py, pz, 0.0D, 0.0D, 0.0D));
      // smoke_normal particle
      particleEngine.add(particleEngine.createParticle(
          ParticleTypes.SMOKE, px, py, pz, 0.0D, 0.0D, 0.0D));
    }
  }
}
