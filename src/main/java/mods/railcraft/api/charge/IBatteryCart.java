/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Indicates a battery carried by a cart.
 *
 * <p>
 * This cart battery is not related to the charge system of a world.
 * </p>
 *
 * Created by CovertJaguar on 10/4/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IBatteryCart extends IBattery {

  /**
   * The type of the cart battery.
   */
  enum Type {

    /**
     * Users draw power from tracks, sources, and storage.
     */
    USER,
    /**
     * Sources provide power to users, but not storage. This interface specifies no explicit way to
     * charge Sources, that's up to the implementer. Railcraft provides no Sources currently, and
     * may never do so.
     */
    SOURCE,
    /**
     * Storage provide power to users, but can't draw from tracks or sources. This interface
     * specifies no explicit way to charge Storage, that's up to the implementer. Railcraft may
     * provide a trackside block in the future for charging Storage, but does not currently.
     */
    STORAGE
  }

  /**
   * Returns the type of the battery.
   */
  Type getType();

  /**
   * Returns the per-tick loss of charge in the cart battery.
   */
  float getLosses();

  /**
   * Returns the approximated average charge used in the last 25 ticks.
   */
  float getDraw();

  /**
   * Update the battery and tries to draw charge from other carts.
   *
   * @param owner The cart that carries the battery
   */
  void tick(AbstractMinecartEntity owner);

  /**
   * Update the battery and tries to draw charge from the track.
   *
   * @param owner The cart that carries the battery
   * @param pos The position of the track
   */
  void tickOnTrack(AbstractMinecartEntity owner, BlockPos pos);
}
