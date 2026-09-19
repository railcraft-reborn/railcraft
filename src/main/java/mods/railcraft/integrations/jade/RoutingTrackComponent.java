package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.track.outfitted.RoutingTrackBlock;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.JadeIds;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;

class RoutingTrackComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlock() instanceof RoutingTrackBlock) {
      var tag = accessor.getServerData();
      tag.getString(CompoundTagKeys.DESTINATION).ifPresent(
          dest -> tooltip.add(Component.translatable(Translations.Tips.ROUTING_TICKET_DEST)
              .append(CommonComponents.SPACE)
              .append(dest))
      );
      tooltip.remove(JadeIds.UNIVERSAL_ITEM_STORAGE);
    }
  }

  @Override
  public int getDefaultPriority() {
    return TooltipPosition.TAIL;
  }

  @Override
  public Identifier getUid() {
    return RailcraftConstants.id("track_component");
  }
}
