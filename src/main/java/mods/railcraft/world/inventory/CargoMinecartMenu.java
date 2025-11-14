package mods.railcraft.world.inventory;

import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.entity.vehicle.CargoMinecart;
import mods.railcraft.world.inventory.slot.ItemFilterSlot;
import mods.railcraft.world.inventory.slot.SlotLinked;
import net.minecraft.world.entity.player.Inventory;

public class CargoMinecartMenu extends RailcraftMenu {

  public CargoMinecartMenu(int id, Inventory inventory, CargoMinecart minecart) {
    super(RailcraftMenuTypes.CARGO_MINECART.get(), id, inventory.player, minecart::stillValid);

    var filter = new ItemFilterSlot(StackFilter.CARGO, minecart.getFilterInv(), 0, 26, 36)
        .setPhantom()
        .setStackLimit(1)
        .setEnableCheck(minecart::isEmpty);
    this.addSlot(filter);

    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 6; k++) {
        this.addSlot(new SlotLinked(minecart, k + i * 6, 62 + k * 18, 18 + i * 18, filter));
      }
    }

    this.addInventorySlots(inventory);
  }
}
