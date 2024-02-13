package mods.railcraft.world.inventory.slot;

import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.Container;

public class FeedSlot extends ItemFilterSlot {

  public FeedSlot(Container container, int slot, int x, int y) {
    super(StackFilter.FEED, container, slot, x, y);
  }
}
