package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.RoutingTrackBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;

class RoutingTrackComponent implements IBlockComponentProvider,
    IServerDataProvider<BlockAccessor> {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlock() instanceof RoutingTrackBlock) {
      var tag = accessor.getServerData();
      if (tag.contains("destination")) {
        tooltip.add(Component.translatable(Translations.Tips.ROUTING_TICKET_DEST)
            .append(" ").append(tag.getString("destination")));
      }
      tooltip.remove(Identifiers.UNIVERSAL_ITEM_STORAGE);
    }
  }

  @Override
  public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
    if (accessor.getBlockEntity() instanceof RoutingTrackBlockEntity routingTrack) {
      var item = routingTrack.container().getItem(0);
      if (item.is(RailcraftItems.GOLDEN_TICKET.get())) {
        var dest = TicketItem.getDestination(item);
        tag.putString("destination", dest);
      }
    }
  }

  @Override
  public int getDefaultPriority() {
    return TooltipPosition.TAIL;
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("track_component");
  }
}
