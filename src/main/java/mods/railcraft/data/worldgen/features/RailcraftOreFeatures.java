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

    private static final int LEAD_VEIN_DIMENSION = 9;
    private static final int TIN_SMALL_VEIN_DIMENSION = 4;
    private static final int TIN_LARGE_VEIN_DIMENSION = 9;
    private static final int SULFURE_VEIN_DIMENSION = 10;
    private static final int ZINC_VEIN_DIMENSION = 6;
    private static final int NICKEL_VEIN_DIMENSION = 7;
    private static final int NICKEL_SMALL_VEIN_DIMENSION = 4;
    private static final int SILVER_VEIN_DIMENSION = 10;

    private static final DeferredRegister<ConfiguredFeature<?, ?>> deferredRegister =
        DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Railcraft.ID);

    private static final Map<ResourceLocation, RegistryObject<ConfiguredFeature<?, ?>>>
        CONFIGURED_FEATURE_MAP = new HashMap<>();


    private static final Supplier<List<OreConfiguration.TargetBlockState>> LEAD_ORE_TARGET_LIST =
        Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
                RailcraftBlocks.LEAD_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                RailcraftBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())
        ));

    private static final Supplier<List<OreConfiguration.TargetBlockState>> TIN_ORE_TARGET_LIST =
        Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
                RailcraftBlocks.TIN_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                RailcraftBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())
        ));

    private static final Supplier<List<OreConfiguration.TargetBlockState>> SULFURE_ORE_TARGET_LIST =
        Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
                RailcraftBlocks.SULFURE_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                RailcraftBlocks.DEEPSLATE_SULFURE_ORE.get().defaultBlockState())
        ));

    private static final Supplier<List<OreConfiguration.TargetBlockState>> ZINC_ORE_TARGET_LIST =
        Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
                RailcraftBlocks.ZINC_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                RailcraftBlocks.DEEPSLATE_ZINC_ORE.get().defaultBlockState())
        ));

    private static final Supplier<List<OreConfiguration.TargetBlockState>> NICKEL_ORE_TARGET_LIST =
        Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
                RailcraftBlocks.NICKEL_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                RailcraftBlocks.DEEPSLATE_NICKEL_ORE.get().defaultBlockState())
        ));

    private static final Supplier<List<OreConfiguration.TargetBlockState>> SILVER_ORE_TARGET_LIST =
        Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
                RailcraftBlocks.SILVER_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                RailcraftBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())
        ));

    public static final RegistryObject<ConfiguredFeature<?, ?>> LEAD_ORE = register("lead_ore",
        () -> new OreConfiguration(LEAD_ORE_TARGET_LIST.get(), LEAD_VEIN_DIMENSION));

    public static final RegistryObject<ConfiguredFeature<?, ?>> TIN_ORE_SMALL = register("tin_ore_small",
        () -> new OreConfiguration(TIN_ORE_TARGET_LIST.get(), TIN_SMALL_VEIN_DIMENSION));

    public static final RegistryObject<ConfiguredFeature<?, ?>> TIN_ORE_LARGE = register("tin_ore_large",
        () -> new OreConfiguration(TIN_ORE_TARGET_LIST.get(), TIN_LARGE_VEIN_DIMENSION));

    public static final RegistryObject<ConfiguredFeature<?, ?>> SULFURE_ORE = register("sulfure_ore",
        () -> new OreConfiguration(SULFURE_ORE_TARGET_LIST.get(), SULFURE_VEIN_DIMENSION));

    public static final RegistryObject<ConfiguredFeature<?, ?>> SULFURE_ORE_BURIED = register("sulfure_ore_buried",
        () -> new OreConfiguration(SULFURE_ORE_TARGET_LIST.get(), SULFURE_VEIN_DIMENSION, 0.5F));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ZINC_ORE = register("zinc_ore",
        () -> new OreConfiguration(ZINC_ORE_TARGET_LIST.get(), ZINC_VEIN_DIMENSION));

    public static final RegistryObject<ConfiguredFeature<?, ?>> NICKEL_ORE = register("nickel_ore",
        () -> new OreConfiguration(NICKEL_ORE_TARGET_LIST.get(), NICKEL_VEIN_DIMENSION));

    public static final RegistryObject<ConfiguredFeature<?, ?>> NICKEL_ORE_SMALL = register("nickel_ore_small",
        () -> new OreConfiguration(NICKEL_ORE_TARGET_LIST.get(), NICKEL_SMALL_VEIN_DIMENSION));

    public static final RegistryObject<ConfiguredFeature<?, ?>> SILVER_ORE = register("silver_ore",
        () -> new OreConfiguration(SILVER_ORE_TARGET_LIST.get(), SILVER_VEIN_DIMENSION));

    public static final RegistryObject<ConfiguredFeature<?, ?>> SILVER_ORE_BURIED = register("silver_ore_buried",
        () -> new OreConfiguration(SILVER_ORE_TARGET_LIST.get(), SILVER_VEIN_DIMENSION, 0.5F));


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
