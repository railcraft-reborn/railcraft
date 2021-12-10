package mods.railcraft.world.level.material.fluid;

import java.util.Optional;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Helper functions for Fluid Items
 *
 * Created by CovertJaguar on 4/2/2015.
 */
public final class FluidItemHelper {

  public static boolean isContainer(ItemStack stack) {
    return FluidUtil.getFluidHandler(stack).isPresent();
  }

  public static boolean isFluidInContainer(ItemStack stack) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> !item.drain(1, IFluidHandler.FluidAction.SIMULATE).isEmpty())
        .isPresent();
  }

  public static boolean isFullContainer(ItemStack stack) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> {
          for (int i = 0; i < item.getTanks(); i++) {
            if (item.getFluidInTank(i).getAmount() < item.getTankCapacity(i)) {
              return false;
            }
          }
          return true;
        })
        .isPresent();
  }

  public static boolean isEmptyContainer(ItemStack stack) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> {
          for (int i = 0; i < item.getTanks(); i++) {
            if (!item.getFluidInTank(i).isEmpty()) {
              return false;
            }
          }
          return true;
        })
        .isPresent();
  }

  public static boolean isRoomInContainer(ItemStack stack, Fluid fluid) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> item.fill(new FluidStack(fluid, 1),
            IFluidHandler.FluidAction.SIMULATE) > 0)
        .isPresent();
  }

  public static boolean containsFluid(ItemStack stack, Fluid fluid) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> {
          for (int i = 0; i < item.getTanks(); i++) {
            if (!item.getFluidInTank(i).getFluid().isSame(fluid)) {
              return false;
            }
          }
          return true;
        })
        .isPresent();
  }

  public static Optional<FluidStack> getFluidStackInContainer(ItemStack stack) {
    return FluidUtil.getFluidContained(stack);
  }

  private FluidItemHelper() {}
}
