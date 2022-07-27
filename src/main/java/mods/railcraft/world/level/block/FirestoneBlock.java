package mods.railcraft.world.level.block;

import mods.railcraft.particle.RailcraftParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
    var start = new BlockPos(
        pos.getX() - 10 + rand.nextInt(20),
        pos.getY(),
        pos.getZ() - 10 + rand.nextInt(20));
    level.playSound(null, start, SoundEvents.LAVA_POP, SoundSource.BLOCKS,
        0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F);
    level.addParticle(RailcraftParticleTypes.SPARK.get(),
        pos.getX(), pos.getY(), pos.getZ(),
        start.getX() + 0.5D, start.getY() + 0.5D, start.getZ() + 0.5D);
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
