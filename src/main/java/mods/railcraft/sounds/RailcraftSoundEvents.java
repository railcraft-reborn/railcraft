package mods.railcraft.sounds;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
    return deferredRegister.register(name,
        () -> SoundEvent.createVariableRangeEvent(RailcraftConstants.rl(name)));
  }
}
