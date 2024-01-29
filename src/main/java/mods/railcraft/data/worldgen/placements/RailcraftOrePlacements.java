package mods.railcraft.data.worldgen.placements;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluids;

public class RailcraftOrePlacements {

  private static final int LEAD_VEIN_PER_CHUNK = 8;
  private static final int TIN_SMALL_VEIN_PER_CHUNK = 14;
  private static final int TIN_LARGE_VEIN_PER_CHUNK = 12;
  private static final int SULFUR_VEIN_PER_CHUNK = 20;
  private static final int SULFUR_BURIED_VEIN_PER_CHUNK = 12;
  private static final int ZINC_VEIN_PER_CHUNK = 7;
  private static final int NICKEL_UPPER_VEIN_PER_CHUNK = 70;
  private static final int NICKEL_MIDDLE_VEIN_PER_CHUNK = 10;
  private static final int NICKEL_SMALL_VEIN_PER_CHUNK = 10;
  private static final int SILVER_VEIN_PER_CHUNK = 6;
  // private static final int SALTPETER_VEIN_PER_CHUNK = 5;

  public static final ResourceKey<PlacedFeature> LEAD_ORE = createKey("lead_ore");
  public static final ResourceKey<PlacedFeature> TIN_ORE_SMALL = createKey("tin_ore_small");
  public static final ResourceKey<PlacedFeature> TIN_ORE_LARGE = createKey("tin_ore_large");
  public static final ResourceKey<PlacedFeature> SULFUR_ORE_UPPER = createKey("sulfur_ore_upper");
  public static final ResourceKey<PlacedFeature> SULFUR_ORE_LOWER = createKey("sulfur_ore_lower");
  public static final ResourceKey<PlacedFeature> ZINC_ORE = createKey("zinc_ore");
  public static final ResourceKey<PlacedFeature> NICKEL_ORE_UPPER = createKey("nickel_ore_upper");
  public static final ResourceKey<PlacedFeature> NICKEL_ORE_MIDDLE =
      createKey("nickel_ore_middle");
  public static final ResourceKey<PlacedFeature> NICKEL_ORE_SMALL = createKey("nickel_ore_small");
  public static final ResourceKey<PlacedFeature> SILVER_ORE = createKey("silver_ore");
  public static final ResourceKey<PlacedFeature> SILVER_ORE_LOWER = createKey("silver_ore_lower");
  public static final ResourceKey<PlacedFeature> QUARRIED_STONE = createKey("quarried_stone");
  public static final ResourceKey<PlacedFeature> SALTPETER = createKey("saltpeter");
  public static final ResourceKey<PlacedFeature> FIRESTONE = createKey("firestone");

  public static void bootstrap(BootstapContext<PlacedFeature> context) {
    context.register(LEAD_ORE,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.LEAD_ORE),
            OrePlacements.commonOrePlacement(LEAD_VEIN_PER_CHUNK,
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-24),
                    VerticalAnchor.absolute(64)))));
    context.register(TIN_ORE_SMALL,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.TIN_ORE_SMALL),
            OrePlacements.commonOrePlacement(TIN_SMALL_VEIN_PER_CHUNK,
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-20),
                    VerticalAnchor.absolute(94)))));
    context.register(TIN_ORE_LARGE,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.TIN_ORE_LARGE),
            OrePlacements.commonOrePlacement(TIN_LARGE_VEIN_PER_CHUNK,
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-32),
                    VerticalAnchor.absolute(72)))));
    context.register(SULFUR_ORE_UPPER,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.SULFUR_ORE),
            OrePlacements.commonOrePlacement(SULFUR_VEIN_PER_CHUNK,
                HeightRangePlacement.uniform(VerticalAnchor.absolute(136),
                    VerticalAnchor.top()))));
    context.register(SULFUR_ORE_LOWER,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.SULFUR_ORE_BURIED),
            OrePlacements.commonOrePlacement(SULFUR_BURIED_VEIN_PER_CHUNK,
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-10),
                    VerticalAnchor.absolute(182)))));
    context.register(ZINC_ORE,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.ZINC_ORE),
            OrePlacements.commonOrePlacement(ZINC_VEIN_PER_CHUNK,
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-24),
                    VerticalAnchor.absolute(64)))));
    context.register(NICKEL_ORE_UPPER,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.NICKEL_ORE),
            OrePlacements.commonOrePlacement(NICKEL_UPPER_VEIN_PER_CHUNK,
                HeightRangePlacement.triangle(VerticalAnchor.absolute(80),
                    VerticalAnchor.absolute(384)))));
    context.register(NICKEL_ORE_MIDDLE,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.NICKEL_ORE),
            OrePlacements.commonOrePlacement(NICKEL_MIDDLE_VEIN_PER_CHUNK,
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-24),
                    VerticalAnchor.absolute(56)))));
    context.register(NICKEL_ORE_SMALL,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.NICKEL_ORE_SMALL),
            OrePlacements.commonOrePlacement(NICKEL_SMALL_VEIN_PER_CHUNK,
                HeightRangePlacement.uniform(VerticalAnchor.bottom(),
                    VerticalAnchor.absolute(72)))));
    context.register(SILVER_ORE,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.SILVER_ORE_BURIED),
            OrePlacements.commonOrePlacement(SILVER_VEIN_PER_CHUNK,
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(32)))));
    context.register(SILVER_ORE_LOWER,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.SILVER_ORE),
            OrePlacements.orePlacement(
                CountPlacement.of(UniformInt.of(0, 1)),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(-48)))));
    context.register(QUARRIED_STONE,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.QUARRIED_STONE),
            OrePlacements.commonOrePlacement(6,
                HeightRangePlacement.uniform(VerticalAnchor.absolute(20),
                    VerticalAnchor.absolute(80)))));
    context.register(SALTPETER,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.SALTPETER),
            List.of(
                // CountPlacement.of(SALTPETER_VEIN_PER_CHUNK),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                BiomeFilter.biome())));
    context.register(FIRESTONE,
        new PlacedFeature(getConfiguredFeature(context, RailcraftOreFeatures.FIRESTONE),
            List.of(
                CountPlacement.of(2),
                PlacementUtils.FULL_RANGE,
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.LAVA)),
                BiomeFilter.biome())));
  }

  @NotNull
  private static ResourceKey<PlacedFeature> createKey(String name) {
    return ResourceKey.create(Registries.PLACED_FEATURE, RailcraftConstants.rl(name));
  }

  @NotNull
  private static Holder.Reference<ConfiguredFeature<?, ?>> getConfiguredFeature(
      @NotNull BootstapContext<PlacedFeature> context,
      ResourceKey<ConfiguredFeature<?, ?>> resourceKey) {
    return context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(resourceKey);
  }
}
