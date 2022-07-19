package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ControlTrackBlock extends ReversiblePoweredOutfittedTrackBlock {

  private static final double BOOST_AMOUNT = 0.02;
  private static final double SLOW_AMOUNT = 0.02;

  public ControlTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public int getPowerPropagation(BlockState blockState, Level level, BlockPos pos) {
    return 16;
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    final RailShape trackShape = getRailShapeRaw(blockState);
    final Vec3 deltaMovement = cart.getDeltaMovement();
    final boolean powered = isPowered(blockState);
    final boolean reversed = isReversed(blockState);
    if (TrackShapeHelper.isNorthSouth(trackShape)) {
      if (deltaMovement.z() <= 0) {
        if (isPowered(blockState) ^ !reversed) {
          cart.setDeltaMovement(deltaMovement.subtract(0.0D, 0.0D, BOOST_AMOUNT));
        } else {
          cart.setDeltaMovement(deltaMovement.add(0.0D, 0.0D, SLOW_AMOUNT));
        }
      } else if (deltaMovement.z() >= 0) {
        if (!powered ^ !reversed) {
          cart.setDeltaMovement(deltaMovement.add(0.0D, 0.0D, BOOST_AMOUNT));
        } else {
          cart.setDeltaMovement(deltaMovement.subtract(0.0D, 0.0D, SLOW_AMOUNT));
        }
      }
    } else {
      if (deltaMovement.x() <= 0) {
        if (powered ^ reversed) {
          cart.setDeltaMovement(deltaMovement.subtract(BOOST_AMOUNT, 0.0D, 0.0D));
        } else {
          cart.setDeltaMovement(deltaMovement.add(SLOW_AMOUNT, 0.0D, 0.0D));
        }
      } else if (deltaMovement.x() >= 0) {
        if (!powered ^ reversed) {
          cart.setDeltaMovement(deltaMovement.add(BOOST_AMOUNT, 0.0D, 0.0D));
        } else {
          cart.setDeltaMovement(deltaMovement.subtract(SLOW_AMOUNT, 0.0D, 0.0D));
        }
      }
    }

    if (cart instanceof Locomotive && ((Locomotive) cart).isShutdown()) {
      double yaw = cart.getYRot() * Math.PI / 180D;
      double cos = Math.cos(yaw);
      double sin = Math.sin(yaw);
      float limit = 0.01f;
      if ((deltaMovement.x() > limit && cos < 0)
          || (deltaMovement.x() < -limit && cos > 0)
          || (deltaMovement.z() > limit && sin < 0)
          || (deltaMovement.z() < -limit && sin > 0)) {
        cart.setYRot((cart.getYRot() + 180.0F) % 360.0F);
        cart.yRotO = cart.getYRot();
      }
    }
  }
}
