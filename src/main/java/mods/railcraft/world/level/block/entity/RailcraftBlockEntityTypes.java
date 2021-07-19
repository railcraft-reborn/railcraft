package mods.railcraft.world.level.block.entity;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftBlockEntityTypes {

  public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Railcraft.ID);

  public static final RegistryObject<TileEntityType<AnalogSignalControllerBoxBlockEntity>> ANALOG_SIGNAL_CONTROLLER_BOX =
      BLOCK_ENTITY_TYPES.register("analog_signal_controller_box",
          () -> TileEntityType.Builder.of(AnalogSignalControllerBoxBlockEntity::new,
              RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get()).build(null));

  public static final RegistryObject<TileEntityType<SignalSequencerBoxBlockEntity>> SIGNAL_SEQUENCER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_sequencer_box",
          () -> TileEntityType.Builder.of(SignalSequencerBoxBlockEntity::new,
              RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get()).build(null));

  public static final RegistryObject<TileEntityType<SignalCapacitorBoxBlockEntity>> SIGNAL_CAPACITOR_BOX =
      BLOCK_ENTITY_TYPES.register("signal_capacitor_box",
          () -> TileEntityType.Builder.of(SignalCapacitorBoxBlockEntity::new,
              RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get()).build(null));

  public static final RegistryObject<TileEntityType<SignalInterlockBoxBlockEntity>> SIGNAL_INTERLOCK_BOX =
      BLOCK_ENTITY_TYPES.register("signal_interlock_box",
          () -> TileEntityType.Builder.of(SignalInterlockBoxBlockEntity::new,
              RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get()).build(null));

  public static final RegistryObject<TileEntityType<BlockSignalRelayBoxBlockEntity>> SIGNAL_RELAY_BOX =
      BLOCK_ENTITY_TYPES.register("signal_relay_box",
          () -> TileEntityType.Builder
              .of(BlockSignalRelayBoxBlockEntity::new, RailcraftBlocks.SIGNAL_RELAY_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<SignalReceiverBoxBlockEntity>> SIGNAL_RECEIVER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_receiver_box",
          () -> TileEntityType.Builder.of(SignalReceiverBoxBlockEntity::new,
              RailcraftBlocks.SIGNAL_RECEIVER_BOX.get()).build(null));

  public static final RegistryObject<TileEntityType<SignalControllerBoxBlockEntity>> SIGNAL_CONTROLLER_BOX =
      BLOCK_ENTITY_TYPES.register("signal_controller_box",
          () -> TileEntityType.Builder
              .of(SignalControllerBoxBlockEntity::new, RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get())
              .build(null));

  public static final RegistryObject<TileEntityType<DualSignalBlockEntity>> DUAL_SIGNAL =
      BLOCK_ENTITY_TYPES.register("dual_signal",
          () -> TileEntityType.Builder
              .of(DualSignalBlockEntity::new, RailcraftBlocks.DUAL_SIGNAL.get())
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

  public static final RegistryObject<TileEntityType<BlockSignalBlockEntity>> SIGNAL =
      BLOCK_ENTITY_TYPES.register("signal",
          () -> TileEntityType.Builder.of(BlockSignalBlockEntity::new, RailcraftBlocks.SIGNAL.get())
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

  public static final RegistryObject<TileEntityType<?>> OUTFITTED_TRACK =
      BLOCK_ENTITY_TYPES.register("outfitted_track",
          () -> TileEntityType.Builder.of(OutfittedTrackBlockEntity::new).build(null));

  public static final RegistryObject<TileEntityType<?>> TICKING_OUTFITTED_TRACK =
      BLOCK_ENTITY_TYPES.register("ticking_outfitted_track",
          () -> TileEntityType.Builder.of(TickableOutfittedTrackBlockEntity::new).build(null));

  public static final RegistryObject<TileEntityType<?>> RITUAL =
      BLOCK_ENTITY_TYPES.register("ritual",
          () -> TileEntityType.Builder.of(RitualBlockEntity::new, RailcraftBlocks.RITUAL.get())
              .build(null));
}
