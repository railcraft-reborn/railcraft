package mods.railcraft.world.inventory.slots;

import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.Container;

public class PhantomMinecartSlot extends ItemFilterSlot {

  public PhantomMinecartSlot(Container container, int index, int x, int y) {
    super(StackFilter.MINECART, container, index, x, y);
    this.setPhantom();
    this.setStackLimit(1);
  }
}
