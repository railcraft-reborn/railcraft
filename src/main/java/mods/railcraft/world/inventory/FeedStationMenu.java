package mods.railcraft.world.inventory;

import mods.railcraft.world.inventory.slot.FeedSlot;
import mods.railcraft.world.level.block.entity.FeedStationBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class FeedStationMenu extends RailcraftMenu {

  public FeedStationMenu(int id, Inventory inventory, FeedStationBlockEntity blockEntity) {
    super(RailcraftMenuTypes.FEED_STATION.get(), id, inventory.player, blockEntity::isStillValid);
    this.addSlot(new FeedSlot(blockEntity, 0, 60, 24));
    this.addInventorySlots(inventory, 140);
  }
}
