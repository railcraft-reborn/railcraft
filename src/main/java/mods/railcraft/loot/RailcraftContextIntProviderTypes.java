package mods.railcraft.loot;

import java.util.function.Supplier;
import com.mojang.serialization.MapCodec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftContextIntProviderTypes {

  private static final DeferredRegister<MapCodec<? extends ContextIntProvider>> deferredRegister =
      DeferredRegister.create(Registries.CONTEXT_INT_PROVIDER_TYPE, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final Supplier<MapCodec<ChargedValue>> CHARGED =
      deferredRegister.register("charged", () -> ChargedValue.MAP_CODEC);
}
