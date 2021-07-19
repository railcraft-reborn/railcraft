/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import net.minecraft.util.ResourceLocation;

/**
 * Created by CovertJaguar on 7/18/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class RailcraftConstantsAPI {

  public static final String MOD_ID = "railcraft";
  public static final String API_PREFIX = "railcraft:api_";
  public static final String CORE_ID = API_PREFIX + "core";
  public static final String RAILCRAFT_PLAYER = "[" + MOD_ID + "]";
  public static final String UNKNOWN_PLAYER = "[unknown]";

  public static ResourceLocation locationOf(String name) {
    return new ResourceLocation(MOD_ID, name);
  }

  private RailcraftConstantsAPI() {}
}
