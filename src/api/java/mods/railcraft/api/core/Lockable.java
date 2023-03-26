package mods.railcraft.api.core;

import com.mojang.authlib.GameProfile;

public interface Lockable extends Ownable {

  boolean isLocked();

  default boolean canAccess(GameProfile gameProfile) {
    return !this.isLocked() || this.getOwnerOrThrow().equals(gameProfile);
  }
}
