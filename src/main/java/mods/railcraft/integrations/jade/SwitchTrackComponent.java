package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

public class SwitchTrackComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlock() instanceof SwitchTrackActuatorBlock) {
      var theme = IThemeHelper.get();
      Component info;
      if (SwitchTrackBlock.isSwitched(accessor.getBlockState())) {
        info = theme.success(Component.translatable(Translations.LookingAt.YES));
      } else {
        info = theme.danger(Component.translatable(Translations.LookingAt.NO));
      }
      tooltip.add(Component.translatable(Translations.LookingAt.SWITCHED).append(info));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("switch_track");
  }
}
