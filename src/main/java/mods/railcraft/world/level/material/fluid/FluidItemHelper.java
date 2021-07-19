package mods.railcraft.world.level.material.fluid;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * Helper functions for Fluid Items
 *
 * Created by CovertJaguar on 4/2/2015.
 */
@SuppressWarnings("unused")
public final class FluidItemHelper {

  public static boolean isContainer(ItemStack stack) {
    return FluidUtil.getFluidHandler(stack).isPresent();
  }

  public static boolean isFluidInContainer(ItemStack stack) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> item.getTanks() > 0 && !item.getFluidInTank(0).isEmpty())
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


  public static boolean isRoomInContainer(ItemStack stack, @Nullable Fluid fluid) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> item.fill(
            new FluidStack(fluid, FluidTools.BUCKET_VOLUME), FluidAction.SIMULATE) > 0)
        .isPresent();
  }

  public static boolean containsFluid(ItemStack stack, @Nullable Fluid fluid) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> {
          for (int i = 0; i < item.getTanks(); i++) {
            if (item.getFluidInTank(i).getFluid() != fluid) {
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
