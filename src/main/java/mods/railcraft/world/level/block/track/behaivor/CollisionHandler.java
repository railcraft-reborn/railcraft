package mods.railcraft.world.level.block.track.behaivor;

import mods.railcraft.api.charge.Charge;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Created by CovertJaguar on 8/7/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public enum CollisionHandler {

  NULL,
  ELECTRIC {
    @Override
    public void entityInside(Level level, BlockPos pos, BlockState blockState, Entity entity) {
      if (entity instanceof LivingEntity)
        Charge.distribution.network(level).access(pos).zap(entity, Charge.DamageOrigin.TRACK, 2F);
    }
  };

  public void entityInside(Level level, BlockPos pos, BlockState blockState, Entity entity) {}
}
