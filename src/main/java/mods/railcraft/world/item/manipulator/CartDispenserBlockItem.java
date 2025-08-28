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

public class CartDispenserBlockItem extends BlockItem {

  public CartDispenserBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.CART_DISPENSER)
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_ROTATE)
        .withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.translatable(Translations.Tips.PAIR_WITH_CONTROL_TRACK)
        .withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_DISPENSE_CARTS)
        .withStyle(ChatFormatting.RED));
  }
}
