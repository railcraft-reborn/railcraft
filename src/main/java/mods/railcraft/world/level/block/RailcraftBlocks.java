package mods.railcraft.world.level.block;

import mods.railcraft.Railcraft;
import mods.railcraft.client.ClientDist;
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
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.signal.DualSignalBlock;
import mods.railcraft.world.level.block.signal.SelfAttachableSignalBoxBlock;
import mods.railcraft.world.level.block.signal.SignalBlock;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import mods.railcraft.world.level.block.signal.UsableSignalBoxBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackConstants;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackLeverBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackMotorBlock;
import mods.railcraft.world.level.block.track.outfitted.ActivatorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BoosterTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BufferStopTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TransitionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import mods.railcraft.world.level.material.RailcraftMaterials;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftBlocks {

  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, Railcraft.ID);

  public static final RegistryObject<SwitchTrackActuatorBlock> SWITCH_TRACK_LEVER =
      BLOCKS.register("switch_track_lever",
          () -> new SwitchTrackLeverBlock(AbstractBlock.Properties.of(Material.DECORATION)
              .harvestTool(RailcraftToolTypes.CROWBAR)
              .harvestLevel(0)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SwitchTrackActuatorBlock> SWITCH_TRACK_MOTOR =
      BLOCKS.register("switch_track_motor",
          () -> new SwitchTrackMotorBlock(AbstractBlock.Properties.of(Material.DECORATION)
              .harvestTool(RailcraftToolTypes.CROWBAR)
              .harvestLevel(0)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> ANALOG_SIGNAL_CONTROLLER_BOX =
      BLOCKS.register("analog_signal_controller_box",
          () -> new UsableSignalBoxBlock<>(
              AnalogSignalControllerBoxBlockEntity.class,
              ClientDist::openAnalogSignalControllerBoxScreen,
              AnalogSignalControllerBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_SEQUENCER_BOX =
      BLOCKS.register("signal_sequencer_box",
          () -> new SelfAttachableSignalBoxBlock(SignalSequencerBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CAPACITOR_BOX =
      BLOCKS.register("signal_capacitor_box",
          () -> new UsableSignalBoxBlock<>(
              SignalCapacitorBoxBlockEntity.class,
              ClientDist::openSignalCapacitorBoxScreen,
              SignalCapacitorBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_INTERLOCK_BOX =
      BLOCKS.register("signal_interlock_box",
          () -> new SelfAttachableSignalBoxBlock(SignalInterlockBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> BLOCK_SIGNAL_RELAY_BOX =
      BLOCKS.register("block_signal_relay_box",
          () -> new UsableSignalBoxBlock<>(
              BlockSignalRelayBoxBlockEntity.class,
              ClientDist::openActionSignalBoxScreen,
              BlockSignalRelayBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_RECEIVER_BOX =
      BLOCKS.register("signal_receiver_box",
          () -> new UsableSignalBoxBlock<>(
              SignalReceiverBoxBlockEntity.class,
              ClientDist::openActionSignalBoxScreen,
              SignalReceiverBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CONTROLLER_BOX =
      BLOCKS.register("signal_controller_box",
          () -> new UsableSignalBoxBlock<>(
              SignalControllerBoxBlockEntity.class,
              ClientDist::openSignalControllerBoxScreen,
              SignalControllerBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_BLOCK_SIGNAL =
      BLOCKS.register("dual_block_signal",
          () -> new DualSignalBlock(DualBlockSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_DISTANT_SIGNAL =
      BLOCKS.register("dual_distant_signal",
          () -> new DualSignalBlock(DualDistantSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_TOKEN_SIGNAL =
      BLOCKS.register("dual_token_signal",
          () -> new DualSignalBlock(DualTokenSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> BLOCK_SIGNAL =
      BLOCKS.register("block_signal",
          () -> new SignalBlock(BlockSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> DISTANT_SIGNAL =
      BLOCKS.register("distant_signal",
          () -> new SignalBlock(DistantSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> TOKEN_SIGNAL =
      BLOCKS.register("token_signal",
          () -> new SignalBlock(TokenSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<ForceTrackBlock> FORCE_TRACK =
      BLOCKS.register("force_track",
          () -> new ForceTrackBlock(AbstractBlock.Properties.of(Material.DECORATION)
              .sound(SoundType.GLASS)
              .instabreak()
              .noCollission()
              .randomTicks()));

  public static final RegistryObject<ForceTrackEmitterBlock> FORCE_TRACK_EMITTER =
      BLOCKS.register("force_track_emitter",
          () -> new ForceTrackEmitterBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .randomTicks()));

  public static final RegistryObject<TrackBlock> ABANDONED_TRACK =
      BLOCKS.register("abandoned_track",
          () -> new AbandonedTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ABANDONED_LOCKING_TRACK =
      BLOCKS.register("abandoned_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ABANDONED_BUFFER_STOP_TRACK =
      BLOCKS.register("abandoned_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ABANDONED_ACTIVATOR_TRACK =
      BLOCKS.register("abandoned_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ABANDONED_BOOSTER_TRACK =
      BLOCKS.register("abandoned_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ABANDONED_CONTROL_TRACK =
      BLOCKS.register("abandoned_control_track",
          () -> new ControlTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_TRACK =
      BLOCKS.register("electric_track",
          () -> new TrackBlock(TrackTypes.ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_LOCKING_TRACK =
      BLOCKS.register("electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_BUFFER_STOP_TRACK =
      BLOCKS.register("electric_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_ACTIVATOR_TRACK =
      BLOCKS.register("electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_BOOSTER_TRACK =
      BLOCKS.register("electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_CONTROL_TRACK =
      BLOCKS.register("electric_control_track",
          () -> new ControlTrackBlock(TrackTypes.ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TRACK =
      BLOCKS.register("high_speed_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TRANSITION_TRACK =
      BLOCKS.register("high_speed_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_LOCKING_TRACK =
      BLOCKS.register("high_speed_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_BUFFER_STOP_TRACK =
      BLOCKS.register("high_speed_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ACTIVATOR_TRACK =
      BLOCKS.register("high_speed_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_BOOSTER_TRACK =
      BLOCKS.register("high_speed_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_CONTROL_TRACK =
      BLOCKS.register("high_speed_control_track",
          () -> new ControlTrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TRACK =
      BLOCKS.register("high_speed_electric_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      BLOCKS.register("high_speed_electric_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      BLOCKS.register("high_speed_electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_BUFFER_STOP_TRACK =
      BLOCKS.register("high_speed_electric_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      BLOCKS.register("high_speed_electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      BLOCKS.register("high_speed_electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_CONTROL_TRACK =
      BLOCKS.register("high_speed_electric_control_track",
          () -> new ControlTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> IRON_LOCKING_TRACK =
      BLOCKS.register("iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> IRON_BUFFER_STOP_TRACK =
      BLOCKS.register("iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> IRON_ACTIVATOR_TRACK =
      BLOCKS.register("iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> IRON_BOOSTER_TRACK =
      BLOCKS.register("iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> IRON_CONTROL_TRACK =
      BLOCKS.register("iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> REINFORCED_TRACK =
      BLOCKS.register("reinforced_track",
          () -> new TrackBlock(TrackTypes.REINFORCED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<TrackBlock> REINFORCED_LOCKING_TRACK =
      BLOCKS.register("reinforced_locking_track",
          () -> new LockingTrackBlock(TrackTypes.REINFORCED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<TrackBlock> REINFORCED_BUFFER_STOP_TRACK =
      BLOCKS.register("reinforced_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.REINFORCED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<TrackBlock> REINFORCED_ACTIVATOR_TRACK =
      BLOCKS.register("reinforced_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.REINFORCED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<TrackBlock> REINFORCED_BOOSTER_TRACK =
      BLOCKS.register("reinforced_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.REINFORCED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<TrackBlock> REINFORCED_CONTROL_TRACK =
      BLOCKS.register("reinforced_control_track",
          () -> new ControlTrackBlock(TrackTypes.REINFORCED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_TRACK =
      BLOCKS.register("strap_iron_track",
          () -> new TrackBlock(TrackTypes.STRAP_IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_LOCKING_TRACK =
      BLOCKS.register("strap_iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.STRAP_IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_BUFFER_STOP_TRACK =
      BLOCKS.register("strap_iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.STRAP_IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_ACTIVATOR_TRACK =
      BLOCKS.register("strap_iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.STRAP_IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_BOOSTER_TRACK =
      BLOCKS.register("strap_iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.STRAP_IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_CONTROL_TRACK =
      BLOCKS.register("strap_iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.STRAP_IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<Block> ELEVATOR_TRACK =
      BLOCKS.register("elevator_track",
          () -> new ElevatorTrackBlock(AbstractBlock.Properties.of(RailcraftMaterials.ELEVATOR)
              .noCollission()
              .strength(1.05F)
              .sound(SoundType.METAL)
              .harvestTool(RailcraftToolTypes.CROWBAR)
              .harvestLevel(0)));

  public static final RegistryObject<AbstractRailBlock> TURNOUT_TRACK =
      BLOCKS.register("turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<AbstractRailBlock> WYE_TRACK =
      BLOCKS.register("wye_track",
          () -> new WyeTrackBlock(TrackTypes.IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));
  // firestone (ORE)
  public static final RegistryObject<Block> FIRESTONE =
      BLOCKS.register("firestone",
          () -> new FirestoneBlock(AbstractBlock.Properties.of(Material.STONE)
              .lightLevel(__ -> 15)
              .strength(3, 5)
              .harvestLevel(3)
              .harvestTool(ToolType.PICKAXE)));

  public static final RegistryObject<Block> RITUAL =
      BLOCKS.register("ritual",
          () -> new RitualBlock(AbstractBlock.Properties.of(Material.STONE)
              .lightLevel(state -> 1)
              .noOcclusion()));

  public static final RegistryObject<Block> MANUAL_ROLLING_MACHINE =
      BLOCKS.register("manual_rolling_machine",
          () -> new ManualRollingMachineBlock(AbstractBlock.Properties.of(Material.WOOD)
              .sound(SoundType.WOOD)));

  public static final RegistryObject<Block> COKE_OVEN_BLOCK =
      BLOCKS.register("coke_oven",
          () -> new CokeOvenBlock(AbstractBlock.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .harvestTool(ToolType.PICKAXE)));

  public static final RegistryObject<Block> BLACK_POST =
      BLOCKS.register("black_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> RED_POST =
      BLOCKS.register("red_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> GREEN_POST =
      BLOCKS.register("green_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BROWN_POST =
      BLOCKS.register("brown_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BLUE_POST =
      BLOCKS.register("blue_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> PURPLE_POST =
      BLOCKS.register("purple_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> CYAN_POST =
      BLOCKS.register("cyan_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIGHT_GRAY_POST =
      BLOCKS.register("light_gray_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> GRAY_POST =
      BLOCKS.register("gray_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> PINK_POST =
      BLOCKS.register("pink_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIME_POST =
      BLOCKS.register("lime_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> YELLOW_POST =
      BLOCKS.register("yellow_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIGHT_BLUE_POST =
      BLOCKS.register("light_blue_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> MAGENTA_POST =
      BLOCKS.register("magenta_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> ORANGE_POST =
      BLOCKS.register("orange_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> WHITE_POST =
      BLOCKS.register("white_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));
}
