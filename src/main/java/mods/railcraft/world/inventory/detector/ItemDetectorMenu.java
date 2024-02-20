package mods.railcraft.world.inventory.detector;

import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import mods.railcraft.world.level.block.entity.detector.ItemDetectorBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class ItemDetectorMenu extends RailcraftMenu {

  private final ItemDetectorBlockEntity blockEntity;

  public ItemDetectorMenu(int id, Inventory inventory, ItemDetectorBlockEntity blockEntity) {
    super(RailcraftMenuTypes.ITEM_DETECTOR.get(), id, inventory.player, blockEntity::isStillValid);
    this.blockEntity = blockEntity;

    for (int i = 0; i < blockEntity.getContainerSize(); i++) {
      this.addSlot(new RailcraftSlot(blockEntity, i, 8 + i * 18, 61)
          .setPhantom()
          .setEnableCheck(() -> blockEntity.getPrimaryMode() == ItemDetectorBlockEntity.PrimaryMode.FILTERED));
    }
    this.addInventorySlots(inventory);
  }

  public ItemDetectorBlockEntity getItemDetector() {
    return blockEntity;
  }
}
