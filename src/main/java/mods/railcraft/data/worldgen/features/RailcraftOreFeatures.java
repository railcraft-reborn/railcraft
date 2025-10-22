package mods.railcraft.data.worldgen.features;

import java.util.List;
import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftOreFeatures {
  private static DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
      DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, RailcraftConstants.ID);

  private static final int LEAD_VEIN_DIMENSION = 9;
  private static final int TIN_SMALL_VEIN_DIMENSION = 4;
  private static final int TIN_LARGE_VEIN_DIMENSION = 9;
  private static final int SULFUR_VEIN_DIMENSION = 10;
  private static final int ZINC_VEIN_DIMENSION = 6;
  private static final int NICKEL_VEIN_DIMENSION = 7;
  private static final int NICKEL_SMALL_VEIN_DIMENSION = 4;
  private static final int SILVER_VEIN_DIMENSION = 10;
  private static final int QUARRIED_STONE_VEIN_DIMENSION = 32;

  private static final TagMatchTest STONE_ORE_REPLACEABLES =
      new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
  private static final TagMatchTest DEEPSLATE_ORE_REPLACEABLES =
      new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
  private static final TagMatchTest BASE_STONE_OVERWORLD =
      new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);

  private static final Supplier<List<OreConfiguration.TargetBlockState>> LEAD_ORE_TARGET_LIST =
      Suppliers.memoize(
          () ->
              List.of(
                  OreConfiguration.target(
                      STONE_ORE_REPLACEABLES, RailcraftBlocks.LEAD_ORE.get().defaultBlockState()),
                  OreConfiguration.target(
                      DEEPSLATE_ORE_REPLACEABLES,
                      RailcraftBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> TIN_ORE_TARGET_LIST =
      Suppliers.memoize(
          () ->
              List.of(
                  OreConfiguration.target(
                      STONE_ORE_REPLACEABLES, RailcraftBlocks.TIN_ORE.get().defaultBlockState()),
                  OreConfiguration.target(
                      DEEPSLATE_ORE_REPLACEABLES,
                      RailcraftBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> SULFUR_ORE_TARGET_LIST =
      Suppliers.memoize(
          () ->
              List.of(
                  OreConfiguration.target(
                      STONE_ORE_REPLACEABLES, RailcraftBlocks.SULFUR_ORE.get().defaultBlockState()),
                  OreConfiguration.target(
                      DEEPSLATE_ORE_REPLACEABLES,
                      RailcraftBlocks.DEEPSLATE_SULFUR_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> ZINC_ORE_TARGET_LIST =
      Suppliers.memoize(
          () ->
              List.of(
                  OreConfiguration.target(
                      STONE_ORE_REPLACEABLES, RailcraftBlocks.ZINC_ORE.get().defaultBlockState()),
                  OreConfiguration.target(
                      DEEPSLATE_ORE_REPLACEABLES,
                      RailcraftBlocks.DEEPSLATE_ZINC_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> NICKEL_ORE_TARGET_LIST =
      Suppliers.memoize(
          () ->
              List.of(
                  OreConfiguration.target(
                      STONE_ORE_REPLACEABLES, RailcraftBlocks.NICKEL_ORE.get().defaultBlockState()),
                  OreConfiguration.target(
                      DEEPSLATE_ORE_REPLACEABLES,
                      RailcraftBlocks.DEEPSLATE_NICKEL_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> SILVER_ORE_TARGET_LIST =
      Suppliers.memoize(
          () ->
              List.of(
                  OreConfiguration.target(
                      STONE_ORE_REPLACEABLES, RailcraftBlocks.SILVER_ORE.get().defaultBlockState()),
                  OreConfiguration.target(
                      DEEPSLATE_ORE_REPLACEABLES,
                      RailcraftBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())));

  public static final RegistryObject<ConfiguredFeature<?, ?>> LEAD_ORE =
      CONFIGURED_FEATURES.register(
          "lead_ore",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(LEAD_ORE_TARGET_LIST.get(), LEAD_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> TIN_ORE_SMALL =
      CONFIGURED_FEATURES.register(
          "tin_ore_small",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(TIN_ORE_TARGET_LIST.get(), TIN_SMALL_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> TIN_ORE_LARGE =
      CONFIGURED_FEATURES.register(
          "tin_ore_large",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(TIN_ORE_TARGET_LIST.get(), TIN_LARGE_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> SULFUR_ORE =
      CONFIGURED_FEATURES.register(
          "sulfur_ore",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(SULFUR_ORE_TARGET_LIST.get(), SULFUR_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> SULFUR_ORE_BURIED =
      CONFIGURED_FEATURES.register(
          "sulfur_ore_buried",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(SULFUR_ORE_TARGET_LIST.get(), SULFUR_VEIN_DIMENSION, 0.5F)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> ZINC_ORE =
      CONFIGURED_FEATURES.register(
          "zinc_ore",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(ZINC_ORE_TARGET_LIST.get(), ZINC_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> NICKEL_ORE =
      CONFIGURED_FEATURES.register(
          "nickel_ore",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(NICKEL_ORE_TARGET_LIST.get(), NICKEL_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> NICKEL_ORE_SMALL =
      CONFIGURED_FEATURES.register(
          "nickel_ore_small",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(NICKEL_ORE_TARGET_LIST.get(), NICKEL_SMALL_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> SILVER_ORE =
      CONFIGURED_FEATURES.register(
          "silver_ore",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(SILVER_ORE_TARGET_LIST.get(), SILVER_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> SILVER_ORE_BURIED =
      CONFIGURED_FEATURES.register(
          "silver_ore_buried",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(SILVER_ORE_TARGET_LIST.get(), SILVER_VEIN_DIMENSION, 0.5F)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> QUARRIED_STONE =
      CONFIGURED_FEATURES.register(
          "quarried_stone",
          () ->
              new ConfiguredFeature<>(
                  Feature.ORE,
                  new OreConfiguration(
                      BASE_STONE_OVERWORLD,
                      RailcraftBlocks.QUARRIED_STONE.get().defaultBlockState(),
                      QUARRIED_STONE_VEIN_DIMENSION)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> SALTPETER =
      CONFIGURED_FEATURES.register(
          "saltpeter",
          () ->
              new ConfiguredFeature<>(
                  Feature.DISK,
                  new DiskConfiguration(
                      RuleBasedBlockStateProvider.simple(RailcraftBlocks.SALTPETER_ORE.get()),
                      BlockPredicate.matchesBlocks(
                          List.of(Blocks.DIRT, RailcraftBlocks.SALTPETER_ORE.get())),
                      UniformInt.of(2, 3),
                      1)));
  public static final RegistryObject<ConfiguredFeature<?, ?>> FIRESTONE =
      CONFIGURED_FEATURES.register(
          "firestone",
          () ->
              new ConfiguredFeature<>(
                  Feature.DISK,
                  new DiskConfiguration(
                      RuleBasedBlockStateProvider.simple(RailcraftBlocks.FIRESTONE_ORE.get()),
                      BlockPredicate.matchesBlocks(
                          List.of(Blocks.NETHERRACK, RailcraftBlocks.FIRESTONE_ORE.get())),
                      ConstantInt.of(1),
                      1)));
}
