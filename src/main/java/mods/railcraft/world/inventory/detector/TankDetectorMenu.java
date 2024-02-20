package mods.railcraft.world.inventory.detector;

import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.inventory.slot.FluidFilterSlot;
import mods.railcraft.world.level.block.entity.detector.TankDetectorBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class TankDetectorMenu extends RailcraftMenu {

  private final TankDetectorBlockEntity blockEntity;

  public TankDetectorMenu(int id, Inventory inventory, TankDetectorBlockEntity blockEntity) {
    super(RailcraftMenuTypes.TANK_DETECTOR.get(), id, inventory.player, blockEntity::isStillValid);
    this.blockEntity = blockEntity;
    this.addSlot(new FluidFilterSlot(blockEntity, 0, 26 ,24));
    this.addInventorySlots(inventory, 140);
  }

  public TankDetectorBlockEntity getTankDetectorBlockEntity() {
    return this.blockEntity;
  }
}
