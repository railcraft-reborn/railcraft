package mods.railcraft.world.inventory.slot;

import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.Container;

public class ColorFilterSlot extends ItemFilterSlot {

  public ColorFilterSlot(Container container, int slotIndex, int posX, int posY) {
    super(StackFilter.DYES, container, slotIndex, posX, posY);
    this.setStackLimit(1);
    this.setPhantom();
  }
}
