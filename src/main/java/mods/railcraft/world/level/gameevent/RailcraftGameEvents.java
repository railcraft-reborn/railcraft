package mods.railcraft.world.level.gameevent;

import mods.railcraft.Railcraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;

public class RailcraftGameEvents {

  public static final GameEvent NEIGHBOR_NOTIFY = register("neighbor_notify");

  private static GameEvent register(String name) {
    return register(name, GameEvent.DEFAULT_NOTIFICATION_RADIUS);
  }

  private static GameEvent register(String name, int notificationRadius) {
    return Registry.register(Registry.GAME_EVENT, new ResourceLocation(Railcraft.ID, name),
        new GameEvent(name, notificationRadius));
  }

  // Dummy method to force this class to be class loaded.
  public static void init() {}
}
