package mods.railcraft.world.level.block.track.outfitted;

import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.Translations;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class OneWayTrackBlock extends ReversiblePoweredOutfittedTrackBlock {

  private static final double LOSS_FACTOR = 0.49D;

  public OneWayTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
    if (!isPowered(state)) {
      return;
    }

    var deltaMovement = cart.getDeltaMovement();
    var railShape = getRailShapeRaw(state);
    if (RailShapeUtil.isEastWest(railShape)) {
      if (isReversed(state) ? deltaMovement.x() > 0.0D : deltaMovement.x() < 0.0D) {
        var distX = cart.getX() - (pos.getX() + 0.5D);
        if (isReversed(state) ? distX > 0.01 : distX < -0.01) {
          cart.setPos(pos.getX() + 0.5D, cart.getY(), cart.getZ());
        }
        var deltaX = isReversed(state)
            ? -Math.abs(deltaMovement.x()) * LOSS_FACTOR
            : Math.abs(deltaMovement.x()) * LOSS_FACTOR;
        cart.setDeltaMovement(deltaX, deltaMovement.y(), deltaMovement.z());
      }
    } else if (RailShapeUtil.isNorthSouth(railShape)) {
      if (isReversed(state) ? deltaMovement.z() < 0.0D : deltaMovement.z() > 0.0D) {
        var distZ = cart.getZ() - (pos.getZ() + 0.5D);
        if (isReversed(state) ? distZ < -0.01 : distZ > 0.01) {
          cart.setPos(cart.getX(), cart.getY(), pos.getZ() + 0.5D);
        }
        var deltaZ = isReversed(state)
            ? Math.abs(deltaMovement.z()) * LOSS_FACTOR
            : -Math.abs(deltaMovement.z()) * LOSS_FACTOR;
        cart.setDeltaMovement(deltaMovement.x(), deltaMovement.y(), deltaZ);
      }
    }
  }


  @Override
  public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.ONE_WAY_TRACK)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_CHANGE_DIRECTION)
        .withStyle(ChatFormatting.BLUE));
  }
}
