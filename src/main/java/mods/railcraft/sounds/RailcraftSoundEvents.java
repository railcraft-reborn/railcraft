package mods.railcraft.sounds;

import mods.railcraft.Railcraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftSoundEvents {

  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Railcraft.ID);

  public static final RegistryObject<SoundEvent> STEAM_WHISTLE =
      register("locomotive.steam.whistle");

  public static final RegistryObject<SoundEvent> ELECTRIC_WHISTLE =
      register("locomotive.electric.whistle");

  public static final RegistryObject<SoundEvent> STEAM_BURST = register("machine.steam_burst");

  public static final RegistryObject<SoundEvent> STEAM_HISS = register("machine.steam_hiss");

  public static final RegistryObject<SoundEvent> ZAP = register("machine.zap");

  private static RegistryObject<SoundEvent> register(String name) {
    ResourceLocation registryName = new ResourceLocation(Railcraft.ID, name);
    return SOUND_EVENTS.register(name, () -> new SoundEvent(registryName));
  }
}
