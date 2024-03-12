package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.track.LauncherTrackBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class LauncherTrackComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlockEntity() instanceof LauncherTrackBlockEntity launcherTrack) {
      var force = launcherTrack.getLaunchForce();
      tooltip.add(Component.translatable(Translations.Screen.LAUNCHER_TRACK_LAUNCH_FORCE, force));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("track_component");
  }
}
