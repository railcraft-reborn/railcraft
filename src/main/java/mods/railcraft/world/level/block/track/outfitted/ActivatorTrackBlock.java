package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ActivatorTrackBlock extends PoweredOutfittedTrackBlock {

  private static final int POWER_PROPAGATION = 8;

  public ActivatorTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    if (level instanceof ServerLevel serverLevel) {
      cart.activateMinecart(serverLevel, pos.getX(), pos.getY(), pos.getZ(),
          this.isPowered(blockState, level, pos));
    }
  }

  @Override
  public int getPowerPropagation(BlockState blockState, Level level, BlockPos pos) {
    return POWER_PROPAGATION;
  }
}
