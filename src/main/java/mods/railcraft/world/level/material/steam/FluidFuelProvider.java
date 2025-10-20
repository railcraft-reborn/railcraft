package mods.railcraft.world.level.material.steam;

import mods.railcraft.util.FuelUtil;
import mods.railcraft.world.level.material.FuelProvider;
import mods.railcraft.world.level.material.StandardTank;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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
    try (var tx = Transaction.openRoot()) {
      var fuelResource = this.fuelTank.getResource(0);
      if (fuelResource.isEmpty()) {
        return 0;
      }
      var fuelExtracted = this.fuelTank.internalExtract(fuelResource, FluidType.BUCKET_VOLUME, tx);
      if (fuelExtracted == 0) {
        return 0;
      }

      var heatValue = FuelUtil.getFuelValueForSize(new FluidStack(fuelResource.getFluid(), fuelExtracted));
      if (heatValue > 0) {
        tx.commit();
      }
      return heatValue;
    }
  }
}
