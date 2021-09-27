package mods.railcraft.world.level.block.entity.track;

import java.util.List;
import java.util.UUID;
import mods.railcraft.api.tracks.ArrowDirection;
import mods.railcraft.carts.CartTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class WyeTrackBlockEntity extends SwitchTrackBlockEntity {

  public WyeTrackBlockEntity() {
    this(RailcraftBlockEntityTypes.WYE_TRACK.get());
  }

  public WyeTrackBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public ArrowDirection getRedArrowDirection() {
    final boolean switched = SwitchTrackBlock.isSwitched(this.getBlockState());
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());
    return TrackBlock.getRailShapeRaw(this.getBlockState()) == RailShape.EAST_WEST
        ? switched
            ? mirrored ? ArrowDirection.EAST : ArrowDirection.WEST
            : mirrored ? ArrowDirection.WEST : ArrowDirection.EAST
        : switched
            ? mirrored ? ArrowDirection.NORTH : ArrowDirection.SOUTH
            : mirrored ? ArrowDirection.SOUTH : ArrowDirection.NORTH;
  }

  @Override
  public ArrowDirection getWhiteArrowDirection() {
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());
    return TrackBlock.getRailShapeRaw(this.getBlockState()) == RailShape.EAST_WEST
        ? mirrored ? ArrowDirection.NORTH : ArrowDirection.SOUTH
        : mirrored ? ArrowDirection.WEST : ArrowDirection.EAST;
  }

  @Override
  protected List<UUID> getCartsAtLockEntrance() {
    final RailShape dir = SwitchTrackBlock.getRailShapeRaw(this.getBlockState());
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());

    BlockPos offset = this.getBlockPos();
    if (dir == RailShape.EAST_WEST) {
      offset = mirrored ? offset.west() : offset.east();
    } else if (dir == RailShape.NORTH_SOUTH) {
      offset = mirrored ? offset.south() : offset.north();
    }

    return CartTools.getMinecartUUIDsAt(this.level, offset, 0.1f);
  }

  @Override
  protected List<UUID> getCartsAtDecisionEntrance() {
    final RailShape dir = SwitchTrackBlock.getRailShapeRaw(this.getBlockState());
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());

    BlockPos offset = this.getBlockPos();
    if (dir == RailShape.EAST_WEST) {
      offset = mirrored ? offset.north() : offset.south();
    } else if (dir == RailShape.NORTH_SOUTH) {
      offset = mirrored ? offset.east() : offset.west();
    }

    return CartTools.getMinecartUUIDsAt(this.level, offset, 0.1f);
  }

  @Override
  protected List<UUID> getCartsAtSpringEntrance() {
    final RailShape dir = SwitchTrackBlock.getRailShapeRaw(this.getBlockState());
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());

    BlockPos offset = this.getBlockPos();
    if (dir == RailShape.EAST_WEST) {
      offset = mirrored ? offset.east() : offset.west();
    } else if (dir == RailShape.NORTH_SOUTH) {
      offset = mirrored ? offset.north() : offset.south();
    }

    return CartTools.getMinecartUUIDsAt(this.level, offset, 0.1f);
  }

  @Override
  public Direction getActuatorDirection() {
    final boolean mirrored = SwitchTrackBlock.isMirrored(this.getBlockState());
    return SwitchTrackBlock.getRailShapeRaw(this.getBlockState()) == RailShape.EAST_WEST
        ? mirrored ? Direction.SOUTH : Direction.NORTH
        : mirrored ? Direction.EAST : Direction.WEST;
  }
}
