package mods.railcraft.data.worldgen.features;

import com.google.common.base.Suppliers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftOreFeatures {

    private static final int LEAD_VEIN_DIMENSION = 7;

    private static final DeferredRegister<ConfiguredFeature<?, ?>> deferredRegister =
        DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Railcraft.ID);

    private static final Map<ResourceLocation, RegistryObject<ConfiguredFeature<?, ?>>>
        CONFIGURED_FEATURE_MAP = new HashMap<>();


    private static final Supplier<List<OreConfiguration.TargetBlockState>> ORE_LEAD_TARGET_LIST =
        Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
                RailcraftBlocks.LEAD_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                RailcraftBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())
        ));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ORE_LEAD = register("lead_ore",
        () -> new OreConfiguration(ORE_LEAD_TARGET_LIST.get(), LEAD_VEIN_DIMENSION));


    private static RegistryObject<ConfiguredFeature<?, ?>> register(String name,
        Supplier<OreConfiguration> oreConfiguration) {
        RegistryObject<ConfiguredFeature<?, ?>> result = deferredRegister.register(name, () ->
            new ConfiguredFeature<>(Feature.ORE, oreConfiguration.get()));

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
