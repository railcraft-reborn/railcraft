package mods.railcraft.world.inventory;

import mods.railcraft.util.container.filters.StackFilters;
import net.minecraft.world.Container;

public class PhantomMinecartSlot extends ItemFilterSlot {

  public PhantomMinecartSlot(Container inventory, int index, int x, int y) {
    super(StackFilters.MINECART, inventory, index, x, y);
    this.setPhantom();
    this.setStackLimit(1);
  }
}
