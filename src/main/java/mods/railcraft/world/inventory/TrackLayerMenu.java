package mods.railcraft.world.inventory;

import mods.railcraft.world.entity.vehicle.TrackLayer;
import mods.railcraft.world.inventory.slot.SlotLinked;
import mods.railcraft.world.inventory.slot.SlotTrackFilter;
import net.minecraft.world.entity.player.Inventory;

public class TrackLayerMenu extends RailcraftMenu {

  public TrackLayerMenu(int id, Inventory inventory, TrackLayer trackLayer) {
    super(RailcraftMenuTypes.TRACK_LAYER.get(), id, inventory.player, trackLayer::stillValid);

    var track = new SlotTrackFilter(trackLayer.getPattern(), 0, 49, 43);
    this.addSlot(track);
    this.addSlot(new SlotLinked(trackLayer, 0, 130, 43, track));

    this.addInventorySlots(inventory);
  }
}
