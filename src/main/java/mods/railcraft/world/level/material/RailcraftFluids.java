package mods.railcraft.world.level.material;

import mods.railcraft.Railcraft;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftFluids {

  private static final DeferredRegister<Fluid> deferredRegister =
      DeferredRegister.create(ForgeRegistries.FLUIDS, Railcraft.ID);

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
