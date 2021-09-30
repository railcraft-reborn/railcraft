package mods.railcraft.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftContainers {
  public static final DeferredRegister<ContainerType<?>> CONTAINERS =
    DeferredRegister.create(ForgeRegistries.CONTAINERS, Railcraft.ID);

  public static final RegistryObject<ContainerType<RollingTableContainer>> ROLLING =
    CONTAINERS.register("rolling", () -> new ContainerType<RollingTableContainer>(RollingTableContainer::new));
}
