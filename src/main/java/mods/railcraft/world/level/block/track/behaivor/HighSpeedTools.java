package mods.railcraft.world.level.block.track.behaivor;

import javax.annotation.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
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

  public static void checkSafetyAndExplode(Level world, BlockPos pos, AbstractMinecart cart) {
    if (!isTrackSafeForHighSpeed(world, pos, cart)) {
      CartTools.explodeCart(cart);
    }
  }

  public static boolean isTrackSafeForHighSpeed(Level world, BlockPos pos,
      AbstractMinecart cart) {
    if (!isHighSpeedTrackAt(world, pos)) {
      return false;
    }
    var railShape = TrackUtil.getTrackDirection(world, pos, cart);
    if (!RailShapeUtil.isStraight(railShape)) {
      return false;
    }
    if (RailShapeUtil.isNorthSouth(railShape)) {
      BlockPos north = pos.north();
      BlockPos south = pos.south();
      return (isTrackHighSpeedCapable(world, north) || isTrackHighSpeedCapable(world, north.above())
          || isTrackHighSpeedCapable(world, north.below()))
          && (isTrackHighSpeedCapable(world, south) || isTrackHighSpeedCapable(world, south.above())
              || isTrackHighSpeedCapable(world, south.below()));
    } else if (RailShapeUtil.isEastWest(railShape)) {
      BlockPos east = pos.east();
      BlockPos west = pos.west();
      return (isTrackHighSpeedCapable(world, east) || isTrackHighSpeedCapable(world, east.above())
          || isTrackHighSpeedCapable(world, east.below()))
          && (isTrackHighSpeedCapable(world, west) || isTrackHighSpeedCapable(world, west.above())
              || isTrackHighSpeedCapable(world, west.below()));
    }
    return false;
  }

  private static boolean isTrackHighSpeedCapable(Level world, BlockPos pos) {
    return !world.isLoaded(pos) || isHighSpeedTrackAt(world, pos);
  }

  private static void limitSpeed(AbstractMinecart cart) {
    Vec3 motion = cart.getDeltaMovement();
    double motionX = Math.copySign(Math.min(SPEED_CUTOFF, Math.abs(motion.x())), motion.x());
    double motionZ = Math.copySign(Math.min(SPEED_CUTOFF, Math.abs(motion.z())), motion.z());
    cart.setDeltaMovement(motionX, motion.y(), motionZ);
  }

  public static void performHighSpeedChecks(Level world, BlockPos pos,
      AbstractMinecart cart) {
    var extension = MinecartExtension.getOrThrow(cart);
    var highSpeed = extension.isHighSpeed();
    var currentMotion = cart.getDeltaMovement();
    if (highSpeed) {
      checkSafetyAndExplode(world, pos, cart);
    } else if (isTrackSafeForHighSpeed(world, pos, cart)) {
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
    return TrackUtil.getTrackTypeAt(world, pos).isHighSpeed();
  }

  public static double speedForNextTrack(Level world, BlockPos pos, int dist,
      @Nullable AbstractMinecart cart) {
    double maxSpeed = RailcraftConfig.server.highSpeedTrackMaxSpeed.get();
    if (dist < LOOK_AHEAD_DIST) {
      for (Direction side : Direction.Plane.HORIZONTAL) {
        BlockPos nextPos = pos.relative(side);
        boolean foundTrack = BaseRailBlock.isRail(world, nextPos);
        if (!foundTrack) {
          if (BaseRailBlock.isRail(world, nextPos.above())) {
            foundTrack = true;
            nextPos = nextPos.above();
          } else if (BaseRailBlock.isRail(world, nextPos.below())) {
            foundTrack = true;
            nextPos = nextPos.below();
          }
        }
        if (foundTrack) {
          var railShape = TrackUtil.getTrackDirection(world, nextPos, cart);
          if (railShape.isAscending()) {
            return SPEED_SLOPE;
          }
          maxSpeed = speedForNextTrack(world, nextPos, dist + 1, cart);
          if (maxSpeed == SPEED_SLOPE) {
            return SPEED_SLOPE;
          }
        }
      }
    }

    return maxSpeed;
  }
}
