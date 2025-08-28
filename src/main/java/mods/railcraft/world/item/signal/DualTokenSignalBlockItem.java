package mods.railcraft.world.item.signal;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class DualTokenSignalBlockItem extends BlockItem {

  public DualTokenSignalBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.TOKEN_SIGNAL)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.DISTANT_SIGNAL)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.AERIAL_LINKAGES)
        .withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.TOKEN_AREA, 1))
        .withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.CONTROLLERS, 1))
        .withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.RECEIVERS, 1))
        .withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.translatable(Translations.Tips.RELEVANT_TOOLS)
        .withStyle(ChatFormatting.RED));
    tooltipAdder.accept(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.SIGNAL_TUNER))
        .withStyle(ChatFormatting.RED));
    tooltipAdder.accept(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.SIGNAL_SURVEYOR))
        .withStyle(ChatFormatting.RED));
  }
}
