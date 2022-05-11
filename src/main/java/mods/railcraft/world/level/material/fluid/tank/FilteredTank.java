package mods.railcraft.world.level.material.fluid.tank;

import java.util.function.Supplier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class FilteredTank extends StandardTank {

  public FilteredTank(int capacity) {
    super(capacity);
  }

  @SuppressWarnings("deprecation")
  public FilteredTank setFilterTag(TagKey<Fluid> tag) {
    this.setValidator(fluidStack -> fluidStack.getFluid().is(tag));
    return this;
  }

  public FilteredTank setFilterFluid(Fluid filter) {
    this.setFilterFluid(() -> filter);
    return this;
  }

  public FilteredTank setFilterFluid(Supplier<? extends Fluid> filter) {
    this.filter = () -> new FluidStack(filter.get(), 1);
    this.setValidator(fluidStack -> this.filter.get().isFluidEqual(fluidStack));
    return this;
  }
}
