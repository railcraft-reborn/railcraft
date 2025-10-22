package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

class SwitchTrackComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlock() instanceof SwitchTrackActuatorBlock) {
      Component info;
      if (SwitchTrackActuatorBlock.isSwitched(accessor.getBlockState())) {
        info = Component.translatable(Translations.LookingAt.YES)
            .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
      } else {
        info = Component.translatable(Translations.LookingAt.NO)
            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
      }
      tooltip.add(Component.translatable(Translations.LookingAt.SWITCHED).append(info));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("switch_track");
  }
}
