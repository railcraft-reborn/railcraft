package mods.railcraft.world.inventory;

import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SlotWater extends SlotRailcraft {

  public SlotWater(Container iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return FluidItemHelper.getFluidStackInContainer(stack)
        .map(FluidStack::getFluid)
        .filter(fluid -> fluid == Fluids.WATER)
        .isPresent();
  }
}
