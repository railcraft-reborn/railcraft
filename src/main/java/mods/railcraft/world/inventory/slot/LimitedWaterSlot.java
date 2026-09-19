package mods.railcraft.world.inventory.slot;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

public class LimitedWaterSlot extends WaterSlot {

  public LimitedWaterSlot(Container container, int slotIndex, int posX, int posY) {
    super(container, slotIndex, posX, posY);
    this.setStackLimit(4);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    var fluidStack = FluidUtil.getFirstStackContained(stack);
    return fluidStack.is(FluidTags.WATER) && fluidStack.getAmount() <= FluidType.BUCKET_VOLUME;
  }
}
