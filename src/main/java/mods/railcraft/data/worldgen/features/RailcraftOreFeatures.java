package mods.railcraft.data.worldgen.features;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.google.common.base.Suppliers;
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
  private static final int SULFUR_VEIN_DIMENSION = 10;
  private static final int ZINC_VEIN_DIMENSION = 6;
  private static final int NICKEL_VEIN_DIMENSION = 7;
  private static final int NICKEL_SMALL_VEIN_DIMENSION = 4;
  private static final int SILVER_VEIN_DIMENSION = 10;

  private static final DeferredRegister<ConfiguredFeature<?, ?>> deferredRegister =
      DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Railcraft.ID);

  private static final Supplier<List<OreConfiguration.TargetBlockState>> LEAD_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
              RailcraftBlocks.LEAD_ORE.get().defaultBlockState()),
          OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())));
  public static final RegistryObject<ConfiguredFeature<?, ?>> LEAD_ORE =
      register("lead_ore",
          () -> new OreConfiguration(LEAD_ORE_TARGET_LIST.get(), LEAD_VEIN_DIMENSION));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> TIN_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
              RailcraftBlocks.TIN_ORE.get().defaultBlockState()),
          OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())));
  public static final RegistryObject<ConfiguredFeature<?, ?>> TIN_ORE_SMALL =
      register("tin_ore_small",
          () -> new OreConfiguration(TIN_ORE_TARGET_LIST.get(), TIN_SMALL_VEIN_DIMENSION));
  public static final RegistryObject<ConfiguredFeature<?, ?>> TIN_ORE_LARGE =
      register("tin_ore_large",
          () -> new OreConfiguration(TIN_ORE_TARGET_LIST.get(), TIN_LARGE_VEIN_DIMENSION));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> SULFUR_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
              RailcraftBlocks.SULFUR_ORE.get().defaultBlockState()),
          OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_SULFUR_ORE.get().defaultBlockState())));
  public static final RegistryObject<ConfiguredFeature<?, ?>> SULFUR_ORE =
      register("sulfur_ore",
          () -> new OreConfiguration(SULFUR_ORE_TARGET_LIST.get(), SULFUR_VEIN_DIMENSION));
  public static final RegistryObject<ConfiguredFeature<?, ?>> SULFUR_ORE_BURIED =
      register("sulfur_ore_buried",
          () -> new OreConfiguration(SULFUR_ORE_TARGET_LIST.get(), SULFUR_VEIN_DIMENSION, 0.5F));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> ZINC_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
              RailcraftBlocks.ZINC_ORE.get().defaultBlockState()),
          OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_ZINC_ORE.get().defaultBlockState())));
  public static final RegistryObject<ConfiguredFeature<?, ?>> ZINC_ORE =
      register("zinc_ore",
          () -> new OreConfiguration(ZINC_ORE_TARGET_LIST.get(), ZINC_VEIN_DIMENSION));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> NICKEL_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
              RailcraftBlocks.NICKEL_ORE.get().defaultBlockState()),
          OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_NICKEL_ORE.get().defaultBlockState())));
  public static final RegistryObject<ConfiguredFeature<?, ?>> NICKEL_ORE =
      register("nickel_ore",
          () -> new OreConfiguration(NICKEL_ORE_TARGET_LIST.get(), NICKEL_VEIN_DIMENSION));
  public static final RegistryObject<ConfiguredFeature<?, ?>> NICKEL_ORE_SMALL =
      register("nickel_ore_small",
          () -> new OreConfiguration(NICKEL_ORE_TARGET_LIST.get(), NICKEL_SMALL_VEIN_DIMENSION));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> SILVER_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,
              RailcraftBlocks.SILVER_ORE.get().defaultBlockState()),
          OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())));
  public static final RegistryObject<ConfiguredFeature<?, ?>> SILVER_ORE =
      register("silver_ore",
          () -> new OreConfiguration(SILVER_ORE_TARGET_LIST.get(), SILVER_VEIN_DIMENSION));

  public static final RegistryObject<ConfiguredFeature<?, ?>> SILVER_ORE_BURIED =
      register("silver_ore_buried",
          () -> new OreConfiguration(SILVER_ORE_TARGET_LIST.get(), SILVER_VEIN_DIMENSION, 0.5F));

  private static RegistryObject<ConfiguredFeature<?, ?>> register(String name,
      Supplier<OreConfiguration> oreConfiguration) {
    return deferredRegister.register(name,
        () -> new ConfiguredFeature<>(Feature.ORE, oreConfiguration.get()));
  }

  public static Map<ResourceLocation, ConfiguredFeature<?, ?>> collectEntries() {
    return deferredRegister.getEntries().stream()
        .collect(Collectors.toMap(RegistryObject::getId, RegistryObject::get));
  }

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
