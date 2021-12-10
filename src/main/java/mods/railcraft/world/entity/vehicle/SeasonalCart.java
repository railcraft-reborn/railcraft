/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info
 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.world.entity.vehicle;

import mods.railcraft.season.Season;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface SeasonalCart {

  Season getSeason();

  void setSeason(Season season);
}
