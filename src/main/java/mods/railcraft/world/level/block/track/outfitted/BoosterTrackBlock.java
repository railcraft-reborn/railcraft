package mods.railcraft.world.level.block.track.outfitted;

import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BoosterTrackBlock extends PoweredOutfittedTrackBlock {

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
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    var trackType = this.getTrackType();
    if (TrackTypes.REINFORCED.get() == trackType) {
      this.minecartPassStandard(blockState, level, pos, cart, BOOST_FACTOR_REINFORCED);
    } else if (trackType.isHighSpeed()) {
      this.minecartPassHighSpeed(blockState, level, pos, cart);
    } else {
      this.minecartPassStandard(blockState, level, pos, cart, BOOST_FACTOR);
    }
  }

  private void minecartPassStandard(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart, double boostFactor) {
    var dir = getRailShapeRaw(blockState);
    var motion = cart.getDeltaMovement();
    var speed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
    if (this.isPowered(blockState, level, pos)) {
      if (speed > BOOST_THRESHOLD) {
        cart.setDeltaMovement(
            motion.add((motion.x() / speed) * boostFactor, 0, (motion.z() / speed) * boostFactor));
      } else {
        CartTools.startBoost(cart, pos, dir, START_BOOST);
      }
      return;
    }

    if (speed < STALL_THRESHOLD) {
      cart.setDeltaMovement(Vec3.ZERO);
    } else {
      cart.setDeltaMovement(motion.multiply(SLOW_FACTOR, 0.0D, SLOW_FACTOR));
    }
  }

  private void minecartPassHighSpeed(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    Vec3 motion = cart.getDeltaMovement();
    if (this.isPowered(blockState, level, pos)) {
      var speed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
      var dir = getRailShapeRaw(blockState);
      if (speed > BOOST_THRESHOLD) {
        cart.setDeltaMovement(
            motion.add((motion.x() / speed) * BOOST_FACTOR_HS, 0,
                (motion.z() / speed) * BOOST_FACTOR_HS));
      } else {
        CartTools.startBoost(cart, pos, dir, START_BOOST);
      }
      return;
    }

    if (RollingStock.getOrThrow(cart).isHighSpeed()) {
      if (cart instanceof Locomotive locomotive) {
        locomotive.forceIdle(20);
      }
      cart.setDeltaMovement(motion.multiply(SLOW_FACTOR_HS, 0.0D, SLOW_FACTOR_HS));
      return;
    }

    if (Math.abs(motion.x()) > 0) {
      cart.setDeltaMovement(Math.copySign(0.38f, motion.x()), motion.y(), motion.z());
    }

    if (Math.abs(motion.z()) > 0) {
      cart.setDeltaMovement(motion.x(), motion.y(), Math.copySign(0.38f, motion.z()));
    }
  }

  @Override
  public int getPowerPropagation(BlockState blockState, Level level, BlockPos pos) {
    return POWER_PROPAGATION;
  }

  @Override
  public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.BOOSTER_TRACK)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.SLOW_UNPOWERED)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_ENABLE)
        .withStyle(ChatFormatting.RED));
  }
}
