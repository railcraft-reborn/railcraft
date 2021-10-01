package mods.railcraft.world.level.block;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.entity.signal.AnalogSignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.BlockSignalRelayBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DistantSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualDistantSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualBlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualTokenSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalInterlockBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalReceiverBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalSequencerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.TokenSignalBlockEntity;
import mods.railcraft.world.level.block.signal.SelfAttachableSignalBoxBlock;
import mods.railcraft.world.level.block.signal.SignalBlock;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackConstants;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackLeverBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import mods.railcraft.world.level.material.RailcraftMaterials;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractBlock.Properties;
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

  public static final RegistryObject<SignalBoxBlock> ANALOG_SIGNAL_CONTROLLER_BOX =
      BLOCKS.register("analog_signal_controller_box",
          () -> new SignalBoxBlock(AnalogSignalControllerBoxBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_SEQUENCER_BOX =
      BLOCKS.register("signal_sequencer_box",
          () -> new SelfAttachableSignalBoxBlock(SignalSequencerBoxBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CAPACITOR_BOX =
      BLOCKS.register("signal_capacitor_box",
          () -> new SignalBoxBlock(SignalCapacitorBoxBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_INTERLOCK_BOX =
      BLOCKS.register("signal_interlock_box",
          () -> new SelfAttachableSignalBoxBlock(SignalInterlockBoxBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> BLOCK_SIGNAL_RELAY_BOX =
      BLOCKS.register("block_signal_relay_box",
          () -> new SignalBoxBlock(BlockSignalRelayBoxBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_RECEIVER_BOX =
      BLOCKS.register("signal_receiver_box",
          () -> new SignalBoxBlock(SignalReceiverBoxBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CONTROLLER_BOX =
      BLOCKS.register("signal_controller_box",
          () -> new SignalBoxBlock(SignalControllerBoxBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> DUAL_BLOCK_SIGNAL =
      BLOCKS.register("dual_block_signal",
          () -> new SignalBlock(DualBlockSignalBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> DUAL_DISTANT_SIGNAL =
      BLOCKS.register("dual_distant_signal",
          () -> new SignalBlock(DualDistantSignalBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> DUAL_TOKEN_SIGNAL =
      BLOCKS.register("dual_token_signal",
          () -> new SignalBlock(DualTokenSignalBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> SIGNAL =
      BLOCKS.register("block_signal",
          () -> new SignalBlock(BlockSignalBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> DISTANT_SIGNAL =
      BLOCKS.register("distant_signal",
          () -> new SignalBlock(DistantSignalBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> TOKEN_SIGNAL =
      BLOCKS.register("token_signal",
          () -> new SignalBlock(TokenSignalBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<ForceTrackBlock> FORCE_TRACK =
      BLOCKS.register("force_track",
          () -> new ForceTrackBlock(Properties.of(Material.DECORATION)
              .sound(SoundType.GLASS)
              .instabreak()
              .noCollission()
              .randomTicks()));

  public static final RegistryObject<ForceTrackEmitterBlock> FORCE_TRACK_EMITTER =
      BLOCKS.register("force_track_emitter",
          () -> new ForceTrackEmitterBlock(Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .randomTicks()));

  public static final RegistryObject<TrackBlock> ABANDONED_FLEX_TRACK =
      BLOCKS.register("abandoned_flex_track",
          () -> new AbandonedTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_FLEX_TRACK =
      BLOCKS.register("electric_flex_track",
          () -> new TrackBlock(TrackTypes.ELECTRIC,
              Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_FLEX_TRACK =
      BLOCKS.register("high_speed_flex_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED,
              Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_FLEX_TRACK =
      BLOCKS.register("high_speed_electric_flex_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_FLEX_TRACK =
      BLOCKS.register("strap_iron_flex_track",
          () -> new TrackBlock(TrackTypes.STRAP_IRON,
              Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> REINFORCED_FLEX_TRACK =
      BLOCKS.register("reinforced_flex_track",
          () -> new TrackBlock(TrackTypes.REINFORCED,
              Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<Block> ELEVATOR_TRACK =
      BLOCKS.register("elevator_track",
          () -> new ElevatorTrackBlock(Properties.of(RailcraftMaterials.ELEVATOR)
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

  public static final RegistryObject<Block> FIRESTONE =
      BLOCKS.register("firestone",
          () -> new FirestoneBlock(Properties.of(Material.STONE)
              .lightLevel(__ -> 15)
              .strength(3, 5)
              .harvestLevel(3)
              .harvestTool(ToolType.PICKAXE)));

  public static final RegistryObject<Block> RITUAL =
      BLOCKS.register("ritual",
          () -> new RitualBlock(AbstractBlock.Properties.of(Material.STONE)
              .lightLevel(state -> 1)
              .noOcclusion()));

  public static final RegistryObject<Block> ROLLING_TABLE =
      BLOCKS.register("rolling_table_manual",
          () -> new RollingTable(AbstractBlock.Properties.of(Material.WOOD)
              .sound(SoundType.WOOD)));
}
