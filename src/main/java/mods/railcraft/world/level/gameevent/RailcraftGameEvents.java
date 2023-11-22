package mods.railcraft.world.level.gameevent;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftGameEvents {

  private static final DeferredRegister<GameEvent> deferredRegister =
      DeferredRegister.create(Registries.GAME_EVENT, RailcraftConstants.ID);

  public static final DeferredHolder<GameEvent, GameEvent> NEIGHBOR_NOTIFY =
      register("neighbor_notify");

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static DeferredHolder<GameEvent, GameEvent> register(String name) {
    return deferredRegister.register(name,
        () -> new GameEvent(GameEvent.DEFAULT_NOTIFICATION_RADIUS));
  }
}
