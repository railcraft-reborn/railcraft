package mods.railcraft.api.track;

import net.minecraft.world.entity.vehicle.AbstractMinecart;

public interface SwitchActuator {

  /**
   * This method is used by the switch track to ask the actuator device whether it thinks the track
   * should be switched or not. Ultimately, the track itself will decide whether it will be
   * switched, however the track will usually try to honour results of this method when possible.
   *
   * @param cart - the cart that the switch may use to determine switch status
   * @return true if the actuator would like the track switched
   */
  boolean shouldSwitch(AbstractMinecart cart);
}
