package mods.railcraft.data;

import mods.railcraft.Railcraft;
import mods.railcraft.Translations.Subtitle;
import mods.railcraft.sounds.RailcraftSoundEvents;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class RailcraftSoundsProvider extends SoundDefinitionsProvider {

    public RailcraftSoundsProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Railcraft.ID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(RailcraftSoundEvents.STEAM_WHISTLE.get(), definition().with(
            railcraftSound("locomotive/steam/whistle1"),
            railcraftSound("locomotive/steam/whistle2"),
            railcraftSound("locomotive/steam/whistle3")
        ).subtitle(Subtitle.STEAM_WHISTLE));
        this.add(RailcraftSoundEvents.ELECTRIC_WHISTLE.get(), definition().with(
            railcraftSound("locomotive/electric/whistle1"),
            railcraftSound("locomotive/electric/whistle2"),
            railcraftSound("locomotive/electric/whistle3")
        ).subtitle(Subtitle.ELECTRIC_WHISTLE));
        this.add(RailcraftSoundEvents.STEAM_BURST.get(), definition().with(
            railcraftSound("machine/steam_burst")
        ).subtitle(Subtitle.STEAM_BURST));
        this.add(RailcraftSoundEvents.STEAM_HISS.get(), definition().with(
            railcraftSound("machine/steam_hiss")
        ).subtitle(Subtitle.STEAM_HISS));
        this.add(RailcraftSoundEvents.MACHINE_ZAP.get(), definition().with(
            railcraftSound("machine/zap1"),
            railcraftSound("machine/zap2"),
            railcraftSound("machine/zap3"),
            railcraftSound("machine/zap4")
        ).subtitle(Subtitle.MACHINE_ZAP));
    }
    protected static SoundDefinition.Sound railcraftSound(final String name)
    {
        return sound(new ResourceLocation(Railcraft.ID, name));
    }

    @Override
    public String getName() {
        return "Railcraft Sounds";
    }
}
