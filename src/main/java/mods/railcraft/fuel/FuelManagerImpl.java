package mods.railcraft.fuel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.fuel.FuelManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.IRegistryDelegate;

public enum FuelManagerImpl implements FuelManager {

  INSTANCE;

  private static final Map<IRegistryDelegate<Fluid>, Integer> boilerFuel =
      new ConcurrentHashMap<>();

  /**
   * Register the amount of heat in a bucket of liquid fuel.
   *
   * @param fluid the fluid
   * @param heatValuePerBucket the amount of "heat" per bucket of fuel
   */
  @Override
  public void addFuel(Fluid fluid, int heatValuePerBucket) {
    boilerFuel.put(fluid.delegate, heatValuePerBucket);
  }

  @Override
  public float getFuelValue(Fluid fluid) {
    return RailcraftConfig.server.fuelMultiplier.get().floatValue()
        * boilerFuel.getOrDefault(fluid.delegate, 0);
  }
}
