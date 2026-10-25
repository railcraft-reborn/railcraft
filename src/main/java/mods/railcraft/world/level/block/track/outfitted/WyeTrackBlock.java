package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.WyeTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.context.BlockPlaceContext;
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
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var level = context.getLevel();
    var blockPos = context.getClickedPos();
    var blockState = super.getStateForPlacement(context);
    var tracks = Direction.Plane.HORIZONTAL.stream()
        .filter(direction -> BaseRailBlock.isRail(level, blockPos.relative(direction)))
        .toList();
    // Whatever the shape of the neighbouring tracks, the wye faces away from a lone trunk or
    // towards the only free side
    if (tracks.size() == 1) {
      return this.withFacing(blockState, tracks.getFirst().getOpposite());
    }
    if (tracks.size() == 3) {
      return this.withFacing(blockState, Direction.Plane.HORIZONTAL.stream()
          .filter(direction -> !tracks.contains(direction))
          .findFirst()
          .orElseThrow());
    }
    blockState = blockState.setValue(SHAPE,
        this.determineShape(level, blockPos, getRailShapeRaw(blockState)));
    // The branch is behind the facing, so turn around if it is only in front
    var facing = getFacing(blockState);
    if (BaseRailBlock.isRail(level, blockPos.relative(facing))
        && !BaseRailBlock.isRail(level, blockPos.relative(facing.getOpposite()))) {
      blockState = blockState.cycle(REVERSED);
    }
    return blockState;
  }

  private BlockState withFacing(BlockState blockState, Direction facing) {
    return blockState
        .setValue(SHAPE, facing.getAxis() == Direction.Axis.X
            ? RailShape.EAST_WEST
            : RailShape.NORTH_SOUTH)
        .setValue(REVERSED, facing == Direction.SOUTH || facing == Direction.WEST);
  }

  private RailShape determineShape(Level level, BlockPos pos, RailShape shape) {
    var north = pos.north();
    var south = pos.south();
    var east = pos.east();
    var west = pos.west();
    if (BaseRailBlock.isRail(level, north) && BaseRailBlock.isRail(level, south) &&
        BaseRailBlock.isRail(level, east) && BaseRailBlock.isRail(level, west)) {
      return shape;
    }
    return switch (shape) {
      case NORTH_SOUTH -> BaseRailBlock.isRail(level, north) && BaseRailBlock.isRail(level, south)
          && TrackUtil.getTrackDirection(level, north) == RailShape.NORTH_SOUTH
          && TrackUtil.getTrackDirection(level, south) == RailShape.NORTH_SOUTH
          ? RailShape.EAST_WEST
          : shape;
      case EAST_WEST -> BaseRailBlock.isRail(level, east) && BaseRailBlock.isRail(level, west)
          && TrackUtil.getTrackDirection(level, east) == RailShape.EAST_WEST
          && TrackUtil.getTrackDirection(level, west) == RailShape.EAST_WEST
          ? RailShape.NORTH_SOUTH
          : shape;
      default -> shape;
    };
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
