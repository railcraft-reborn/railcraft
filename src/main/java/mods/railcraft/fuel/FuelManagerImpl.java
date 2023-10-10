package mods.railcraft.fuel;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.fuel.FuelManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.material.Fluid;

public class FuelManagerImpl implements FuelManager {

  private static final List<Tuple<TagKey<Fluid>, Integer>> BOILER_FUEL = new ArrayList<>();

  /**
   * Register the amount of heat in a bucket of liquid fuel.
   *
   * @param fluid the fluid
   * @param heatValuePerBucket the amount of "heat" per bucket of fuel
   */
  @Override
  public void addFuel(TagKey<Fluid> fluid, int heatValuePerBucket) {
    BOILER_FUEL.add(new Tuple<>(fluid, heatValuePerBucket));
  }

  @SuppressWarnings("deprecation")
  @Override
  public float getFuelValue(Fluid fluid) {
    return BOILER_FUEL.stream()
        .filter(tpl -> fluid.is(tpl.getA()))
        .map(tpl -> RailcraftConfig.SERVER.fuelMultiplier.get().floatValue() * tpl.getB())
        .findFirst()
        .orElse(0F);
  }
}
