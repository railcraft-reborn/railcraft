package mods.railcraft.api.fuel;

import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;

public class FuelUtil {

  private static FuelManager fuelManager;

  public static FuelManager fuelManager() {
    Objects.requireNonNull(fuelManager);
    return fuelManager;
  }

  @ApiStatus.Internal
  public static void _setFuelManager(FuelManager fuelManager) {
    if (FuelUtil.fuelManager != null) {
      throw new IllegalStateException("fuelManager already set.");
    }
    FuelUtil.fuelManager = fuelManager;
  }
}
