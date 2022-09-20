package mods.railcraft.world.inventory;

import mods.railcraft.world.entity.vehicle.TrackLayer;
import net.minecraft.world.entity.player.Inventory;

public class TrackLayerMenu extends RailcraftMenu {

  public TrackLayerMenu(int id, Inventory inventory, TrackLayer trackLayer) {
    super(RailcraftMenuTypes.TRACK_LAYER.get(), id, inventory.player, trackLayer::stillValid);

    //this.addSlot(new RailcraftSlot(trackLayer, 0, 49, 43));
    //this.addSlot(new RailcraftSlot(trackLayer, 1, 130, 43));

    this.addInventorySlots(inventory);
  }
}
