package mods.railcraft.world.level.block.entity;

import java.util.Collection;
import java.util.stream.Stream;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.charge.BatteryBlockEntity;
import mods.railcraft.world.level.block.entity.detector.AgeDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.AnyDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.EmptyDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.LocomotiveDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.MobDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.PlayerDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.SheepDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.TankDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.CartDispenserBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.FluidLoaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.FluidUnloaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ItemLoaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ItemUnloaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.TrainDispenserBlockEntity;
import mods.railcraft.world.level.block.entity.signal.AnalogSignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.BlockSignalRelayBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DistantSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualBlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualDistantSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualTokenSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalInterlockBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalReceiverBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalSequencerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.TokenSignalBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.FluidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SolidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.tank.IronTankBlockEntity;
import mods.railcraft.world.level.block.entity.tank.SteelTankBlockEntity;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.DumpingTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.LauncherTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.TurnoutTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.WyeTrackBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftBlockEntityTypes {

  private static final DeferredRegister<BlockEntityType<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final RegistryObject<BlockEntityType<IronTankBlockEntity>> IRON_TANK =
      deferredRegister.register("iron_tank",
          () -> BlockEntityType.Builder
              .of(IronTankBlockEntity::new,
                  toArray(
                      RailcraftBlocks.IRON_TANK_GAUGE.variants(),
                      RailcraftBlocks.IRON_TANK_VALVE.variants(),
                      RailcraftBlocks.IRON_TANK_WALL.variants()))
              .build(null));

  public static final RegistryObject<BlockEntityType<SteelTankBlockEntity>> STEEL_TANK =
      deferredRegister.register("steel_tank",
          () -> BlockEntityType.Builder
              .of(SteelTankBlockEntity::new,
                  toArray(
                      RailcraftBlocks.STEEL_TANK_GAUGE.variants(),
                      RailcraftBlocks.STEEL_TANK_VALVE.variants(),
                      RailcraftBlocks.STEEL_TANK_WALL.variants()))
              .build(null));

  public static final RegistryObject<BlockEntityType<SteamBoilerBlockEntity>> STEAM_BOILER =
      deferredRegister.register("steam_boiler",
          () -> BlockEntityType.Builder
              .of(SteamBoilerBlockEntity::new,
                  RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get(),
                  RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SolidFueledSteamBoilerBlockEntity>> SOLID_FUELED_STEAM_BOILER =
      deferredRegister.register("solid_fueled_steam_boiler",
          () -> BlockEntityType.Builder
              .of(SolidFueledSteamBoilerBlockEntity::new,
                  RailcraftBlocks.SOLID_FUELED_FIREBOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<FluidFueledSteamBoilerBlockEntity>> FLUID_FUELED_STEAM_BOILER =
      deferredRegister.register("fluid_fueled_steam_boiler",
          () -> BlockEntityType.Builder
              .of(FluidFueledSteamBoilerBlockEntity::new,
                  RailcraftBlocks.FLUID_FUELED_FIREBOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SteamTurbineBlockEntity>> STEAM_TURBINE =
      deferredRegister.register("steam_turbine",
          () -> BlockEntityType.Builder
              .of(SteamTurbineBlockEntity::new, RailcraftBlocks.STEAM_TURBINE.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<BlastFurnaceBlockEntity>> BLAST_FURNACE =
      deferredRegister.register("blast_furnace",
          () -> BlockEntityType.Builder
              .of(BlastFurnaceBlockEntity::new, RailcraftBlocks.BLAST_FURNACE_BRICKS.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<FeedStationBlockEntity>> FEED_STATION =
      deferredRegister.register("feed_station",
          () -> BlockEntityType.Builder
              .of(FeedStationBlockEntity::new, RailcraftBlocks.FEED_STATION.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<LogBookBlockEntity>> LOGBOOK =
      deferredRegister.register("logbook",
          () -> BlockEntityType.Builder
              .of(LogBookBlockEntity::new, RailcraftBlocks.LOGBOOK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<FluidLoaderBlockEntity>> FLUID_LOADER =
      deferredRegister.register("fluid_loader",
          () -> BlockEntityType.Builder
              .of(FluidLoaderBlockEntity::new, RailcraftBlocks.FLUID_LOADER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<FluidUnloaderBlockEntity>> FLUID_UNLOADER =
      deferredRegister.register("fluid_unloader",
          () -> BlockEntityType.Builder
              .of(FluidUnloaderBlockEntity::new, RailcraftBlocks.FLUID_UNLOADER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ItemLoaderBlockEntity>> ITEM_LOADER =
      deferredRegister.register("item_loader",
          () -> BlockEntityType.Builder
              .of(ItemLoaderBlockEntity::new, RailcraftBlocks.ITEM_LOADER.get(),
                  RailcraftBlocks.ADVANCED_ITEM_LOADER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ItemUnloaderBlockEntity>> ITEM_UNLOADER =
      deferredRegister.register("item_unloader",
          () -> BlockEntityType.Builder
              .of(ItemUnloaderBlockEntity::new, RailcraftBlocks.ITEM_UNLOADER.get(),
                  RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<CartDispenserBlockEntity>> CART_DISPENSER =
      deferredRegister.register("cart_dispenser",
          () -> BlockEntityType.Builder
              .of(CartDispenserBlockEntity::new, RailcraftBlocks.CART_DISPENSER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<TrainDispenserBlockEntity>> TRAIN_DISPENSER =
      deferredRegister.register("train_dispenser",
          () -> BlockEntityType.Builder
              .of(TrainDispenserBlockEntity::new, RailcraftBlocks.TRAIN_DISPENSER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<AgeDetectorBlockEntity>> AGE_DETECTOR =
      deferredRegister.register("age_detector",
          () -> BlockEntityType.Builder
              .of(AgeDetectorBlockEntity::new, RailcraftBlocks.AGE_DETECTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<AnyDetectorBlockEntity>> ANY_DETECTOR =
      deferredRegister.register("any_detector",
          () -> BlockEntityType.Builder
              .of(AnyDetectorBlockEntity::new, RailcraftBlocks.ANY_DETECTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<EmptyDetectorBlockEntity>> EMPTY_DETECTOR =
      deferredRegister.register("empty_detector",
          () -> BlockEntityType.Builder
              .of(EmptyDetectorBlockEntity::new, RailcraftBlocks.EMPTY_DETECTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<LocomotiveDetectorBlockEntity>> LOCOMOTIVE_DETECTOR =
      deferredRegister.register("locomotive_detector",
          () -> BlockEntityType.Builder
              .of(LocomotiveDetectorBlockEntity::new, RailcraftBlocks.LOCOMOTIVE_DETECTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<MobDetectorBlockEntity>> MOB_DETECTOR =
      deferredRegister.register("mob_detector",
          () -> BlockEntityType.Builder
              .of(MobDetectorBlockEntity::new, RailcraftBlocks.MOB_DETECTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<PlayerDetectorBlockEntity>> PLAYER_DETECTOR =
      deferredRegister.register("player_detector",
          () -> BlockEntityType.Builder
              .of(PlayerDetectorBlockEntity::new, RailcraftBlocks.PLAYER_DETECTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SheepDetectorBlockEntity>> SHEEP_DETECTOR =
      deferredRegister.register("sheep_detector",
          () -> BlockEntityType.Builder
              .of(SheepDetectorBlockEntity::new, RailcraftBlocks.SHEEP_DETECTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<TankDetectorBlockEntity>> TANK_DETECTOR =
      deferredRegister.register("tank_detector",
          () -> BlockEntityType.Builder
              .of(TankDetectorBlockEntity::new, RailcraftBlocks.TANK_DETECTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<AnalogSignalControllerBoxBlockEntity>> ANALOG_SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("analog_signal_controller_box",
          () -> BlockEntityType.Builder
              .of(AnalogSignalControllerBoxBlockEntity::new,
                  RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalSequencerBoxBlockEntity>> SIGNAL_SEQUENCER_BOX =
      deferredRegister.register("signal_sequencer_box",
          () -> BlockEntityType.Builder
              .of(SignalSequencerBoxBlockEntity::new, RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalCapacitorBoxBlockEntity>> SIGNAL_CAPACITOR_BOX =
      deferredRegister.register("signal_capacitor_box",
          () -> BlockEntityType.Builder
              .of(SignalCapacitorBoxBlockEntity::new, RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalInterlockBoxBlockEntity>> SIGNAL_INTERLOCK_BOX =
      deferredRegister.register("signal_interlock_box",
          () -> BlockEntityType.Builder
              .of(SignalInterlockBoxBlockEntity::new, RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<BlockSignalRelayBoxBlockEntity>> BLOCK_SIGNAL_RELAY_BOX =
      deferredRegister.register("block_signal_relay_box",
          () -> BlockEntityType.Builder
              .of(BlockSignalRelayBoxBlockEntity::new, RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalReceiverBoxBlockEntity>> SIGNAL_RECEIVER_BOX =
      deferredRegister.register("signal_receiver_box",
          () -> BlockEntityType.Builder
              .of(SignalReceiverBoxBlockEntity::new, RailcraftBlocks.SIGNAL_RECEIVER_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalControllerBoxBlockEntity>> SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("signal_controller_box",
          () -> BlockEntityType.Builder
              .of(SignalControllerBoxBlockEntity::new, RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DualBlockSignalBlockEntity>> DUAL_BLOCK_SIGNAL =
      deferredRegister.register("dual_block_signal",
          () -> BlockEntityType.Builder
              .of(DualBlockSignalBlockEntity::new, RailcraftBlocks.DUAL_BLOCK_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DualDistantSignalBlockEntity>> DUAL_DISTANT_SIGNAL =
      deferredRegister.register("dual_distant_signal",
          () -> BlockEntityType.Builder
              .of(DualDistantSignalBlockEntity::new, RailcraftBlocks.DUAL_DISTANT_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DualTokenSignalBlockEntity>> DUAL_TOKEN_SIGNAL =
      deferredRegister.register("dual_token_signal",
          () -> BlockEntityType.Builder
              .of(DualTokenSignalBlockEntity::new, RailcraftBlocks.DUAL_TOKEN_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<BlockSignalBlockEntity>> BLOCK_SIGNAL =
      deferredRegister.register("block_signal",
          () -> BlockEntityType.Builder
              .of(BlockSignalBlockEntity::new, RailcraftBlocks.BLOCK_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DistantSignalBlockEntity>> DISTANT_SIGNAL =
      deferredRegister.register("distant_signal",
          () -> BlockEntityType.Builder
              .of(DistantSignalBlockEntity::new, RailcraftBlocks.DISTANT_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<TokenSignalBlockEntity>> TOKEN_SIGNAL =
      deferredRegister.register("token_signal",
          () -> BlockEntityType.Builder
              .of(TokenSignalBlockEntity::new, RailcraftBlocks.TOKEN_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ForceTrackEmitterBlockEntity>> FORCE_TRACK_EMITTER =
      deferredRegister.register("force_track_emitter",
          () -> BlockEntityType.Builder
              .of(ForceTrackEmitterBlockEntity::new, RailcraftBlocks.FORCE_TRACK_EMITTER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ForceTrackBlockEntity>> FORCE_TRACK =
      deferredRegister.register("force_track",
          () -> BlockEntityType.Builder
              .of(ForceTrackBlockEntity::new, RailcraftBlocks.FORCE_TRACK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<BatteryBlockEntity>> BATTERY =
      deferredRegister.register("battery",
          () -> BlockEntityType.Builder
              .of(BatteryBlockEntity::new,
                  RailcraftBlocks.NICKEL_IRON_BATTERY.get(),
                  RailcraftBlocks.NICKEL_ZINC_BATTERY.get(),
                  RailcraftBlocks.ZINC_SILVER_BATTERY.get(),
                  RailcraftBlocks.ZINC_CARBON_BATTERY.get(),
                  RailcraftBlocks.FRAME.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<TurnoutTrackBlockEntity>> TURNOUT_TRACK =
      deferredRegister.register("turnout_track",
          () -> BlockEntityType.Builder
              .of(TurnoutTrackBlockEntity::new,
                  RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(),
                  RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(),
                  RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(),
                  RailcraftBlocks.IRON_TURNOUT_TRACK.get(),
                  RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(),
                  RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<WyeTrackBlockEntity>> WYE_TRACK =
      deferredRegister.register("wye_track",
          () -> BlockEntityType.Builder
              .of(WyeTrackBlockEntity::new,
                  RailcraftBlocks.ABANDONED_WYE_TRACK.get(),
                  RailcraftBlocks.ELECTRIC_WYE_TRACK.get(),
                  RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(),
                  RailcraftBlocks.IRON_WYE_TRACK.get(),
                  RailcraftBlocks.REINFORCED_WYE_TRACK.get(),
                  RailcraftBlocks.STRAP_IRON_WYE_TRACK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<RitualBlockEntity>> RITUAL =
      deferredRegister.register("ritual",
          () -> BlockEntityType.Builder
              .of(RitualBlockEntity::new, RailcraftBlocks.RITUAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ManualRollingMachineBlockEntity>> MANUAL_ROLLING_MACHINE =
      deferredRegister.register("manual_rolling_machine",
          () -> BlockEntityType.Builder
              .of(ManualRollingMachineBlockEntity::new,
                  RailcraftBlocks.MANUAL_ROLLING_MACHINE.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<PoweredRollingMachineBlockEntity>> POWERED_ROLLING_MACHINE =
      deferredRegister.register("powered_rolling_machine",
          () -> BlockEntityType.Builder
              .of(PoweredRollingMachineBlockEntity::new,
                  RailcraftBlocks.POWERED_ROLLING_MACHINE.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN =
      deferredRegister.register("coke_oven",
          () -> BlockEntityType.Builder
              .of(CokeOvenBlockEntity::new, RailcraftBlocks.COKE_OVEN_BRICKS.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<CrusherBlockEntity>> CRUSHER =
      deferredRegister.register("crusher",
          () -> BlockEntityType.Builder
              .of(CrusherBlockEntity::new, RailcraftBlocks.CRUSHER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SteamOvenBlockEntity>> STEAM_OVEN =
      deferredRegister.register("steam_oven",
          () -> BlockEntityType.Builder
              .of(SteamOvenBlockEntity::new, RailcraftBlocks.STEAM_OVEN.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<WaterTankSidingBlockEntity>> WATER_TANK_SIDING =
      deferredRegister.register("water_tank_siding",
          () -> BlockEntityType.Builder
              .of(WaterTankSidingBlockEntity::new, RailcraftBlocks.WATER_TANK_SIDING.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SwitchTrackLeverBlockEntity>> SWITCH_TRACK_LEVER =
      deferredRegister.register("switch_track_lever",
          () -> BlockEntityType.Builder
              .of(SwitchTrackLeverBlockEntity::new, RailcraftBlocks.SWITCH_TRACK_LEVER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SwitchTrackMotorBlockEntity>> SWITCH_TRACK_MOTOR =
      deferredRegister.register("switch_track_motor",
          () -> BlockEntityType.Builder
              .of(SwitchTrackMotorBlockEntity::new, RailcraftBlocks.SWITCH_TRACK_MOTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SwitchTrackRouterBlockEntity>> SWITCH_TRACK_ROUTER =
      deferredRegister.register("switch_track_router",
          () -> BlockEntityType.Builder
              .of(SwitchTrackRouterBlockEntity::new, RailcraftBlocks.SWITCH_TRACK_ROUTER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<LockingTrackBlockEntity>> LOCKING_TRACK =
      deferredRegister.register("locking_track",
          () -> BlockEntityType.Builder
              .of(LockingTrackBlockEntity::new,
                  RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
                  RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
                  RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
                  RailcraftBlocks.IRON_LOCKING_TRACK.get(),
                  RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
                  RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<CouplerTrackBlockEntity>> COUPLER_TRACK =
      deferredRegister.register("coupler_track",
          () -> BlockEntityType.Builder
              .of(CouplerTrackBlockEntity::new,
                  RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(),
                  RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(),
                  RailcraftBlocks.IRON_COUPLER_TRACK.get(),
                  RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(),
                  RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<LauncherTrackBlockEntity>> LAUNCHER_TRACK =
      deferredRegister.register("launcher_track",
          () -> BlockEntityType.Builder
              .of(LauncherTrackBlockEntity::new,
                  RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.get(),
                  RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.get(),
                  RailcraftBlocks.IRON_LAUNCHER_TRACK.get(),
                  RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.get(),
                  RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<RoutingTrackBlockEntity>> ROUTING_TRACK =
      deferredRegister.register("routing_track",
          () -> BlockEntityType.Builder
              .of(RoutingTrackBlockEntity::new,
                  RailcraftBlocks.ABANDONED_ROUTING_TRACK.get(),
                  RailcraftBlocks.ELECTRIC_ROUTING_TRACK.get(),
                  RailcraftBlocks.IRON_ROUTING_TRACK.get(),
                  RailcraftBlocks.REINFORCED_ROUTING_TRACK.get(),
                  RailcraftBlocks.STRAP_IRON_ROUTING_TRACK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DumpingTrackBlockEntity>> DUMPING_TRACK =
      deferredRegister.register("dumping_track",
          () -> BlockEntityType.Builder
              .of(DumpingTrackBlockEntity::new,
                  RailcraftBlocks.ABANDONED_DUMPING_TRACK.get(),
                  RailcraftBlocks.ELECTRIC_DUMPING_TRACK.get(),
                  RailcraftBlocks.IRON_DUMPING_TRACK.get(),
                  RailcraftBlocks.REINFORCED_DUMPING_TRACK.get(),
                  RailcraftBlocks.STRAP_IRON_DUMPING_TRACK.get())
              .build(null));

  @SafeVarargs
  private static Block[] toArray(
      Collection<? extends RegistryObject<? extends Block>>... collections) {
    return Stream.of(collections)
        .flatMap(Collection::stream)
        .map(RegistryObject::get)
        .toArray(Block[]::new);
  }
}
