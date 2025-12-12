package mods.railcraft.util;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.datamaps.RailcraftDataMaps;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public final class FuelUtil {

  private FuelUtil() {
  }

  @SuppressWarnings("deprecation")
  public static float getFuelValue(FluidResource fluidResource) {
    var fluidHeat = fluidResource.getFluid().builtInRegistryHolder().getData(RailcraftDataMaps.FLUID_HEAT);
    if (fluidHeat == null) {
      return 0;
    }
    var fuelMultiplier = RailcraftConfig.SERVER.fuelMultiplier.get().floatValue();
    return fuelMultiplier * fluidHeat.heatValuePerBucket();
  }

  public static float getFuelValueForSize(FluidStack fluid) {
    return getFuelValue(FluidResource.of(fluid)) * fluid.getAmount() / 1000;
  }
}
