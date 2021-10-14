package mods.railcraft.world.level.material.fluid.steam;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;

/**
 * Created by CovertJaguar on 9/10/2016 for Railcraft.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public interface IBoilerContainer {

  /**
   * Get the boiler controller from this instance.
   */
  @Nullable
  SteamBoiler getBoiler();

  /**
   * Checks if the fluid can cause a boiler explotion.
   * @param resource The fluid, mostly water.
   */
  void steamExplosion(FluidStack resource);

  /**
   * Checks if this boiler container requires fuel to burn on the SteamBoiler.
   * @return TRUE if it needs fuel.
   */
  boolean needsFuel();

  /**
   * Gets the current boiler's temperature.
   * @return The temperature in celsius.
   */
  float getTemperature();

  /**
   * Callback for adding water.
   * @param resource The fluidstack (should be water).
   */
  default FluidStack onFillWater(FluidStack resource) {
    if (FluidStack.EMPTY.equals(resource)) {
      return FluidStack.EMPTY;
    }
    if (!resource.isEmpty()) {
      SteamBoiler boiler = getBoiler();
      if (boiler != null && boiler.isSuperHeated()) {
        FluidStack water = boiler.getWaterTank().getFluid();
        if (water.isEmpty()) {
          steamExplosion(resource);
          return FluidStack.EMPTY;
        }
      }
    }
    return resource;
  }
}
