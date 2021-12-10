package mods.railcraft.world.inventory;

import javax.annotation.Nullable;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;

public class SlotWaterLimited extends SlotWater {

  public SlotWaterLimited(Container iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
    this.setStackLimit(4);
  }

  @Override
  public boolean mayPlace(@Nullable ItemStack stack) {
    return FluidItemHelper.getFluidStackInContainer(stack)
        .filter(fluid -> fluid.getFluid() == Fluids.WATER)
        .filter(fluid -> fluid.getAmount() <= FluidAttributes.BUCKET_VOLUME)
        .isPresent();
  }
}
