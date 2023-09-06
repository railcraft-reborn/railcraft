package mods.railcraft.world.level.block.track.behaivor;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.world.entity.vehicle.MinecartUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public enum SpeedController implements TrackType.EventHandler {

  IRON, // 0.4 vanilla
  ABANDONED {
    @Override
    public double getMaxSpeed(Level level, @Nullable AbstractMinecart cart, BlockPos pos) {
      // vanilla is 0.4f, this track is ""broken"" so you only get 90% of the vanilla speed
      return 0.36D;
    }

    private boolean isDerailing(RollingStock cart) {
      return (MinecartUtil.getCartSpeedUncapped(cart.entity().getDeltaMovement()) > 0.35F
          && cart.level().getRandom().nextInt(500) == 250)
          || cart.train().stream().anyMatch(RollingStock::isDerailed);
    }

    @Override
    @Nullable
    // FIXME: Client and Server sync is not maintained here. Could result in strange
    // behavior.
    public Optional<RailShape> getRailShapeOverride(BlockGetter level, BlockPos pos,
        BlockState state,
        @Nullable AbstractMinecart cart) {
      if (cart == null || cart.level().isClientSide()) {
        return Optional.empty();
      }

      var shape = TrackUtil.getRailShapeRaw(state);
      if (!RailShapeUtil.isLevelStraight(shape)) {
        return Optional.empty();
      }

      var extension = RollingStock.getOrThrow(cart);
      if (!this.isDerailing(extension)) {
        return Optional.empty();
      }

      extension.setDerailedRemainingTicks(100);
      var motion = cart.getDeltaMovement();
      if (Math.abs(motion.x()) > Math.abs(motion.z())) {
        cart.setDeltaMovement(motion.x(), motion.y(), motion.x());
      } else {
        cart.setDeltaMovement(motion.z(), motion.y(), motion.z());
      }

      // TODO make derail ( is this not good enough? -CJ )
      return switch (shape) {
        case NORTH_SOUTH -> Optional.of(RailShape.EAST_WEST);
        case EAST_WEST -> Optional.of(RailShape.NORTH_SOUTH);
        default -> Optional.empty();
      };
    }
  },
  HIGH_SPEED {

    @Override
    public void minecartPass(Level level, AbstractMinecart cart, BlockPos pos) {
      RollingStock.getOrThrow(cart).checkHighSpeed(pos);
    }

    @Override
    public double getMaxSpeed(Level level, @Nullable AbstractMinecart cart,
        BlockPos pos) {
      return HighSpeedTrackUtil.getMaxSpeed(level, cart, pos);
    }
  },
  REINFORCED {

    @Override
    public double getMaxSpeed(Level level, @Nullable AbstractMinecart cart,
        BlockPos pos) {
      var shape = TrackUtil.getTrackDirection(level, pos, cart);
      // 0.4f vanilla, this gets 10% more so 1.1*(ourspeed)
      return RailShapeUtil.isTurn(shape) || shape.isAscending() ? 0.4F : 0.44F;
    }
  },

  STRAP_IRON {
    @Override
    public double getMaxSpeed(Level level, @Nullable AbstractMinecart cart, BlockPos pos) {
      return RailcraftConfig.SERVER.strapIronTrackMaxSpeed.get();
    }
  };
}
