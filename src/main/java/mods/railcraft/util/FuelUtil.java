package mods.railcraft.util;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.util.datamaps.RailcraftDataMaps;
import net.neoforged.neoforge.fluids.FluidStack;

public final class FuelUtil {

  private FuelUtil() {
  }

  public static float getFuelValue(FluidStack fluid) {
    var fluidHeat = fluid.getFluidHolder().getData(RailcraftDataMaps.FLUID_HEAT);
    if (fluidHeat == null) {
      return 0;
    }
    var fuelMultiplier = RailcraftConfig.SERVER.fuelMultiplier.get().floatValue();
    return fuelMultiplier * fluidHeat.heatValuePerBucket();
  }

  public static float getFuelValueForSize(FluidStack fluid) {
    return getFuelValue(fluid) * fluid.getAmount() / 1000;
  }
}
