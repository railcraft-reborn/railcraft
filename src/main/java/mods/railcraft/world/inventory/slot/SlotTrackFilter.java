package mods.railcraft.world.inventory.slot;

import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.Container;

public class SlotTrackFilter extends ItemFilterSlot {

  public SlotTrackFilter(Container container, int index, int x, int y) {
    super(StackFilter.TRACK, container, index, x, y);
    this.setPhantom();
    this.setStackLimit(1);
  }
}
