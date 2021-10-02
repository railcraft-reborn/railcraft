package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.track.SwitchTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.TurnoutTrackBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class TurnoutTrackBlock extends ReversableSwitchTrackBlock {

  public TurnoutTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public SwitchTrackBlockEntity createTileEntity(BlockState state, IBlockReader reader) {
    return new TurnoutTrackBlockEntity();
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, IBlockReader world, BlockPos pos,
      @Nullable AbstractMinecartEntity cart) {
    final TurnoutTrackBlockEntity blockEntity = (TurnoutTrackBlockEntity) world.getBlockEntity(pos);
    final boolean mirrored = isMirrored(blockState);
    final boolean reversed = blockState.getValue(REVERSED);
    RailShape current = getRailShapeRaw(blockState);
    if (cart != null && blockEntity.shouldSwitchForCart(cart)) {
      if (current == RailShape.NORTH_SOUTH) {
        return mirrored
            ? reversed ? RailShape.SOUTH_WEST : RailShape.NORTH_WEST
            : reversed ? RailShape.NORTH_EAST : RailShape.SOUTH_EAST;
      } else if (current == RailShape.EAST_WEST) {
        return mirrored
            ? reversed ? RailShape.NORTH_WEST : RailShape.NORTH_EAST
            : reversed ? RailShape.SOUTH_EAST : RailShape.SOUTH_WEST;
      }
    }
    return current;
  }
}
