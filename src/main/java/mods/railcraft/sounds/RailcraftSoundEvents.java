package mods.railcraft.sounds;

import mods.railcraft.Railcraft;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftSoundEvents {

  private static final DeferredRegister<SoundEvent> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, RailcraftConstants.ID);

  public static final DeferredHolder<SoundEvent, SoundEvent> STEAM_WHISTLE =
      register("locomotive.steam.whistle");

  public static final DeferredHolder<SoundEvent, SoundEvent> ELECTRIC_WHISTLE =
      register("locomotive.electric.whistle");

  public static final DeferredHolder<SoundEvent, SoundEvent> STEAM_BURST = register("machine.steam_burst");

  public static final DeferredHolder<SoundEvent, SoundEvent> STEAM_HISS = register("machine.steam_hiss");

  public static final DeferredHolder<SoundEvent, SoundEvent> MACHINE_ZAP = register("machine.zap");

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
    var registryName = Railcraft.rl(name);
    return deferredRegister.register(name,
        () -> SoundEvent.createVariableRangeEvent(registryName));
  }
}
