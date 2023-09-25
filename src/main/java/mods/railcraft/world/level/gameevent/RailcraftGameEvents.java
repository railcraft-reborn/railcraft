package mods.railcraft.world.level.gameevent;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftGameEvents {

  private static final DeferredRegister<GameEvent> deferredRegister =
      DeferredRegister.create(Registries.GAME_EVENT, RailcraftConstants.ID);

  public static final RegistryObject<GameEvent> NEIGHBOR_NOTIFY = register("neighbor_notify");

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static RegistryObject<GameEvent> register(String name) {
    return register(name, GameEvent.DEFAULT_NOTIFICATION_RADIUS);
  }

  private static RegistryObject<GameEvent> register(String name, int notificationRadius) {
    return deferredRegister.register(name, () -> new GameEvent(name, notificationRadius));
  }
}
