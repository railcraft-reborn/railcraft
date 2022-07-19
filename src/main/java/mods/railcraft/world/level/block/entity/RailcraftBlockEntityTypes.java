package mods.railcraft.world.level.block.entity;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.manipulator.FluidLoaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.FluidUnloaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ItemLoaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ItemUnloaderBlockEntity;
import mods.railcraft.world.level.block.entity.signal.*;
import mods.railcraft.world.level.block.entity.steamboiler.FluidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SolidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.tank.IronTankBlockEntity;
import mods.railcraft.world.level.block.entity.tank.SteelTankBlockEntity;
import mods.railcraft.world.level.block.entity.track.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftBlockEntityTypes {

  private static final DeferredRegister<BlockEntityType<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Railcraft.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final RegistryObject<BlockEntityType<IronTankBlockEntity>> IRON_TANK =
      deferredRegister.register("iron_tank",
          () -> BlockEntityType.Builder
              .of(IronTankBlockEntity::new,
                  RailcraftBlocks.WHITE_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.ORANGE_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.MAGENTA_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.LIGHT_BLUE_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.YELLOW_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.LIME_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.PINK_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.GRAY_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.LIGHT_GRAY_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.CYAN_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.PURPLE_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.BLUE_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.BROWN_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.GREEN_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.RED_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.BLACK_IRON_TANK_GAUGE.get(),
                  RailcraftBlocks.WHITE_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.ORANGE_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.MAGENTA_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.LIGHT_BLUE_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.YELLOW_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.LIME_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.PINK_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.GRAY_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.LIGHT_GRAY_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.CYAN_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.PURPLE_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.BLUE_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.BROWN_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.GREEN_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.RED_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.BLACK_IRON_TANK_VALVE.get(),
                  RailcraftBlocks.WHITE_IRON_TANK_WALL.get(),
                  RailcraftBlocks.ORANGE_IRON_TANK_WALL.get(),
                  RailcraftBlocks.MAGENTA_IRON_TANK_WALL.get(),
                  RailcraftBlocks.LIGHT_BLUE_IRON_TANK_WALL.get(),
                  RailcraftBlocks.YELLOW_IRON_TANK_WALL.get(),
                  RailcraftBlocks.LIME_IRON_TANK_WALL.get(),
                  RailcraftBlocks.PINK_IRON_TANK_WALL.get(),
                  RailcraftBlocks.GRAY_IRON_TANK_WALL.get(),
                  RailcraftBlocks.LIGHT_GRAY_IRON_TANK_WALL.get(),
                  RailcraftBlocks.CYAN_IRON_TANK_WALL.get(),
                  RailcraftBlocks.PURPLE_IRON_TANK_WALL.get(),
                  RailcraftBlocks.BLUE_IRON_TANK_WALL.get(),
                  RailcraftBlocks.BROWN_IRON_TANK_WALL.get(),
                  RailcraftBlocks.GREEN_IRON_TANK_WALL.get(),
                  RailcraftBlocks.RED_IRON_TANK_WALL.get(),
                  RailcraftBlocks.BLACK_IRON_TANK_WALL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SteelTankBlockEntity>> STEEL_TANK =
      deferredRegister.register("steel_tank",
          () -> BlockEntityType.Builder
              .of(SteelTankBlockEntity::new,
                  RailcraftBlocks.WHITE_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.ORANGE_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.MAGENTA_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.LIGHT_BLUE_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.YELLOW_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.LIME_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.PINK_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.GRAY_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.LIGHT_GRAY_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.CYAN_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.PURPLE_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.BLUE_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.BROWN_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.GREEN_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.RED_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.BLACK_STEEL_TANK_GAUGE.get(),
                  RailcraftBlocks.WHITE_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.ORANGE_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.MAGENTA_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.LIGHT_BLUE_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.YELLOW_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.LIME_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.PINK_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.GRAY_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.LIGHT_GRAY_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.CYAN_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.PURPLE_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.BLUE_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.BROWN_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.GREEN_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.RED_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.BLACK_STEEL_TANK_VALVE.get(),
                  RailcraftBlocks.WHITE_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.ORANGE_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.MAGENTA_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.LIGHT_BLUE_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.YELLOW_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.LIME_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.PINK_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.GRAY_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.LIGHT_GRAY_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.CYAN_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.PURPLE_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.BLUE_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.BROWN_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.GREEN_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.RED_STEEL_TANK_WALL.get(),
                  RailcraftBlocks.BLACK_STEEL_TANK_WALL.get())
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
              .of(BlockSignalRelayBoxBlockEntity::new, RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get())
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

  public static final RegistryObject<BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN =
      deferredRegister.register("coke_oven",
          () -> BlockEntityType.Builder
              .of(CokeOvenBlockEntity::new, RailcraftBlocks.COKE_OVEN_BRICKS.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SwitchTrackMotorBlockEntity>> SWITCH_TRACK_MOTOR =
      deferredRegister.register("switch_track_motor",
          () -> BlockEntityType.Builder
              .of(SwitchTrackMotorBlockEntity::new, RailcraftBlocks.SWITCH_TRACK_MOTOR.get())
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
}
