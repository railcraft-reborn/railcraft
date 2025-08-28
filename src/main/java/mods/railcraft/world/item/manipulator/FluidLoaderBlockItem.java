package mods.railcraft.world.item.manipulator;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class FluidLoaderBlockItem extends BlockItem {

  public FluidLoaderBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.FLUID_LOADER)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.PLACE_ABOVE_TRACK)
        .withStyle(ChatFormatting.RED));
  }
}
