package mods.railcraft.world.inventory;

import javax.annotation.Nullable;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;

public class SlotWaterLimited extends SlotWater {

  public SlotWaterLimited(IInventory iinventory, int slotIndex, int posX, int posY) {
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
