package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.world.entity.vehicle.TrackUndercutter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class TrackUndercutterCartItem extends CartItem {

  public TrackUndercutterCartItem(Properties properties) {
    super(TrackUndercutter::new, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    tooltipAdder.accept(Component.translatable(Translations.Tips.TRACK_UNDERCUTTER)
        .withStyle(ChatFormatting.GRAY));
  }
}
