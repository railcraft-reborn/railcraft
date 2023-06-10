package mods.railcraft.world.level.block.track;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;

public class ReinforcedTrackBlock extends TrackBlock {

  public ReinforcedTrackBlock(Properties properties) {
    super(TrackTypes.REINFORCED, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.REINFORCED_TRACK)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.EXPLOSION_RESISTANT)
        .withStyle(ChatFormatting.GRAY));
  }
}
