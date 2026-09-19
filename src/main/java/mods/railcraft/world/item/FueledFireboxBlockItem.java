package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Consumer;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class FueledFireboxBlockItem extends BlockItem {

  public FueledFireboxBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.FUELED_BOILER_FIREBOX)
        .withStyle(ChatFormatting.GRAY));
    var dimensions = String.join(", ", List.of("1x1", "2x2", "3x3"));
    tooltipAdder.accept(Component.translatable(Translations.Tips.DIMENSIONS, dimensions)
        .withStyle(ChatFormatting.GRAY));
  }
}
