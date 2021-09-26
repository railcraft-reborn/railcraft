package mods.railcraft.world.level.block.track.kit;

import mods.railcraft.api.tracks.TrackKit;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;

public class ActivatorTrackKit extends PoweredTrackKit {

  private static final int POWER_PROPAGATION = 8;

  @Override
  public TrackKit getTrackKit() {
    return TrackKits.ACTIVATOR.get();
  }

  @Override
  public void onMinecartPass(AbstractMinecartEntity cart) {
    BlockPos pos = getPos();
    cart.activateMinecart(pos.getX(), pos.getY(), pos.getZ(), isPowered());
  }

  @Override
  public int getPowerPropagation() {
    return POWER_PROPAGATION;
  }
}
