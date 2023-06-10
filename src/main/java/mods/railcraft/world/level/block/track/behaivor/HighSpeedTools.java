package mods.railcraft.world.level.block.track.behaivor;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.api.track.TypedTrack;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Created by CovertJaguar on 8/2/2016 for Railcraft.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public final class HighSpeedTools {

  public static final float SPEED_EXPLODE = 0.5f;
  public static final float SPEED_CUTOFF = 0.499f;
  public static final int LOOK_AHEAD_DIST = 2;
  public static final float SPEED_SLOPE = 0.45f;

  public static void checkSafetyAndExplode(Level level, BlockPos pos, AbstractMinecart cart) {
    if (!isTrackSafeForHighSpeed(level, pos, cart)) {
      CartTools.explodeCart(cart);
    }
  }

  public static boolean isTrackSafeForHighSpeed(Level level, BlockPos pos,
      AbstractMinecart cart) {
    if (!isHighSpeedTrackAt(level, pos)) {
      return false;
    }
    var railShape = TrackUtil.getTrackDirection(level, pos, cart);
    if (!RailShapeUtil.isStraight(railShape)) {
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

  private static void limitSpeed(AbstractMinecart cart) {
    Vec3 motion = cart.getDeltaMovement();
    double motionX = Math.copySign(Math.min(SPEED_CUTOFF, Math.abs(motion.x())), motion.x());
    double motionZ = Math.copySign(Math.min(SPEED_CUTOFF, Math.abs(motion.z())), motion.z());
    cart.setDeltaMovement(motionX, motion.y(), motionZ);
  }

  public static void performHighSpeedChecks(Level level, BlockPos pos,
      RollingStock extension) {
    var cart = extension.entity();
    var highSpeed = extension.isHighSpeed();
    var currentMotion = cart.getDeltaMovement();
    if (highSpeed) {
      checkSafetyAndExplode(level, pos, cart);
    } else if (isTrackSafeForHighSpeed(level, pos, cart)) {
      if (Math.abs(currentMotion.x()) > SPEED_CUTOFF) {
        double motionX = Math.copySign(SPEED_CUTOFF, currentMotion.x());
        cart.setDeltaMovement(motionX, currentMotion.y(), currentMotion.z());
        extension.setHighSpeed(true);
      }
      if (Math.abs(currentMotion.z()) > SPEED_CUTOFF) {
        double motionZ = Math.copySign(SPEED_CUTOFF, currentMotion.z());
        cart.setDeltaMovement(currentMotion.x(), currentMotion.y(), motionZ);
        extension.setHighSpeed(true);
      }
    } else {
      limitSpeed(cart);
    }
  }

  public static boolean isHighSpeedTrackAt(BlockGetter world, BlockPos pos) {
    return getTrackTypeAt(world, pos).isHighSpeed();
  }

  public static TrackType getTrackTypeAt(BlockGetter level, BlockPos pos) {
    return getTrackTypeAt(level, pos, level.getBlockState(pos));
  }

  public static TrackType getTrackTypeAt(BlockGetter level, BlockPos pos, BlockState state) {
    if (state.getBlock() instanceof TypedTrack typedTrack) {
      return typedTrack.getTrackType();
    }
    return TrackTypes.IRON.get();
  }

  public static double speedForNextTrack(Level level, BlockPos pos, int dist,
      @Nullable AbstractMinecart cart) {
    double maxSpeed = RailcraftConfig.SERVER.highSpeedTrackMaxSpeed.get();
    if (dist < LOOK_AHEAD_DIST) {
      for (Direction side : Direction.Plane.HORIZONTAL) {
        BlockPos nextPos = pos.relative(side);
        boolean foundTrack = BaseRailBlock.isRail(level, nextPos);
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
    }

    return maxSpeed;
  }
}
