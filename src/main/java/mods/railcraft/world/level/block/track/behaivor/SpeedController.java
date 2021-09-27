package mods.railcraft.world.level.block.track.behaivor;

import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.CartToolsAPI;
import mods.railcraft.api.tracks.TrackType;
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
public enum SpeedController implements TrackType.EventHandler {

  IRON, // 0.4 vanilla
  ABANDONED {
    @Override
    public double getMaxSpeed(World level, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
      return 0.36D; // vanilla is 0.4f, this track is ""broken"" so you only get 90% of the vanilla
                    // speed
    }

    private boolean isDerailing(
        AbstractMinecartEntity cart) {
      if (CartToolsAPI.getCartSpeedUncapped(cart) > 0.35F && MiscTools.RANDOM.nextInt(500) == 250)
        return true;
      return Train.streamCarts(cart)
          .anyMatch(Railcraft.getInstance().getMinecartHandler()::isDerailed);
    }

    @Override
    @Nullable
    // FIXME: Client and Server sync is not maintained here. Could result in strange behavior.
    public RailShape getRailShapeOverride(IBlockReader level,
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
    public void minecartPass(World level, AbstractMinecartEntity cart, BlockPos pos) {
      HighSpeedTools.performHighSpeedChecks(level, pos, cart);
    }

    @Override
    public double getMaxSpeed(World level, @Nullable AbstractMinecartEntity cart,
        BlockPos pos) {
      return TrackTools.getTrackDirection(level, pos, cart).isAscending()
          ? HighSpeedTools.SPEED_SLOPE
          : HighSpeedTools.speedForNextTrack(level, pos, 0, cart);
    }
  },
  REINFORCED {

    @Override
    public double getMaxSpeed(World level, @Nullable AbstractMinecartEntity cart,
        BlockPos pos) {
      final RailShape shape = TrackTools.getTrackDirection(level, pos, cart);
      // 0.4f vanilla, this gets 10% more so 1.1*(ourspeed)
      return TrackShapeHelper.isTurn(shape) || TrackShapeHelper.isAscending(shape) ? 0.4F : 0.44F;
    }
  },

  STRAP_IRON {

    @Override
    public double getMaxSpeed(World level, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
      return Railcraft.serverConfig.strapIronTrackMaxSpeed.get();
    }
  };
}
