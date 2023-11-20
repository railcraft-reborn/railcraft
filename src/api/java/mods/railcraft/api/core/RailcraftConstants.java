/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import java.util.UUID;
import com.mojang.authlib.GameProfile;

public final class RailcraftConstants {

  public static final String ID = "railcraft";
  public static final String NAME = "Railcraft Reborn";
  public static final String RAILCRAFT_PLAYER = "[railcraft]";
  public static final GameProfile FAKE_GAMEPROFILE =
      new GameProfile(UUID.nameUUIDFromBytes(RAILCRAFT_PLAYER.getBytes()), RAILCRAFT_PLAYER);
}
