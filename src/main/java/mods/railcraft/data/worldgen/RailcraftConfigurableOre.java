package mods.railcraft.data.worldgen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import mods.railcraft.Railcraft;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Holder for configurable ore stuff. Because config loads before registry
 */
public class RailcraftConfigurableOre {

  public enum PLACEMENT_TYPE {
    RARE,
    COMMON
  }

  /* === PLACED FEATURE VALUES === */
  private PlacedFeature placedFeature;
  public final ResourceLocation resourceLocation;
  private PLACEMENT_TYPE placementType;
  private List<Pair<RuleTest, ResourceLocation>> oreBlockTypes;
  private int count;
  private int maxGroupSize;
  private int minY;
  private int maxY;

  /* === CONFIGURATION VALUES === */
  private EnumValue<PLACEMENT_TYPE> placementTypeConfig;
  private IntValue countConfig;
  private IntValue maxGroupSizeConfig;
  private IntValue minYConfig;
  private IntValue maxYConfig;
  private ConfigValue<List<? extends String>> biomeIgnoreList;

  /**
   * Creates a new configurable ore spawn
   * Will only take config changes during game restarts (or maybe during data reload (biomes etc) as we register there.)
   * @param id - The id used in config, PlacedFeature, and ConfiguredFeature
   * @param oreBlockTypes - A list containing another list with its variants.
   * @param placementType
   * @param count
   * @param maxVeinCount
   * @param minY
   * @param maxY
   */
  public RailcraftConfigurableOre(String id, List<Pair<RuleTest, ResourceLocation>> oreBlockTypes,
      PLACEMENT_TYPE placementType, int count, int maxGroupSize, // pls confirm if its max vein count, count one is chunk max i think
      int minY, int maxY) {

    this.oreBlockTypes = oreBlockTypes;
    this.placementType = placementType;
    this.count = count;
    this.maxGroupSize = maxGroupSize;
    this.minY = minY;
    this.maxY = maxY;
    this.resourceLocation = new ResourceLocation(Railcraft.ID, id);
  }

  /**
   * Generate the config spec for this ore generator.
   */
  public void generateOreConfig(Builder builder) {
    builder.comment("Ore generation for " + resourceLocation.getPath())
      .push(resourceLocation.getPath());

    this.placementTypeConfig = builder
        .comment("What placing system should this ore generator use? RARE (average) or COMMON (max count in chunk)")
        .defineEnum("placementType", this.placementType);

    this.countConfig = builder
        .comment("Maximum amount of ores that can spawn in a vein? (unconfirmed)")
        .defineInRange("count", this.count, 0, 64);

    this.maxGroupSizeConfig = builder
        .comment("Maximum amount of ores that can spawn in a clump. If RARE, it will be the average.")
        .defineInRange("maxGroupSize", this.maxGroupSize, 0, 256);

    this.minYConfig = builder
        .comment("Minimum y level spawn")
        .defineInRange("minY", this.minY, -32, 319);

    this.maxYConfig = builder
        .comment("Maximum y level spawn")
        .defineInRange("maxY", this.maxY, -32, 319);

    this.biomeIgnoreList = builder
        .comment("Biome ignore list. Must be a valid resource path (eg: 'minecraft:nether').")
        .defineList("biomeIgnoreList", List.<String>of(),
            obj -> ResourceLocation.isValidResourceLocation(obj.toString()));
    builder.pop();
  }

  /**
   * Hook for reloading ore configs.
   */
  public void handleConfigSpecLoad() {
    this.placementType = this.placementTypeConfig.get();
    this.count = this.countConfig.get();
    this.maxGroupSize = this.maxGroupSizeConfig.get();
    this.minY = this.minYConfig.get();
    this.maxY = this.maxYConfig.get();
  }

  public List<? extends String> bannedBiomesList() {
    return this.biomeIgnoreList.get();
  }

  /**
   * Get the {@link PlacedFeature PlacedFeature} of this, for registring.
   * <p/>
   * IMPORTANT! Once this is called, there is no going back (yet)
   * <p/>
   * TODO: check if sideness can break this
   */
  public PlacedFeature placedFeature() {
    if (this.placedFeature == null) {
      List<OreConfiguration.TargetBlockState> oreConfigurationTarget = new ArrayList<>();

      this.oreBlockTypes.forEach(things -> {
        oreConfigurationTarget.add(OreConfiguration.target(
          things.getLeft(),
          ForgeRegistries.BLOCKS.getValue(things.getRight()).defaultBlockState()));
      });

      var oreFeature = FeatureUtils.register(
          resourceLocation.toString(),
          Feature.ORE.configured(new OreConfiguration(oreConfigurationTarget, this.count)));

      var heightRange = HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minY), VerticalAnchor.absolute(this.maxY));

      this.placedFeature = PlacementUtils.register(
          resourceLocation.toString(),
          oreFeature.placed(
            placementType == PLACEMENT_TYPE.COMMON
                ? commonOrePlacement(this.maxGroupSize, heightRange)
                : rareOrePlacement(this.maxGroupSize, heightRange)
          ));
    }

    return this.placedFeature;
  }

  private static final List<PlacementModifier> orePlacement(
        PlacementModifier placementModifier, PlacementModifier secondaryPlacementModifier) {
    return List.of(placementModifier, InSquarePlacement.spread(), secondaryPlacementModifier, BiomeFilter.biome());
  }

  private static final List<PlacementModifier> commonOrePlacement(int oreCount, PlacementModifier placementModifier) {
    return orePlacement(CountPlacement.of(oreCount), placementModifier);
  }

  private static final List<PlacementModifier> rareOrePlacement(int oreCountAvg, PlacementModifier placementModifier) {
    return orePlacement(RarityFilter.onAverageOnceEvery(oreCountAvg), placementModifier);
  }
}
