package mods.railcraft.api.core;

import net.minecraft.server.players.NameAndId;

public interface Lockable extends Ownable {

  boolean isLocked();

  default boolean canAccess(NameAndId gameProfile) {
    return !this.isLocked() || this.getOwnerOrThrow().equals(gameProfile);
  }
}
