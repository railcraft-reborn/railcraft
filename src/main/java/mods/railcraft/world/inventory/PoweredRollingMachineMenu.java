package mods.railcraft.world.inventory;

import mods.railcraft.world.level.block.entity.PoweredRollingMachineBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class PoweredRollingMachineMenu extends RailcraftMenu {

  private final PoweredRollingMachineBlockEntity blockEntity;

  public PoweredRollingMachineMenu(int id, Inventory inventory, PoweredRollingMachineBlockEntity blockEntity) {
    super(RailcraftMenuTypes.POWERED_ROLLING_MACHINE.get(), id, inventory.player, blockEntity::stillValid);
    this.blockEntity = blockEntity;
    this.addInventorySlots(inventory);
  }

  public float rollingProgress() {
    return this.blockEntity.getRollingProgress();
  }
}
