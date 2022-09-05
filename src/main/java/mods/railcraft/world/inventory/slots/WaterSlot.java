package mods.railcraft.world.inventory.slots;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class WaterSlot extends RailcraftSlot {

  public WaterSlot(Container container, int slotIndex, int posX, int posY) {
    super(container, slotIndex, posX, posY);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return FluidUtil.getFluidContained(stack)
        .map(FluidStack::getFluid)
        .filter(fluid -> fluid.is(FluidTags.WATER))
        .isPresent();
  }
}
