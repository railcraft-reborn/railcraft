package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.loot.RailcraftContextIntProviders;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class CoalCokeBlockItem extends BlockItem {

  public CoalCokeBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    // Burn times are resolved on the server only, so the tooltip shows the base value
    tooltipAdder.accept(Component.translatable(Translations.Tips.COAL_COKE_BLOCK,
            RailcraftContextIntProviders.COAL_COKE_BLOCK_BURN_TIME)
        .withStyle(ChatFormatting.GRAY));
  }
}
