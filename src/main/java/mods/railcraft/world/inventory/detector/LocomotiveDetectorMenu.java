package mods.railcraft.world.inventory.detector;

import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.inventory.slot.ColorFilterSlot;
import mods.railcraft.world.level.block.entity.detector.LocomotiveDetectorBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class LocomotiveDetectorMenu extends RailcraftMenu {

  public LocomotiveDetectorMenu(int id, Inventory inventory, LocomotiveDetectorBlockEntity blockEntity) {
    super(RailcraftMenuTypes.LOCOMOTIVE_DETECTOR.get(), id, inventory.player, blockEntity::isStillValid);
    this.addSlot(new ColorFilterSlot(blockEntity, 0, 35 ,26));
    this.addSlot(new ColorFilterSlot(blockEntity, 1, 35 ,52));
    this.addInventorySlots(inventory, 170);
  }
}
