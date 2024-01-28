package mods.railcraft.world.level.block.track.outfitted;

import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.Translations;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

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
    if (RailShapeUtil.isNorthSouth(trackShape)) {
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

    if (cart instanceof Locomotive locomotive && locomotive.isShutdown()) {
      double yaw = cart.getYRot() * Mth.DEG_TO_RAD;
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


  @Override
  public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.CONTROL_TRACK)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_CHANGE_DIRECTION)
        .withStyle(ChatFormatting.BLUE));
    lines.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_CHANGE_DIRECTION)
        .withStyle(ChatFormatting.RED));
  }
}
