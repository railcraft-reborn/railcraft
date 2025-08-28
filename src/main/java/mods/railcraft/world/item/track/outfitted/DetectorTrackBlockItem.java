package mods.railcraft.world.item.track.outfitted;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class DetectorTrackBlockItem extends BlockItem {

  public DetectorTrackBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.DETECTOR_TRACK)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_CHANGE_DETECTION_DIRECTION)
        .withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.translatable(Translations.Tips.COMPARATOR_OUTPUT_FROM_CARTS)
        .withStyle(ChatFormatting.RED));
  }
}
