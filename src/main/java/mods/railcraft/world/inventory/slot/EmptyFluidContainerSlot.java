package mods.railcraft.world.inventory.slot;

import mods.railcraft.world.level.material.FluidItemHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EmptyFluidContainerSlot extends Slot {
  
  public EmptyFluidContainerSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack item) {
    return !item.isEmpty()
        //&& item.getCount() == 1
        && FluidItemHelper.isEmptyContainer(item);
  }
}
