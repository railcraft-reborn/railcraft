package mods.railcraft.world.inventory;

import mods.railcraft.world.inventory.slot.BlockFilterSlot;
import mods.railcraft.world.inventory.slot.PhantomMinecartSlot;
import mods.railcraft.world.level.block.entity.track.DumpingTrackBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class DumpingTrackMenu extends RailcraftMenu {

  public DumpingTrackMenu(int id, Inventory inventory, DumpingTrackBlockEntity blockEntity) {
    super(RailcraftMenuTypes.DUMPING_TRACK.get(), id, inventory.player, blockEntity::isStillValid);

    for (int i = 0; i < 3; i++) {
      this.addSlot(new PhantomMinecartSlot(blockEntity.getCartFilter(), i, 25 + i * 18, 45));
    }

    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 3; j++) {
        this.addSlot(new BlockFilterSlot(blockEntity.getItemFilter(), i * 3 + j,
            98 + j * 18, 36 + i * 18));
      }
    }

    this.addInventorySlots(inventory);
  }
}
