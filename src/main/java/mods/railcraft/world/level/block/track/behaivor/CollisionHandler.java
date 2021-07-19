package mods.railcraft.world.level.block.track.behaivor;

import mods.railcraft.api.charge.Charge;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 8/7/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public enum CollisionHandler {

  NULL,
  ELECTRIC {
    @Override
    public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
      if (entity instanceof LivingEntity)
        Charge.distribution.network(world).access(pos).zap(entity, Charge.DamageOrigin.TRACK, 2F);
    }
  };

  public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {}
}
