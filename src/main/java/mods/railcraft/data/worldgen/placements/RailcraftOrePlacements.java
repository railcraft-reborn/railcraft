package mods.railcraft.data.worldgen.placements;

import static net.minecraft.data.worldgen.placement.OrePlacements.commonOrePlacement;
import static net.minecraft.data.worldgen.placement.OrePlacements.orePlacement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
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

  private static final DeferredRegister<PlacedFeature> deferredRegister =
      DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Railcraft.ID);

  private static final Map<ResourceLocation, RegistryObject<PlacedFeature>>
      PLACED_FEATURE_MAP = new HashMap<>();

  public static final RegistryObject<PlacedFeature> LEAD_ORE = register("lead_ore",
      RailcraftOreFeatures.LEAD_ORE, () -> commonOrePlacement(LEAD_VEIN_PER_CHUNK,
          HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(64))
      ));

  public static final RegistryObject<PlacedFeature> TIN_ORE_SMALL = register("tin_ore_small",
      RailcraftOreFeatures.TIN_ORE_SMALL, () -> commonOrePlacement(TIN_SMALL_VEIN_PER_CHUNK,
          HeightRangePlacement.triangle(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(94))
      ));
  public static final RegistryObject<PlacedFeature> TIN_ORE_LARGE = register("tin_ore_large",
      RailcraftOreFeatures.TIN_ORE_LARGE, () -> commonOrePlacement(TIN_LARGE_VEIN_PER_CHUNK,
          HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(72))
      ));

  public static final RegistryObject<PlacedFeature> SULFUR_ORE_UPPER = register("sulfur_ore_upper",
      RailcraftOreFeatures.SULFUR_ORE, () -> commonOrePlacement(SULFUR_VEIN_PER_CHUNK,
          HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top())
      ));
  public static final RegistryObject<PlacedFeature> SULFUR_ORE_LOWER = register("sulfur_ore_lower",
      RailcraftOreFeatures.SULFUR_ORE_BURIED, () -> commonOrePlacement(
          SULFUR_BURIED_VEIN_PER_CHUNK,
          HeightRangePlacement.triangle(VerticalAnchor.absolute(-10), VerticalAnchor.absolute(182))
      ));

  public static final RegistryObject<PlacedFeature> ZINC_ORE = register("zinc_ore",
      RailcraftOreFeatures.ZINC_ORE, () -> commonOrePlacement(ZINC_VEIN_PER_CHUNK,
          HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(64))
      ));

  public static final RegistryObject<PlacedFeature> NICKEL_ORE_UPPER = register("nickel_ore_upper",
      RailcraftOreFeatures.NICKEL_ORE, () -> commonOrePlacement(NICKEL_UPPER_VEIN_PER_CHUNK,
          HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))
      ));
  public static final RegistryObject<PlacedFeature> NICKEL_ORE_MIDDLE = register(
      "nickel_ore_middle",
      RailcraftOreFeatures.NICKEL_ORE, () -> commonOrePlacement(NICKEL_MIDDLE_VEIN_PER_CHUNK,
          HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))
      ));
  public static final RegistryObject<PlacedFeature> NICKEL_ORE_SMALL = register("nickel_ore_small",
      RailcraftOreFeatures.NICKEL_ORE_SMALL, () -> commonOrePlacement(NICKEL_SMALL_VEIN_PER_CHUNK,
          HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))
      ));


  public static final RegistryObject<PlacedFeature> SILVER_ORE = register("silver_ore",
      RailcraftOreFeatures.SILVER_ORE_BURIED, () -> commonOrePlacement(SILVER_VEIN_PER_CHUNK,
          HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))
      ));
  public static final RegistryObject<PlacedFeature> SILVER_ORE_LOWER = register("silver_ore_lower",
      RailcraftOreFeatures.SILVER_ORE, () -> orePlacement(
          CountPlacement.of(UniformInt.of(0, 1)),
          HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))
      ));

  private static RegistryObject<PlacedFeature> register(String name,
      RegistryObject<ConfiguredFeature<?, ?>> configuredFeature,
      Supplier<List<PlacementModifier>> placementModifier) {

    var result = deferredRegister.register(name, () ->
        new PlacedFeature(Holder.direct(configuredFeature.get()), placementModifier.get()));

    PLACED_FEATURE_MAP.put(new ResourceLocation(Railcraft.ID, name), result);
    return result;
  }

  public static Map<ResourceLocation, PlacedFeature> getPlacedFeatureMap() {
    var result = new HashMap<ResourceLocation, PlacedFeature>();
    PLACED_FEATURE_MAP.forEach((key, value) -> result.put(key, value.get()));
    return result;
  }

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
