package mods.railcraft.world.inventory;

import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FeedSlot extends Slot {

  public FeedSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public int getMaxStackSize() {
    return 64;
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return canPlaceItem(stack);
  }

  public static boolean canPlaceItem(ItemStack stack) {
    return StackFilter.FEED.test(stack);
  }
}
