package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftFluids {

  private static final DeferredRegister<Fluid> fluidDeferredRegister =
    DeferredRegister.create(ForgeRegistries.FLUIDS, Railcraft.ID);

  private static final DeferredRegister<FluidType> fluidTypeDeferredRegister =
    DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Railcraft.ID);

  public static final RegistryObject<Fluid> STEAM = fluidDeferredRegister.register("steam", SteamFluid::new);
  public static final RegistryObject<FluidType> STEAM_TYPE =
    fluidTypeDeferredRegister.register("steam", SteamFluid::createFluidType);

  public static final RegistryObject<FlowingFluid> CREOSOTE =
    fluidDeferredRegister.register("creosote", CreosoteFluid.Source::new);
  public static final RegistryObject<FlowingFluid> FLOWING_CREOSOTE =
    fluidDeferredRegister.register("flowing_creosote", CreosoteFluid.Flowing::new);
  public static final RegistryObject<FluidType> CREOSOTE_TYPE =
    fluidTypeDeferredRegister.register("creosote", CreosoteFluid::createFluidType);

  public static void register(IEventBus modEventBus) {
    fluidDeferredRegister.register(modEventBus);
    fluidTypeDeferredRegister.register(modEventBus);
  }
}
