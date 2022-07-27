package mods.railcraft.charge;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.sounds.RailcraftSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class ZapEffectProviderImpl implements Charge.ZapEffectProvider {

  public static final short TRACKING_DISTANCE = 32 * 32;

  @Override
  public void zapEffectPoint(Level level, double x, double y, double z) {
    var rand = level.getRandom();
    level.addParticle(RailcraftParticleTypes.SPARK.get(), x, y, z,
        rand.nextDouble() - 0.5D,
        rand.nextDouble() - 0.5D,
        rand.nextDouble() - 0.5D);

    level.playLocalSound(x, y, z, RailcraftSoundEvents.ZAP.get(),
        SoundSource.BLOCKS, 0.2F, 0.75F, false);
  }

  @Override
  public void zapEffectSurface(BlockState blockState, Level level, BlockPos pos) {
    var rand = level.getRandom();

    level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), RailcraftSoundEvents.ZAP.get(),
        SoundSource.BLOCKS,
        0.1F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);

    for (var side : Direction.values()) {
      if (!Block.shouldRenderFace(blockState, level, pos, side, pos.relative(side))) {
        continue;
      }
      var normal = Vec3.atLowerCornerOf(side.getNormal());
      var variance = new Vec3((rand.nextGaussian() - 0.5) * 0.2, (rand.nextGaussian() - 0.5) * 0.2,
          (rand.nextGaussian() - 0.5) * 0.2);
      var vel = normal.add(variance);
      // TODO This should probably use the bounding box or something.
      // Its got to be wrong for tracks atm.
      var start =
          new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).add(normal.scale(0.5));
      switch (side.getAxis()) {
        case X:
          start = start.add(new Vec3(0.0, rand.nextDouble() - 0.5, rand.nextDouble() - 0.5));
          break;
        case Y:
          start = start.add(new Vec3(rand.nextDouble() - 0.5, 0.0, rand.nextDouble() - 0.5));
          break;
        case Z:
          start = start.add(new Vec3(rand.nextDouble() - 0.5, rand.nextDouble() - 0.5, 0.0));
          break;
        default:
          break;
      }
      level.addParticle(
          RailcraftParticleTypes.SPARK.get(), start.x(), start.y(), start.z(),
          vel.x(), vel.y(), vel.z());
    }
  }

  @Override
  public void zapEffectDeath(Level level, double x, double y, double z) {
    level.playSound(null, x, y, z, RailcraftSoundEvents.ZAP.get(),
        SoundSource.BLOCKS, 3F, 0.75F);

    for (int i = 0; i < 20; i++) {
      level.addParticle(RailcraftParticleTypes.SPARK.get(), x, y, z,
          level.getRandom().nextDouble() - 0.5D,
          level.getRandom().nextDouble() - 0.5D,
          level.getRandom().nextDouble() - 0.5D);
    }
  }
}
