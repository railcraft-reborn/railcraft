package mods.railcraft.sounds;

import java.util.function.Function;
import mods.railcraft.Railcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftSoundEvents {

  private static final DeferredRegister<SoundEvent> deferredRegister =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Railcraft.ID);

  public static final RegistryObject<SoundEvent> STEAM_WHISTLE =
      register("locomotive.steam.whistle", SoundEvent::createVariableRangeEvent);

  public static final RegistryObject<SoundEvent> ELECTRIC_WHISTLE =
      register("locomotive.electric.whistle", SoundEvent::createVariableRangeEvent);

  public static final RegistryObject<SoundEvent> STEAM_BURST =
      register("machine.steam_burst", SoundEvent::createVariableRangeEvent);

  public static final RegistryObject<SoundEvent> STEAM_HISS =
      register("machine.steam_hiss", SoundEvent::createVariableRangeEvent);

  public static final RegistryObject<SoundEvent> MACHINE_ZAP =
      register("machine.zap", SoundEvent::createVariableRangeEvent);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static RegistryObject<SoundEvent> register(String name,
      Function<ResourceLocation, SoundEvent> factory) {
    var registryName = new ResourceLocation(Railcraft.ID, name);
    return deferredRegister.register(name, () -> factory.apply(registryName));
  }
}
