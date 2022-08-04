package mods.railcraft.data.worldgen.placements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.data.worldgen.features.RailcraftMiscOverworldFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftMiscOverworldPlacements {

    private static final int SALTPETER_VEIN_PER_CHUNK = 5;


    private static final DeferredRegister<PlacedFeature> deferredRegister =
        DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Railcraft.ID);

    private static final Map<ResourceLocation, RegistryObject<PlacedFeature>>
        PLACED_FEATURE_MAP = new HashMap<>();


    public static final RegistryObject<PlacedFeature> SALTPETER_ORE = register("saltpeter_disk",
        RailcraftMiscOverworldFeatures.SALTPETER_ORE,
        () -> List.of(
            //CountPlacement.of(SALTPETER_VEIN_PER_CHUNK),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
            BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
            BiomeFilter.biome()
        ));

    private static RegistryObject<PlacedFeature> register(String name,
        RegistryObject<ConfiguredFeature<?, ?>> configuredFeature,
        Supplier<List<PlacementModifier>> placementModifier) {

        RegistryObject<PlacedFeature> result = deferredRegister.register(name, () ->
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
