package mods.railcraft.world.level.block.entity;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.multiblock.BlastFurnaceBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.CokeOvenBlockEntity;
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
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.TurnoutTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.WyeTrackBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftBlockEntityTypes {

  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Railcraft.ID);

  public static final RegistryObject<BlockEntityType<BlastFurnaceBlockEntity>> BLAST_FURNACE =
      BLOCK_ENTITY_TYPES.register("blast_furnace",
          () -> BlockEntityType.Builder
              .of(BlastFurnaceBlockEntity::new, RailcraftBlocks.BLAST_FURNACE_BRICKS.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<FeedStationBlockEntity>> FEED_STATION =
      BLOCK_ENTITY_TYPES.register("feed_station",
          () -> BlockEntityType.Builder
              .of(FeedStationBlockEntity::new, RailcraftBlocks.FEED_STATION.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<FluidLoaderBlockEntity>> FLUID_LOADER =
      BLOCK_ENTITY_TYPES.register("fluid_loader",
          () -> BlockEntityType.Builder
              .of(FluidLoaderBlockEntity::new, RailcraftBlocks.FLUID_LOADER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<FluidUnloaderBlockEntity>> FLUID_UNLOADER =
      BLOCK_ENTITY_TYPES.register("fluid_unloader",
          () -> BlockEntityType.Builder
              .of(FluidUnloaderBlockEntity::new, RailcraftBlocks.FLUID_UNLOADER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ItemLoaderBlockEntity>> ITEM_LOADER =
      BLOCK_ENTITY_TYPES.register("item_loader",
          () -> BlockEntityType.Builder
              .of(ItemLoaderBlockEntity::new, RailcraftBlocks.ITEM_LOADER.get(),
                  RailcraftBlocks.ADVANCED_ITEM_LOADER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ItemUnloaderBlockEntity>> ITEM_UNLOADER =
      BLOCK_ENTITY_TYPES.register("item_unloader",
          () -> BlockEntityType.Builder
              .of(ItemUnloaderBlockEntity::new, RailcraftBlocks.ITEM_UNLOADER.get(),
                  RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<AnalogSignalControllerBoxBlockEntity>> ANALOG_SIGNAL_CONTROLLER_BOX =
      BLOCK_ENTITY_TYPES.register("analog_signal_controller_box",
          () -> BlockEntityType.Builder
              .of(AnalogSignalControllerBoxBlockEntity::new,
                  RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalSequencerBoxBlockEntity>> SIGNAL_SEQUENCER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_sequencer_box",
          () -> BlockEntityType.Builder
              .of(SignalSequencerBoxBlockEntity::new, RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalCapacitorBoxBlockEntity>> SIGNAL_CAPACITOR_BOX =
      BLOCK_ENTITY_TYPES.register("signal_capacitor_box",
          () -> BlockEntityType.Builder
              .of(SignalCapacitorBoxBlockEntity::new, RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalInterlockBoxBlockEntity>> SIGNAL_INTERLOCK_BOX =
      BLOCK_ENTITY_TYPES.register("signal_interlock_box",
          () -> BlockEntityType.Builder
              .of(SignalInterlockBoxBlockEntity::new, RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<BlockSignalRelayBoxBlockEntity>> BLOCK_SIGNAL_RELAY_BOX =
      BLOCK_ENTITY_TYPES.register("block_signal_relay_box",
          () -> BlockEntityType.Builder
              .of(BlockSignalRelayBoxBlockEntity::new, RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalReceiverBoxBlockEntity>> SIGNAL_RECEIVER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_receiver_box",
          () -> BlockEntityType.Builder
              .of(SignalReceiverBoxBlockEntity::new, RailcraftBlocks.SIGNAL_RECEIVER_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SignalControllerBoxBlockEntity>> SIGNAL_CONTROLLER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_controller_box",
          () -> BlockEntityType.Builder
              .of(SignalControllerBoxBlockEntity::new, RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DualBlockSignalBlockEntity>> DUAL_BLOCK_SIGNAL =
      BLOCK_ENTITY_TYPES.register("dual_block_signal",
          () -> BlockEntityType.Builder
              .of(DualBlockSignalBlockEntity::new, RailcraftBlocks.DUAL_BLOCK_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DualDistantSignalBlockEntity>> DUAL_DISTANT_SIGNAL =
      BLOCK_ENTITY_TYPES.register("dual_distant_signal",
          () -> BlockEntityType.Builder
              .of(DualDistantSignalBlockEntity::new, RailcraftBlocks.DUAL_DISTANT_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DualTokenSignalBlockEntity>> DUAL_TOKEN_SIGNAL =
      BLOCK_ENTITY_TYPES.register("dual_token_signal",
          () -> BlockEntityType.Builder
              .of(DualTokenSignalBlockEntity::new, RailcraftBlocks.DUAL_TOKEN_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<BlockSignalBlockEntity>> BLOCK_SIGNAL =
      BLOCK_ENTITY_TYPES.register("block_signal",
          () -> BlockEntityType.Builder
              .of(BlockSignalBlockEntity::new, RailcraftBlocks.BLOCK_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<DistantSignalBlockEntity>> DISTANT_SIGNAL =
      BLOCK_ENTITY_TYPES.register("distant_signal",
          () -> BlockEntityType.Builder
              .of(DistantSignalBlockEntity::new, RailcraftBlocks.DISTANT_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<TokenSignalBlockEntity>> TOKEN_SIGNAL =
      BLOCK_ENTITY_TYPES.register("token_signal",
          () -> BlockEntityType.Builder
              .of(TokenSignalBlockEntity::new, RailcraftBlocks.TOKEN_SIGNAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ForceTrackEmitterBlockEntity>> FORCE_TRACK_EMITTER =
      BLOCK_ENTITY_TYPES.register("force_track_emitter",
          () -> BlockEntityType.Builder
              .of(ForceTrackEmitterBlockEntity::new, RailcraftBlocks.FORCE_TRACK_EMITTER.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ForceTrackBlockEntity>> FORCE_TRACK =
      BLOCK_ENTITY_TYPES.register("force_track",
          () -> BlockEntityType.Builder
              .of(ForceTrackBlockEntity::new, RailcraftBlocks.FORCE_TRACK.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<TurnoutTrackBlockEntity>> TURNOUT_TRACK =
      BLOCK_ENTITY_TYPES.register("turnout_track",
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
      BLOCK_ENTITY_TYPES.register("wye_track",
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
      BLOCK_ENTITY_TYPES.register("ritual",
          () -> BlockEntityType.Builder
              .of(RitualBlockEntity::new, RailcraftBlocks.RITUAL.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<ManualRollingMachineBlockEntity>> MANUAL_ROLLING_MACHINE =
      BLOCK_ENTITY_TYPES.register("manual_rolling_machine",
          () -> BlockEntityType.Builder
              .of(ManualRollingMachineBlockEntity::new,
                  RailcraftBlocks.MANUAL_ROLLING_MACHINE.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN =
      BLOCK_ENTITY_TYPES.register("coke_oven",
          () -> BlockEntityType.Builder
              .of(CokeOvenBlockEntity::new, RailcraftBlocks.COKE_OVEN_BRICKS.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<SwitchTrackMotorBlockEntity>> SWITCH_TRACK_MOTOR =
      BLOCK_ENTITY_TYPES.register("switch_track_motor",
          () -> BlockEntityType.Builder
              .of(SwitchTrackMotorBlockEntity::new, RailcraftBlocks.SWITCH_TRACK_MOTOR.get())
              .build(null));

  public static final RegistryObject<BlockEntityType<LockingTrackBlockEntity>> LOCKING_TRACK =
      BLOCK_ENTITY_TYPES.register("locking_track",
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
      BLOCK_ENTITY_TYPES.register("coupler_track",
          () -> BlockEntityType.Builder
              .of(CouplerTrackBlockEntity::new,
                  RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(),
                  RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(),
                  RailcraftBlocks.IRON_COUPLER_TRACK.get(),
                  RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(),
                  RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get())
              .build(null));
}
