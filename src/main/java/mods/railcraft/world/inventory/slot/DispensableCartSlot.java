package mods.railcraft.world.inventory.slot;

import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class DispensableCartSlot extends RailcraftSlot {

  public DispensableCartSlot(Container container, int index, int x, int y) {
    super(container, index, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack itemStack) {
    if (itemStack.isEmpty()) {
      return false;
    }
    return StackFilter.MINECART.test(itemStack);
  }
}
