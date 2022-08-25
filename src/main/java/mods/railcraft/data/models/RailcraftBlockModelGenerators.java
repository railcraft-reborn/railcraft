package mods.railcraft.data.models;

import com.google.gson.JsonElement;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DisembarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LocomotiveTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversibleOutfittedTrackBlock;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class RailcraftBlockModelGenerators {

  private final Consumer<BlockStateGenerator> blockStateOutput;
  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
  private final Consumer<Item> skippedAutoModelsOutput;

  private final StraightTrackModelSet controlTrackModels;
  private final StraightTrackModelSet detectorTrackModels;
  private final StraightTrackModelSet activeDetectorTrackModels;
  private final StraightTrackModelSet travelDetectorTrackModels;
  private final StraightTrackModelSet activeTravelDetectorTrackModels;

  private final StraightTrackModelSet couplerTrackCoupler;
  private final StraightTrackModelSet activeCouplerTrackCoupler;
  private final StraightTrackModelSet couplerTrackDecoupler;
  private final StraightTrackModelSet activeCouplerTrackDecoupler;
  private final StraightTrackModelSet couplerTrackAutoCoupler;
  private final StraightTrackModelSet activeCouplerTrackAutoCoupler;

  private final StraightTrackModelSet disembarkingTrackLeft;
  private final StraightTrackModelSet activeDisembarkingTrackLeft;
  private final StraightTrackModelSet disembarkingTrackRight;
  private final StraightTrackModelSet activeDisembarkingTrackRight;

  private final StraightTrackModelSet locomotiveTrackShutdownModel;
  private final StraightTrackModelSet locomotiveTrackIdleModel;
  private final StraightTrackModelSet locomotiveTrackRunningModel;
  private final StraightTrackModelSet activeLocomotiveTrackShutdownModel;
  private final StraightTrackModelSet activeLocomotiveTrackIdleModel;
  private final StraightTrackModelSet activeLocomotiveTrackRunningModel;

  public RailcraftBlockModelGenerators(Consumer<BlockStateGenerator> blockStateOutput,
      BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput,
      Consumer<Item> skippedAutoModelsOutput) {
    this.blockStateOutput = blockStateOutput;
    this.modelOutput = modelOutput;
    this.skippedAutoModelsOutput = skippedAutoModelsOutput;

    this.controlTrackModels = this.createTrackModelSet("control_track");

    this.detectorTrackModels = this.createTrackModelSet("detector_track");
    this.activeDetectorTrackModels = this.createActiveTrackModelSet("detector_track");
    this.travelDetectorTrackModels = this.createTrackModelSet("detector_track_travel");
    this.activeTravelDetectorTrackModels = this.createActiveTrackModelSet("detector_track_travel");

    this.couplerTrackCoupler = this.createTrackModelSet("coupler_track_coupler");
    this.activeCouplerTrackCoupler = this.createActiveTrackModelSet("coupler_track_coupler");
    this.couplerTrackDecoupler = this.createTrackModelSet("coupler_track_decoupler");
    this.activeCouplerTrackDecoupler = this.createActiveTrackModelSet("coupler_track_decoupler");
    this.couplerTrackAutoCoupler = this.createTrackModelSet("coupler_track_auto_coupler");
    this.activeCouplerTrackAutoCoupler =
        this.createActiveTrackModelSet("coupler_track_auto_coupler");

    this.disembarkingTrackLeft = this.createTrackModelSet("disembarking_track_left");
    this.activeDisembarkingTrackLeft = this.createActiveTrackModelSet("disembarking_track_left");
    this.disembarkingTrackRight = this.createTrackModelSet("disembarking_track_right");
    this.activeDisembarkingTrackRight = this.createActiveTrackModelSet("disembarking_track_right");

    this.locomotiveTrackShutdownModel = this.createTrackModelSet("locomotive_track_shutdown");
    this.locomotiveTrackIdleModel = this.createTrackModelSet("locomotive_track_idle");
    this.locomotiveTrackRunningModel = this.createTrackModelSet("locomotive_track_running");
    this.activeLocomotiveTrackShutdownModel =
        this.createActiveTrackModelSet("locomotive_track_shutdown");
    this.activeLocomotiveTrackIdleModel = this.createActiveTrackModelSet("locomotive_track_idle");
    this.activeLocomotiveTrackRunningModel =
        this.createActiveTrackModelSet("locomotive_track_running");
  }

  public void run() {
    this.skipAutoItemBlock(RailcraftBlocks.FORCE_TRACK_EMITTER.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_RECEIVER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.BLOCK_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DISTANT_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.TOKEN_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_BLOCK_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_DISTANT_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_TOKEN_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.SWITCH_TRACK_LEVER.get());
    this.skipAutoItemBlock(RailcraftBlocks.SWITCH_TRACK_MOTOR.get());

    this.createAbandonedTracks(
        RailcraftBlocks.ABANDONED_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
        RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
        RailcraftBlocks.ABANDONED_GATED_TRACK.get(),
        RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(),
        RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(),
        RailcraftBlocks.ABANDONED_EMBARKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(),
        RailcraftBlocks.ABANDONED_WYE_TRACK.get(),
        RailcraftBlocks.ABANDONED_JUNCTION_TRACK.get(),
        RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.get(),
        RailcraftBlocks.ABANDONED_ONE_WAY_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK.get());
    this.createTracks(
        RailcraftBlocks.ELECTRIC_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
        RailcraftBlocks.ELECTRIC_GATED_TRACK.get(),
        RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK.get());
    this.createHighSpeedTracks(
        RailcraftBlocks.HIGH_SPEED_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK.get());
    this.createHighSpeedTracks(
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK.get());
    this.createOutfittedTracks(Blocks.RAIL,
        RailcraftBlocks.IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.IRON_CONTROL_TRACK.get(),
        RailcraftBlocks.IRON_GATED_TRACK.get(),
        RailcraftBlocks.IRON_DETECTOR_TRACK.get(),
        RailcraftBlocks.IRON_COUPLER_TRACK.get(),
        RailcraftBlocks.IRON_EMBARKING_TRACK.get(),
        RailcraftBlocks.IRON_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.IRON_TURNOUT_TRACK.get(),
        RailcraftBlocks.IRON_WYE_TRACK.get(),
        RailcraftBlocks.IRON_JUNCTION_TRACK.get(),
        RailcraftBlocks.IRON_LAUNCHER_TRACK.get(),
        RailcraftBlocks.IRON_ONE_WAY_TRACK.get(),
        RailcraftBlocks.IRON_LOCOMOTIVE_TRACK.get());
    this.createTracks(
        RailcraftBlocks.REINFORCED_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
        RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
        RailcraftBlocks.REINFORCED_GATED_TRACK.get(),
        RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(),
        RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(),
        RailcraftBlocks.REINFORCED_EMBARKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(),
        RailcraftBlocks.REINFORCED_WYE_TRACK.get(),
        RailcraftBlocks.REINFORCED_JUNCTION_TRACK.get(),
        RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.get(),
        RailcraftBlocks.REINFORCED_ONE_WAY_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK.get());
    this.createTracks(
        RailcraftBlocks.STRAP_IRON_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_WYE_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK.get());
  }

  private void skipAutoItemBlock(Block block) {
    this.skippedAutoModelsOutput.accept(block.asItem());
  }

  private void createSimpleFlatItemModel(Item item) {
    ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(item), this.modelOutput);
  }


  private ResourceLocation createVariant(String name,
      ModelTemplate model, Function<ResourceLocation, TextureMapping> textureFactory) {
    return model.create(
        new ResourceLocation(Railcraft.ID, "block/" + name + model.suffix.orElse("")),
        textureFactory.apply(new ResourceLocation(Railcraft.ID, "block/" + name)),
        this.modelOutput);
  }

  private void createAbandonedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock,
      Block couplerTrackBlock, Block embarkingTrackBlock, Block disembarkingTrackBlock,
      Block turnoutTrackBlock, Block wyeTrackBlock, Block junctionTrackBlock,
      Block launcherTrackBlock, Block oneWayTrackBlock, Block locomotiveTrackBlock) {
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock, oneWayTrackBlock,
        locomotiveTrackBlock);
  }

  private void createTracks(Block block, Block lockingTrackBlock, Block bufferStopTrackBlock,
      Block activatorTrackBlock, Block boosterTrackBlock, Block controlTrackBlock,
      Block gatedTrackBlock, Block detectorTrackBlock, Block couplerTrackBlock,
      Block embarkingTrackBlock, Block disembarkingTrackBlock, Block turnoutTrackBlock,
      Block wyeTrackBlock, Block junctionTrackBlock, Block launcherTrackBlock,
      Block oneWayTrackBlock, Block locomotiveTrackBlock) {
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock, oneWayTrackBlock,
        locomotiveTrackBlock);
  }

  private void createOutfittedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock,
      Block couplerTrackBlock, Block embarkingTrackBlock, Block disembarkingTrackBlock,
      Block turnoutTrackBlock, Block wyeTrackBlock, Block junctionTrackBlock,
      Block launcherTrackBlock, Block oneWayTrackBlock, Block locomotiveTrackBlock) {
    var outfittedTrackModels = this.createOutfittedTrackModelSet(block);
    this.createControlTrack(controlTrackBlock, outfittedTrackModels);
    this.createGatedTrack(gatedTrackBlock, outfittedTrackModels);
    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels);
    this.createCouplerTrack(couplerTrackBlock, outfittedTrackModels);
    this.createDisembarkingTrack(disembarkingTrackBlock, outfittedTrackModels);
    this.createLocomotiveTrack(locomotiveTrackBlock, outfittedTrackModels);
  }

  private void createHighSpeedTracks(Block block, Block transitionTrackBlock,
      Block lockingTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block detectorTrackBlock, Block turnoutTrackBlock, Block wyeTrackBlock,
      Block junctionTrackBlock, Block locomotiveTrackBlock) {
    var outfittedTrackModels = this.createOutfittedTrackModelSet(block);
    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels);
    this.createLocomotiveTrack(locomotiveTrackBlock, outfittedTrackModels);
  }

  private void createControlTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, OutfittedTrackBlock.SHAPE, true, false);
    this.controlTrackModels.apply(generator, OutfittedTrackBlock.SHAPE, true, false,
        Condition.or(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false),
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, true)
                .term(ControlTrackBlock.REVERSED, true)));
    this.controlTrackModels.apply(generator, OutfittedTrackBlock.SHAPE, true, true,
        Condition.or(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, true)
                .term(ControlTrackBlock.REVERSED, false),
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, true)));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createGatedTrack(Block block, StraightTrackModelSet trackModels) {
    var closedGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE);
    var openGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_open");
    var closedWallGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall");
    var openWallGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall_open");

    var generator = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.flatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.flatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.flatModel()))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.flatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.flatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.flatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));

    this.addGateVariants(generator, false, false, closedGateModel);
    this.addGateVariants(generator, true, false, openGateModel);
    this.addGateVariants(generator, false, true, closedWallGateModel);
    this.addGateVariants(generator, true, true, openWallGateModel);

    this.blockStateOutput.accept(generator);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void addGateVariants(MultiPartGenerator blockState, boolean open, boolean inWall,
      ResourceLocation model) {
    blockState
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false) // North
                .term(GatedTrackBlock.OPEN, open)
                .term(GatedTrackBlock.IN_WALL, inWall),
            Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true) // South
                .term(GatedTrackBlock.OPEN, open)
                .term(GatedTrackBlock.IN_WALL, inWall),
            Variant.variant()
                .with(VariantProperties.MODEL, model))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false) // East
                .term(GatedTrackBlock.OPEN, open)
                .term(GatedTrackBlock.IN_WALL, inWall),
            Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true) // West
                .term(GatedTrackBlock.OPEN, open)
                .term(GatedTrackBlock.IN_WALL, inWall),
            Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
  }

  private void createDetectorTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false);
    this.detectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
            .term(DetectorTrackBlock.POWERED, false));
    this.activeDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
            .term(DetectorTrackBlock.POWERED, true));
    this.travelDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
            .term(DetectorTrackBlock.POWERED, false));
    this.activeTravelDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
            .term(DetectorTrackBlock.POWERED, true));
    this.travelDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
            .term(DetectorTrackBlock.POWERED, false));
    this.activeTravelDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
            .term(DetectorTrackBlock.POWERED, true));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createCouplerTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, CouplerTrackBlock.SHAPE, true, false);
    this.couplerTrackCoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.COUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    this.activeCouplerTrackCoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.COUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    this.couplerTrackDecoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    this.activeCouplerTrackDecoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    this.couplerTrackAutoCoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.AUTO_COUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    this.activeCouplerTrackAutoCoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.AUTO_COUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createDisembarkingTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, CouplerTrackBlock.SHAPE, true, false);
    this.disembarkingTrackLeft.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, false)
            .term(PoweredOutfittedTrackBlock.POWERED, false));
    this.activeDisembarkingTrackLeft.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, false)
            .term(PoweredOutfittedTrackBlock.POWERED, true));
    this.disembarkingTrackRight.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, true)
            .term(PoweredOutfittedTrackBlock.POWERED, false));
    this.activeDisembarkingTrackRight.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, true)
            .term(PoweredOutfittedTrackBlock.POWERED, true));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createLocomotiveTrack(Block block, StraightTrackModelSet trackModel) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModel.apply(generator, LocomotiveTrackBlock.SHAPE, true, false);
    this.addLocomotiveMode(Locomotive.Mode.SHUTDOWN, this.locomotiveTrackShutdownModel,
        this.activeLocomotiveTrackShutdownModel, generator);
    this.addLocomotiveMode(Locomotive.Mode.IDLE, this.locomotiveTrackIdleModel,
        this.activeLocomotiveTrackIdleModel, generator);
    this.addLocomotiveMode(Locomotive.Mode.RUNNING, this.locomotiveTrackRunningModel,
        this.activeLocomotiveTrackRunningModel, generator);
    this.blockStateOutput.accept(generator);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void addLocomotiveMode(Locomotive.Mode locomotiveMode, StraightTrackModelSet model,
      StraightTrackModelSet poweredModel, MultiPartGenerator generator) {
    model.apply(generator, LocomotiveTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(LocomotiveTrackBlock.LOCOMOTIVE_MODE, locomotiveMode)
            .term(LocomotiveTrackBlock.POWERED, false));
    poweredModel.apply(generator, LocomotiveTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(LocomotiveTrackBlock.LOCOMOTIVE_MODE, locomotiveMode)
            .term(LocomotiveTrackBlock.POWERED, true));
  }

  private ResourceLocation createPassiveRail(String name) {
    return this.createVariant(name, ModelTemplates.RAIL_FLAT, TextureMapping::rail);
  }

  private ResourceLocation createActiveRail(String name) {
    return this.createVariant(name + "_on", ModelTemplates.RAIL_FLAT, TextureMapping::rail);
  }

  private StraightTrackModelSet createOutfittedTrackModelSet(Block block) {
    return this.createTrackModelSet(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_outfitted");
  }

  private StraightTrackModelSet createTrackModelSet(String name) {
    return new StraightTrackModelSet(this.createPassiveRail(name),
        this.createVariant(name, ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail),
        this.createVariant(name, ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
  }

  private StraightTrackModelSet createActiveTrackModelSet(String name) {
    return new StraightTrackModelSet(this.createActiveRail(name),
        this.createVariant(name + "_on",
            ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail),
        this.createVariant(name + "_on",
            ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
  }

  private record StraightTrackModelSet(
      ResourceLocation flatModel,
      ResourceLocation raisedNorthEastModel,
      ResourceLocation raisedSouthWestModel) {

    private void apply(MultiPartGenerator generator, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed) {
      this.apply(generator, shapeProperty, includeRaised, reversed, null);
    }

    private static <T extends Comparable<T>> Condition andWith(Property<T> property, T value,
        @Nullable Condition optionalCondition) {
      return and(Condition.condition().term(property, value), optionalCondition);
    }

    private static Condition and(Condition condition, @Nullable Condition optionalCondition) {
      return optionalCondition == null
          ? condition
          : Condition.and(condition, optionalCondition);
    }

    private void apply(MultiPartGenerator generator, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed,
        @Nullable Condition condition) {
      generator
          .with(
              andWith(shapeProperty, RailShape.NORTH_SOUTH, condition),
              Variant.variant()
                  .with(VariantProperties.MODEL, this.flatModel)
                  .with(VariantProperties.Y_ROT,
                      reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
          .with(
              andWith(shapeProperty, RailShape.EAST_WEST, condition),
              Variant.variant()
                  .with(VariantProperties.MODEL, this.flatModel)
                  .with(VariantProperties.Y_ROT,
                      reversed ? VariantProperties.Rotation.R270 : VariantProperties.Rotation.R90));

      if (includeRaised) {
        generator.with(
            andWith(shapeProperty, RailShape.ASCENDING_NORTH, condition),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    reversed ? this.raisedSouthWestModel : this.raisedNorthEastModel)
                .with(VariantProperties.Y_ROT,
                    reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
            .with(
                andWith(shapeProperty, RailShape.ASCENDING_SOUTH, condition),
                Variant.variant()
                    .with(VariantProperties.MODEL,
                        reversed ? this.raisedNorthEastModel : this.raisedSouthWestModel)
                    .with(VariantProperties.Y_ROT,
                        reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
            .with(
                andWith(shapeProperty, RailShape.ASCENDING_EAST, condition),
                Variant.variant()
                    .with(VariantProperties.MODEL,
                        reversed ? this.raisedSouthWestModel : this.raisedNorthEastModel)
                    .with(VariantProperties.Y_ROT,
                        reversed ? VariantProperties.Rotation.R270
                            : VariantProperties.Rotation.R90))
            .with(
                andWith(shapeProperty, RailShape.ASCENDING_WEST, condition),
                Variant.variant()
                    .with(VariantProperties.MODEL,
                        reversed ? this.raisedNorthEastModel : this.raisedSouthWestModel)
                    .with(VariantProperties.Y_ROT,
                        reversed ? VariantProperties.Rotation.R270
                            : VariantProperties.Rotation.R90));
      }
    }
  }
}
