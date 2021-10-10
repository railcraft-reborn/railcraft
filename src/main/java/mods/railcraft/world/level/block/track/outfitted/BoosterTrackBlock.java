package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.entity.cart.CartTools;
import mods.railcraft.world.entity.cart.LocomotiveEntity;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class BoosterTrackBlock extends PoweredTrackBlock {

  private static final int POWER_PROPAGATION = 8;
  private static final double BOOST_FACTOR = 0.04;
  private static final double BOOST_FACTOR_REINFORCED = 0.065;
  private static final double BOOST_FACTOR_HS = 0.06;
  private static final double SLOW_FACTOR = 0.5;
  private static final double SLOW_FACTOR_HS = 0.65;
  private static final double START_BOOST = 0.02;
  private static final double STALL_THRESHOLD = 0.03;
  private static final double BOOST_THRESHOLD = 0.01;

  public BoosterTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void onMinecartPass(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart) {
    TrackType trackType = this.getTrackType();
    if (TrackTypes.REINFORCED.get() == trackType)
      this.onMinecartPassStandard(blockState, level, pos, cart, BOOST_FACTOR_REINFORCED);
    else if (trackType.isHighSpeed())
      this.onMinecartPassHighSpeed(blockState, level, pos, cart);
    else
      this.onMinecartPassStandard(blockState, level, pos, cart, BOOST_FACTOR);
  }

  private void onMinecartPassStandard(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart, double boostFactor) {
    RailShape dir = getRailShapeRaw(blockState);
    Vector3d motion = cart.getDeltaMovement();
    double speed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
    if (this.isPowered(blockState, level, pos)) {
      if (speed > BOOST_THRESHOLD) {
        cart.setDeltaMovement(
            motion.add((motion.x() / speed) * boostFactor, 0, (motion.z() / speed) * boostFactor));
      } else {
        CartTools.startBoost(cart, pos, dir, START_BOOST);
      }
    } else {
      if (speed < STALL_THRESHOLD) {
        cart.setDeltaMovement(Vector3d.ZERO);
      } else {
        cart.setDeltaMovement(motion.multiply(SLOW_FACTOR, 0.0D, SLOW_FACTOR));
      }
    }
  }

  private void onMinecartPassHighSpeed(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart) {
    Vector3d motion = cart.getDeltaMovement();
    if (this.isPowered(blockState, level, pos)) {
      double speed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
      RailShape dir = getRailShapeRaw(blockState);
      if (speed > BOOST_THRESHOLD) {
        cart.setDeltaMovement(
            motion.add((motion.x() / speed) * BOOST_FACTOR_HS, 0,
                (motion.z() / speed) * BOOST_FACTOR_HS));
      } else {
        CartTools.startBoost(cart, pos, dir, START_BOOST);
      }
    } else {
      boolean highSpeed = HighSpeedTools.isTravellingHighSpeed(cart);
      if (highSpeed) {
        if (cart instanceof LocomotiveEntity) {
          ((LocomotiveEntity) cart).forceIdle(20);
        }
        cart.setDeltaMovement(motion.multiply(SLOW_FACTOR_HS, 0.0D, SLOW_FACTOR_HS));

      } else {
        if (Math.abs(motion.x()) > 0) {
          cart.setDeltaMovement(Math.copySign(0.38f, motion.x()), motion.y(), motion.z());
        }
        if (Math.abs(motion.z()) > 0) {
          cart.setDeltaMovement(motion.x(), motion.y(), Math.copySign(0.38f, motion.z()));
        }
      }
    }
  }

  @Override
  public int getPowerPropagation(BlockState blockState, World level, BlockPos pos) {
    return POWER_PROPAGATION;
  }
}
