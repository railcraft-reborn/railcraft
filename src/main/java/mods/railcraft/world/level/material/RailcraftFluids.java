package mods.railcraft.world.level.material;

import java.util.function.Supplier;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftFluids {

  private static final DeferredRegister<Fluid> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.FLUID, RailcraftConstants.ID);

  public static final Supplier<SteamFluid> STEAM =
      deferredRegister.register("steam", SteamFluid::new);

  public static final Supplier<CreosoteFluid.Source> CREOSOTE =
      deferredRegister.register("creosote", CreosoteFluid.Source::new);

  public static final Supplier<CreosoteFluid.Flowing> FLOWING_CREOSOTE =
      deferredRegister.register("flowing_creosote", CreosoteFluid.Flowing::new);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
