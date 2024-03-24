package mods.railcraft.world.level.material;

import java.util.function.Supplier;
import mods.railcraft.api.core.RailcraftConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RailcraftFluidTypes {

  private static final DeferredRegister<FluidType> deferredRegister =
      DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, RailcraftConstants.ID);

  public static final Supplier<FluidType> STEAM =
      deferredRegister.register("steam", SteamFluid::createFluidType);

  public static final Supplier<FluidType> CREOSOTE =
      deferredRegister.register("creosote", CreosoteFluid::createFluidType);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
