package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.WyeTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class WyeTrackBlock extends SwitchTrackBlock implements EntityBlock {

  public WyeTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldBlockState,
      boolean moved) {
    this.adjustShape(state, level, pos);
    super.onPlace(state, level, pos, oldBlockState, moved);
  }

  protected void adjustShape(BlockState blockState, Level level, BlockPos pos) {
    switch (TrackUtil.getRailShapeRaw(blockState)) {
      case NORTH_SOUTH -> {
        var north = pos.north();
        var south = pos.south();
        if (!BaseRailBlock.isRail(level, north) || !BaseRailBlock.isRail(level, south)) {
          return;
        }

        var northShape = TrackUtil.getTrackDirection(level, north);
        var southShape = TrackUtil.getTrackDirection(level, south);

        if (northShape.equals(RailShape.NORTH_SOUTH) && southShape.equals(RailShape.NORTH_SOUTH)) {
          TrackUtil.setRailShape(level, pos, RailShape.EAST_WEST);
        }
      }
      case EAST_WEST -> {
        var east = pos.east();
        var west = pos.west();
        if (!BaseRailBlock.isRail(level, east) || !BaseRailBlock.isRail(level, west)) {
          return;
        }

        var eastShape = TrackUtil.getTrackDirection(level, east);
        var westShape = TrackUtil.getTrackDirection(level, west);

        if (eastShape.equals(RailShape.EAST_WEST) && westShape.equals(RailShape.EAST_WEST)) {
          TrackUtil.setRailShape(level, pos, RailShape.NORTH_SOUTH);
        }
      }
      default -> {}
    }
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, BlockGetter world, BlockPos pos,
      @Nullable AbstractMinecart cart) {
    final boolean switched = isSwitched(blockState);
    return switch (getFacing(blockState)) {
      case NORTH -> switched ? RailShape.SOUTH_WEST : RailShape.SOUTH_EAST;
      case SOUTH -> switched ? RailShape.NORTH_EAST : RailShape.NORTH_WEST;
      case EAST -> switched ? RailShape.NORTH_WEST : RailShape.SOUTH_WEST;
      case WEST -> switched ? RailShape.SOUTH_EAST : RailShape.NORTH_EAST;
      default -> throw new IllegalStateException("Invalid facing direction.");
    };
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new WyeTrackBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : BaseEntityBlock.createTickerHelper(type,
            RailcraftBlockEntityTypes.WYE_TRACK.get(),
            WyeTrackBlockEntity::serverTick);
  }
}
