package mods.railcraft.data.worldgen.placements;

import static net.minecraft.data.worldgen.placement.OrePlacements.commonOrePlacement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftOrePlacements {

    private static final int LEAD_VEIN_PER_CHUNCK = 10;

    private static final DeferredRegister<PlacedFeature> deferredRegister =
        DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Railcraft.ID);

    private static final Map<ResourceLocation, RegistryObject<PlacedFeature>>
        PLACED_FEATURE_MAP = new HashMap<>();

    public static final RegistryObject<PlacedFeature> ORE_LEAD_MIDDLE = register("ore_lead_middle",
        RailcraftOreFeatures.ORE_LEAD, () -> commonOrePlacement(LEAD_VEIN_PER_CHUNCK,
            HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))
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
