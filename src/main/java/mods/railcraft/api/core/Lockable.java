package mods.railcraft.api.core;

import com.mojang.authlib.GameProfile;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface Lockable extends Ownable {

  boolean isLocked();

  default boolean canAccess(GameProfile gameProfile) {
    return !this.isLocked() || this.getOwnerOrThrow().equals(gameProfile);
  }
}
