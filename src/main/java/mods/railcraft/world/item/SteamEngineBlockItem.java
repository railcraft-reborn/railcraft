package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.util.HumanReadableNumberFormatter;
import mods.railcraft.world.level.block.SteamEngineBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class SteamEngineBlockItem extends BlockItem {

  public SteamEngineBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    if (!(this.getBlock() instanceof SteamEngineBlock engine)) {
      return;
    }
    var tier = engine.getTier();
    tooltipAdder.accept(Component.translatable(Translations.Tips.STEAM_ENGINE_OUTPUT,
            HumanReadableNumberFormatter.format(tier.getOutput()))
        .withStyle(ChatFormatting.GRAY));
    tooltipAdder.accept(Component.translatable(Translations.Tips.STEAM_ENGINE_STEAM_USAGE,
            tier.getSteamUsedPerTick())
        .withStyle(ChatFormatting.GRAY));
    if (tier == SteamEngineBlock.Tier.HOBBYIST) {
      tooltipAdder.accept(Component.translatable(Translations.Tips.HOBBYIST_STEAM_ENGINE_DESC)
          .withStyle(ChatFormatting.GRAY));
    }
    tooltipAdder.accept(Component.translatable(Translations.Tips.STEAM_ENGINE_REDSTONE)
        .withStyle(ChatFormatting.RED));
    tooltipAdder.accept(Component.translatable(Translations.Tips.STEAM_ENGINE_CROWBAR)
        .withStyle(ChatFormatting.GRAY));
  }
}
