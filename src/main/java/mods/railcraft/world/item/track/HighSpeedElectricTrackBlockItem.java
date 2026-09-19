package mods.railcraft.world.item.track;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class HighSpeedElectricTrackBlockItem extends BlockItem {

  public HighSpeedElectricTrackBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.DANGER)
        .append(CommonComponents.SPACE)
        .append(Component.translatable(Translations.Tips.HIGH_VOLTAGE_SPEED))
        .withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.translatable(Translations.Tips.VERY_FAST)
        .withStyle(ChatFormatting.WHITE));
    tooltipAdder.accept(Component.translatable(Translations.Tips.POWERED_BY_ELECTRICITY)
        .withStyle(ChatFormatting.WHITE));
    tooltipAdder.accept(Component.translatable(Translations.Tips.USE_ELECTRIC_LOCOMOTIVE)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.REQUIRE_BOOSTERS_TRANSITION)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.CANNOT_MAKE_CORNERS_HIGH_SPEED)
        .withStyle(ChatFormatting.GRAY));
  }
}
