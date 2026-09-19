package mods.railcraft.data;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.sounds.RailcraftSoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class RailcraftSoundsProvider extends SoundDefinitionsProvider {

  public RailcraftSoundsProvider(PackOutput packOutput) {
    super(packOutput, RailcraftConstants.ID);
  }

  @Override
  public void registerSounds() {
    this.add(RailcraftSoundEvents.STEAM_WHISTLE.get(),
        definition()
            .with(
                sound(RailcraftConstants.id("locomotive/steam/whistle1")),
                sound(RailcraftConstants.id("locomotive/steam/whistle2")),
                sound(RailcraftConstants.id("locomotive/steam/whistle3")))
            .subtitle(Translations.Subtitle.STEAM_WHISTLE));
    this.add(RailcraftSoundEvents.ELECTRIC_WHISTLE.get(),
        definition()
            .with(
                sound(RailcraftConstants.id("locomotive/electric/whistle1")),
                sound(RailcraftConstants.id("locomotive/electric/whistle2")),
                sound(RailcraftConstants.id("locomotive/electric/whistle3")))
            .subtitle(Translations.Subtitle.ELECTRIC_WHISTLE));
    this.add(RailcraftSoundEvents.STEAM_BURST.get(),
        definition()
            .with(sound(RailcraftConstants.id("machine/steam_burst")))
            .subtitle(Translations.Subtitle.STEAM_BURST));
    this.add(RailcraftSoundEvents.STEAM_HISS.get(),
        definition()
            .with(sound(RailcraftConstants.id("machine/steam_hiss")))
            .subtitle(Translations.Subtitle.STEAM_HISS));
    this.add(RailcraftSoundEvents.MACHINE_ZAP.get(),
        definition()
            .with(
                sound(RailcraftConstants.id("machine/zap1")),
                sound(RailcraftConstants.id("machine/zap2")),
                sound(RailcraftConstants.id("machine/zap3")),
                sound(RailcraftConstants.id("machine/zap4")))
            .subtitle(Translations.Subtitle.MACHINE_ZAP));
  }
}
