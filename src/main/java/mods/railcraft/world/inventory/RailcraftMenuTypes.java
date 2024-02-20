package mods.railcraft.world.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.TrackLayer;
import mods.railcraft.world.entity.vehicle.TrackRelayer;
import mods.railcraft.world.entity.vehicle.TrackUndercutter;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.inventory.detector.AdvancedDetectorMenu;
import mods.railcraft.world.inventory.detector.ItemDetectorMenu;
import mods.railcraft.world.inventory.detector.LocomotiveDetectorMenu;
import mods.railcraft.world.inventory.detector.RoutingDetectorMenu;
import mods.railcraft.world.inventory.detector.SheepDetectorMenu;
import mods.railcraft.world.inventory.detector.TankDetectorMenu;
import mods.railcraft.world.level.block.entity.BlastFurnaceBlockEntity;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import mods.railcraft.world.level.block.entity.FeedStationBlockEntity;
import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.PoweredRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.SteamOvenBlockEntity;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
import mods.railcraft.world.level.block.entity.detector.AdvancedDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.ItemDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.LocomotiveDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.RoutingDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.SheepDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.TankDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.CartDispenserBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ItemManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.TrainDispenserBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import mods.railcraft.world.level.block.entity.track.DumpingTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
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
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final RegistryObject<MenuType<SolidFueledSteamBoilerMenu>> SOLID_FUELED_STEAM_BOILER =
      deferredRegister.register("solid_fueled_steam_boiler",
          () -> blockEntityMenu(SteamBoilerBlockEntity.class, SolidFueledSteamBoilerMenu::new));

  public static final RegistryObject<MenuType<FluidFueledSteamBoilerMenu>> FLUID_FUELED_STEAM_BOILER =
      deferredRegister.register("fluid_fueled_steam_boiler",
          () -> blockEntityMenu(SteamBoilerBlockEntity.class, FluidFueledSteamBoilerMenu::new));

  public static final RegistryObject<MenuType<SteamTurbineMenu>> STEAM_TURBINE =
      deferredRegister.register("steam_turbine",
          () -> blockEntityMenu(SteamTurbineBlockEntity.class, SteamTurbineMenu::new));

  public static final RegistryObject<MenuType<TankMenu>> TANK =
      deferredRegister.register("tank",
          () -> blockEntityMenu(TankBlockEntity.class, TankMenu::new));

  public static final RegistryObject<MenuType<WaterTankSidingMenu>> WATER_TANK_SIDING =
      deferredRegister.register("water_tank_siding",
          () -> blockEntityMenu(WaterTankSidingBlockEntity.class, WaterTankSidingMenu::new));

  public static final RegistryObject<MenuType<BlastFurnaceMenu>> BLAST_FURNACE =
      deferredRegister.register("blast_furnace",
          () -> blockEntityMenu(BlastFurnaceBlockEntity.class, BlastFurnaceMenu::new));

  public static final RegistryObject<MenuType<FeedStationMenu>> FEED_STATION =
      deferredRegister.register("feed_station",
          () -> blockEntityMenu(FeedStationBlockEntity.class, FeedStationMenu::new));

  public static final RegistryObject<MenuType<SwitchTrackRouterMenu>> SWITCH_TRACK_ROUTER =
      deferredRegister.register("switch_track_routing",
          () -> blockEntityMenu(SwitchTrackRouterBlockEntity.class, SwitchTrackRouterMenu::new));

  public static final RegistryObject<MenuType<LocomotiveMenu<CreativeLocomotive>>> CREATIVE_LOCOMOTIVE =
      deferredRegister.register("creative_locomotive",
          () -> entityMenu(CreativeLocomotive.class, CreativeLocomotiveMenu::new));

  public static final RegistryObject<MenuType<ElectricLocomotiveMenu>> ELECTRIC_LOCOMOTIVE =
      deferredRegister.register("electric_locomotive",
          () -> entityMenu(ElectricLocomotive.class, ElectricLocomotiveMenu::new));

  public static final RegistryObject<MenuType<SteamLocomotiveMenu>> STEAM_LOCOMOTIVE =
      deferredRegister.register("steam_locomotive",
          () -> entityMenu(SteamLocomotive.class, SteamLocomotiveMenu::new));

  public static final RegistryObject<MenuType<ManualRollingMachineMenu>> MANUAL_ROLLING_MACHINE =
      deferredRegister.register("manual_rolling_machine",
          () -> blockEntityMenu(
              ManualRollingMachineBlockEntity.class, ManualRollingMachineMenu::new));

  public static final RegistryObject<MenuType<PoweredRollingMachineMenu>> POWERED_ROLLING_MACHINE =
      deferredRegister.register("powered_rolling_machine",
          () -> blockEntityMenu(
              PoweredRollingMachineBlockEntity.class, PoweredRollingMachineMenu::new));

  public static final RegistryObject<MenuType<CokeOvenMenu>> COKE_OVEN =
      deferredRegister.register("coke_oven",
          () -> blockEntityMenu(CokeOvenBlockEntity.class, CokeOvenMenu::new));

  public static final RegistryObject<MenuType<CrusherMenu>> CRUSHER =
      deferredRegister.register("crusher",
          () -> blockEntityMenu(CrusherBlockEntity.class, CrusherMenu::new));

  public static final RegistryObject<MenuType<SteamOvenMenu>> STEAM_OVEN =
      deferredRegister.register("steam_oven",
          () -> blockEntityMenu(SteamOvenBlockEntity.class, SteamOvenMenu::new));

  public static final RegistryObject<MenuType<ItemManipulatorMenu>> ITEM_MANIPULATOR =
      deferredRegister.register("item_manipulator",
          () -> blockEntityMenu(ItemManipulatorBlockEntity.class, ItemManipulatorMenu::new));

  public static final RegistryObject<MenuType<FluidManipulatorMenu>> FLUID_MANIPULATOR =
      deferredRegister.register("fluid_manipulator",
          () -> blockEntityMenu(FluidManipulatorBlockEntity.class, FluidManipulatorMenu::new));

  public static final RegistryObject<MenuType<CartDispenserMenu>> CART_DISPENSER =
      deferredRegister.register("cart_dispenser",
          () -> blockEntityMenu(CartDispenserBlockEntity.class, CartDispenserMenu::new));

  public static final RegistryObject<MenuType<TrainDispenserMenu>> TRAIN_DISPENSER =
      deferredRegister.register("train_dispenser",
          () -> blockEntityMenu(TrainDispenserBlockEntity.class, TrainDispenserMenu::new));

  public static final RegistryObject<MenuType<TankMinecartMenu>> TANK_MINECART =
      deferredRegister.register("tank_minecart",
          () -> entityMenu(TankMinecart.class, TankMinecartMenu::new));

  public static final RegistryObject<MenuType<TunnelBoreMenu>> TUNNEL_BORE =
      deferredRegister.register("tunnel_bore",
          () -> entityMenu(TunnelBore.class, TunnelBoreMenu::new));

  public static final RegistryObject<MenuType<TrackLayerMenu>> TRACK_LAYER =
      deferredRegister.register("track_layer",
          () -> entityMenu(TrackLayer.class, TrackLayerMenu::new));

  public static final RegistryObject<MenuType<TrackRelayerMenu>> TRACK_RELAYER =
      deferredRegister.register("track_relayer",
          () -> entityMenu(TrackRelayer.class, TrackRelayerMenu::new));

  public static final RegistryObject<MenuType<TrackUndercutterMenu>> TRACK_UNDERCUTTER =
      deferredRegister.register("track_undercutter",
          () -> entityMenu(TrackUndercutter.class, TrackUndercutterMenu::new));

  public static final RegistryObject<MenuType<RoutingTrackMenu>> ROUTING_TRACK =
      deferredRegister.register("routing_track",
          () -> blockEntityMenu(RoutingTrackBlockEntity.class, RoutingTrackMenu::new));

  public static final RegistryObject<MenuType<DumpingTrackMenu>> DUMPING_TRACK =
      deferredRegister.register("dumping_track",
          () -> blockEntityMenu(DumpingTrackBlockEntity.class, DumpingTrackMenu::new));

  public static final RegistryObject<MenuType<SheepDetectorMenu>> SHEEP_DETECTOR =
      deferredRegister.register("sheep_detector",
          () -> blockEntityMenu(SheepDetectorBlockEntity.class, SheepDetectorMenu::new));

  public static final RegistryObject<MenuType<LocomotiveDetectorMenu>> LOCOMOTIVE_DETECTOR =
      deferredRegister.register("locomotive_detector",
          () -> blockEntityMenu(LocomotiveDetectorBlockEntity.class, LocomotiveDetectorMenu::new));

  public static final RegistryObject<MenuType<TankDetectorMenu>> TANK_DETECTOR =
      deferredRegister.register("tank_detector",
          () -> blockEntityMenu(TankDetectorBlockEntity.class, TankDetectorMenu::new));

  public static final RegistryObject<MenuType<AdvancedDetectorMenu>> ADVANCED_DETECTOR =
      deferredRegister.register("advanced_detector",
          () -> blockEntityMenu(AdvancedDetectorBlockEntity.class, AdvancedDetectorMenu::new));

  public static final RegistryObject<MenuType<ItemDetectorMenu>> ITEM_DETECTOR =
      deferredRegister.register("item_detector",
          () -> blockEntityMenu(ItemDetectorBlockEntity.class, ItemDetectorMenu::new));

  public static final RegistryObject<MenuType<RoutingDetectorMenu>> ROUTING_DETECTOR =
      deferredRegister.register("routing_detector",
          () -> blockEntityMenu(RoutingDetectorBlockEntity.class, RoutingDetectorMenu::new));

  private static <T extends AbstractContainerMenu, E extends Entity> MenuType<T> entityMenu(
      Class<E> entityType, CustomMenuFactory<T, E> factory) {
    IContainerFactory<T> containerFactory = (id, inventory, packetBuffer) -> {
      int entityId = packetBuffer.readVarInt();
      Entity entity = inventory.player.level().getEntity(entityId);
      if (entityType.isInstance(entity)) {
        return factory.create(id, inventory, entityType.cast(entity));
      }
      throw new IllegalStateException(
          "Cannot find entity of type %s with ID %s".formatted(entityType.getName(), entityId));
    };
    return new MenuType<>(containerFactory, FeatureFlags.DEFAULT_FLAGS);
  }

  private static <T extends AbstractContainerMenu, E extends BlockEntity> MenuType<T>
  blockEntityMenu(Class<E> entityType, CustomMenuFactory<T, E> factory) {
    IContainerFactory<T> containerFactory =  (id, inventory, packetBuffer) -> {
      BlockPos blockPos = packetBuffer.readBlockPos();
      BlockEntity entity = inventory.player.level().getBlockEntity(blockPos);
      if (entityType.isInstance(entity)) {
        return factory.create(id, inventory, entityType.cast(entity));
      }
      throw new IllegalStateException(
          "Cannot find block entity of type %s at [%s]".formatted(entityType.getName(), blockPos));
    };
    return new MenuType<>(containerFactory, FeatureFlags.DEFAULT_FLAGS);
  }

  private interface CustomMenuFactory<C extends AbstractContainerMenu, T> {

    C create(int id, Inventory inventory, T data);
  }
}
