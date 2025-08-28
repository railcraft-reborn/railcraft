package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.world.entity.vehicle.TrackRelayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class TrackRelayerCartItem extends CartItem {

  public TrackRelayerCartItem(Properties properties) {
    super(TrackRelayer::new, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    tooltipAdder.accept(Component.translatable(Translations.Tips.TRACK_RELAYER)
        .withStyle(ChatFormatting.GRAY));
  }
}
