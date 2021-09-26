package mods.railcraft.world.level.block;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.entity.*;
import mods.railcraft.world.level.block.track.*;
import mods.railcraft.world.level.material.RailcraftMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftBlocks {

  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, Railcraft.ID);

  public static final RegistryObject<SignalBoxBlock> SWITCH_TRACK_LEVER_ACTUATOR =
      BLOCKS.register("switch_track_lever_actuator",
          () -> new SignalBoxBlock(
            AnalogSignalControllerBoxBlockEntity::new,
            Properties.of(Material.DECORATION)
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
          () -> new SignalBoxBlock(SignalSequencerBoxBlockEntity::new,
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
          () -> new SignalBoxBlock(SignalInterlockBoxBlockEntity::new,
              Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_RELAY_BOX =
      BLOCKS.register("signal_relay_box",
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

  public static final RegistryObject<SignalBlock> DUAL_SIGNAL =
      BLOCKS.register("dual_signal",
          () -> new SignalBlock(DualSignalBlockEntity::new,
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
      BLOCKS.register("signal",
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
          () -> new AbandonedFlexTrackBlock(TrackTypes.ABANDONED,
              Properties.of(Material.DECORATION)
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

  public static final RegistryObject<Block> FIRESTONE =
      BLOCKS.register("firestone",
          () -> new MagicOreBlock(Properties.of(Material.STONE)
              .strength(3, 5)
              .harvestLevel(3)
              .harvestTool(ToolType.PICKAXE)));

  public static final RegistryObject<Block> RITUAL =
      BLOCKS.register("ritual",
          () -> new BlockRitual(Properties.of(Material.STONE)
              .lightLevel(state -> 1)
              .noOcclusion()));
}
