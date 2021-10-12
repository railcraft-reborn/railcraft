package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.track.SwitchTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.WyeTrackBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class WyeTrackBlock extends SwitchTrackBlock {

  public WyeTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public SwitchTrackBlockEntity createTileEntity(BlockState state, IBlockReader reader) {
    return new WyeTrackBlockEntity();
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, IBlockReader world, BlockPos pos,
      @Nullable AbstractMinecartEntity cart) {
    final boolean switched = isSwitched(blockState);
    switch (getFacing(blockState)) {
      case NORTH:
        return switched ? RailShape.SOUTH_WEST : RailShape.SOUTH_EAST;
      case SOUTH:
        return switched ? RailShape.NORTH_EAST : RailShape.NORTH_WEST;
      case EAST:
        return switched ? RailShape.NORTH_WEST : RailShape.SOUTH_WEST;
      case WEST:
        return switched ? RailShape.SOUTH_EAST : RailShape.NORTH_EAST;
      default:
        throw new IllegalStateException("Invalid facing direction.");
    }
  }
}
