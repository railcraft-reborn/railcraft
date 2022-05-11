/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.fuel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mods.railcraft.RailcraftConfig;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IRegistryDelegate;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class FluidFuelManager {

  private static final Map<IRegistryDelegate<Fluid>, Integer> boilerFuel =
      new ConcurrentHashMap<>();

  /**
   * Register the amount of heat in a bucket of liquid fuel.
   *
   * @param fluid the fluid
   * @param heatValuePerBucket the amount of "heat" per bucket of fuel
   */
  public static void addFuel(Fluid fluid, int heatValuePerBucket) {
    boilerFuel.put(fluid.delegate, heatValuePerBucket);
  }

  public static float getFuelValue(Fluid fluid) {
    return RailcraftConfig.server.fuelMultiplier.get().floatValue()
        * boilerFuel.getOrDefault(fluid.delegate, 0);
  }

  public static float getFuelValueForSize(FluidStack fluid) {
    return getFuelValue(fluid.getFluid()) * fluid.getAmount() / 1000.0F;
  }
}
