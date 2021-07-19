package mods.railcraft.world.level.block.track.outfitted.kit;

import mods.railcraft.api.tracks.ITrackKitReversible;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.carts.CartTools;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.entity.LocomotiveEntity;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.vector.Vector3d;

public class TrackKitSpeedTransition extends TrackKitPowered implements ITrackKitReversible {

  private static final double BOOST_AMOUNT = 0.04;
  private static final double SLOW_FACTOR = 0.65;
  private static final double BOOST_THRESHOLD = 0.01;
  private static final double START_BOOST = 0.02;
  private boolean reversed;

  @Override
  public TrackKit getTrackKit() {
    return TrackKits.HIGH_SPEED_TRANSITION.get();
  }

  @Override
  public int getRenderState() {
    int state = isReversed() ? 1 : 0;
    if (isPowered())
      state += 2;
    return state;
  }

  @Override
  public int getPowerPropagation() {
    return 16;
  }

  @Override
  public void onMinecartPass(AbstractMinecartEntity cart) {
    if (isPowered()) {
      Vector3d motion = cart.getDeltaMovement();
      double speed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
      if (speed > BOOST_THRESHOLD) {
        RailShape trackShape = getRailDirectionRaw();
        boolean highSpeed = HighSpeedTools.isTravellingHighSpeed(cart);
        if (TrackShapeHelper.isNorthSouth(trackShape)) {
          if (reversed ^ motion.z() < 0) {
            boostCartSpeed(cart, speed);
          } else {
            slowOrNormalCartSpeed(cart, highSpeed);
          }
        } else {
          if (!reversed ^ motion.x() < 0) {
            boostCartSpeed(cart, speed);
          } else {
            slowOrNormalCartSpeed(cart, highSpeed);
          }
        }
      } else {
        CartTools.startBoost(cart, getPos(), getRailDirectionRaw(), START_BOOST);
      }
    }
  }

  private void boostCartSpeed(AbstractMinecartEntity cart, double currentSpeed) {
    Vector3d motion = cart.getDeltaMovement();
    cart.setDeltaMovement(motion.add((motion.x() / currentSpeed) * BOOST_AMOUNT, 0.0D,
        (motion.z() / currentSpeed) * BOOST_AMOUNT));
  }

  private void slowCartSpeed(AbstractMinecartEntity cart) {
    if (cart instanceof LocomotiveEntity) {
      ((LocomotiveEntity) cart).forceIdle(20);
    }

    cart.setDeltaMovement(cart.getDeltaMovement().multiply(SLOW_FACTOR, 1.0D, SLOW_FACTOR));
  }

  private void slowOrNormalCartSpeed(AbstractMinecartEntity cart, boolean highSpeed) {
    if (highSpeed) {
      slowCartSpeed(cart);
    } else {
      normalCartSpeed(cart);
    }
  }

  private void normalCartSpeed(AbstractMinecartEntity cart) {
    Vector3d motion = cart.getDeltaMovement();
    if (Math.abs(motion.x()) > 0.01) {
      cart.setDeltaMovement(Math.copySign(0.3f, motion.x()), motion.y(), motion.z());
    }
    if (Math.abs(motion.z()) > 0.01) {
      cart.setDeltaMovement(motion.x(), motion.y(), Math.copySign(0.3f, motion.z()));
    }
  }

  @Override
  public void writeToNBT(CompoundNBT nbttagcompound) {
    super.writeToNBT(nbttagcompound);
    nbttagcompound.putBoolean("reversed", reversed);
  }

  @Override
  public void readFromNBT(CompoundNBT nbttagcompound) {
    super.readFromNBT(nbttagcompound);
    reversed = nbttagcompound.getBoolean("reversed");
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    data.writeBoolean(reversed);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    reversed = data.readBoolean();
    markBlockNeedsUpdate();
  }

  @Override
  public boolean isReversed() {
    return reversed;
  }

  @Override
  public void setReversed(boolean r) {
    reversed = r;
  }
}
