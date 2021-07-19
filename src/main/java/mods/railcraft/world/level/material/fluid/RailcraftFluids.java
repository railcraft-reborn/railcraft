package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftFluids {

  public static final DeferredRegister<Fluid> FLUIDS =
      DeferredRegister.create(ForgeRegistries.FLUIDS, Railcraft.ID);

  public static final RegistryObject<Fluid> STEAM = FLUIDS.register("steam", GasFluid::new);
}
