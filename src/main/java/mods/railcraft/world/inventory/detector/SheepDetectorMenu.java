package mods.railcraft.world.inventory.detector;

import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.inventory.slot.ColorFilterSlot;
import mods.railcraft.world.level.block.entity.detector.SheepDetectorBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class SheepDetectorMenu extends RailcraftMenu {

  public SheepDetectorMenu(int id, Inventory inventory, SheepDetectorBlockEntity blockEntity) {
    super(RailcraftMenuTypes.SHEEP_DETECTOR.get(), id, inventory.player, blockEntity::isStillValid);
    this.addSlot(new ColorFilterSlot(blockEntity, 0, 60 ,24));
    this.addInventorySlots(inventory, 140);
  }
}
