package mods.railcraft.world.level.levelgen.placement;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftPlacementModifierTypes {

  private static final DeferredRegister<PlacementModifierType<?>> deferredRegister =
      DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, RailcraftConstants.ID);

  public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<OreConfigFilter>>
      ORE_CONFIG = deferredRegister.register("ore_config", () -> () -> OreConfigFilter.CODEC);

  public static void register(IEventBus eventBus) {
    deferredRegister.register(eventBus);
  }
}
