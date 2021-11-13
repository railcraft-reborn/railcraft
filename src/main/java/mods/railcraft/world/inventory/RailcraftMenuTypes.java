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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
              entityMenu(CreativeLocomotiveEntity.class, LocomotiveMenu::creative)));

  public static final RegistryObject<ContainerType<ElectricLocomotiveMenu>> ELECTRIC_LOCOMOTIVE =
      MENU_TYPES.register("electric_locomotive",
          () -> new ContainerType<>(
              entityMenu(ElectricLocomotiveEntity.class, ElectricLocomotiveMenu::new)));

  public static final RegistryObject<ContainerType<SteamLocomotiveMenu>> STEAM_LOCOMOTIVE =
      MENU_TYPES.register("steam_locomotive",
          () -> new ContainerType<>(
              entityMenu(SteamLocomotiveEntity.class, SteamLocomotiveMenu::new)));

  public static final RegistryObject<ContainerType<ManualRollingMachineMenu>> MANUAL_ROLLING_MACHINE =
      MENU_TYPES.register("manual_rolling_machine",
          () -> new ContainerType<>(ManualRollingMachineMenu::new));

  public static final RegistryObject<ContainerType<CokeOvenMenu>> COKE_OVEN =
      MENU_TYPES.register("coke_oven",
          () -> new ContainerType<>(CokeOvenMenu::new));

  public static final RegistryObject<ContainerType<ItemManipulatorMenu>> ITEM_MANIPULATOR =
      MENU_TYPES.register("item_manipulator",
          () -> new ContainerType<>(
              blockEntityMenu(ItemManipulatorBlockEntity.class, ItemManipulatorMenu::new)));

  public static final RegistryObject<ContainerType<FluidManipulatorMenu>> FLUID_MANIPULATOR =
      MENU_TYPES.register("fluid_manipulator",
          () -> new ContainerType<>(
              blockEntityMenu(FluidManipulatorBlockEntity.class, FluidManipulatorMenu::new)));

  public static final RegistryObject<ContainerType<TankMinecartMenu>> TANK_MINECART =
      MENU_TYPES.register("tank_minecart",
          () -> new ContainerType<>(entityMenu(TankMinecartEntity.class, TankMinecartMenu::new)));

  public static final RegistryObject<ContainerType<TunnelBoreMenu>> TUNNEL_BORE =
      MENU_TYPES.register("tunnel_bore",
          () -> new ContainerType<>(entityMenu(TunnelBoreEntity.class, TunnelBoreMenu::new)));

  private static <T extends Container, E extends Entity> IContainerFactory<T> entityMenu(
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

  private static <T extends Container, E extends TileEntity> IContainerFactory<T> blockEntityMenu(
      Class<E> entityType, CustomMenuFactory<T, E> factory) {
    return (id, inventory, packetBuffer) -> {
      BlockPos blockPos = packetBuffer.readBlockPos();
      TileEntity entity = inventory.player.level.getBlockEntity(blockPos);
      if (!entityType.isInstance(entity)) {
        throw new IllegalStateException(
            "Cannot find block entity of type " + entityType.getName() + " at ["
                + blockPos.toString() + "]");
      }
      return factory.create(id, inventory, entityType.cast(entity));
    };
  }

  private interface CustomMenuFactory<C extends Container, T> {

    C create(int id, PlayerInventory inventory, T data);
  }
}
