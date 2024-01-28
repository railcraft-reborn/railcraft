package mods.railcraft.world.level.material.steam;

import mods.railcraft.api.fuel.FuelUtil;
import mods.railcraft.world.level.material.FuelProvider;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FluidFuelProvider implements FuelProvider {

  private final StandardTank fuelTank;

  public FluidFuelProvider(StandardTank fuelTank) {
    this.fuelTank = fuelTank;
  }

  @Override
  public float getHeatStep() {
    return SteamConstants.HEAT_STEP;
  }

  @Override
  public float consumeFuel() {
    var fuel = this.fuelTank.internalDrain(FluidType.BUCKET_VOLUME, FluidAction.SIMULATE);
    if (fuel.isEmpty()) {
      return 0;
    }

    var heatValue = FuelUtil.fuelManager().getFuelValueForSize(fuel);
    if (heatValue > 0) {
      this.fuelTank.internalDrain(FluidType.BUCKET_VOLUME, FluidAction.EXECUTE);
    }
    return heatValue;
  }
}
