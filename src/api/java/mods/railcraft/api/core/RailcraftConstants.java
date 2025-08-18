/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.resources.ResourceLocation;

public final class RailcraftConstants {

  public static final String ID = "railcraft";
  public static final String NAME = "Railcraft Reborn";
  private static final String RAILCRAFT_PLAYER = "[" + ID + "]";
  public static final GameProfile FAKE_GAMEPROFILE =
      new GameProfile(UUID.nameUUIDFromBytes(RAILCRAFT_PLAYER.getBytes()), RAILCRAFT_PLAYER);
  public static float DEFAULT_MAX_SPEED_AIR_LATERAL = 0.4F;
  public static float DEFAULT_MAX_SPEED_AIR_VERTICAL = -1.0F;
  public static float DEFAULT_AIR_DRAG = 0.95F;

  private RailcraftConstants() {
  }

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(ID, path);
  }

  public static String makeTranslationKey(String type, String name) {
    return type + "." + ID + "." + name;
  }
}
