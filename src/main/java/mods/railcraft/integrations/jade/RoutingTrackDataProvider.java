package mods.railcraft.integrations.jade;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.TooltipPosition;

class RoutingTrackDataProvider implements IServerDataProvider<BlockAccessor> {

  @Override
  public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
    if (accessor.getBlockEntity() instanceof RoutingTrackBlockEntity routingTrack) {
      var item = routingTrack.container().getItem(0);
      if (item.is(RailcraftItems.GOLDEN_TICKET.get())) {
        var dest = TicketItem.getDestination(item);
        tag.putString(CompoundTagKeys.DESTINATION, dest);
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
