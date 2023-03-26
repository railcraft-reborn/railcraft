package mods.railcraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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

      level.addParticle(ParticleTypes.FLAME, px, py, pz, 0, 0, 0);
      level.addParticle(ParticleTypes.SMOKE, px, py, pz, 0, 0, 0);
    }
  }

  @Override
  public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
    if (entity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) {
      entity.hurt(level.damageSources().hotFloor(), 1.5F);
    }
    super.stepOn(level, pos, state, entity);
  }
}
