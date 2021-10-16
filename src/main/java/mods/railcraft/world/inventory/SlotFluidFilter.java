package mods.railcraft.world.inventory;

import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotFluidFilter extends SlotRailcraft {

  public SlotFluidFilter(IInventory iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
   this. setPhantom();
   this. setStackLimit(1);
  }

  @Override
  public boolean mayPlace( ItemStack itemstack) {
    return FluidItemHelper.isFluidInContainer(itemstack);
  }
}
