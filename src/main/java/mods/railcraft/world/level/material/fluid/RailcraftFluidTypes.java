package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftFluidTypes {

  private static final DeferredRegister<FluidType> deferredRegister =
      DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Railcraft.ID);

  public static final RegistryObject<FluidType> STEAM =
      deferredRegister.register("steam", SteamFluid::createFluidType);

  public static final RegistryObject<FluidType> CREOSOTE =
      deferredRegister.register("creosote", CreosoteFluid::createFluidType);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
