package mods.railcraft.world.level.levelgen.placement;

import com.mojang.serialization.MapCodec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftPlacementModifierTypes {

  private static final DeferredRegister<MapCodec<? extends PlacementModifier>> deferredRegister =
      DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, RailcraftConstants.ID);

  public static final DeferredHolder<MapCodec<? extends PlacementModifier>, MapCodec<OreConfigFilter>> ORE_CONFIG =
      deferredRegister.register("ore_config", () -> OreConfigFilter.CODEC);

  public static void register(IEventBus eventBus) {
    deferredRegister.register(eventBus);
  }
}
