package mods.railcraft.world.level.block.entity;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
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
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.TurnoutTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.WyeTrackBlockEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftBlockEntityTypes {

  public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Railcraft.ID);

  public static final RegistryObject<TileEntityType<AnalogSignalControllerBoxBlockEntity>> ANALOG_SIGNAL_CONTROLLER_BOX =
      BLOCK_ENTITY_TYPES.register("analog_signal_controller_box",
          () -> TileEntityType.Builder
              .of(AnalogSignalControllerBoxBlockEntity::new,
                  RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<SignalSequencerBoxBlockEntity>> SIGNAL_SEQUENCER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_sequencer_box",
          () -> TileEntityType.Builder
              .of(SignalSequencerBoxBlockEntity::new, RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<SignalCapacitorBoxBlockEntity>> SIGNAL_CAPACITOR_BOX =
      BLOCK_ENTITY_TYPES.register("signal_capacitor_box",
          () -> TileEntityType.Builder
              .of(SignalCapacitorBoxBlockEntity::new, RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<SignalInterlockBoxBlockEntity>> SIGNAL_INTERLOCK_BOX =
      BLOCK_ENTITY_TYPES.register("signal_interlock_box",
          () -> TileEntityType.Builder
              .of(SignalInterlockBoxBlockEntity::new, RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<BlockSignalRelayBoxBlockEntity>> SIGNAL_RELAY_BOX =
      BLOCK_ENTITY_TYPES.register("signal_relay_box",
          () -> TileEntityType.Builder
              .of(BlockSignalRelayBoxBlockEntity::new, RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<SignalReceiverBoxBlockEntity>> SIGNAL_RECEIVER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_receiver_box",
          () -> TileEntityType.Builder
              .of(SignalReceiverBoxBlockEntity::new, RailcraftBlocks.SIGNAL_RECEIVER_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<SignalControllerBoxBlockEntity>> SIGNAL_CONTROLLER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_controller_box",
          () -> TileEntityType.Builder
              .of(SignalControllerBoxBlockEntity::new, RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<DualBlockSignalBlockEntity>> DUAL_SIGNAL =
      BLOCK_ENTITY_TYPES.register("dual_signal",
          () -> TileEntityType.Builder
              .of(DualBlockSignalBlockEntity::new, RailcraftBlocks.DUAL_BLOCK_SIGNAL.get())
              .build(null));

  public static final RegistryObject<TileEntityType<DualDistantSignalBlockEntity>> DUAL_DISTANT_SIGNAL =
      BLOCK_ENTITY_TYPES.register("dual_distant_signal",
          () -> TileEntityType.Builder
              .of(DualDistantSignalBlockEntity::new, RailcraftBlocks.DUAL_DISTANT_SIGNAL.get())
              .build(null));

  public static final RegistryObject<TileEntityType<DualTokenSignalBlockEntity>> DUAL_TOKEN_SIGNAL =
      BLOCK_ENTITY_TYPES.register("dual_token_signal",
          () -> TileEntityType.Builder
              .of(DualTokenSignalBlockEntity::new, RailcraftBlocks.DUAL_TOKEN_SIGNAL.get())
              .build(null));

  public static final RegistryObject<TileEntityType<BlockSignalBlockEntity>> BLOCK_SIGNAL =
      BLOCK_ENTITY_TYPES.register("block_signal",
          () -> TileEntityType.Builder
              .of(BlockSignalBlockEntity::new, RailcraftBlocks.BLOCK_SIGNAL.get())
              .build(null));

  public static final RegistryObject<TileEntityType<DistantSignalBlockEntity>> DISTANT_SIGNAL =
      BLOCK_ENTITY_TYPES.register("distant_signal",
          () -> TileEntityType.Builder
              .of(DistantSignalBlockEntity::new, RailcraftBlocks.DISTANT_SIGNAL.get())
              .build(null));

  public static final RegistryObject<TileEntityType<TokenSignalBlockEntity>> TOKEN_SIGNAL =
      BLOCK_ENTITY_TYPES.register("token_signal",
          () -> TileEntityType.Builder
              .of(TokenSignalBlockEntity::new, RailcraftBlocks.TOKEN_SIGNAL.get())
              .build(null));

  public static final RegistryObject<TileEntityType<?>> FORCE_TRACK_EMITTER =
      BLOCK_ENTITY_TYPES.register("force_track_emitter",
          () -> TileEntityType.Builder
              .of(ForceTrackEmitterBlockEntity::new, RailcraftBlocks.FORCE_TRACK_EMITTER.get())
              .build(null));

  public static final RegistryObject<TileEntityType<?>> FORCE_TRACK =
      BLOCK_ENTITY_TYPES.register("force_track",
          () -> TileEntityType.Builder
              .of(ForceTrackBlockEntity::new, RailcraftBlocks.FORCE_TRACK.get())
              .build(null));

  public static final RegistryObject<TileEntityType<?>> TURNOUT_TRACK =
      BLOCK_ENTITY_TYPES.register("turnout_track",
          () -> TileEntityType.Builder
              .of(TurnoutTrackBlockEntity::new, RailcraftBlocks.TURNOUT_TRACK.get())
              .build(null));

  public static final RegistryObject<TileEntityType<?>> WYE_TRACK =
      BLOCK_ENTITY_TYPES.register("wye_track",
          () -> TileEntityType.Builder
              .of(WyeTrackBlockEntity::new, RailcraftBlocks.WYE_TRACK.get())
              .build(null));

  public static final RegistryObject<TileEntityType<?>> RITUAL =
      BLOCK_ENTITY_TYPES.register("ritual",
          () -> TileEntityType.Builder
              .of(RitualBlockEntity::new, RailcraftBlocks.RITUAL.get())
              .build(null));

  public static final RegistryObject<TileEntityType<?>> MANUAL_ROLLING_MACHINE =
      BLOCK_ENTITY_TYPES.register("manual_rolling_machine",
          () -> TileEntityType.Builder
              .of(ManualRollingMachineBlockEntity::new,
                  RailcraftBlocks.MANUAL_ROLLING_MACHINE.get())
              .build(null));

  public static final RegistryObject<TileEntityType<?>> SWITCH_TRACK_MOTOR =
      BLOCK_ENTITY_TYPES.register("switch_track_motor",
          () -> TileEntityType.Builder
              .of(SwitchTrackMotorBlockEntity::new, RailcraftBlocks.SWITCH_TRACK_MOTOR.get())
              .build(null));

  public static final RegistryObject<TileEntityType<?>> LOCKING_TRACK =
      BLOCK_ENTITY_TYPES.register("locking_track",
          () -> TileEntityType.Builder
              .of(LockingTrackBlockEntity::new, RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get())
              .build(null));

}
