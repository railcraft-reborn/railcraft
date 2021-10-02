package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.PoweredTrack;
import mods.railcraft.api.track.TrackType;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractPoweredTrackBlock extends OutfittedTrackBlock
    implements PoweredTrack {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public AbstractPoweredTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED);
  }

  @Override
  public boolean isPowered(BlockState blockState, World level, BlockPos pos) {
    return blockState.getValue(POWERED);
  }

  @Override
  public void setPowered(BlockState blockState, World level, BlockPos pos, boolean powered) {
    level.setBlockAndUpdate(pos, blockState.setValue(POWERED, true));
  }

  protected final void testPower(BlockState blockState, World level, BlockPos pos) {
    boolean powered = level.getBestNeighborSignal(pos) > 0
        || this.testPowerPropagation(level, pos, blockState,
            this.getPowerPropagation(blockState, level, pos));
    if (powered != this.isPowered(blockState, level, pos)) {
      this.setPowered(blockState, level, pos, powered);
      level.updateNeighbourForOutputSignal(pos, this);
    }
  }

  private boolean testPowerPropagation(World world, BlockPos pos,
      BlockState state, int maxDist) {
    return this.isConnectedRailPowered(world, pos, state, true, 0, maxDist)
        || this.isConnectedRailPowered(world, pos, state, false, 0, maxDist);
  }

  private boolean isConnectedRailPowered(World world, BlockPos pos,
      BlockState state, boolean dir, int dist, int maxDist) {
    if (dist >= maxDist)
      return false;
    boolean powered = true;
    BlockPos.Mutable newPos = pos.mutable();
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

  private boolean testPowered(World world, BlockPos nextPos, boolean dir,
      int dist, int maxDist, RailShape prevOrientation) {
    BlockState nextBlockState = world.getBlockState(nextPos);
    if (AbstractRailBlock.isRail(nextBlockState)
        && nextBlockState.getBlock() instanceof PoweredTrack) {
      PoweredTrack nextBlock = (PoweredTrack) nextBlockState.getBlock();
      RailShape nextOrientation = ((AbstractRailBlock) nextBlockState.getBlock())
          .getRailDirection(nextBlockState, world, nextPos, null);
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
      if (nextBlock.isPowered(nextBlockState, world, nextPos))
        return world.hasNeighborSignal(nextPos) || world.hasNeighborSignal(nextPos.above())
            || this.isConnectedRailPowered(world, nextPos, nextBlockState, dir, dist + 1, maxDist);
    }
    return false;
  }
}
