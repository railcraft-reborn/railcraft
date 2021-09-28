package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.carts.CartTools;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.entity.LocomotiveEntity;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class HighSpeedTransitionTrackBlock extends AbstractPoweredTrackBlock {

  private static final double BOOST_AMOUNT = 0.04;
  private static final double SLOW_FACTOR = 0.65;
  private static final double BOOST_THRESHOLD = 0.01;
  private static final double START_BOOST = 0.02;

  public static final BooleanProperty REVERSED = BooleanProperty.create("reversed");

  public HighSpeedTransitionTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false)
        .setValue(REVERSED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(REVERSED);
  }

  @Override
  public int getPowerPropagation(BlockState blockState, World level, BlockPos pos) {
    return 16;
  }

  @Override
  public void onMinecartPass(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart) {
    final boolean reversed = blockState.getValue(REVERSED);
    final RailShape railShape = getRailShapeRaw(blockState);
    if (blockState.getValue(POWERED)) {
      Vector3d motion = cart.getDeltaMovement();
      double speed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
      if (speed > BOOST_THRESHOLD) {
        boolean highSpeed = HighSpeedTools.isTravellingHighSpeed(cart);
        if (TrackShapeHelper.isNorthSouth(railShape)) {
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
        CartTools.startBoost(cart, pos, railShape, START_BOOST);
      }
    }
  }

  private static void boostCartSpeed(AbstractMinecartEntity cart, double currentSpeed) {
    Vector3d motion = cart.getDeltaMovement();
    cart.setDeltaMovement(motion.add((motion.x() / currentSpeed) * BOOST_AMOUNT, 0.0D,
        (motion.z() / currentSpeed) * BOOST_AMOUNT));
  }

  private static void slowCartSpeed(AbstractMinecartEntity cart) {
    if (cart instanceof LocomotiveEntity) {
      ((LocomotiveEntity) cart).forceIdle(20);
    }
    cart.setDeltaMovement(cart.getDeltaMovement().multiply(SLOW_FACTOR, 1.0D, SLOW_FACTOR));
  }

  private static void slowOrNormalCartSpeed(AbstractMinecartEntity cart, boolean highSpeed) {
    if (highSpeed) {
      slowCartSpeed(cart);
    } else {
      normalCartSpeed(cart);
    }
  }

  private static void normalCartSpeed(AbstractMinecartEntity cart) {
    Vector3d motion = cart.getDeltaMovement();
    if (Math.abs(motion.x()) > 0.01) {
      cart.setDeltaMovement(Math.copySign(0.3f, motion.x()), motion.y(), motion.z());
    }
    if (Math.abs(motion.z()) > 0.01) {
      cart.setDeltaMovement(motion.x(), motion.y(), Math.copySign(0.3f, motion.z()));
    }
  }
}
