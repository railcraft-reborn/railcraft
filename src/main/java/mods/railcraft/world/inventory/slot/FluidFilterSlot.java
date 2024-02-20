package mods.railcraft.world.inventory.slot;

import mods.railcraft.util.fluids.FluidTools;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class FluidFilterSlot extends RailcraftSlot {

  public FluidFilterSlot(Container container, int slotIndex, int posX, int posY) {
    super(container, slotIndex, posX, posY);
    this.setPhantom();
    this.setStackLimit(1);
  }

  @Override
  public boolean mayPlace(ItemStack itemstack) {
    return !FluidTools.isEmptyContainer(itemstack);
  }
}
