package mods.railcraft.world.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.cart.CreativeLocomotiveEntity;
import mods.railcraft.world.item.crafting.CokeOvenMenu;
import mods.railcraft.world.item.crafting.ManualRollingMachineMenu;
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

  public static final RegistryObject<ContainerType<ManualRollingMachineMenu>> MANUAL_ROLLING_MACHINE =
      MENU_TYPES.register("manual_rolling_machine",
          () -> new ContainerType<>(ManualRollingMachineMenu::new));

  public static final RegistryObject<ContainerType<CokeOvenMenu>> COKE_OVEN =
      MENU_TYPES.register("coke_oven",
          () -> new ContainerType<>(CokeOvenMenu::new));

  public static final RegistryObject<ContainerType<ItemManipulatorMenu>> ITEM_MANIPULATOR =
      MENU_TYPES.register("item_manipulator",
          () -> new ContainerType<>(
              (IContainerFactory<ItemManipulatorMenu>) ItemManipulatorMenu::create));
}
