package mods.railcraft.data.worldgen.modifiers;

import com.mojang.serialization.Codec;
import mods.railcraft.data.worldgen.placements.RailcraftMiscOverworldPlacements;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;

public record RailcraftSaltpeterBiomeModifier (HolderSet<Biome> biomes, Decoration step)
    implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
        if (phase == Phase.ADD && biomes.contains(biome)) {
            var generation = builder.getGenerationSettings();
            generation.addFeature(step, RailcraftMiscOverworldPlacements.SALTPETER.getHolder().get());
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return RailcraftBiomeModifier.SALTPETER_BIOME_MODIFIER.get();
    }
}

