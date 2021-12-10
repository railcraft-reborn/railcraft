package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftFluids {

  public static final DeferredRegister<Fluid> FLUIDS =
      DeferredRegister.create(ForgeRegistries.FLUIDS, Railcraft.ID);

  public static final RegistryObject<Fluid> STEAM = FLUIDS.register("steam", SteamFluid::new);
  public static final RegistryObject<Fluid> CREOSOTE = FLUIDS.register("creosote", CreosoteFluid::new);
}
