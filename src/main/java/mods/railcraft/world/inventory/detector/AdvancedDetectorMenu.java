package mods.railcraft.world.inventory.detector;

import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.inventory.slot.PhantomMinecartSlot;
import mods.railcraft.world.level.block.entity.detector.AdvancedDetectorBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedDetectorMenu extends RailcraftMenu {

  public AdvancedDetectorMenu(int id, Inventory inventory, AdvancedDetectorBlockEntity blockEntity) {
    super(RailcraftMenuTypes.ADVANCED_DETECTOR.get(), id, inventory.player, blockEntity::isStillValid);

    for (int i = 0; i < blockEntity.getContainerSize(); i++) {
      this.addSlot(new PhantomMinecartSlot(blockEntity, i, 8 + i * 18, 24));
    }
    this.addInventorySlots(inventory, 140);
  }
}
