package mods.railcraft.world.level.material.fluid.steam;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;

/**
 * Created by CovertJaguar on 9/10/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface IBoilerContainer {

  @Nullable
  SteamBoiler getBoiler();

  void steamExplosion(FluidStack resource);

  boolean needsFuel();

  float getTemperature();

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
