package mods.railcraft.world.level.block.track.behaivor;

import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.carts.CartConstants;
import mods.railcraft.carts.CartTools;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.track.outfitted.kit.TrackKits;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 8/2/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class HighSpeedTools {

  public static final float SPEED_EXPLODE = 0.5f;
  public static final float SPEED_CUTOFF = 0.499f;
  public static final int LOOK_AHEAD_DIST = 2;
  public static final float SPEED_SLOPE = 0.45f;

  public static void checkSafetyAndExplode(World world, BlockPos pos, AbstractMinecartEntity cart) {
    if (!isTrackSafeForHighSpeed(world, pos, cart)) {
      CartTools.explodeCart(cart);
    }
  }

  public static boolean isTrackSafeForHighSpeed(World world, BlockPos pos,
      AbstractMinecartEntity cart) {
    if (!isHighSpeedTrackAt(world, pos))
      return false;
    RailShape dir = TrackTools.getTrackDirection(world, pos, cart);
    if (!TrackShapeHelper.isStraight(dir)) {
      return false;
    }
    if (TrackShapeHelper.isNorthSouth(dir)) {
      BlockPos north = pos.north();
      BlockPos south = pos.south();
      return (isTrackHighSpeedCapable(world, north) || isTrackHighSpeedCapable(world, north.above())
          || isTrackHighSpeedCapable(world, north.below()))
          && (isTrackHighSpeedCapable(world, south) || isTrackHighSpeedCapable(world, south.above())
              || isTrackHighSpeedCapable(world, south.below()));
    } else if (TrackShapeHelper.isEastWest(dir)) {
      BlockPos east = pos.east();
      BlockPos west = pos.west();
      return (isTrackHighSpeedCapable(world, east) || isTrackHighSpeedCapable(world, east.above())
          || isTrackHighSpeedCapable(world, east.below()))
          && (isTrackHighSpeedCapable(world, west) || isTrackHighSpeedCapable(world, west.above())
              || isTrackHighSpeedCapable(world, west.below()));
    }
    return false;
  }

  private static boolean isTrackHighSpeedCapable(World world, BlockPos pos) {
    return !world.isLoaded(pos) || isHighSpeedTrackAt(world, pos);
  }

  private static void limitSpeed(AbstractMinecartEntity cart) {
    Vector3d motion = cart.getDeltaMovement();
    double motionX = Math.copySign(Math.min(SPEED_CUTOFF, Math.abs(motion.x())), motion.x());
    double motionZ = Math.copySign(Math.min(SPEED_CUTOFF, Math.abs(motion.z())), motion.z());
    cart.setDeltaMovement(motionX, motion.y(), motionZ);
  }

  public static void performHighSpeedChecks(World world, BlockPos pos, AbstractMinecartEntity cart,
      @Nullable TrackKit trackKit) {
    boolean highSpeed = isTravellingHighSpeed(cart);
    Vector3d currentMotion = cart.getDeltaMovement();
    if (highSpeed) {
      checkSafetyAndExplode(world, pos, cart);
    } else if (trackKit == TrackKits.BOOSTER.get()
        || trackKit == TrackKits.HIGH_SPEED_TRANSITION.get()) {
      if (isTrackSafeForHighSpeed(world, pos, cart)) {
        if (Math.abs(currentMotion.x()) > SPEED_CUTOFF) {
          double motionX = Math.copySign(SPEED_CUTOFF, currentMotion.x());
          cart.setDeltaMovement(motionX, currentMotion.y(), currentMotion.z());
          setTravellingHighSpeed(cart, true);
        }
        if (Math.abs(currentMotion.z()) > SPEED_CUTOFF) {
          double motionZ = Math.copySign(SPEED_CUTOFF, currentMotion.z());
          cart.setDeltaMovement(currentMotion.x(), currentMotion.y(), motionZ);
          setTravellingHighSpeed(cart, true);
        }
      }
    } else {
      limitSpeed(cart);
    }
  }

  public static boolean isHighSpeedTrackAt(IBlockReader world, BlockPos pos) {
    return TrackTools.getTrackTypeAt(world, pos).isHighSpeed();
  }

  public static double speedForNextTrack(World world, BlockPos pos, int dist,
      @Nullable AbstractMinecartEntity cart) {
    double maxSpeed = Railcraft.serverConfig.highSpeedTrackMaxSpeed.get();
    if (dist < LOOK_AHEAD_DIST)
      for (Direction side : Direction.Plane.HORIZONTAL) {
        BlockPos nextPos = pos.relative(side);
        boolean foundTrack = AbstractRailBlock.isRail(world, nextPos);
        if (!foundTrack) {
          if (AbstractRailBlock.isRail(world, nextPos.above())) {
            foundTrack = true;
            nextPos = nextPos.above();
          } else if (AbstractRailBlock.isRail(world, nextPos.below())) {
            foundTrack = true;
            nextPos = nextPos.below();
          }
        }
        if (foundTrack) {
          RailShape dir = TrackTools.getTrackDirection(world, nextPos, cart);
          if (dir.isAscending())
            return SPEED_SLOPE;
          maxSpeed = speedForNextTrack(world, nextPos, dist + 1, cart);
          if (maxSpeed == SPEED_SLOPE)
            return SPEED_SLOPE;
        }
      }

    return maxSpeed;
  }

  public static void setTravellingHighSpeed(AbstractMinecartEntity cart, boolean flag) {
    cart.getPersistentData().putBoolean(CartConstants.TAG_HIGH_SPEED, flag);
  }

  public static boolean isTravellingHighSpeed(AbstractMinecartEntity cart) {
    return cart.getPersistentData().getBoolean(CartConstants.TAG_HIGH_SPEED);
  }
}
