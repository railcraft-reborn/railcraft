package mods.railcraft.world.inventory.slot;

import mods.railcraft.util.fluids.FluidTools;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class EmptyFluidContainerSlot extends RailcraftSlot {

  public EmptyFluidContainerSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack item) {
    return !item.isEmpty() && FluidTools.isEmptyContainer(item);
  }
}
