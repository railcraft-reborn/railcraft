package mods.railcraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class WaterSlot extends RailcraftSlot {

  public WaterSlot(Container iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return FluidUtil.getFluidContained(stack)
        .map(FluidStack::getFluid)
        .filter(fluid -> fluid == Fluids.WATER)
        .isPresent();
  }
}
