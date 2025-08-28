package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations.Tips;
import mods.railcraft.api.charge.ChargeProtectionItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class OverallsItem extends Item implements ChargeProtectionItem {

  public OverallsItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    tooltipAdder.accept(Component.translatable(Tips.OVERALLS).withStyle(ChatFormatting.GRAY));
  }
}
