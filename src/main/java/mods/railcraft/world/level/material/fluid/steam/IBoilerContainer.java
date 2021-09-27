package mods.railcraft.world.level.material.fluid.steam;

import javax.annotation.Nullable;
import mods.railcraft.api.fuel.INeedsFuel;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by CovertJaguar on 9/10/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IBoilerContainer extends ITemperature, INeedsFuel {

  @Nullable
  SteamBoiler getBoiler();

  void steamExplosion(FluidStack resource);

  default @Nullable FluidStack onFillWater(@Nullable FluidStack resource) {
    if (resource == null) {
      return null;
    }
    if (!resource.isEmpty()) {
      SteamBoiler boiler = getBoiler();
      if (boiler != null && boiler.isSuperHeated()) {
        FluidStack water = boiler.getWaterTank().getFluid();
        if (water.isEmpty()) {
          steamExplosion(resource);
          return null;
        }
      }
    }
    return resource;
  }
}
