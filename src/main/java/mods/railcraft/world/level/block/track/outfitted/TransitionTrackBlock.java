package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TransitionTrackBlock extends ReversiblePoweredOutfittedTrackBlock {

  private static final double BOOST_AMOUNT = 0.04;
  private static final double SLOW_FACTOR = 0.65;
  private static final double BOOST_THRESHOLD = 0.01;
  private static final double START_BOOST = 0.02;

  public TransitionTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
  }

  @Override
  public int getPowerPropagation(BlockState blockState, Level level, BlockPos pos) {
    return 16;
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    final var reversed = isReversed(blockState);
    final var railShape = getRailShapeRaw(blockState);

    if (!isPowered(blockState)) {
      return;
    }

    final var deltaMovement = cart.getDeltaMovement();
    final var speed = CartUtil.getCartSpeedUncapped(deltaMovement);

    if (speed <= BOOST_THRESHOLD) {
      CartTools.startBoost(cart, pos, railShape, START_BOOST);
      return;
    }

    final var highSpeed = MinecartExtension.getOrThrow(cart).isHighSpeed();
    if (RailShapeUtil.isNorthSouth(railShape)) {
      if (reversed ^ deltaMovement.z() < 0) {
        boostCartSpeed(cart, speed);
      } else {
        slowOrNormalCartSpeed(cart, highSpeed);
      }
    } else {
      if (!reversed ^ deltaMovement.x() < 0) {
        boostCartSpeed(cart, speed);
      } else {
        slowOrNormalCartSpeed(cart, highSpeed);
      }
    }
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    level.setBlockAndUpdate(pos, blockState.setValue(REVERSED, !blockState.getValue(REVERSED)));
    return true;
  }

  private static void boostCartSpeed(AbstractMinecart cart, double currentSpeed) {
    Vec3 motion = cart.getDeltaMovement();
    cart.setDeltaMovement(motion.add((motion.x() / currentSpeed) * BOOST_AMOUNT, 0.0D,
        (motion.z() / currentSpeed) * BOOST_AMOUNT));
  }

  private static void slowCartSpeed(AbstractMinecart cart) {
    if (cart instanceof Locomotive locomotive) {
      locomotive.forceIdle(20);
    }
    cart.setDeltaMovement(cart.getDeltaMovement().multiply(SLOW_FACTOR, 1.0D, SLOW_FACTOR));
  }

  private static void slowOrNormalCartSpeed(AbstractMinecart cart, boolean highSpeed) {
    if (highSpeed) {
      slowCartSpeed(cart);
    } else {
      normalCartSpeed(cart);
    }
  }

  private static void normalCartSpeed(AbstractMinecart cart) {
    final Vec3 deltaMovement = cart.getDeltaMovement();
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
