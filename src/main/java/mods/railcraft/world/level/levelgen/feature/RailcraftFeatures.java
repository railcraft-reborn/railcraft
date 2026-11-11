package mods.railcraft.world.level.levelgen.feature;

import com.mojang.serialization.MapCodec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftFeatures {

  private static final DeferredRegister<MapCodec<? extends Feature>> deferredRegister =
      DeferredRegister.create(Registries.FEATURE_TYPE, RailcraftConstants.ID);

  public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<QuarriedFeature>> QUARRIED_STONE =
      deferredRegister.register("quarried", () -> QuarriedFeature.CODEC);

  public static void register(IEventBus eventBus) {
    deferredRegister.register(eventBus);
  }
}
