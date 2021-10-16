package mods.railcraft.world.inventory;

import mods.railcraft.util.inventory.filters.StackFilters;
import net.minecraft.inventory.IInventory;

public class SlotMinecartPhantom extends SlotStackFilter {

  public SlotMinecartPhantom(IInventory inventory, int index, int x, int y) {
    super(StackFilters.MINECART, inventory, index, x, y);
    this.setPhantom();
    this.setStackLimit(1);
  }
}
