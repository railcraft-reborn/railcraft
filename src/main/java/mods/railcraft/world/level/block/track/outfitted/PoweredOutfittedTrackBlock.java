package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.PoweredTrack;
import mods.railcraft.api.track.TrackType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;

public abstract class PoweredOutfittedTrackBlock extends OutfittedTrackBlock
    implements PoweredTrack {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public PoweredOutfittedTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(POWERED, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return super.getStateForPlacement(context)
        .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
  }

  @Override
  public boolean isPowered(BlockState blockState, Level level, BlockPos pos) {
    return isPowered(blockState);
  }

  @Override
  public void setPowered(BlockState blockState, Level level, BlockPos pos, boolean powered) {
    level.setBlockAndUpdate(pos, blockState.setValue(POWERED, powered));
  }

  @Override
  public int getPowerPropagation(BlockState blockState, Level level, BlockPos blockPos) {
    return 0;
  }

  @Override
  public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState,
      boolean moved) {
    super.onPlace(blockState, level, pos, oldBlockState, moved);
    this.testPower(blockState, level, pos);
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos,
      Block neighborBlock, BlockPos neighborPos, boolean moved) {
    this.testPower(blockState, level, pos);
  }

  protected final void testPower(BlockState blockState, Level level, BlockPos pos) {
    boolean powered = level.getBestNeighborSignal(pos) > 0
        || this.testPowerPropagation(level, pos, blockState,
            this.getPowerPropagation(blockState, level, pos));
    if (powered != this.isPowered(blockState, level, pos)) {
      this.setPowered(blockState, level, pos, powered);
      level.updateNeighbourForOutputSignal(pos, this);
    }
  }

  private boolean testPowerPropagation(Level world, BlockPos pos,
      BlockState state, int maxDist) {
    return this.isConnectedRailPowered(world, pos, state, true, 0, maxDist)
        || this.isConnectedRailPowered(world, pos, state, false, 0, maxDist);
  }

  private boolean isConnectedRailPowered(Level world, BlockPos pos,
      BlockState state, boolean dir, int dist, int maxDist) {
    if (dist >= maxDist) {
      return false;
    }
    boolean powered = true;
    BlockPos.MutableBlockPos newPos = pos.mutable();
    RailShape railDirection = this.getRailDirection(state, world, pos, null);
    switch (railDirection) {
      case NORTH_SOUTH: // '\0'
        if (dir)
          newPos.setZ(newPos.getZ() + 1);
        else
          newPos.setZ(newPos.getZ() - 1);
        break;

      case EAST_WEST: // '\001'
        if (dir)
          newPos.setX(newPos.getX() - 1);
        else
          newPos.setX(newPos.getX() + 1);
        break;

      case ASCENDING_EAST: // '\002'
        if (dir)
          newPos.setX(newPos.getX() - 1);
        else {
          newPos.setX(newPos.getX() + 1);
          newPos.setY(newPos.getY() + 1);
          powered = false;
        }
        railDirection = RailShape.EAST_WEST;
        break;

      case ASCENDING_WEST: // '\003'
        if (dir) {
          newPos.setX(newPos.getX() - 1);
          newPos.setY(newPos.getY() + 1);
          powered = false;
        } else
          newPos.setX(newPos.getX() + 1);
        railDirection = RailShape.EAST_WEST;
        break;

      case ASCENDING_NORTH: // '\004'
        if (dir)
          newPos.setZ(newPos.getZ() + 1);
        else {
          newPos.setZ(newPos.getZ() - 1);
          newPos.setY(newPos.getY() + 1);
          powered = false;
        }
        railDirection = RailShape.NORTH_SOUTH;
        break;

      case ASCENDING_SOUTH: // '\005'
        if (dir) {
          newPos.setZ(newPos.getZ() + 1);
          newPos.setY(newPos.getY() + 1);
          powered = false;
        } else
          newPos.setZ(newPos.getZ() - 1);
        railDirection = RailShape.NORTH_SOUTH;
        break;
      default:
        break;
    }
    return this.testPowered(world, newPos, dir, dist, maxDist, railDirection)
        || (powered && this.testPowered(world, newPos.below(), dir, dist, maxDist, railDirection));
  }

  private boolean testPowered(Level level, BlockPos blockPos, boolean dir,
      int dist, int maxDist, RailShape prevOrientation) {
    BlockState nextBlockState = level.getBlockState(blockPos);
    if (BaseRailBlock.isRail(nextBlockState)
        && nextBlockState.getBlock() instanceof PoweredTrack) {
      PoweredTrack nextBlock = (PoweredTrack) nextBlockState.getBlock();
      RailShape nextOrientation = ((BaseRailBlock) nextBlockState.getBlock())
          .getRailDirection(nextBlockState, level, blockPos, null);
      if (!(this.canPropagatePowerTo(nextBlockState)))
        return false;
      if (prevOrientation == RailShape.EAST_WEST && (nextOrientation == RailShape.NORTH_SOUTH
          || nextOrientation == RailShape.ASCENDING_NORTH
          || nextOrientation == RailShape.ASCENDING_SOUTH))
        return false;
      if (prevOrientation == RailShape.NORTH_SOUTH && (nextOrientation == RailShape.EAST_WEST
          || nextOrientation == RailShape.ASCENDING_EAST
          || nextOrientation == RailShape.ASCENDING_WEST))
        return false;
      if (nextBlock.isPowered(nextBlockState, level, blockPos))
        return level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above())
            || this.isConnectedRailPowered(level, blockPos, nextBlockState, dir, dist + 1, maxDist);
    }
    return false;
  }

  public static boolean isPowered(BlockState blockState) {
    return blockState.getValue(POWERED);
  }
}
