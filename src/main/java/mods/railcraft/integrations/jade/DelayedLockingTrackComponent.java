package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.track.DelayedLockingTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.DelayedLockingTrackBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

class DelayedLockingTrackComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlockEntity() instanceof DelayedLockingTrackBlockEntity track) {
      var seconds = track.getDelay() / SharedConstants.TICKS_PER_SECOND;
      tooltip.add(Component.translatable(Translations.Screen.DELAYED_LOCKING_TRACK_DELAY, seconds));
      if (DelayedLockingTrackBlock.isHolding(accessor.getBlockState())) {
        tooltip.add(Component.translatable(Translations.Tips.DELAYED_LOCKING_TRACK_HOLDING)
            .withStyle(ChatFormatting.GOLD));
      }
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("track_component");
  }
}
