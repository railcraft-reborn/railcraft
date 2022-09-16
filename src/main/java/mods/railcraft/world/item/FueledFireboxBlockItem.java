package mods.railcraft.world.item;

import java.util.List;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class FueledFireboxBlockItem extends BlockItem {

  public FueledFireboxBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents,
      TooltipFlag isAdvanced) {
    tooltipComponents.add(Component.translatable(Translations.Tips.FUELED_BOILER_FIREBOX)
        .withStyle(ChatFormatting.GRAY));
    var dimensions = String.join(", ", List.of("1x1", "2x2", "3x3"));
    tooltipComponents.add(Component.translatable(Translations.Tips.DIMENSIONS, dimensions)
        .withStyle(ChatFormatting.GRAY));
  }
}
