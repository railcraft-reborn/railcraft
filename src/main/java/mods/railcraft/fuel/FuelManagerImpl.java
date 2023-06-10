package mods.railcraft.fuel;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.fuel.FuelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public enum FuelManagerImpl implements FuelManager {

  INSTANCE;

  private static final Object2IntMap<ResourceLocation> boilerFuel =
      Object2IntMaps.synchronize(new Object2IntOpenHashMap<>());

  /**
   * Register the amount of heat in a bucket of liquid fuel.
   *
   * @param fluid the fluid
   * @param heatValuePerBucket the amount of "heat" per bucket of fuel
   */
  @Override
  public void addFuel(Fluid fluid, int heatValuePerBucket) {
    var resourceLocation = ForgeRegistries.FLUIDS.getKey(fluid);
    boilerFuel.put(resourceLocation, heatValuePerBucket);
  }

  @Override
  public float getFuelValue(Fluid fluid) {
    var resourceLocation = ForgeRegistries.FLUIDS.getKey(fluid);
    return RailcraftConfig.SERVER.fuelMultiplier.get().floatValue()
        * boilerFuel.getOrDefault(resourceLocation, 0);
  }
}
