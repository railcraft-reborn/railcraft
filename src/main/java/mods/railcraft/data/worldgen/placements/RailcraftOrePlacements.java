package mods.railcraft.data.worldgen.placements;

import static net.minecraft.data.worldgen.placement.OrePlacements.commonOrePlacement;

import mods.railcraft.Railcraft;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftOrePlacements {

    private static final int LEAD_VEIN_PER_CHUNCK = 10;

    private static final DeferredRegister<PlacedFeature> deferredRegister =
        DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Railcraft.ID);

    public static final RegistryObject<PlacedFeature> ORE_LEAD_MIDDLE =
        deferredRegister.register("ore_lead_middle",
            () -> new PlacedFeature(Holder.direct(RailcraftOreFeatures.ORE_LEAD.get()),
                commonOrePlacement(LEAD_VEIN_PER_CHUNCK,
                    HeightRangePlacement.triangle(
                        VerticalAnchor.absolute(-24),
                        VerticalAnchor.absolute(56)))));


    public static void register(IEventBus modEventBus) {
        deferredRegister.register(modEventBus);
    }
}
