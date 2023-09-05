package mods.railcraft.world.level.block.entity.track;

import java.util.List;
import java.util.UUID;
import mods.railcraft.api.track.ArrowDirection;
import mods.railcraft.world.entity.vehicle.MinecartUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TurnoutTrackBlockEntity extends SwitchTrackBlockEntity {

  public TurnoutTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.TURNOUT_TRACK.get(), blockPos, blockState);
  }

  public TurnoutTrackBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
  }

  @Override
  public ArrowDirection getRedArrowDirection() {
    final boolean switched = SwitchTrackBlock.isSwitched(this.getBlockState());
    final Direction facing = SwitchTrackBlock.getFacing(this.getBlockState());
    final boolean mirrored = TurnoutTrackBlock.isMirrored(this.getBlockState());
    return ArrowDirection.fromHorizontalDirection(
        switched ? mirrored ? facing.getCounterClockWise() : facing.getClockWise()
            : mirrored ? facing.getOpposite() : facing);
  }

  @Override
  public ArrowDirection getWhiteArrowDirection() {
    final boolean switched = SwitchTrackBlock.isSwitched(this.getBlockState());
    final Direction facing = SwitchTrackBlock.getFacing(this.getBlockState());
    return facing.getAxis() == Direction.Axis.X
        ? switched ? ArrowDirection.EAST_WEST : ArrowDirection.NORTH_SOUTH
        : switched ? ArrowDirection.NORTH_SOUTH : ArrowDirection.EAST_WEST;
  }

  @Override
  protected List<UUID> getCartsAtLockEntrance() {
    return MinecartUtil.getMinecartUUIDsAt(this.level,
        this.getBlockPos().relative(SwitchTrackBlock.getFacing(this.getBlockState())),
        0.1F);
  }

  @Override
  protected List<UUID> getCartsAtDecisionEntrance() {
    return MinecartUtil.getMinecartUUIDsAt(this.level,
        this.getBlockPos().relative(SwitchTrackBlock.getFacing(this.getBlockState()).getOpposite()),
        0.1F);
  }

  @Override
  protected List<UUID> getCartsAtSpringEntrance() {
    final Direction facing = SwitchTrackBlock.getFacing(this.getBlockState());
    final boolean mirrored = TurnoutTrackBlock.isMirrored(this.getBlockState());
    return MinecartUtil.getMinecartUUIDsAt(this.level,
        this.getBlockPos()
            .relative(mirrored ? facing.getCounterClockWise() : facing.getClockWise()),
        0.1F);
  }

  @Override
  public Direction getActuatorDirection() {
    final boolean mirrored = TurnoutTrackBlock.isMirrored(this.getBlockState());
    final Direction facing = SwitchTrackBlock.getFacing(this.getBlockState());
    return mirrored ? facing.getClockWise() : facing.getCounterClockWise();
  }
}
