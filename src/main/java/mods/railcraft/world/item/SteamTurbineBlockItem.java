package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class SteamTurbineBlockItem extends BlockItem {

  public SteamTurbineBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.MULTIBLOCK3X2X2)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.STEAM_TURBINE_DESC_1)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.STEAM_TURBINE_DESC_2)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.STEAM_TURBINE_DESC_3)
        .withStyle(ChatFormatting.GRAY));
  }
}
