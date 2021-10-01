package mods.railcraft.world.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.crafting.RollingTableContainer;
import mods.railcraft.world.entity.CreativeLocomotiveEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftMenuTypes {

  public static final DeferredRegister<ContainerType<?>> MENU_TYPES =
      DeferredRegister.create(ForgeRegistries.CONTAINERS, Railcraft.ID);

  public static final RegistryObject<ContainerType<LocomotiveMenu<CreativeLocomotiveEntity>>> CREATIVE_LOCOMOTIVE =
      MENU_TYPES.register("creative_locomotive",
          () -> new ContainerType<>(
              (IContainerFactory<LocomotiveMenu<CreativeLocomotiveEntity>>) LocomotiveMenu::creative));

  public static final RegistryObject<ContainerType<ElectricLocomotiveMenu>> ELECTRIC_LOCOMOTIVE =
      MENU_TYPES.register("electric_locomotive",
          () -> new ContainerType<>(
              (IContainerFactory<ElectricLocomotiveMenu>) ElectricLocomotiveMenu::create));

  public static final RegistryObject<ContainerType<SteamLocomotiveMenu>> STEAM_LOCOMOTIVE =
      MENU_TYPES.register("steam_locomotive",
          () -> new ContainerType<>(
              (IContainerFactory<SteamLocomotiveMenu>) SteamLocomotiveMenu::create));

  public static final RegistryObject<ContainerType<RollingTableContainer>> ROLLING_TABLE =
    MENU_TYPES.register("rolling",
      () -> new ContainerType<RollingTableContainer>(RollingTableContainer::new));

}
