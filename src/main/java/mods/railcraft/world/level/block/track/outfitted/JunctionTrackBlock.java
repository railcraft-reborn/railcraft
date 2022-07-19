package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.track.TrackType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class JunctionTrackBlock extends OutfittedTrackBlock {

  public JunctionTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, BlockGetter blockGetter,
      BlockPos blockPos, @Nullable AbstractMinecart cart) {
    if (cart == null) {
      return RailShape.NORTH_SOUTH;
    }

    if (cart.getMotionDirection().getAxis() == Direction.Axis.X) {
      return RailShape.EAST_WEST;
    } else {
      return RailShape.NORTH_SOUTH;
    }
  }
}
