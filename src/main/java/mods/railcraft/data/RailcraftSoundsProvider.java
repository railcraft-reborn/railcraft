package mods.railcraft.data;

import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.sounds.RailcraftSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class RailcraftSoundsProvider extends SoundDefinitionsProvider {

  public RailcraftSoundsProvider(PackOutput packOutput, ExistingFileHelper fileHelper) {
    super(packOutput, RailcraftConstants.ID, fileHelper);
  }

  @Override
  public void registerSounds() {
    this.add(RailcraftSoundEvents.STEAM_WHISTLE.get(),
        definition()
            .with(
                sound(Railcraft.rl("locomotive/steam/whistle1")),
                sound(Railcraft.rl("locomotive/steam/whistle2")),
                sound(Railcraft.rl("locomotive/steam/whistle3")))
            .subtitle(Translations.Subtitle.STEAM_WHISTLE));
    this.add(RailcraftSoundEvents.ELECTRIC_WHISTLE.get(),
        definition()
            .with(
                sound(Railcraft.rl("locomotive/electric/whistle1")),
                sound(Railcraft.rl("locomotive/electric/whistle2")),
                sound(Railcraft.rl("locomotive/electric/whistle3")))
            .subtitle(Translations.Subtitle.ELECTRIC_WHISTLE));
    this.add(RailcraftSoundEvents.STEAM_BURST.get(),
        definition()
            .with(sound(Railcraft.rl("machine/steam_burst")))
            .subtitle(Translations.Subtitle.STEAM_BURST));
    this.add(RailcraftSoundEvents.STEAM_HISS.get(),
        definition()
            .with(sound(Railcraft.rl("machine/steam_hiss")))
            .subtitle(Translations.Subtitle.STEAM_HISS));
    this.add(RailcraftSoundEvents.MACHINE_ZAP.get(),
        definition()
            .with(
                sound(Railcraft.rl("machine/zap1")),
                sound(Railcraft.rl("machine/zap2")),
                sound(Railcraft.rl("machine/zap3")),
                sound(Railcraft.rl("machine/zap4")))
            .subtitle(Translations.Subtitle.MACHINE_ZAP));
  }
}
