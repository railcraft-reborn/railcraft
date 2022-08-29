package mods.railcraft.world.level.block;

import net.minecraft.client.Minecraft;
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
  public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
    super.animateTick(blockState, level, pos, random);
    var start = new BlockPos(
        pos.getX() - 10 + random.nextInt(20),
        pos.getY(),
        pos.getZ() - 10 + random.nextInt(20));
    level.playSound(null, start, SoundEvents.LAVA_POP, SoundSource.BLOCKS,
        0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
    this.spawnBurningFaceParticles(level, pos, random);
  }

  private void spawnBurningFaceParticles(Level level, BlockPos pos, RandomSource random){
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

      var particleEngine = Minecraft.getInstance().particleEngine;
      var flame = particleEngine.createParticle(ParticleTypes.FLAME, px, py, pz, 0.0D, 0.0D, 0.0D);
      var smoke = particleEngine.createParticle(ParticleTypes.SMOKE, px, py, pz, 0.0D, 0.0D, 0.0D);
      particleEngine.add(flame);
      particleEngine.add(smoke);
    }
  }
}
