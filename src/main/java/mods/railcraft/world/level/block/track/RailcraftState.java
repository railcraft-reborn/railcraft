package mods.railcraft.world.level.block.track;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import mods.railcraft.world.level.block.track.outfitted.JunctionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class RailcraftState {
  private final Level level;
  private final BlockPos pos;
  private final BaseRailBlock block;
  private BlockState state;
  private final boolean isStraight;
  private final List<BlockPos> connections = Lists.newArrayList();
  private final boolean canMakeSlopes;

  public RailcraftState(Level level, BlockPos pos, BlockState state) {
    this.level = level;
    this.pos = pos;
    this.state = state;
    this.block = (BaseRailBlock)state.getBlock();
    this.isStraight = !this.block.isFlexibleRail(this.state, level, pos);
    this.canMakeSlopes = this.block.canMakeSlopes(this.state, level, pos);
    this.getRailDirection();
  }

  private void getRailDirection() {
    if (this.block instanceof JunctionTrackBlock) {
      this.updateConnections(RailShape.NORTH_SOUTH);
      this.updateConnections(RailShape.EAST_WEST);
    } else if (this.block instanceof TurnoutTrackBlock turnoutTrackBlock) {
      this.updateConnections(turnoutTrackBlock
          .getRailDirection(state.setValue(SwitchTrackBlock.SWITCHED, true), level, pos, null));
      this.updateConnections(turnoutTrackBlock
          .getRailDirection(state.setValue(SwitchTrackBlock.SWITCHED, false), level, pos, null));
    } else if (this.block instanceof WyeTrackBlock wyeTrackBlock) {
      this.updateConnections(wyeTrackBlock
          .getRailDirection(state.setValue(SwitchTrackBlock.SWITCHED, true), level, pos, null));
      this.updateConnections(wyeTrackBlock
          .getRailDirection(state.setValue(SwitchTrackBlock.SWITCHED, false), level, pos, null));
    } else {
      this.updateConnections(this.block.getRailDirection(this.state, this.level, this.pos, null));
    }
  }

  private void updateConnections(RailShape shape) {
    switch (shape) {
      case NORTH_SOUTH:
        this.connections.add(this.pos.north());
        this.connections.add(this.pos.south());
        break;
      case EAST_WEST:
        this.connections.add(this.pos.west());
        this.connections.add(this.pos.east());
        break;
      case ASCENDING_EAST:
        this.connections.add(this.pos.west());
        this.connections.add(this.pos.east().above());
        break;
      case ASCENDING_WEST:
        this.connections.add(this.pos.west().above());
        this.connections.add(this.pos.east());
        break;
      case ASCENDING_NORTH:
        this.connections.add(this.pos.north().above());
        this.connections.add(this.pos.south());
        break;
      case ASCENDING_SOUTH:
        this.connections.add(this.pos.north());
        this.connections.add(this.pos.south().above());
        break;
      case SOUTH_EAST:
        this.connections.add(this.pos.east());
        this.connections.add(this.pos.south());
        break;
      case SOUTH_WEST:
        this.connections.add(this.pos.west());
        this.connections.add(this.pos.south());
        break;
      case NORTH_WEST:
        this.connections.add(this.pos.west());
        this.connections.add(this.pos.north());
        break;
      case NORTH_EAST:
        this.connections.add(this.pos.east());
        this.connections.add(this.pos.north());
    }
  }

  private void removeSoftConnections() {
    for(int i = 0; i < this.connections.size(); ++i) {
      RailcraftState railstate = this.getRail(this.connections.get(i));
      if (railstate != null && railstate.connectsTo(this)) {
        this.connections.set(i, railstate.pos);
      } else {
        this.connections.remove(i--);
      }
    }
  }

  @Nullable
  private RailcraftState getRail(BlockPos pos) {
    BlockState blockstate = this.level.getBlockState(pos);
    if (BaseRailBlock.isRail(blockstate)) {
      return new RailcraftState(this.level, pos, blockstate);
    } else {
      BlockPos above = pos.above();
      blockstate = this.level.getBlockState(above);
      if (BaseRailBlock.isRail(blockstate)) {
        return new RailcraftState(this.level, above, blockstate);
      } else {
        above = pos.below();
        blockstate = this.level.getBlockState(above);
        return BaseRailBlock.isRail(blockstate) ? new RailcraftState(this.level, above, blockstate) : null;
      }
    }
  }

  private boolean connectsTo(RailcraftState state) {
    return this.hasConnection(state.pos);
  }

  private boolean hasConnection(BlockPos pos) {
    for (var blockpos : this.connections) {
      if (blockpos.getX() == pos.getX() && blockpos.getZ() == pos.getZ()) {
        return true;
      }
    }
    return false;
  }

  private boolean canConnectTo(RailcraftState state) {
    return this.connectsTo(state) || this.connections.size() != 2;
  }

  private void connectTo(RailcraftState state) {
    this.connections.add(state.pos);
    BlockPos north = this.pos.north();
    BlockPos south = this.pos.south();
    BlockPos west = this.pos.west();
    BlockPos east = this.pos.east();
    boolean northConnected = this.hasConnection(north);
    boolean southConnected = this.hasConnection(south);
    boolean westConnected = this.hasConnection(west);
    boolean eastConnected = this.hasConnection(east);
    RailShape railshape = null;
    if (northConnected || southConnected) {
      railshape = RailShape.NORTH_SOUTH;
    }

    if (westConnected || eastConnected) {
      railshape = RailShape.EAST_WEST;
    }

    if (!this.isStraight) {
      if (southConnected && eastConnected && !northConnected && !westConnected) {
        railshape = RailShape.SOUTH_EAST;
      }

      if (southConnected && westConnected && !northConnected && !eastConnected) {
        railshape = RailShape.SOUTH_WEST;
      }

      if (northConnected && westConnected && !southConnected && !eastConnected) {
        railshape = RailShape.NORTH_WEST;
      }

      if (northConnected && eastConnected && !southConnected && !westConnected) {
        railshape = RailShape.NORTH_EAST;
      }
    }

    if (railshape == RailShape.NORTH_SOUTH && this.canMakeSlopes) {
      if (BaseRailBlock.isRail(this.level, north.above())) {
        railshape = RailShape.ASCENDING_NORTH;
      }

      if (BaseRailBlock.isRail(this.level, south.above())) {
        railshape = RailShape.ASCENDING_SOUTH;
      }
    }

    if (railshape == RailShape.EAST_WEST && this.canMakeSlopes) {
      if (BaseRailBlock.isRail(this.level, east.above())) {
        railshape = RailShape.ASCENDING_EAST;
      }

      if (BaseRailBlock.isRail(this.level, west.above())) {
        railshape = RailShape.ASCENDING_WEST;
      }
    }

    if (railshape == null) {
      railshape = RailShape.NORTH_SOUTH;
    }

    if (!this.block.isValidRailShape(railshape)) {
      this.connections.remove(state.pos);
    } else {
      this.state = this.state.setValue(this.block.getShapeProperty(), railshape);
      this.level.setBlock(this.pos, this.state, 3);
    }
  }

  private boolean hasNeighborRail(BlockPos pos) {
    RailcraftState railstate = this.getRail(pos);
    if (railstate == null) {
      return false;
    } else {
      railstate.removeSoftConnections();
      return railstate.canConnectTo(this);
    }
  }

  public RailcraftState place(boolean powered, boolean alwaysPlace, RailShape shape) {
    BlockPos north = this.pos.north();
    BlockPos south = this.pos.south();
    BlockPos west = this.pos.west();
    BlockPos east = this.pos.east();
    boolean northConnected = this.hasNeighborRail(north);
    boolean southConnected = this.hasNeighborRail(south);
    boolean westConnected = this.hasNeighborRail(west);
    boolean eastConnected = this.hasNeighborRail(east);
    RailShape railshape = null;
    boolean NORTH_SOUTH = northConnected || southConnected;
    boolean EAST_WEST = westConnected || eastConnected;
    if (NORTH_SOUTH && !EAST_WEST) {
      railshape = RailShape.NORTH_SOUTH;
    }

    if (EAST_WEST && !NORTH_SOUTH) {
      railshape = RailShape.EAST_WEST;
    }

    boolean SOUTH_EAST = southConnected && eastConnected;
    boolean SOUTH_WEST = southConnected && westConnected;
    boolean NORTH_EAST = northConnected && eastConnected;
    boolean NORTH_WEST = northConnected && westConnected;
    if (!this.isStraight) {
      if (SOUTH_EAST && !northConnected && !westConnected) {
        railshape = RailShape.SOUTH_EAST;
      }

      if (SOUTH_WEST && !northConnected && !eastConnected) {
        railshape = RailShape.SOUTH_WEST;
      }

      if (NORTH_WEST && !southConnected && !eastConnected) {
        railshape = RailShape.NORTH_WEST;
      }

      if (NORTH_EAST && !southConnected && !westConnected) {
        railshape = RailShape.NORTH_EAST;
      }
    }

    if (railshape == null) {
      if (NORTH_SOUTH && EAST_WEST) {
        railshape = shape;
      } else if (NORTH_SOUTH) {
        railshape = RailShape.NORTH_SOUTH;
      } else if (EAST_WEST) {
        railshape = RailShape.EAST_WEST;
      }

      if (!this.isStraight) {
        if (powered) {
          if (SOUTH_EAST) {
            railshape = RailShape.SOUTH_EAST;
          }

          if (SOUTH_WEST) {
            railshape = RailShape.SOUTH_WEST;
          }

          if (NORTH_EAST) {
            railshape = RailShape.NORTH_EAST;
          }

          if (NORTH_WEST) {
            railshape = RailShape.NORTH_WEST;
          }
        } else {
          if (NORTH_WEST) {
            railshape = RailShape.NORTH_WEST;
          }

          if (NORTH_EAST) {
            railshape = RailShape.NORTH_EAST;
          }

          if (SOUTH_WEST) {
            railshape = RailShape.SOUTH_WEST;
          }

          if (SOUTH_EAST) {
            railshape = RailShape.SOUTH_EAST;
          }
        }
      }
    }

    if (railshape == RailShape.NORTH_SOUTH && this.canMakeSlopes) {
      if (BaseRailBlock.isRail(this.level, north.above())) {
        railshape = RailShape.ASCENDING_NORTH;
      }

      if (BaseRailBlock.isRail(this.level, south.above())) {
        railshape = RailShape.ASCENDING_SOUTH;
      }
    }

    if (railshape == RailShape.EAST_WEST && this.canMakeSlopes) {
      if (BaseRailBlock.isRail(this.level, east.above())) {
        railshape = RailShape.ASCENDING_EAST;
      }

      if (BaseRailBlock.isRail(this.level, west.above())) {
        railshape = RailShape.ASCENDING_WEST;
      }
    }

    if (railshape == null || !this.block.isValidRailShape(railshape)) {
      railshape = shape;
    }

    this.connections.clear();
    this.updateConnections(railshape);
    this.state = this.state.setValue(this.block.getShapeProperty(), railshape);
    if (alwaysPlace || this.level.getBlockState(this.pos) != this.state) {
      this.level.setBlock(this.pos, this.state, 3);

      for (var connection : this.connections) {
        RailcraftState railstate = this.getRail(connection);
        if (railstate != null) {
          railstate.removeSoftConnections();
          if (railstate.canConnectTo(this)) {
            railstate.connectTo(this);
          }
        }
      }
    }

    return this;
  }

  public BlockState getState() {
    return this.state;
  }
}
