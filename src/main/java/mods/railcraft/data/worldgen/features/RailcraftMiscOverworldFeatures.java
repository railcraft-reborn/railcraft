package mods.railcraft.data.worldgen.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftMiscOverworldFeatures {

    private static final DeferredRegister<ConfiguredFeature<?, ?>> deferredRegister =
        DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Railcraft.ID);

    private static final Map<ResourceLocation, RegistryObject<ConfiguredFeature<?, ?>>>
        CONFIGURED_FEATURE_MAP = new HashMap<>();

    public static final RegistryObject<ConfiguredFeature<?, ?>> SALTPETER_ORE = register(
        "saltpeter_disk",
        () -> new DiskConfiguration(RuleBasedBlockStateProvider.simple(RailcraftBlocks.SALTPETER_ORE.get()),
            BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, RailcraftBlocks.SALTPETER_ORE.get())),
            UniformInt.of(2, 3), 1));



    private static RegistryObject<ConfiguredFeature<?, ?>> register(String name,
        Supplier<DiskConfiguration> diskConfiguration) {
        RegistryObject<ConfiguredFeature<?, ?>> result = deferredRegister.register(name, () ->
            new ConfiguredFeature<>(Feature.DISK, diskConfiguration.get()));

        CONFIGURED_FEATURE_MAP.put(new ResourceLocation(Railcraft.ID, name), result);
        return result;
    }

    public static Map<ResourceLocation, ConfiguredFeature<?, ?>> getConfiguredFeatureMap() {
        var result = new HashMap<ResourceLocation, ConfiguredFeature<?, ?>>();
        CONFIGURED_FEATURE_MAP.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    public static void register(IEventBus modEventBus) {
        deferredRegister.register(modEventBus);
    }
}
