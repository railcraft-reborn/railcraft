package mods.railcraft.world.inventory.slot;

import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.Container;

public class DispensableCartSlot extends ItemFilterSlot {

  public DispensableCartSlot(Container container, int index, int x, int y) {
    super(StackFilter.MINECART, container, index, x, y);
  }
}
