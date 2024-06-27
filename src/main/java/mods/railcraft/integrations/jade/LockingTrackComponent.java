package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

class LockingTrackComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlock() instanceof LockingTrackBlock) {
      var mode = LockingTrackBlock.getLockingMode(accessor.getBlockState()).getDisplayName();
      tooltip.add(Component.translatable(Translations.Tips.CURRENT_MODE)
          .append(" ").append(mode.copy().withStyle(ChatFormatting.DARK_PURPLE)));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("track_component");
  }
}
