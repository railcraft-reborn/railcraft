/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.fuel;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class FluidFuelManager {

  private static final Map<FluidStack, Integer> boilerFuel = new HashMap<>();

  public static void addFuel(Fluid fluid, int heatValuePerBucket) {
    addFuel(new FluidStack(fluid, 1), heatValuePerBucket);
  }

  /**
   * Register the amount of heat in a bucket of liquid fuel.
   *
   * @param fluid the fluid
   * @param heatValuePerBucket the amount of "heat" per bucket of fuel
   */
  public static void addFuel(FluidStack fluid, int heatValuePerBucket) {
    FluidStack toSave = fluid.copy();
    toSave.setAmount(1); // hashcode uses this
    boilerFuel.put(toSave, heatValuePerBucket);
  }

  @Deprecated // Use fluid stack aware version
  public static int getFuelValue(Fluid fluid) {
    FluidStack key = new FluidStack(fluid, 1);
    return boilerFuel.getOrDefault(key, 0);
  }

  public static int getFuelValue(FluidStack fluid) {
    FluidStack key = fluid.copy();
    key.setAmount(1); // hashcode
    return boilerFuel.getOrDefault(key, 0);
  }

  public static double getFuelValueForSize(FluidStack fluid) {
    int amount = fluid.getAmount();
    return getFuelValue(fluid) * amount / 1000D;
  }
}
