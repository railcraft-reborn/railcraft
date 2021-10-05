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
          () -> new SignalBoxBlock(BlockSignalRelayBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_RECEIVER_BOX =
      BLOCKS.register("signal_receiver_box",
          () -> new SignalBoxBlock(SignalReceiverBoxBlockEntity::new,
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

  public static final RegistryObject<SignalBlock> SIGNAL =
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

  public static final RegistryObject<TrackBlock> ABANDONED_FLEX_TRACK =
      BLOCKS.register("abandoned_flex_track",
          () -> new AbandonedTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_FLEX_TRACK =
      BLOCKS.register("electric_flex_track",
          () -> new TrackBlock(TrackTypes.ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_FLEX_TRACK =
      BLOCKS.register("high_speed_flex_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_FLEX_TRACK =
      BLOCKS.register("high_speed_electric_flex_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_FLEX_TRACK =
      BLOCKS.register("strap_iron_flex_track",
          () -> new TrackBlock(TrackTypes.STRAP_IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> REINFORCED_FLEX_TRACK =
      BLOCKS.register("reinforced_flex_track",
          () -> new TrackBlock(TrackTypes.REINFORCED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 80F)));

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

  public static final RegistryObject<Block> WOOD_POST =
      BLOCKS.register("wood_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.WOOD)
              .sound(SoundType.WOOD)));

  public static final RegistryObject<Block> STONE_POST =
      BLOCKS.register("stone_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<Block> BLACK_METAL_POST =
      BLOCKS.register("black_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> RED_METAL_POST =
      BLOCKS.register("red_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> GREEN_METAL_POST =
      BLOCKS.register("green_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BROWN_METAL_POST =
      BLOCKS.register("brown_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BLUE_METAL_POST =
      BLOCKS.register("blue_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> PURPLE_METAL_POST =
      BLOCKS.register("purple_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> CYAN_METAL_POST =
      BLOCKS.register("cyan_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIGHT_GRAY_METAL_POST =
      BLOCKS.register("light_gray_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> GRAY_METAL_POST =
      BLOCKS.register("gray_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> PINK_METAL_POST =
      BLOCKS.register("pink_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIME_METAL_POST =
      BLOCKS.register("lime_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> YELLOW_METAL_POST =
      BLOCKS.register("yellow_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIGHT_BLUE_METAL_POST =
      BLOCKS.register("light_blue_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> MAGENTA_METAL_POST =
      BLOCKS.register("magenta_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> ORANGE_METAL_POST =
      BLOCKS.register("orange_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> WHITE_METAL_POST =
      BLOCKS.register("white_metal_post",
          () -> new PostBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));
}
