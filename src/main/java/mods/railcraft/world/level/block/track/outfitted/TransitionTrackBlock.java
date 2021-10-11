package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.entity.cart.CartTools;
import mods.railcraft.world.entity.cart.LocomotiveEntity;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class TransitionTrackBlock extends PoweredOutfittedTrackBlock {

  private static final double BOOST_AMOUNT = 0.04;
  private static final double SLOW_FACTOR = 0.65;
  private static final double BOOST_THRESHOLD = 0.01;
  private static final double START_BOOST = 0.02;

  public static final BooleanProperty REVERSED = ReversibleOutfittedTrackBlock.REVERSED;

  public TransitionTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(POWERED, false)
        .setValue(REVERSED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(REVERSED);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return super.getStateForPlacement(context)
        .setValue(REVERSED, context.getHorizontalDirection() == Direction.SOUTH
            || context.getHorizontalDirection() == Direction.WEST);
  }

  @Override
  public int getPowerPropagation(BlockState blockState, World level, BlockPos pos) {
    return 16;
  }

  @Override
  public void onMinecartPass(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    final boolean reversed = isReversed(blockState);
    final RailShape railShape = getRailShapeRaw(blockState);

    if (!isPowered(blockState)) {
      return;
    }

    final Vector3d motion = cart.getDeltaMovement();
    final double speed = motion.length();

    if (speed <= BOOST_THRESHOLD) {
      CartTools.startBoost(cart, pos, railShape, START_BOOST);
      return;
    }

    final boolean highSpeed = HighSpeedTools.isTravellingHighSpeed(cart);
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
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand, ItemStack itemStack) {
    level.setBlockAndUpdate(pos, blockState.setValue(REVERSED, !blockState.getValue(REVERSED)));
    return true;
  }

  public static boolean isReversed(BlockState blockState) {
    return blockState.getValue(REVERSED);
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
    final Vector3d deltaMovement = cart.getDeltaMovement();
    if (Math.abs(deltaMovement.x()) > 0.01) {
      cart.setDeltaMovement(Math.copySign(0.3F, deltaMovement.x()), deltaMovement.y(),
          deltaMovement.z());
    }
    if (Math.abs(deltaMovement.z()) > 0.0F) {
      cart.setDeltaMovement(deltaMovement.x(), deltaMovement.y(),
          Math.copySign(0.3f, deltaMovement.z()));
    }
  }
}
