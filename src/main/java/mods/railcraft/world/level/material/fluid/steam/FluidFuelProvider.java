package mods.railcraft.world.level.material.fluid.steam;

import mods.railcraft.api.fuel.FuelUtil;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.FuelProvider;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
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
    var fuel = this.fuelTank.drain(FluidTools.BUCKET_VOLUME, FluidAction.SIMULATE);
    if (fuel.isEmpty()) {
      return 0.0F;
    }

    var heatValue = FuelUtil.fuelManager().getFuelValueForSize(fuel);
    if (heatValue > 0.0F) {
      this.fuelTank.drain(FluidTools.BUCKET_VOLUME, FluidAction.EXECUTE);
    }
    return heatValue;
  }
}
