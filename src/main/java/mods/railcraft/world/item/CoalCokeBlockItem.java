package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.util.FuelUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class CoalCokeBlockItem extends BlockItem {

  public CoalCokeBlockItem(Block block, Properties properties) {
    super(block, properties.component(DataComponents.COOKING_FUEL,
        FuelUtil.cookingFuel(32000)));
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    if (context.level() == null) {
      return;
    }
    var burnTime = FuelUtil.burnTime(stack, context.level());
    tooltipAdder.accept(Component.translatable(Translations.Tips.COAL_COKE_BLOCK, burnTime)
        .withStyle(ChatFormatting.GRAY));
  }
}
