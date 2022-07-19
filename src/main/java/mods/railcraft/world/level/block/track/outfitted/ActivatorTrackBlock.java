package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ActivatorTrackBlock extends PoweredOutfittedTrackBlock {

  private static final int POWER_PROPAGATION = 8;

  public ActivatorTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    cart.activateMinecart(pos.getX(), pos.getY(), pos.getZ(),
        this.isPowered(blockState, level, pos));
  }

  @Override
  public int getPowerPropagation(BlockState blockState, Level level, BlockPos pos) {
    return POWER_PROPAGATION;
  }
}
