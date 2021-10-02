package mods.railcraft.world.level.block.entity.track;

import java.util.List;
import java.util.UUID;
import mods.railcraft.api.track.ArrowDirection;
import mods.railcraft.carts.CartTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversableSwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TurnoutTrackBlockEntity extends SwitchTrackBlockEntity {

  public TurnoutTrackBlockEntity() {
    this(RailcraftBlockEntityTypes.TURNOUT_TRACK.get());
  }

  public TurnoutTrackBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public ArrowDirection getRedArrowDirection() {
    final boolean switched = SwitchTrackBlock.isSwitched(this.getBlockState());
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());
    final boolean reversed = ReversableSwitchTrackBlock.isReversed(this.getBlockState());
    return TrackBlock.getRailShapeRaw(this.getBlockState()) == RailShape.EAST_WEST
        ? switched
            ? mirrored ? ArrowDirection.NORTH : ArrowDirection.SOUTH
            : reversed != mirrored ? ArrowDirection.EAST : ArrowDirection.WEST
        : switched
            ? mirrored ? ArrowDirection.WEST : ArrowDirection.EAST
            : reversed != mirrored ? ArrowDirection.NORTH : ArrowDirection.SOUTH;
  }

  @Override
  public ArrowDirection getWhiteArrowDirection() {
    final boolean switched = SwitchTrackBlock.isSwitched(this.getBlockState());
    return TrackBlock.getRailShapeRaw(this.getBlockState()) == RailShape.EAST_WEST
        ? switched ? ArrowDirection.EAST_WEST : ArrowDirection.NORTH_SOUTH
        : switched ? ArrowDirection.NORTH_SOUTH : ArrowDirection.EAST_WEST;
  }

  @Override
  protected List<UUID> getCartsAtLockEntrance() {
    final RailShape dir = TrackBlock.getRailShapeRaw(this.getBlockState());
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());
    final boolean reversed = isReversed(this.getBlockState());

    BlockPos offset = this.getBlockPos();
    if (dir == RailShape.NORTH_SOUTH) {
      offset = reversed != mirrored ? offset.south() : offset.north();
    } else if (dir == RailShape.EAST_WEST) {
      offset = reversed == mirrored ? offset.east() : offset.west();
    }

    return CartTools.getMinecartUUIDsAt(this.level, offset, 0.1f);
  }

  @Override
  protected List<UUID> getCartsAtDecisionEntrance() {
    final RailShape dir = TrackBlock.getRailShapeRaw(this.getBlockState());
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());
    final boolean reversed = isReversed(this.getBlockState());

    BlockPos offset = this.getBlockPos();
    if (dir == RailShape.NORTH_SOUTH) {
      offset = reversed != mirrored ? offset.north() : offset.south();
    } else if (dir == RailShape.EAST_WEST) {
      offset = reversed == mirrored ? offset.west() : offset.east();
    }

    return CartTools.getMinecartUUIDsAt(this.level, offset, 0.1f);
  }

  @Override
  protected List<UUID> getCartsAtSpringEntrance() {
    final RailShape dir = TrackBlock.getRailShapeRaw(this.getBlockState());
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());

    BlockPos offset = this.getBlockPos();
    if (dir == RailShape.NORTH_SOUTH) {
      offset = mirrored ? offset.west() : offset.east();
    } else if (dir == RailShape.EAST_WEST) {
      offset = mirrored ? offset.north() : offset.south();
    }

    return CartTools.getMinecartUUIDsAt(this.level, offset, 0.1f);
  }


  @Override
  public Direction getActuatorDirection() {
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());
    final RailShape dir = TrackBlock.getRailShapeRaw(this.getBlockState());

    Direction face = Direction.NORTH;
    if (dir == RailShape.NORTH_SOUTH) {
      face = mirrored ? Direction.EAST : Direction.WEST;
    } else if (dir == RailShape.EAST_WEST) {
      face = mirrored ? Direction.SOUTH : Direction.NORTH;
    }

    return face;
  }

  public static boolean isReversed(BlockState blockState) {
    return blockState.getValue(ReversableSwitchTrackBlock.REVERSED);
  }
}
