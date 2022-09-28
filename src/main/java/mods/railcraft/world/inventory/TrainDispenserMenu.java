package mods.railcraft.world.inventory;

import mods.railcraft.world.inventory.slot.DispensableCartSlot;
import mods.railcraft.world.inventory.slot.PhantomMinecartSlot;
import mods.railcraft.world.level.block.entity.manipulator.TrainDispenserBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class TrainDispenserMenu extends RailcraftMenu {

  public TrainDispenserMenu(int id, Inventory inventory, TrainDispenserBlockEntity manipulator) {
    super(RailcraftMenuTypes.TRAIN_DISPENSER.get(), id, inventory.player, manipulator::stillValid);

    for (int i = 0; i < 9; i++) {
      this.addSlot(new PhantomMinecartSlot(manipulator.getInvPattern(), i, 8 + i * 18, 29));
    }

    for (int i = 0; i < 2; i++) {
      for (int k = 0; k < 9; k++) {
        this.addSlot(new DispensableCartSlot(manipulator, k + i * 9, 8 + k * 18, 61 + i * 18));
      }
    }

    this.addInventorySlots(inventory, 193);
  }
}
