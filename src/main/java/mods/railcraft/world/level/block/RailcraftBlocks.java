package mods.railcraft.world.level.block;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.entity.AnalogSignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.BlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.BlockSignalRelayBoxBlockEntity;
import mods.railcraft.world.level.block.entity.DistantSignalBlockEntity;
import mods.railcraft.world.level.block.entity.DualDistantSignalBlockEntity;
import mods.railcraft.world.level.block.entity.DualSignalBlockEntity;
import mods.railcraft.world.level.block.entity.DualTokenSignalBlockEntity;
import mods.railcraft.world.level.block.entity.SignalCapacitorBoxBlockEntity;
import mods.railcraft.world.level.block.entity.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.SignalInterlockBoxBlockEntity;
import mods.railcraft.world.level.block.entity.SignalReceiverBoxBlockEntity;
import mods.railcraft.world.level.block.entity.SignalSequencerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.TokenSignalBlockEntity;
import mods.railcraft.world.level.block.track.TrackConstants;
import mods.railcraft.world.level.block.track.TrackTypes;
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

  public static final RegistryObject<SignalBoxBlock> SWITCH_TRACK_LEVER_ACTUATOR =
      BLOCKS.register("switch_track_lever_actuator",
          () -> new SignalBoxBlock(AnalogSignalControllerBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));
  
  public static final RegistryObject<SignalBoxBlock> ANALOG_SIGNAL_CONTROLLER_BOX =
      BLOCKS.register("analog_signal_controller_box",
          () -> new SignalBoxBlock(AnalogSignalControllerBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_SEQUENCER_BOX =
      BLOCKS.register("signal_sequencer_box",
          () -> new SignalBoxBlock(SignalSequencerBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CAPACITOR_BOX =
      BLOCKS.register("signal_capacitor_box",
          () -> new SignalBoxBlock(SignalCapacitorBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_INTERLOCK_BOX =
      BLOCKS.register("signal_interlock_box",
          () -> new SignalBoxBlock(SignalInterlockBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_RELAY_BOX =
      BLOCKS.register("signal_relay_box",
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
          () -> new SignalBoxBlock(SignalControllerBoxBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> DUAL_SIGNAL =
      BLOCKS.register("dual_signal",
          () -> new SignalBlock(DualSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> DUAL_DISTANT_SIGNAL =
      BLOCKS.register("dual_distant_signal",
          () -> new SignalBlock(DualDistantSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> DUAL_TOKEN_SIGNAL =
      BLOCKS.register("dual_token_signal",
          () -> new SignalBlock(DualTokenSignalBlockEntity::new,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .harvestTool(RailcraftToolTypes.CROWBAR)
                  .harvestLevel(0)
                  .strength(8.0F, 50.0F)
                  .noOcclusion()));

  public static final RegistryObject<SignalBlock> SIGNAL =
      BLOCKS.register("signal",
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
              .randomTicks()));

  public static final RegistryObject<ForceTrackEmitterBlock> FORCE_TRACK_EMITTER =
      BLOCKS.register("force_track_emitter",
          () -> new ForceTrackEmitterBlock(AbstractBlock.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .randomTicks()));

  public static final RegistryObject<AbstractRailBlock> ABANDONED_FLEX_TRACK =
      BLOCKS.register("abandoned_flex_track",
          () -> new AbandonedFlexTrackBlock(TrackTypes.ABANDONED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noOcclusion()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<AbstractRailBlock> ELECTRIC_FLEX_TRACK =
      BLOCKS.register("electric_flex_track",
          () -> new TrackBlock(TrackTypes.ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noOcclusion()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<AbstractRailBlock> HIGH_SPEED_FLEX_TRACK =
      BLOCKS.register("high_speed_flex_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noOcclusion()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<AbstractRailBlock> HIGH_SPEED_ELECTRIC_FLEX_TRACK =
      BLOCKS.register("high_speed_electric_flex_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noOcclusion()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<AbstractRailBlock> STRAP_IRON_FLEX_TRACK =
      BLOCKS.register("strap_iron_flex_track",
          () -> new TrackBlock(TrackTypes.STRAP_IRON,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noOcclusion()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<AbstractRailBlock> REINFORCED_FLEX_TRACK =
      BLOCKS.register("reinforced_flex_track",
          () -> new TrackBlock(TrackTypes.REINFORCED,
              AbstractBlock.Properties.of(Material.DECORATION)
                  .noOcclusion()
                  .strength(TrackConstants.HARDNESS, 80F)));

  public static final RegistryObject<Block> ELEVATOR_TRACK =
      BLOCKS.register("elevator_track",
          () -> new ElevatorTrackBlock(AbstractBlock.Properties.of(RailcraftMaterials.ELEVATOR)
              .noOcclusion()
              .strength(1.05F)
              .sound(SoundType.METAL)
              .harvestTool(RailcraftToolTypes.CROWBAR)
              .harvestLevel(0)));

  public static final RegistryObject<Block> FIRESTONE =
      BLOCKS.register("firestone",
          () -> new MagicOreBlock(AbstractBlock.Properties.of(Material.STONE)
              .strength(3, 5)
              .harvestLevel(3)
              .harvestTool(ToolType.PICKAXE)));

  public static final RegistryObject<Block> RITUAL =
      BLOCKS.register("ritual",
          () -> new BlockRitual(AbstractBlock.Properties.of(Material.STONE)
              .lightLevel(state -> 1)
              .noOcclusion()));
}
