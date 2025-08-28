package mods.railcraft.world.item.charge;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class BatteryBlockItem extends BlockItem {

  public BatteryBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.CHARGE_NETWORK_BATTERY)
        .withStyle(ChatFormatting.BLUE));
    var battery = stack.get(RailcraftDataComponents.BATTERY);
    if (battery != null) {
      battery.addToTooltip(context, tooltipAdder, flag, this.components());
    }
  }
}
