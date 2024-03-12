package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.track.outfitted.ThrottleTrackBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class ThrottleTrackComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlock() instanceof ThrottleTrackBlock) {
      var speed = ThrottleTrackBlock.getSpeedMode(accessor.getBlockState()).getLevel();
      var reverse = ThrottleTrackBlock.isReverse(accessor.getBlockState())
          ? Translations.LookingAt.YES : Translations.LookingAt.NO;
      tooltip.add(Component.translatable(Translations.LookingAt.SPEED, speed));
      tooltip.add(Component.translatable(Translations.LookingAt.REVERSE)
          .append(Component.translatable(reverse)));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("track_component");
  }
}
