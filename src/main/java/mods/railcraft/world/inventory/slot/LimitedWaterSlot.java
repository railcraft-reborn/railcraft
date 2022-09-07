package mods.railcraft.world.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;

public class LimitedWaterSlot extends WaterSlot {

  public LimitedWaterSlot(Container container, int slotIndex, int posX, int posY) {
    super(container, slotIndex, posX, posY);
    this.setStackLimit(4);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return FluidUtil.getFluidContained(stack)
        .filter(fluid -> fluid.getFluid() == Fluids.WATER)
        .filter(fluid -> fluid.getAmount() <= FluidType.BUCKET_VOLUME)
        .isPresent();
  }
}
