package mods.railcraft.world.level.block.track.behaivor;

import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.CartToolsAPI;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.carts.CartConstants;
import mods.railcraft.carts.Train;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public enum SpeedController {

  IRON,
  ABANDONED {
    @Override
    public double getMaxSpeed(World world, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
      return 0.499D;
    }

    private boolean isDerailing(
        AbstractMinecartEntity cart) {
      if (CartToolsAPI.getCartSpeedUncapped(cart) > 0.35F && MiscTools.RANDOM.nextInt(500) == 250)
        return true;
      return Train.streamCarts(cart)
          .anyMatch(Railcraft.getInstance().getMinecartHandler()::isDerailed);
    }

    @Override
    // FIXME: Client and Server sync is not maintained here. Could result in strange behavior.
    public @Nullable RailShape getRailDirectionOverride(IBlockReader world,
        BlockPos pos, BlockState state, @Nullable AbstractMinecartEntity cart) {
      if (cart != null && !cart.level.isClientSide()) {
        RailShape shape = TrackTools.getTrackDirectionRaw(state);
        if (TrackShapeHelper.isLevelStraight(shape) && isDerailing(cart)) {
          cart.getPersistentData().putByte(CartConstants.TAG_DERAIL, (byte) 100);
          Vector3d motion = cart.getDeltaMovement();
          if (Math.abs(motion.x()) > Math.abs(motion.z()))
            cart.setDeltaMovement(motion.x(), motion.y(), motion.x());
          else
            cart.setDeltaMovement(motion.z(), motion.y(), motion.z());

          // TODO make derail ( is this not good enough? -CJ )
          switch (shape) {
            case NORTH_SOUTH:
              return RailShape.EAST_WEST;
            case EAST_WEST:
              return RailShape.NORTH_SOUTH;
            default:
              break;
          }
        }
      }
      return null;
    }
  },
  HIGH_SPEED {
    @Override
    public void onMinecartPass(World world, AbstractMinecartEntity cart, BlockPos pos,
        @Nullable TrackKit trackKit) {
      HighSpeedTools.performHighSpeedChecks(world, pos, cart, trackKit);
    }

    @Override
    public double getMaxSpeed(World world, @Nullable AbstractMinecartEntity cart,
        BlockPos pos) {
      RailShape dir = TrackTools.getTrackDirection(world, pos, cart);
      if (dir.isAscending())
        return HighSpeedTools.SPEED_SLOPE;
      return HighSpeedTools.speedForNextTrack(world, pos, 0, cart);
    }
  },
  REINFORCED {
    public static final float MAX_SPEED = 0.499f;
    public static final float CORNER_SPEED = 0.4f;

    @Override
    public double getMaxSpeed(World world, @Nullable AbstractMinecartEntity cart,
        BlockPos pos) {
      RailShape dir = TrackTools.getTrackDirection(world, pos, cart);
      if (TrackShapeHelper.isTurn(dir) || TrackShapeHelper.isAscending(dir))
        return CORNER_SPEED;
      return MAX_SPEED;
    }
  },
  STRAP_IRON {
    @Override
    public double getMaxSpeed(World world, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
      return Railcraft.serverConfig.strapIronTrackMaxSpeed.get();
    }
  };

  public void onMinecartPass(World world, AbstractMinecartEntity cart, BlockPos pos,
      @Nullable TrackKit trackKit) {}

  public @Nullable RailShape getRailDirectionOverride(IBlockReader world,
      BlockPos pos, BlockState state, @Nullable AbstractMinecartEntity cart) {
    return null;
  }

  public double getMaxSpeed(World world, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
    return 0.4D;
  }
}
