package mods.railcraft.sounds;

import mods.railcraft.Railcraft;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class RailcraftSoundEvents {

  private static final DeferredRegister<SoundEvent> deferredRegister =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RailcraftConstants.ID);

  public static final RegistryObject<SoundEvent> STEAM_WHISTLE =
      register("locomotive.steam.whistle");

  public static final RegistryObject<SoundEvent> ELECTRIC_WHISTLE =
      register("locomotive.electric.whistle");

  public static final RegistryObject<SoundEvent> STEAM_BURST = register("machine.steam_burst");

  public static final RegistryObject<SoundEvent> STEAM_HISS = register("machine.steam_hiss");

  public static final RegistryObject<SoundEvent> MACHINE_ZAP = register("machine.zap");

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static RegistryObject<SoundEvent> register(String name) {
    var registryName = Railcraft.rl(name);
    return deferredRegister.register(name,
        () -> SoundEvent.createVariableRangeEvent(registryName));
  }
}
