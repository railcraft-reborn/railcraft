package mods.railcraft.world.inventory;

import mods.railcraft.world.entity.vehicle.TrackRelayer;
import mods.railcraft.world.inventory.slot.SlotLinked;
import mods.railcraft.world.inventory.slot.SlotTrackFilter;
import net.minecraft.world.entity.player.Inventory;

public class TrackRelayerMenu extends RailcraftMenu {

  private final TrackRelayer trackRelayer;

  public TrackRelayerMenu(int id, Inventory inventory, TrackRelayer trackRelayer) {
    super(RailcraftMenuTypes.TRACK_RELAYER.get(), id, inventory.player, trackRelayer::stillValid);
    this.trackRelayer = trackRelayer;

    this.addSlot(new SlotTrackFilter(trackRelayer.getPattern(), 0, 26, 43));
    var track = new SlotTrackFilter(trackRelayer.getPattern(), 1, 71, 43);
    this.addSlot(track);
    this.addSlot(new SlotLinked(trackRelayer, 0, 130, 43, track));

    this.addInventorySlots(inventory);
  }

  public TrackRelayer getTrackRelayer() {
    return trackRelayer;
  }
}
