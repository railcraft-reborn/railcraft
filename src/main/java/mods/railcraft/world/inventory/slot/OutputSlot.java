package mods.railcraft.world.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class OutputSlot extends RailcraftSlot {

  public OutputSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack item) {
    return false;
  }
}
