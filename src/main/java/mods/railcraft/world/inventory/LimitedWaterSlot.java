package mods.railcraft.world.inventory;

import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class LimitedWaterSlot extends WaterSlot {

  public LimitedWaterSlot(Container iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
    this.setStackLimit(4);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return FluidItemHelper.getFluidStackInContainer(stack)
        .filter(fluid -> fluid.getFluid() == Fluids.WATER)
        .filter(fluid -> fluid.getAmount() >= 1) // disallow tanks with water still in (0 units though)
        .isPresent();
  }
}
