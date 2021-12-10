package mods.railcraft.world.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.cart.TankMinecartEntity;
import mods.railcraft.world.entity.cart.TunnelBoreEntity;
import mods.railcraft.world.entity.cart.locomotive.CreativeLocomotiveEntity;
import mods.railcraft.world.entity.cart.locomotive.ElectricLocomotiveEntity;
import mods.railcraft.world.entity.cart.locomotive.SteamLocomotiveEntity;
import mods.railcraft.world.item.crafting.CokeOvenMenu;
import mods.railcraft.world.item.crafting.ManualRollingMachineMenu;
import mods.railcraft.world.level.block.entity.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.ItemManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftMenuTypes {

  public static final DeferredRegister<MenuType<?>> MENU_TYPES =
      DeferredRegister.create(ForgeRegistries.CONTAINERS, Railcraft.ID);

  public static final RegistryObject<MenuType<LocomotiveMenu<CreativeLocomotiveEntity>>> CREATIVE_LOCOMOTIVE =
      MENU_TYPES.register("creative_locomotive",
          () -> new MenuType<>(
              entityMenu(CreativeLocomotiveEntity.class, LocomotiveMenu::creative)));

  public static final RegistryObject<MenuType<ElectricLocomotiveMenu>> ELECTRIC_LOCOMOTIVE =
      MENU_TYPES.register("electric_locomotive",
          () -> new MenuType<>(
              entityMenu(ElectricLocomotiveEntity.class, ElectricLocomotiveMenu::new)));

  public static final RegistryObject<MenuType<SteamLocomotiveMenu>> STEAM_LOCOMOTIVE =
      MENU_TYPES.register("steam_locomotive",
          () -> new MenuType<>(
              entityMenu(SteamLocomotiveEntity.class, SteamLocomotiveMenu::new)));

  public static final RegistryObject<MenuType<ManualRollingMachineMenu>> MANUAL_ROLLING_MACHINE =
      MENU_TYPES.register("manual_rolling_machine",
          () -> new MenuType<>(ManualRollingMachineMenu::new));

  public static final RegistryObject<MenuType<CokeOvenMenu>> COKE_OVEN =
      MENU_TYPES.register("coke_oven",
          () -> new MenuType<>(CokeOvenMenu::new));

  public static final RegistryObject<MenuType<ItemManipulatorMenu>> ITEM_MANIPULATOR =
      MENU_TYPES.register("item_manipulator",
          () -> new MenuType<>(
              blockEntityMenu(ItemManipulatorBlockEntity.class, ItemManipulatorMenu::new)));

  public static final RegistryObject<MenuType<FluidManipulatorMenu>> FLUID_MANIPULATOR =
      MENU_TYPES.register("fluid_manipulator",
          () -> new MenuType<>(
              blockEntityMenu(FluidManipulatorBlockEntity.class, FluidManipulatorMenu::new)));

  public static final RegistryObject<MenuType<TankMinecartMenu>> TANK_MINECART =
      MENU_TYPES.register("tank_minecart",
          () -> new MenuType<>(entityMenu(TankMinecartEntity.class, TankMinecartMenu::new)));

  public static final RegistryObject<MenuType<TunnelBoreMenu>> TUNNEL_BORE =
      MENU_TYPES.register("tunnel_bore",
          () -> new MenuType<>(entityMenu(TunnelBoreEntity.class, TunnelBoreMenu::new)));

  private static <T extends AbstractContainerMenu, E extends Entity> IContainerFactory<T> entityMenu(
      Class<E> entityType, CustomMenuFactory<T, E> factory) {
    return (id, inventory, packetBuffer) -> {
      int entityId = packetBuffer.readVarInt();
      Entity entity = inventory.player.level.getEntity(entityId);
      if (!entityType.isInstance(entity)) {
        throw new IllegalStateException(
            "Cannot find entity of type " + entityType.getName() + " with ID " + entityId);
      }
      return factory.create(id, inventory, entityType.cast(entity));
    };
  }

  private static <T extends AbstractContainerMenu, E extends BlockEntity> IContainerFactory<T> blockEntityMenu(
      Class<E> entityType, CustomMenuFactory<T, E> factory) {
    return (id, inventory, packetBuffer) -> {
      BlockPos blockPos = packetBuffer.readBlockPos();
      BlockEntity entity = inventory.player.level.getBlockEntity(blockPos);
      if (!entityType.isInstance(entity)) {
        throw new IllegalStateException(
            "Cannot find block entity of type " + entityType.getName() + " at ["
                + blockPos.toString() + "]");
      }
      return factory.create(id, inventory, entityType.cast(entity));
    };
  }

  private interface CustomMenuFactory<C extends AbstractContainerMenu, T> {

    C create(int id, Inventory inventory, T data);
  }
}
