package mods.railcraft.world.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.level.block.entity.BlastFurnaceBlockEntity;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import mods.railcraft.world.level.block.entity.FeedStationBlockEntity;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ItemManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftMenuTypes {

  private static final DeferredRegister<MenuType<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, Railcraft.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final RegistryObject<MenuType<SolidFueledSteamBoilerMenu>> SOLID_FUELED_STEAM_BOILER =
      deferredRegister.register("solid_fueled_steam_boiler",
          () -> new MenuType<>(
              blockEntityMenu(SteamBoilerBlockEntity.class, SolidFueledSteamBoilerMenu::new)));

  public static final RegistryObject<MenuType<FluidFueledSteamBoilerMenu>> FLUID_FUELED_STEAM_BOILER =
      deferredRegister.register("fluid_fueled_steam_boiler",
          () -> new MenuType<>(
              blockEntityMenu(SteamBoilerBlockEntity.class, FluidFueledSteamBoilerMenu::new)));


  public static final RegistryObject<MenuType<SteamTurbineMenu>> STEAM_TURBINE =
      deferredRegister.register("steam_turbine",
          () -> new MenuType<>(
              blockEntityMenu(SteamTurbineBlockEntity.class, SteamTurbineMenu::new)));

  public static final RegistryObject<MenuType<TankMenu>> TANK =
      deferredRegister.register("tank",
          () -> new MenuType<>(blockEntityMenu(TankBlockEntity.class, TankMenu::new)));

  public static final RegistryObject<MenuType<WaterTankSidingMenu>> WATER_TANK_SIDING =
      deferredRegister.register("water_tank_siding",
          () -> new MenuType<>(
              blockEntityMenu(WaterTankSidingBlockEntity.class, WaterTankSidingMenu::new)));

  public static final RegistryObject<MenuType<BlastFurnaceMenu>> BLAST_FURNACE =
      deferredRegister.register("blast_furnace",
          () -> new MenuType<>(
              blockEntityMenu(BlastFurnaceBlockEntity.class, BlastFurnaceMenu::new)));

  public static final RegistryObject<MenuType<FeedStationMenu>> FEED_STATION =
      deferredRegister.register("feed_station",
          () -> new MenuType<>(
              blockEntityMenu(FeedStationBlockEntity.class, FeedStationMenu::new)));

  public static final RegistryObject<MenuType<LocomotiveMenu<CreativeLocomotive>>> CREATIVE_LOCOMOTIVE =
      deferredRegister.register("creative_locomotive",
          () -> new MenuType<>(
              entityMenu(CreativeLocomotive.class, LocomotiveMenu::creative)));

  public static final RegistryObject<MenuType<ElectricLocomotiveMenu>> ELECTRIC_LOCOMOTIVE =
      deferredRegister.register("electric_locomotive",
          () -> new MenuType<>(
              entityMenu(ElectricLocomotive.class, ElectricLocomotiveMenu::new)));

  public static final RegistryObject<MenuType<SteamLocomotiveMenu>> STEAM_LOCOMOTIVE =
      deferredRegister.register("steam_locomotive",
          () -> new MenuType<>(
              entityMenu(SteamLocomotive.class, SteamLocomotiveMenu::new)));

  public static final RegistryObject<MenuType<ManualRollingMachineMenu>> MANUAL_ROLLING_MACHINE =
      deferredRegister.register("manual_rolling_machine",
          () -> new MenuType<>(ManualRollingMachineMenu::new));

  public static final RegistryObject<MenuType<CokeOvenMenu>> COKE_OVEN =
      deferredRegister.register("coke_oven",
          () -> new MenuType<>(blockEntityMenu(CokeOvenBlockEntity.class, CokeOvenMenu::new)));

  public static final RegistryObject<MenuType<CrusherMenu>> CRUSHER =
      deferredRegister.register("crusher",
          () -> new MenuType<>(blockEntityMenu(CrusherBlockEntity.class, CrusherMenu::new)));

  public static final RegistryObject<MenuType<ItemManipulatorMenu>> ITEM_MANIPULATOR =
      deferredRegister.register("item_manipulator",
          () -> new MenuType<>(
              blockEntityMenu(ItemManipulatorBlockEntity.class, ItemManipulatorMenu::new)));

  public static final RegistryObject<MenuType<FluidManipulatorMenu>> FLUID_MANIPULATOR =
      deferredRegister.register("fluid_manipulator",
          () -> new MenuType<>(
              blockEntityMenu(FluidManipulatorBlockEntity.class, FluidManipulatorMenu::new)));

  public static final RegistryObject<MenuType<TankMinecartMenu>> TANK_MINECART =
      deferredRegister.register("tank_minecart",
          () -> new MenuType<>(entityMenu(TankMinecart.class, TankMinecartMenu::new)));

  public static final RegistryObject<MenuType<TunnelBoreMenu>> TUNNEL_BORE =
      deferredRegister.register("tunnel_bore",
          () -> new MenuType<>(entityMenu(TunnelBore.class, TunnelBoreMenu::new)));

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
