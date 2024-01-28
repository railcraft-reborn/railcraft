package mods.railcraft.world.level.block.track.behaivor;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.api.track.TypedTrack;
import mods.railcraft.world.entity.vehicle.MinecartUtil;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class HighSpeedTrackUtil {

  private static final int LOOK_AHEAD_DIST = 2;
  private static final float SPEED_SLOPE = 0.45F;

  public static double getMaxSpeed(Level level, @Nullable AbstractMinecart cart,
      BlockPos pos) {
    return TrackUtil.getTrackDirection(level, pos, cart).isAscending()
        ? SPEED_SLOPE
        : speedForNextTrack(level, pos, 0, cart);
  }

  public static void checkSafetyAndExplode(Level level, BlockPos pos, AbstractMinecart cart) {
    if (!isTrackSafeForHighSpeed(level, pos, cart)) {
      MinecartUtil.explodeCart(cart);
    }
  }

  public static boolean isTrackSafeForHighSpeed(Level level, BlockPos pos,
      AbstractMinecart cart) {
    if (!isHighSpeedTrackAt(level, pos)) {
      return false;
    }
    var railShape = TrackUtil.getTrackDirection(level, pos, cart);
    if (RailShapeUtil.isTurn(railShape)) {
      return false;
    }
    if (RailShapeUtil.isNorthSouth(railShape)) {
      BlockPos north = pos.north();
      BlockPos south = pos.south();
      return (isTrackHighSpeedCapable(level, north) || isTrackHighSpeedCapable(level, north.above())
          || isTrackHighSpeedCapable(level, north.below()))
          && (isTrackHighSpeedCapable(level, south) || isTrackHighSpeedCapable(level, south.above())
              || isTrackHighSpeedCapable(level, south.below()));
    } else if (RailShapeUtil.isEastWest(railShape)) {
      BlockPos east = pos.east();
      BlockPos west = pos.west();
      return (isTrackHighSpeedCapable(level, east) || isTrackHighSpeedCapable(level, east.above())
          || isTrackHighSpeedCapable(level, east.below()))
          && (isTrackHighSpeedCapable(level, west) || isTrackHighSpeedCapable(level, west.above())
              || isTrackHighSpeedCapable(level, west.below()));
    }
    return false;
  }

  private static boolean isTrackHighSpeedCapable(Level level, BlockPos pos) {
    return !level.isLoaded(pos) || isHighSpeedTrackAt(level, pos);
  }

  private static boolean isHighSpeedTrackAt(BlockGetter level, BlockPos pos) {
    return getTrackTypeAt(level, pos, level.getBlockState(pos)).isHighSpeed();
  }

  private static TrackType getTrackTypeAt(BlockGetter level, BlockPos pos, BlockState state) {
    return state.getBlock() instanceof TypedTrack typedTrack
        ? typedTrack.getTrackType()
        : TrackTypes.IRON.get();
  }

  private static double speedForNextTrack(Level level, BlockPos pos, int dist,
      @Nullable AbstractMinecart cart) {
    double maxSpeed = RailcraftConfig.SERVER.highSpeedTrackMaxSpeed.get();

    if (dist >= LOOK_AHEAD_DIST) {
      return maxSpeed;
    }

    for (var direction : Direction.Plane.HORIZONTAL) {
      var nextPos = pos.relative(direction);
      var foundTrack = BaseRailBlock.isRail(level, nextPos);
      if (!foundTrack) {
        if (BaseRailBlock.isRail(level, nextPos.above())) {
          foundTrack = true;
          nextPos = nextPos.above();
        } else if (BaseRailBlock.isRail(level, nextPos.below())) {
          foundTrack = true;
          nextPos = nextPos.below();
        }
      }
      if (foundTrack) {
        var railShape = TrackUtil.getTrackDirection(level, nextPos, cart);
        if (railShape.isAscending()) {
          return SPEED_SLOPE;
        }
        maxSpeed = speedForNextTrack(level, nextPos, dist + 1, cart);
        if (maxSpeed == SPEED_SLOPE) {
          return SPEED_SLOPE;
        }
      }
    }

    return maxSpeed;
  }
}
