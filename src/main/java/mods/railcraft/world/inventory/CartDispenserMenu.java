package mods.railcraft.world.inventory;

import mods.railcraft.world.inventory.slot.DispensableCartSlot;
import mods.railcraft.world.level.block.entity.manipulator.CartDispenserBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class CartDispenserMenu extends RailcraftMenu {

  public CartDispenserMenu(int id, Inventory inventory, CartDispenserBlockEntity manipulator) {
    super(RailcraftMenuTypes.CART_DISPENSER.get(), id, inventory.player, manipulator::isStillValid);

    this.addSlot(new DispensableCartSlot(manipulator, 0, 62, 24));
    this.addSlot(new DispensableCartSlot(manipulator, 1, 80, 24));
    this.addSlot(new DispensableCartSlot(manipulator, 2, 98, 24));

    this.addInventorySlots(inventory, 140);
  }
}
