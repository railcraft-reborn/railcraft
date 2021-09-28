package mods.railcraft.world.level.material.fluid.tank;

import java.util.function.Supplier;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class FilteredTank extends StandardTank {

  public FilteredTank(int capacity) {
    super(capacity);
  }

  public FilteredTank setFilterFluid(Supplier<? extends Fluid> filter) {
    this.filter = () -> new FluidStack(filter.get(), 1);
    return this;
  }

  public FilteredTank setFilterFluidStack(Supplier<FluidStack> filter) {
    this.filter = filter;
    return this;
  }
}
