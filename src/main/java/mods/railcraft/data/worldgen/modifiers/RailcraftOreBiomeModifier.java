package mods.railcraft.data.worldgen.modifiers;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;

public record RailcraftOreBiomeModifier(HolderSet<Biome> biomes, Holder<PlacedFeature> feature)
    implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
        if (phase == Phase.ADD && biomes.contains(biome)) {
            var generation = builder.getGenerationSettings();
            generation.addFeature(Decoration.UNDERGROUND_ORES, feature);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return RailcraftBiomeModifier.BIOME_MODIFIER.get();
    }
}
