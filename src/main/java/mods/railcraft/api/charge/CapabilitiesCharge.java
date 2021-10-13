/*------------------------------------------------------------------------------
Copyright (c) CovertJaguar, 2011-2020

This work (the API) is licensed under the "MIT" License,
see LICENSE.md for details.
-----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
* Keeps the cart battery capability instance.
*
* Created by CovertJaguar on 10/4/2016 for Railcraft.
*
* @author CovertJaguar (https://www.railcraft.info)
*/
public final class CapabilitiesCharge {

  @CapabilityInject(IBatteryCart.class)
  public static Capability<IBatteryCart> CART_BATTERY;
}
