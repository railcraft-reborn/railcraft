package mods.railcraft.world.inventory.slot;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

public class WaterSlot extends RailcraftSlot {

  public WaterSlot(Container container, int slotIndex, int posX, int posY) {
    super(container, slotIndex, posX, posY);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean mayPlace(ItemStack stack) {
    return FluidUtil.getFluidContained(stack)
        .map(FluidStack::getFluid)
        .filter(fluid -> fluid.is(FluidTags.WATER))
        .isPresent();
  }
}
