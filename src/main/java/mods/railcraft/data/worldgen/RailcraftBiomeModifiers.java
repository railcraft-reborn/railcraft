package mods.railcraft.data.worldgen;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.worldgen.placements.RailcraftOrePlacements;
import net.minecraft.core.HolderSet;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftBiomeModifiers {
  private static final DeferredRegister<BiomeModifier> BIOME_MODIFIERS =
      DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIERS, RailcraftConstants.ID);

  private static final RegistryObject<BiomeModifier> LEAD_ORE;
  private static final RegistryObject<BiomeModifier> TIN_ORE_SMALL;
  private static final RegistryObject<BiomeModifier> TIN_ORE_LARGE;
  private static final RegistryObject<BiomeModifier> SULFUR_ORE_UPPER;
  private static final RegistryObject<BiomeModifier> SULFUR_ORE_LOWER;
  private static final RegistryObject<BiomeModifier> ZINC_ORE;
  private static final RegistryObject<BiomeModifier> NICKEL_ORE_UPPER;
  private static final RegistryObject<BiomeModifier> NICKEL_ORE_MIDDLE;
  private static final RegistryObject<BiomeModifier> NICKEL_ORE_SMALL;
  private static final RegistryObject<BiomeModifier> SILVER_ORE;
  private static final RegistryObject<BiomeModifier> SILVER_ORE_LOWER;
  private static final RegistryObject<BiomeModifier> QUARRIED_STONE;
  private static final RegistryObject<BiomeModifier> SALTPETER;
  private static final RegistryObject<BiomeModifier> FIRESTONE;

  static {
    var overworldTag = BuiltinRegistries.BIOME.getOrCreateTag(BiomeTags.IS_OVERWORLD);
    var netherTag = BuiltinRegistries.BIOME.getOrCreateTag(BiomeTags.IS_NETHER);
    var forestTag = BuiltinRegistries.BIOME.getOrCreateTag(BiomeTags.IS_FOREST);

    LEAD_ORE =
        BIOME_MODIFIERS.register(
            "add_lead_ore",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.LEAD_ORE),
                    Decoration.UNDERGROUND_ORES));
    TIN_ORE_SMALL =
        BIOME_MODIFIERS.register(
            "add_tin_ore_small",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.TIN_ORE_SMALL),
                    Decoration.UNDERGROUND_ORES));
    TIN_ORE_LARGE =
        BIOME_MODIFIERS.register(
            "add_tin_ore_large",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.TIN_ORE_LARGE),
                    Decoration.UNDERGROUND_ORES));
    SULFUR_ORE_UPPER =
        BIOME_MODIFIERS.register(
            "add_sulfur_ore_upper",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.SULFUR_ORE_UPPER),
                    Decoration.UNDERGROUND_ORES));
    SULFUR_ORE_LOWER =
        BIOME_MODIFIERS.register(
            "add_sulfur_ore_lower",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.SULFUR_ORE_LOWER),
                    Decoration.UNDERGROUND_ORES));
    ZINC_ORE =
        BIOME_MODIFIERS.register(
            "add_zinc_ore",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.ZINC_ORE),
                    Decoration.UNDERGROUND_ORES));
    NICKEL_ORE_UPPER =
        BIOME_MODIFIERS.register(
            "add_nickel_ore_upper",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.NICKEL_ORE_UPPER),
                    Decoration.UNDERGROUND_ORES));
    NICKEL_ORE_MIDDLE =
        BIOME_MODIFIERS.register(
            "add_nickel_ore_middle",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.NICKEL_ORE_MIDDLE),
                    Decoration.UNDERGROUND_ORES));
    NICKEL_ORE_SMALL =
        BIOME_MODIFIERS.register(
            "add_nickel_ore_small",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.NICKEL_ORE_SMALL),
                    Decoration.UNDERGROUND_ORES));
    SILVER_ORE =
        BIOME_MODIFIERS.register(
            "add_silver_ore",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.SILVER_ORE),
                    Decoration.UNDERGROUND_ORES));
    SILVER_ORE_LOWER =
        BIOME_MODIFIERS.register(
            "add_silver_ore_lower",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.SILVER_ORE_LOWER),
                    Decoration.UNDERGROUND_ORES));
    QUARRIED_STONE =
        BIOME_MODIFIERS.register(
            "add_quarried_stone",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    forestTag,
                    getPlacedFeature(RailcraftOrePlacements.QUARRIED_STONE),
                    Decoration.UNDERGROUND_ORES));
    SALTPETER =
        BIOME_MODIFIERS.register(
            "add_saltpeter",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    overworldTag,
                    getPlacedFeature(RailcraftOrePlacements.SALTPETER),
                    Decoration.UNDERGROUND_ORES));
    FIRESTONE =
        BIOME_MODIFIERS.register(
            "add_firestone",
            () ->
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    netherTag,
                    getPlacedFeature(RailcraftOrePlacements.FIRESTONE),
                    Decoration.UNDERGROUND_ORES));
  }

  private static HolderSet<PlacedFeature> getPlacedFeature(
      RegistryObject<PlacedFeature> registryObject) {
    return HolderSet.direct(registryObject.getHolder().get());
  }
}
