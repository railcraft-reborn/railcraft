package mods.railcraft.world.inventory;

import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SlotWater extends SlotRailcraft {

  public SlotWater(IInventory iinventory, int slotIndex, int posX, int posY) {
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
