package mods.railcraft.data.worldgen.placements;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import mods.railcraft.Railcraft;
import mods.railcraft.data.worldgen.features.RailcraftMiscOverworldFeatures;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

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

  private static final DeferredRegister<PlacedFeature> deferredRegister =
      DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Railcraft.ID);

  public static final RegistryObject<PlacedFeature> LEAD_ORE =
      register("lead_ore",
          RailcraftOreFeatures.LEAD_ORE, () -> OrePlacements.commonOrePlacement(LEAD_VEIN_PER_CHUNK,
              HeightRangePlacement.triangle(VerticalAnchor.absolute(-24),
                  VerticalAnchor.absolute(64))));

  public static final RegistryObject<PlacedFeature> TIN_ORE_SMALL =
      register("tin_ore_small",
          RailcraftOreFeatures.TIN_ORE_SMALL,
          () -> OrePlacements.commonOrePlacement(TIN_SMALL_VEIN_PER_CHUNK,
              HeightRangePlacement.triangle(VerticalAnchor.absolute(-20),
                  VerticalAnchor.absolute(94))));
  public static final RegistryObject<PlacedFeature> TIN_ORE_LARGE =
      register("tin_ore_large",
          RailcraftOreFeatures.TIN_ORE_LARGE,
          () -> OrePlacements.commonOrePlacement(TIN_LARGE_VEIN_PER_CHUNK,
              HeightRangePlacement.triangle(VerticalAnchor.absolute(-32),
                  VerticalAnchor.absolute(72))));

  public static final RegistryObject<PlacedFeature> SULFUR_ORE_UPPER =
      register("sulfur_ore_upper",
          RailcraftOreFeatures.SULFUR_ORE,
          () -> OrePlacements.commonOrePlacement(SULFUR_VEIN_PER_CHUNK,
              HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top())));
  public static final RegistryObject<PlacedFeature> SULFUR_ORE_LOWER =
      register("sulfur_ore_lower",
          RailcraftOreFeatures.SULFUR_ORE_BURIED, () -> OrePlacements.commonOrePlacement(
              SULFUR_BURIED_VEIN_PER_CHUNK,
              HeightRangePlacement.triangle(VerticalAnchor.absolute(-10),
                  VerticalAnchor.absolute(182))));

  public static final RegistryObject<PlacedFeature> ZINC_ORE =
      register("zinc_ore",
          RailcraftOreFeatures.ZINC_ORE, () -> OrePlacements.commonOrePlacement(ZINC_VEIN_PER_CHUNK,
              HeightRangePlacement.triangle(VerticalAnchor.absolute(-24),
                  VerticalAnchor.absolute(64))));

  public static final RegistryObject<PlacedFeature> NICKEL_ORE_UPPER =
      register("nickel_ore_upper",
          RailcraftOreFeatures.NICKEL_ORE,
          () -> OrePlacements.commonOrePlacement(NICKEL_UPPER_VEIN_PER_CHUNK,
              HeightRangePlacement.triangle(VerticalAnchor.absolute(80),
                  VerticalAnchor.absolute(384))));
  public static final RegistryObject<PlacedFeature> NICKEL_ORE_MIDDLE =
      register("nickel_ore_middle",
          RailcraftOreFeatures.NICKEL_ORE,
          () -> OrePlacements.commonOrePlacement(NICKEL_MIDDLE_VEIN_PER_CHUNK,
              HeightRangePlacement.triangle(VerticalAnchor.absolute(-24),
                  VerticalAnchor.absolute(56))));
  public static final RegistryObject<PlacedFeature> NICKEL_ORE_SMALL =
      register("nickel_ore_small",
          RailcraftOreFeatures.NICKEL_ORE_SMALL,
          () -> OrePlacements.commonOrePlacement(NICKEL_SMALL_VEIN_PER_CHUNK,
              HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))));

  public static final RegistryObject<PlacedFeature> SILVER_ORE =
      register("silver_ore",
          RailcraftOreFeatures.SILVER_ORE_BURIED,
          () -> OrePlacements.commonOrePlacement(SILVER_VEIN_PER_CHUNK,
              HeightRangePlacement.triangle(VerticalAnchor.absolute(-64),
                  VerticalAnchor.absolute(32))));
  public static final RegistryObject<PlacedFeature> SILVER_ORE_LOWER =
      register("silver_ore_lower",
          RailcraftOreFeatures.SILVER_ORE, () -> OrePlacements.orePlacement(
              CountPlacement.of(UniformInt.of(0, 1)),
              HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                  VerticalAnchor.absolute(-48))));

  public static final RegistryObject<PlacedFeature> QUARRIED_STONE =
      register("quarried_stone",
          RailcraftOreFeatures.QUARRIED_STONE, () -> OrePlacements.commonOrePlacement(
              6, HeightRangePlacement.uniform(VerticalAnchor.absolute(20),
                  VerticalAnchor.absolute(80))
          ));

  public static final RegistryObject<PlacedFeature> SALTPETER =
      register("saltpeter",
          RailcraftMiscOverworldFeatures.SALTPETER,
          () -> List.of(
              // CountPlacement.of(SALTPETER_VEIN_PER_CHUNK),
              InSquarePlacement.spread(),
              PlacementUtils.HEIGHTMAP_TOP_SOLID,
              BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
              BiomeFilter.biome()));

  public static final RegistryObject<PlacedFeature> FIRESTONE =
      register("firestone",
          RailcraftMiscOverworldFeatures.FIRESTONE,
          () -> List.of(
              CountPlacement.of(2),
              PlacementUtils.FULL_RANGE,
              BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.LAVA)),
              BiomeFilter.biome()));

  private static RegistryObject<PlacedFeature> register(String name,
      RegistryObject<ConfiguredFeature<?, ?>> configuredFeature,
      Supplier<List<PlacementModifier>> placementModifier) {
    return deferredRegister.register(name,
        () -> new PlacedFeature(Holder.direct(configuredFeature.get()), placementModifier.get()));
  }

  public static Map<ResourceLocation, PlacedFeature> collectEntries() {
    return deferredRegister.getEntries().stream()
        .collect(Collectors.toMap(RegistryObject::getId, RegistryObject::get));
  }

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
