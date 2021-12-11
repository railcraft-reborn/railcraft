package mods.railcraft.world.inventory;

import mods.railcraft.world.level.block.entity.FeedStationBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class FeedStationMenu extends RailcraftMenu {

  public FeedStationMenu(int id, Inventory inventory, FeedStationBlockEntity blockEntity) {
    super(RailcraftMenuTypes.FEED_STATION.get(), id, inventory);

    this.addSlot(new FeedSlot(blockEntity, 0, 60, 24));

    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 9; k++) {
        this.addSlot(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, 58 + i * 18));
      }

    }

    for (int j = 0; j < 9; j++) {
      this.addSlot(new Slot(inventory, j, 8 + j * 18, 116));
    }
  }

}
