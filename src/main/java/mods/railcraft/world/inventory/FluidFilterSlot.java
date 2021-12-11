package mods.railcraft.world.inventory;

import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class FluidFilterSlot extends RailcraftSlot {

  public FluidFilterSlot(Container iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
    this.setPhantom();
    this.setStackLimit(1);
  }

  @Override
  public boolean mayPlace(ItemStack itemstack) {
    return FluidItemHelper.isFluidInContainer(itemstack);
  }
}
