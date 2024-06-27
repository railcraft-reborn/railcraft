package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

class EmbarkingTrackComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlock() instanceof EmbarkingTrackBlock) {
      var radius = EmbarkingTrackBlock.getRadius(accessor.getBlockState());
      tooltip.add(Component.translatable(Translations.Screen.EMBARKING_TRACK_RADIUS, radius));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("track_component");
  }
}
