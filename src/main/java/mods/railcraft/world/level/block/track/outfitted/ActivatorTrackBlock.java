package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ActivatorTrackBlock extends PoweredOutfittedTrackBlock {

  private static final int POWER_PROPAGATION = 8;

  public ActivatorTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void onMinecartPass(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart) {
    cart.activateMinecart(pos.getX(), pos.getY(), pos.getZ(),
        this.isPowered(blockState, level, pos));
  }

  @Override
  public int getPowerPropagation(BlockState blockState, World level, BlockPos pos) {
    return POWER_PROPAGATION;
  }
}
