package mods.railcraft.world.level.block.track.outfitted.kit;

import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.carts.CartTools;
import mods.railcraft.world.entity.LocomotiveEntity;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.vector.Vector3d;

public class TrackKitBooster extends TrackKitPowered {

  private static final int POWER_PROPAGATION = 8;
  private static final double BOOST_FACTOR = 0.04;
  private static final double BOOST_FACTOR_REINFORCED = 0.065;
  private static final double BOOST_FACTOR_HS = 0.06;
  private static final double SLOW_FACTOR = 0.5;
  private static final double SLOW_FACTOR_HS = 0.65;
  private static final double START_BOOST = 0.02;
  private static final double STALL_THRESHOLD = 0.03;
  private static final double BOOST_THRESHOLD = 0.01;

  @Override
  public TrackKit getTrackKit() {
    return TrackKits.BOOSTER.get();
  }

  @Override
  public void onMinecartPass(AbstractMinecartEntity cart) {
    TrackType trackType = getTile().getTrackType();
    if (TrackTypes.REINFORCED.get() == trackType)
      onMinecartPassStandard(cart, BOOST_FACTOR_REINFORCED);
    else if (trackType.isHighSpeed())
      onMinecartPassHighSpeed(cart);
    else
      onMinecartPassStandard(cart, BOOST_FACTOR);

  }

  private void onMinecartPassStandard(AbstractMinecartEntity cart, double boostFactor) {
    RailShape dir = getRailDirectionRaw();
    Vector3d motion = cart.getDeltaMovement();
    double speed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
    if (isPowered()) {
      if (speed > BOOST_THRESHOLD) {
        cart.setDeltaMovement(
            motion.add((motion.x() / speed) * boostFactor, 0, (motion.z() / speed) * boostFactor));
      } else {
        CartTools.startBoost(cart, getPos(), dir, START_BOOST);
      }
    } else {
      if (speed < STALL_THRESHOLD) {
        cart.setDeltaMovement(Vector3d.ZERO);
      } else {
        cart.setDeltaMovement(motion.multiply(SLOW_FACTOR, 0.0D, SLOW_FACTOR));
      }
    }
  }

  private void onMinecartPassHighSpeed(AbstractMinecartEntity cart) {
    Vector3d motion = cart.getDeltaMovement();
    if (isPowered()) {
      double speed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
      RailShape dir = getRailDirectionRaw();
      if (speed > BOOST_THRESHOLD) {
        cart.setDeltaMovement(
            motion.add((motion.x() / speed) * BOOST_FACTOR_HS, 0,
                (motion.z() / speed) * BOOST_FACTOR_HS));
      } else {
        CartTools.startBoost(cart, getPos(), dir, START_BOOST);
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
  public int getPowerPropagation() {
    return POWER_PROPAGATION;
  }
}
