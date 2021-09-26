package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.tracks.TrackType;
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
    final WyeTrackBlockEntity blockEntity = (WyeTrackBlockEntity) world.getBlockEntity(pos);
    final boolean mirrored = isMirrored(blockState);
    RailShape dir = getRailShapeRaw(blockState);
    if (cart != null) {
      if (dir == RailShape.NORTH_SOUTH) {
        dir = mirrored
            ? blockEntity.shouldSwitchForCart(cart) ? RailShape.NORTH_WEST : RailShape.SOUTH_WEST
            : blockEntity.shouldSwitchForCart(cart) ? RailShape.SOUTH_EAST : RailShape.NORTH_EAST;
      } else if (dir == RailShape.EAST_WEST) {
        dir = mirrored
            ? blockEntity.shouldSwitchForCart(cart) ? RailShape.NORTH_EAST : RailShape.NORTH_WEST
            : blockEntity.shouldSwitchForCart(cart) ? RailShape.SOUTH_WEST : RailShape.SOUTH_EAST;
      }
    }
    return dir;
  }
}
