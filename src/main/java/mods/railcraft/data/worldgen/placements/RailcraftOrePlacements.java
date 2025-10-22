package mods.railcraft.data.worldgen.placements;

import java.util.List;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftOrePlacements {
  private static DeferredRegister<PlacedFeature> PLACED_FEATURES =
      DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, RailcraftConstants.ID);

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

  public static final RegistryObject<PlacedFeature> LEAD_ORE =
      PLACED_FEATURES.register(
          "lead_ore",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.LEAD_ORE.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      LEAD_VEIN_PER_CHUNK,
                      HeightRangePlacement.triangle(
                          VerticalAnchor.absolute(-24), VerticalAnchor.absolute(64)))));
  public static final RegistryObject<PlacedFeature> TIN_ORE_SMALL =
      PLACED_FEATURES.register(
          "tin_ore_small",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.TIN_ORE_SMALL.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      TIN_SMALL_VEIN_PER_CHUNK,
                      HeightRangePlacement.triangle(
                          VerticalAnchor.absolute(-20), VerticalAnchor.absolute(94)))));
  public static final RegistryObject<PlacedFeature> TIN_ORE_LARGE =
      PLACED_FEATURES.register(
          "tin_ore_large",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.TIN_ORE_LARGE.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      TIN_LARGE_VEIN_PER_CHUNK,
                      HeightRangePlacement.triangle(
                          VerticalAnchor.absolute(-32), VerticalAnchor.absolute(72)))));
  public static final RegistryObject<PlacedFeature> SULFUR_ORE_UPPER =
      PLACED_FEATURES.register(
          "sulfur_ore_upper",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.SULFUR_ORE.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      SULFUR_VEIN_PER_CHUNK,
                      HeightRangePlacement.uniform(
                          VerticalAnchor.absolute(136), VerticalAnchor.top()))));
  public static final RegistryObject<PlacedFeature> SULFUR_ORE_LOWER =
      PLACED_FEATURES.register(
          "sulfur_ore_lower",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.SULFUR_ORE_BURIED.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      SULFUR_BURIED_VEIN_PER_CHUNK,
                      HeightRangePlacement.triangle(
                          VerticalAnchor.absolute(-10), VerticalAnchor.absolute(182)))));
  public static final RegistryObject<PlacedFeature> ZINC_ORE =
      PLACED_FEATURES.register(
          "zinc_ore",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.ZINC_ORE.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      ZINC_VEIN_PER_CHUNK,
                      HeightRangePlacement.triangle(
                          VerticalAnchor.absolute(-24), VerticalAnchor.absolute(64)))));
  public static final RegistryObject<PlacedFeature> NICKEL_ORE_UPPER =
      PLACED_FEATURES.register(
          "nickel_ore_upper",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.NICKEL_ORE.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      NICKEL_UPPER_VEIN_PER_CHUNK,
                      HeightRangePlacement.triangle(
                          VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))));
  public static final RegistryObject<PlacedFeature> NICKEL_ORE_MIDDLE =
      PLACED_FEATURES.register(
          "nickel_ore_middle",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.NICKEL_ORE.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      NICKEL_MIDDLE_VEIN_PER_CHUNK,
                      HeightRangePlacement.triangle(
                          VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)))));
  public static final RegistryObject<PlacedFeature> NICKEL_ORE_SMALL =
      PLACED_FEATURES.register(
          "nickel_ore_small",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.NICKEL_ORE_SMALL.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      NICKEL_SMALL_VEIN_PER_CHUNK,
                      HeightRangePlacement.uniform(
                          VerticalAnchor.bottom(), VerticalAnchor.absolute(72)))));
  public static final RegistryObject<PlacedFeature> SILVER_ORE =
      PLACED_FEATURES.register(
          "silver_ore",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.SILVER_ORE_BURIED.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      SILVER_VEIN_PER_CHUNK,
                      HeightRangePlacement.triangle(
                          VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)))));
  public static final RegistryObject<PlacedFeature> SILVER_ORE_LOWER =
      PLACED_FEATURES.register(
          "silver_ore_lower",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.SILVER_ORE.getHolder().get(),
                  OrePlacements.orePlacement(
                      CountPlacement.of(UniformInt.of(0, 1)),
                      HeightRangePlacement.uniform(
                          VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48)))));
  public static final RegistryObject<PlacedFeature> QUARRIED_STONE =
      PLACED_FEATURES.register(
          "quarried_stone",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.QUARRIED_STONE.getHolder().get(),
                  OrePlacements.commonOrePlacement(
                      6,
                      HeightRangePlacement.uniform(
                          VerticalAnchor.absolute(20), VerticalAnchor.absolute(80)))));
  public static final RegistryObject<PlacedFeature> SALTPETER =
      PLACED_FEATURES.register(
          "saltpeter",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.SALTPETER.getHolder().get(),
                  List.of(
                      // CountPlacement.of(SALTPETER_VEIN_PER_CHUNK),
                      InSquarePlacement.spread(),
                      PlacementUtils.HEIGHTMAP_TOP_SOLID,
                      BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                      BiomeFilter.biome())));
  public static final RegistryObject<PlacedFeature> FIRESTONE =
      PLACED_FEATURES.register(
          "firestone",
          () ->
              new PlacedFeature(
                  RailcraftOreFeatures.FIRESTONE.getHolder().get(),
                  List.of(
                      CountPlacement.of(2),
                      PlacementUtils.FULL_RANGE,
                      BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.LAVA)),
                      BiomeFilter.biome())));
}
