/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import javax.annotation.Nullable;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.IStringSerializable;

public interface ISwitchMotor {

  /**
   * This method is used by the switch track to ask the actuator device whether it thinks the track
   * should be switched or not. Ultimately, the track itself will decide whether it will be
   * switched, however the track will usually try to honor results of this method when possible.
   *
   * @param cart The cart that the switch may use to determine switch status. Implementations should
   *        expect null values.
   * @return true if the actuator would like the track switched
   * @see ITrackKitSwitch
   */
  boolean shouldSwitch(@Nullable AbstractMinecartEntity cart);

  /**
   * Announces track state changes to the actuator. Server side only.
   */
  void onSwitch(boolean isSwitched);

  /**
   * Tell the actuator to refresh its arrows directions.
   */
  void updateArrows();

  enum ArrowDirection implements IStringSerializable {

    NORTH("north"), SOUTH("south"), EAST("east"), WEST("west"), NORTH_SOUTH("north_south"),
    EAST_WEST("east_west");

    private final String name;

    private ArrowDirection(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
