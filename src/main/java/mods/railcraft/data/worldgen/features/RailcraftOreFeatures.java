package mods.railcraft.data.worldgen.features;

import java.util.List;
import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
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

public class RailcraftOreFeatures {

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
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(STONE_ORE_REPLACEABLES,
              RailcraftBlocks.LEAD_ORE.get().defaultBlockState()),
          OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> TIN_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(STONE_ORE_REPLACEABLES,
              RailcraftBlocks.TIN_ORE.get().defaultBlockState()),
          OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> SULFUR_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(STONE_ORE_REPLACEABLES,
              RailcraftBlocks.SULFUR_ORE.get().defaultBlockState()),
          OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_SULFUR_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> ZINC_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(STONE_ORE_REPLACEABLES,
              RailcraftBlocks.ZINC_ORE.get().defaultBlockState()),
          OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_ZINC_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> NICKEL_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(STONE_ORE_REPLACEABLES,
              RailcraftBlocks.NICKEL_ORE.get().defaultBlockState()),
          OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_NICKEL_ORE.get().defaultBlockState())));
  private static final Supplier<List<OreConfiguration.TargetBlockState>> SILVER_ORE_TARGET_LIST =
      Suppliers.memoize(() -> List.of(
          OreConfiguration.target(STONE_ORE_REPLACEABLES,
              RailcraftBlocks.SILVER_ORE.get().defaultBlockState()),
          OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES,
              RailcraftBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())));

  public static final ResourceKey<ConfiguredFeature<?, ?>> LEAD_ORE = createKey("lead_ore");
  public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE_SMALL =
      createKey("tin_ore_small");
  public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE_LARGE =
      createKey("tin_ore_large");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_ORE = createKey("sulfur_ore");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_ORE_BURIED =
      createKey("sulfur_ore_buried");
  public static final ResourceKey<ConfiguredFeature<?, ?>> ZINC_ORE = createKey("zinc_ore");
  public static final ResourceKey<ConfiguredFeature<?, ?>> NICKEL_ORE = createKey("nickel_ore");
  public static final ResourceKey<ConfiguredFeature<?, ?>> NICKEL_ORE_SMALL =
      createKey("nickel_ore_small");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_ORE = createKey("silver_ore");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_ORE_BURIED =
      createKey("silver_ore_buried");
  public static final ResourceKey<ConfiguredFeature<?, ?>> QUARRIED_STONE =
      createKey("quarried_stone");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SALTPETER = createKey("saltpeter");
  public static final ResourceKey<ConfiguredFeature<?, ?>> FIRESTONE = createKey("firestone");

  public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
    context.register(LEAD_ORE, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(LEAD_ORE_TARGET_LIST.get(), LEAD_VEIN_DIMENSION)));
    context.register(TIN_ORE_SMALL, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(TIN_ORE_TARGET_LIST.get(), TIN_SMALL_VEIN_DIMENSION)));
    context.register(TIN_ORE_LARGE, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(TIN_ORE_TARGET_LIST.get(), TIN_LARGE_VEIN_DIMENSION)));
    context.register(SULFUR_ORE, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(SULFUR_ORE_TARGET_LIST.get(), SULFUR_VEIN_DIMENSION)));
    context.register(SULFUR_ORE_BURIED, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(SULFUR_ORE_TARGET_LIST.get(), SULFUR_VEIN_DIMENSION, 0.5F)));
    context.register(ZINC_ORE, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(ZINC_ORE_TARGET_LIST.get(), ZINC_VEIN_DIMENSION)));
    context.register(NICKEL_ORE, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(NICKEL_ORE_TARGET_LIST.get(), NICKEL_VEIN_DIMENSION)));
    context.register(NICKEL_ORE_SMALL, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(NICKEL_ORE_TARGET_LIST.get(), NICKEL_SMALL_VEIN_DIMENSION)));
    context.register(SILVER_ORE, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(SILVER_ORE_TARGET_LIST.get(), SILVER_VEIN_DIMENSION)));
    context.register(SILVER_ORE_BURIED, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(SILVER_ORE_TARGET_LIST.get(), SILVER_VEIN_DIMENSION, 0.5F)));
    context.register(QUARRIED_STONE, new ConfiguredFeature<>(Feature.ORE,
        new OreConfiguration(BASE_STONE_OVERWORLD,
            RailcraftBlocks.QUARRIED_STONE.get().defaultBlockState(),
            QUARRIED_STONE_VEIN_DIMENSION)));
    context.register(SALTPETER, new ConfiguredFeature<>(Feature.DISK,
        new DiskConfiguration(
            RuleBasedBlockStateProvider.simple(RailcraftBlocks.SALTPETER_ORE.get()),
            BlockPredicate.matchesBlocks(
                List.of(Blocks.DIRT, RailcraftBlocks.SALTPETER_ORE.get())),
            UniformInt.of(2, 3), 1)));
    context.register(FIRESTONE, new ConfiguredFeature<>(Feature.DISK,
        new DiskConfiguration(
            RuleBasedBlockStateProvider.simple(RailcraftBlocks.FIRESTONE_ORE.get()),
            BlockPredicate.matchesBlocks(
                List.of(Blocks.NETHERRACK, RailcraftBlocks.FIRESTONE_ORE.get())),
            ConstantInt.of(1), 1)));
  }

  private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
    return ResourceKey.create(Registries.CONFIGURED_FEATURE, RailcraftConstants.rl(name));
  }
}
