package mods.railcraft.data;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.sounds.RailcraftSoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class RailcraftSoundsProvider extends SoundDefinitionsProvider {

  public RailcraftSoundsProvider(PackOutput packOutput, ExistingFileHelper fileHelper) {
    super(packOutput, RailcraftConstants.ID, fileHelper);
  }

  @Override
  public void registerSounds() {
    this.add(RailcraftSoundEvents.STEAM_WHISTLE.get(),
        definition()
            .with(
                sound(RailcraftConstants.rl("locomotive/steam/whistle1")),
                sound(RailcraftConstants.rl("locomotive/steam/whistle2")),
                sound(RailcraftConstants.rl("locomotive/steam/whistle3")))
            .subtitle(Translations.Subtitle.STEAM_WHISTLE));
    this.add(RailcraftSoundEvents.ELECTRIC_WHISTLE.get(),
        definition()
            .with(
                sound(RailcraftConstants.rl("locomotive/electric/whistle1")),
                sound(RailcraftConstants.rl("locomotive/electric/whistle2")),
                sound(RailcraftConstants.rl("locomotive/electric/whistle3")))
            .subtitle(Translations.Subtitle.ELECTRIC_WHISTLE));
    this.add(RailcraftSoundEvents.STEAM_BURST.get(),
        definition()
            .with(sound(RailcraftConstants.rl("machine/steam_burst")))
            .subtitle(Translations.Subtitle.STEAM_BURST));
    this.add(RailcraftSoundEvents.STEAM_HISS.get(),
        definition()
            .with(sound(RailcraftConstants.rl("machine/steam_hiss")))
            .subtitle(Translations.Subtitle.STEAM_HISS));
    this.add(RailcraftSoundEvents.MACHINE_ZAP.get(),
        definition()
            .with(
                sound(RailcraftConstants.rl("machine/zap1")),
                sound(RailcraftConstants.rl("machine/zap2")),
                sound(RailcraftConstants.rl("machine/zap3")),
                sound(RailcraftConstants.rl("machine/zap4")))
            .subtitle(Translations.Subtitle.MACHINE_ZAP));
  }
}
