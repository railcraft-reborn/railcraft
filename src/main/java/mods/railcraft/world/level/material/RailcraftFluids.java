package mods.railcraft.world.level.material;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class RailcraftFluids {

  private static final DeferredRegister<Fluid> deferredRegister =
      DeferredRegister.create(ForgeRegistries.FLUIDS, RailcraftConstants.ID);

  public static final RegistryObject<SteamFluid> STEAM =
      deferredRegister.register("steam", SteamFluid::new);

  public static final RegistryObject<CreosoteFluid.Source> CREOSOTE =
      deferredRegister.register("creosote", CreosoteFluid.Source::new);

  public static final RegistryObject<CreosoteFluid.Flowing> FLOWING_CREOSOTE =
      deferredRegister.register("flowing_creosote", CreosoteFluid.Flowing::new);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
