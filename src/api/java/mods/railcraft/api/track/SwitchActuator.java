package mods.railcraft.api.track;

import mods.railcraft.api.carts.RollingStock;

public interface SwitchActuator {

  /**
   * This method is used by the switch track to ask the actuator device whether it thinks the track
   * should be switched or not. Ultimately, the track itself will decide whether it will be
   * switched, however the track will usually try to honour results of this method when possible.
   *
   * @param rollingStock - the {@link RollingStock} to be switched
   * @return <code>true</code> if the actuator would like the track switched
   */
  boolean shouldSwitch(RollingStock rollingStock);
}
