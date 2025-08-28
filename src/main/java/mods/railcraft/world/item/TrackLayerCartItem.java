package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.world.entity.vehicle.TrackLayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class TrackLayerCartItem extends CartItem {

  public TrackLayerCartItem(Properties properties) {
    super(TrackLayer::new, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    tooltipAdder.accept(Component.translatable(Translations.Tips.TRACK_LAYER)
        .withStyle(ChatFormatting.GRAY));
  }
}
