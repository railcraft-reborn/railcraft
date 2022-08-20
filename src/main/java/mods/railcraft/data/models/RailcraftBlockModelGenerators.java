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
import mods.railcraft.world.level.block.post.Column;
import mods.railcraft.world.level.block.post.Connection;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.steamboiler.SteamBoilerTankBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DisembarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LocomotiveTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversibleOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class RailcraftBlockModelGenerators {

  private final Consumer<BlockStateGenerator> blockStateOutput;
  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
  private final Consumer<Item> skippedAutoModelsOutput;

  private final ResourceLocation lockingTrackLockdownModel;
  private final ResourceLocation lockingTrackTrainLockdownModel;
  private final ResourceLocation lockingTrackHoldingModel;
  private final ResourceLocation lockingTrackTrainHoldingModel;
  private final ResourceLocation lockingTrackBoardingModel;
  private final ResourceLocation lockingTrackBoardingReversedModel;
  private final ResourceLocation lockingTrackTrainBoardingModel;
  private final ResourceLocation lockingTrackTrainBoardingReversedModel;
  private final ResourceLocation lockingTrackActiveLockdownModel;
  private final ResourceLocation lockingTrackActiveTrainLockdownModel;
  private final ResourceLocation lockingTrackActiveHoldingModel;
  private final ResourceLocation lockingTrackActiveTrainHoldingModel;
  private final ResourceLocation lockingTrackActiveBoardingModel;
  private final ResourceLocation lockingTrackActiveBoardingReversedModel;
  private final ResourceLocation lockingTrackActiveTrainBoardingModel;
  private final ResourceLocation lockingTrackActiveTrainBoardingReversedModel;

  private final StraightTrackModelSet transitionTrackModels;
  private final StraightTrackModelSet activeTransitionTrackModels;
  private final StraightTrackModelSet activatorTrackModels;
  private final StraightTrackModelSet activeActivatorTrackModels;
  private final StraightTrackModelSet boosterTrackModels;
  private final StraightTrackModelSet activeBoosterTrackModels;
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

  private final StraightTrackModelSet embarkingTrack;
  private final StraightTrackModelSet activeEmbarkingTrack;

  private final StraightTrackModelSet disembarkingTrackLeft;
  private final StraightTrackModelSet activeDisembarkingTrackLeft;
  private final StraightTrackModelSet disembarkingTrackRight;
  private final StraightTrackModelSet activeDisembarkingTrackRight;

  private final StraightTrackModelSet launcherTrackModels;
  private final StraightTrackModelSet activeLauncherTrackModels;

  private final StraightTrackModelSet oneWayTrackModels;
  private final StraightTrackModelSet activeOneWayTrackModels;

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

    this.lockingTrackLockdownModel =
        this.createPassiveRail("locking_track_lockdown");
    this.lockingTrackTrainLockdownModel =
        this.createPassiveRail("locking_track_train_lockdown");
    this.lockingTrackHoldingModel =
        this.createPassiveRail("locking_track_holding");
    this.lockingTrackTrainHoldingModel =
        this.createPassiveRail("locking_track_train_holding");
    this.lockingTrackBoardingModel =
        this.createPassiveRail("locking_track_boarding");
    this.lockingTrackBoardingReversedModel =
        this.createPassiveRail("locking_track_boarding_reversed");
    this.lockingTrackTrainBoardingModel =
        this.createPassiveRail("locking_track_train_boarding");
    this.lockingTrackTrainBoardingReversedModel =
        this.createPassiveRail("locking_track_train_boarding_reversed");
    this.lockingTrackActiveLockdownModel =
        this.createActiveRail("locking_track_lockdown");
    this.lockingTrackActiveTrainLockdownModel =
        this.createActiveRail("locking_track_train_lockdown");
    this.lockingTrackActiveHoldingModel =
        this.createActiveRail("locking_track_holding");
    this.lockingTrackActiveTrainHoldingModel =
        this.createActiveRail("locking_track_train_holding");
    this.lockingTrackActiveBoardingModel =
        this.createActiveRail("locking_track_boarding");
    this.lockingTrackActiveBoardingReversedModel =
        this.createActiveRail("locking_track_boarding_reversed");
    this.lockingTrackActiveTrainBoardingModel =
        this.createActiveRail("locking_track_train_boarding");
    this.lockingTrackActiveTrainBoardingReversedModel =
        this.createActiveRail("locking_track_train_boarding_reversed");

    this.transitionTrackModels = this.createTrackModelSet("transition_track");
    this.activeTransitionTrackModels = this.createActiveTrackModelSet("transition_track");

    this.activatorTrackModels = this.createTrackModelSet("activator_track");
    this.activeActivatorTrackModels = this.createActiveTrackModelSet("activator_track");

    this.boosterTrackModels = this.createTrackModelSet("booster_track");
    this.activeBoosterTrackModels = this.createActiveTrackModelSet("booster_track");

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

    this.embarkingTrack = this.createTrackModelSet("embarking_track");
    this.activeEmbarkingTrack = this.createActiveTrackModelSet("embarking_track");

    this.disembarkingTrackLeft = this.createTrackModelSet("disembarking_track_left");
    this.activeDisembarkingTrackLeft = this.createActiveTrackModelSet("disembarking_track_left");
    this.disembarkingTrackRight = this.createTrackModelSet("disembarking_track_right");
    this.activeDisembarkingTrackRight = this.createActiveTrackModelSet("disembarking_track_right");

    this.launcherTrackModels = this.createTrackModelSet("launcher_track");
    this.activeLauncherTrackModels = this.createActiveTrackModelSet("launcher_track");

    this.oneWayTrackModels = this.createTrackModelSet("one_way_track");
    this.activeOneWayTrackModels = this.createActiveTrackModelSet("one_way_track");

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

    this.createSteamBoilerTank(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get());
    this.createSteamBoilerTank(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get());



    this.createTrivialBlock(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get(),
        TexturedModel.CUBE_TOP_BOTTOM);
    this.createTrivialBlock(RailcraftBlocks.CRUSHER.get(),
        TexturedModel.CUBE_TOP);

    this.createTrivialBlock(RailcraftBlocks.CREOSOTE.get());


    this.createSteelAnvil(RailcraftBlocks.STEEL_ANVIL.get());
    this.createSteelAnvil(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get());
    this.createSteelAnvil(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get());


    for (DyeColor dyeColor : DyeColor.values()) {
      this.createPost(RailcraftBlocks.POST.variantFor(dyeColor).get());
    }


    this.createElevatorTrack(RailcraftBlocks.ELEVATOR_TRACK.get());

    this.createForceTrack(RailcraftBlocks.FORCE_TRACK.get());

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

  private void createTrivialBlock(Block block, TexturedModel.Provider textureFactory) {
    this.blockStateOutput.accept(
        createSimpleBlock(block, textureFactory.create(block, this.modelOutput)));
  }

  private void createTrivialBlock(Block block) {
    this.createTrivialBlock(block, block);
  }

  private void createTrivialBlock(Block block, Block block2) {
    this.blockStateOutput.accept(createSimpleBlock(block,
        ModelLocationUtils.getModelLocation(block2)));
  }

  private static MultiVariantGenerator createSimpleBlock(Block block,
      ResourceLocation modelLocation) {
    return MultiVariantGenerator.multiVariant(block,
        Variant.variant().with(VariantProperties.MODEL, modelLocation));
  }

  private void delegateItemModel(Block block, ResourceLocation modelLocation) {
    this.modelOutput.accept(ModelLocationUtils.getModelLocation(block.asItem()),
        new DelegatedModel(modelLocation));
  }

  private void createSimpleFlatItemModel(Block block) {
    Item item = block.asItem();
    if (item != Items.AIR) {
      ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
          TextureMapping.layer0(block), this.modelOutput);
    }
  }

  private void createSimpleFlatItemModel(Item item) {
    ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(item), this.modelOutput);
  }

  private void createSimpleFlatItemModel(Block block, String textureSuffix) {
    Item item = block.asItem();
    ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(TextureMapping.getBlockTexture(block, textureSuffix)),
        this.modelOutput);
  }

  private ResourceLocation createSuffixedVariant(Block block, String suffix,
      ModelTemplate model, Function<ResourceLocation, TextureMapping> textureFactory) {
    return model.createWithSuffix(block, suffix,
        textureFactory.apply(TextureMapping.getBlockTexture(block, suffix)),
        this.modelOutput);
  }

  private ResourceLocation createVariant(String name,
      ModelTemplate model, Function<ResourceLocation, TextureMapping> textureFactory) {
    return model.create(
        new ResourceLocation(Railcraft.ID, "block/" + name + model.suffix.orElse("")),
        textureFactory.apply(new ResourceLocation(Railcraft.ID, "block/" + name)),
        this.modelOutput);
  }

  private void createSteelAnvil(Block block) {
    var model = RailcraftTexturedModel.STEEL_ANVIL.create(block, this.modelOutput);
    this.blockStateOutput.accept(
        createSimpleBlock(block, model).with(createHorizontalFacingDispatchAlt()));
  }

  private void createForceTrack(Block block) {
    var model = RailcraftModelTemplates.FORCE_TRACK.create(
        block, TextureMapping.rail(block), this.modelOutput);
    this.createSimpleFlatItemModel(block);
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(ForceTrackBlock.SHAPE)
            .select(RailShape.NORTH_SOUTH,
                Variant.variant().with(VariantProperties.MODEL, model))
            .select(RailShape.EAST_WEST,
                Variant.variant().with(VariantProperties.MODEL, model)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))));
  }

  private static PropertyDispatch createHorizontalFacingDispatchAlt() {
    return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
        .select(Direction.SOUTH, Variant.variant())
        .select(Direction.WEST,
            Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .select(Direction.NORTH,
            Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .select(Direction.EAST,
            Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
  }

  private void createElevatorTrack(Block block) {
    var model =
        RailcraftModelTemplates.ELEVATOR_TRACK.create(block, TextureMapping.defaultTexture(block),
            this.modelOutput);
    var activeModel =
        this.createSuffixedVariant(block, "_on", RailcraftModelTemplates.ELEVATOR_TRACK,
            TextureMapping::defaultTexture);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch
            .properties(ElevatorTrackBlock.POWERED, ElevatorTrackBlock.FACING)
            .generate((powered, facing) -> {
              var yRot = switch (facing) {
                case SOUTH -> VariantProperties.Rotation.R180;
                case EAST -> VariantProperties.Rotation.R90;
                case WEST -> VariantProperties.Rotation.R270;
                default -> VariantProperties.Rotation.R0;
              };
              return Variant.variant()
                  .with(VariantProperties.MODEL, powered ? activeModel : model)
                  .with(VariantProperties.Y_ROT, yRot);
            })));

    this.createSimpleFlatItemModel(block);
  }

  private void createSteamBoilerTank(Block block) {
    var textureMapping = new TextureMapping()
        .put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_end"))
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"));

    var model =
        RailcraftModelTemplates.STEAM_BOILER_TANK.create(block, textureMapping, this.modelOutput);

    var allModel = ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_all", textureMapping,
        this.modelOutput);

    var northEastModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NE.create(block, textureMapping,
            this.modelOutput);
    var northEastWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NEW.create(block, textureMapping,
            this.modelOutput);
    var northSouthEastModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NSE.create(block, textureMapping,
            this.modelOutput);
    var northSouthWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NSW.create(block, textureMapping,
            this.modelOutput);
    var northWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NW.create(block, textureMapping,
            this.modelOutput);
    var southEastModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_SE.create(block, textureMapping,
            this.modelOutput);
    var southEastWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_SEW.create(block, textureMapping,
            this.modelOutput);
    var southWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_SW.create(block, textureMapping,
            this.modelOutput);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(SteamBoilerTankBlock.CONNECTION_TYPE)
            .select(SteamBoilerTankBlock.ConnectionType.NONE, Variant.variant()
                .with(VariantProperties.MODEL, model))
            .select(SteamBoilerTankBlock.ConnectionType.ALL, Variant.variant()
                .with(VariantProperties.MODEL, allModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, northEastModel))
            .select(SteamBoilerTankBlock.ConnectionType.SOUTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, southEastModel))
            .select(SteamBoilerTankBlock.ConnectionType.SOUTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, southWestModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, northWestModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_SOUTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, northSouthEastModel))
            .select(SteamBoilerTankBlock.ConnectionType.SOUTH_EAST_WEST, Variant.variant()
                .with(VariantProperties.MODEL, southEastWestModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_EAST_WEST, Variant.variant()
                .with(VariantProperties.MODEL, northEastWestModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_SOUTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, northSouthWestModel))));
  }

  private void createPost(Block block) {
    var textures = TextureMapping.defaultTexture(block);
    var fullColumnModel = RailcraftModelTemplates.POST_COLUMN.create(
        block, textures, this.modelOutput);
    var doubleConnectionModel = RailcraftModelTemplates.POST_DOUBLE_CONNECTION.create(
        block, textures, this.modelOutput);
    var topColumnModel = RailcraftModelTemplates.POST_TOP_COLUMN.create(
        block, textures, this.modelOutput);
    var middleColumnModel = RailcraftModelTemplates.POST_SMALL_COLUMN.create(
        block, textures, this.modelOutput);
    var platformModel = RailcraftModelTemplates.POST_PLATFORM.create(
        block, textures, this.modelOutput);
    var singleConnectionModel = RailcraftModelTemplates.POST_SINGLE_CONNECTION.create(
        block, textures, this.modelOutput);
    var inventoryModel =
        RailcraftModelTemplates.POST_INVENTORY.create(block, textures, this.modelOutput);
    this.delegateItemModel(block, inventoryModel);
    this.blockStateOutput.accept(
        MultiPartGenerator.multiPart(block)
            .with(
                Condition.condition()
                    .term(PostBlock.COLUMN, Column.PLATFORM),
                Variant.variant()
                    .with(VariantProperties.MODEL, platformModel))
            .with(
                Condition.condition()
                    .term(PostBlock.COLUMN, Column.TOP),
                Variant.variant()
                    .with(VariantProperties.MODEL, topColumnModel))
            .with(
                Condition.condition()
                    .term(PostBlock.COLUMN, Column.SMALL),
                Variant.variant()
                    .with(VariantProperties.MODEL, middleColumnModel))
            .with(
                Condition.condition()
                    .term(PostBlock.COLUMN, Column.FULL),
                Variant.variant()
                    .with(VariantProperties.MODEL, fullColumnModel))
            .with(
                Condition.condition()
                    .term(PostBlock.NORTH, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.NORTH, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.SOUTH, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.SOUTH, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.EAST, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.EAST, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.WEST, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.WEST, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                    .with(VariantProperties.UV_LOCK, true)));
  }

  private void createAbandonedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock,
      Block couplerTrackBlock, Block embarkingTrackBlock, Block disembarkingTrackBlock,
      Block turnoutTrackBlock, Block wyeTrackBlock, Block junctionTrackBlock,
      Block launcherTrackBlock, Block oneWayTrackBlock, Block locomotiveTrackBlock) {
    this.createAbandonedFlexTrack(block);
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock, oneWayTrackBlock,
        locomotiveTrackBlock);
  }

  private void createAbandonedFlexTrack(Block block) {
    var texture0 =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_0"));
    var texture1 =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_1"));
    var cornerTextures =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));
    var flatModel0 =
        ModelTemplates.RAIL_FLAT.createWithSuffix(block, "_0", texture0, this.modelOutput);
    var flatModel1 =
        ModelTemplates.RAIL_FLAT.createWithSuffix(block, "_1", texture1, this.modelOutput);
    var cornerModel =
        ModelTemplates.RAIL_CURVED.create(block, cornerTextures, this.modelOutput);
    var raisedNorthEastModel =
        ModelTemplates.RAIL_RAISED_NE.create(block, texture0, this.modelOutput);
    var raisedSouthWestModel =
        ModelTemplates.RAIL_RAISED_SW.create(block, texture0, this.modelOutput);

    this.createSimpleFlatItemModel(block, "_0");

    this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(AbandonedTrackBlock.GRASS, true),
            Variant.variant()
                .with(VariantProperties.MODEL, new ResourceLocation("block/grass")))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant().with(VariantProperties.MODEL, flatModel0),
            Variant.variant().with(VariantProperties.MODEL, flatModel1))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, flatModel0)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
            Variant.variant()
                .with(VariantProperties.MODEL, flatModel1)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, raisedNorthEastModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, raisedSouthWestModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, raisedNorthEastModel))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, raisedSouthWestModel))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, cornerModel))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
  }

  private void createTracks(Block block, Block lockingTrackBlock, Block bufferStopTrackBlock,
      Block activatorTrackBlock, Block boosterTrackBlock, Block controlTrackBlock,
      Block gatedTrackBlock, Block detectorTrackBlock, Block couplerTrackBlock,
      Block embarkingTrackBlock, Block disembarkingTrackBlock, Block turnoutTrackBlock,
      Block wyeTrackBlock, Block junctionTrackBlock, Block launcherTrackBlock,
      Block oneWayTrackBlock, Block locomotiveTrackBlock) {
    this.createFlexTrack(block);
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
    this.createLockingTrack(lockingTrackBlock, outfittedTrackModels.flatModel());
    this.createBufferStopTrack(bufferStopTrackBlock, outfittedTrackModels.flatModel());
    this.createActiveOutfittedTrack(activatorTrackBlock, true, false, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, false, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createControlTrack(controlTrackBlock, outfittedTrackModels);
    this.createGatedTrack(gatedTrackBlock, outfittedTrackModels);
    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels);
    this.createCouplerTrack(couplerTrackBlock, outfittedTrackModels);
    this.createActiveOutfittedTrack(embarkingTrackBlock, true, false, outfittedTrackModels,
        this.embarkingTrack, this.activeEmbarkingTrack);
    this.createDisembarkingTrack(disembarkingTrackBlock, outfittedTrackModels);
    this.createTurnoutTrack(turnoutTrackBlock);
    this.createWyeTrack(wyeTrackBlock);
    this.createJunctionTrack(junctionTrackBlock);
    this.createActiveOutfittedTrack(launcherTrackBlock, false, false, outfittedTrackModels,
        this.launcherTrackModels, this.activeLauncherTrackModels);
    this.createActiveOutfittedTrack(oneWayTrackBlock, false, true, outfittedTrackModels,
        this.oneWayTrackModels, this.activeOneWayTrackModels);
    this.createLocomotiveTrack(locomotiveTrackBlock, outfittedTrackModels);
  }

  private void createHighSpeedTracks(Block block, Block transitionTrackBlock,
      Block lockingTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block detectorTrackBlock, Block turnoutTrackBlock, Block wyeTrackBlock,
      Block junctionTrackBlock, Block locomotiveTrackBlock) {
    this.createFlexTrack(block);
    var outfittedTrackModels = this.createOutfittedTrackModelSet(block);
    this.createActiveOutfittedTrack(transitionTrackBlock, true, true, outfittedTrackModels,
        this.transitionTrackModels, this.activeTransitionTrackModels);
    this.createLockingTrack(lockingTrackBlock, outfittedTrackModels.flatModel());
    this.createActiveOutfittedTrack(activatorTrackBlock, true, false, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, false, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels);
    this.createTurnoutTrack(turnoutTrackBlock);
    this.createWyeTrack(wyeTrackBlock);
    this.createJunctionTrack(junctionTrackBlock);
    this.createLocomotiveTrack(locomotiveTrackBlock, outfittedTrackModels);
  }

  private void createFlexTrack(Block block) {
    var textures = TextureMapping.rail(block);
    var cornerTextures = TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));
    var flatModel = ModelTemplates.RAIL_FLAT.create(block, textures, this.modelOutput);
    var cornerModel = ModelTemplates.RAIL_CURVED.create(block, cornerTextures, this.modelOutput);
    var raisedNorthEastModel =
        ModelTemplates.RAIL_RAISED_NE.create(block, textures, this.modelOutput);
    var raisedSouthWestModel =
        ModelTemplates.RAIL_RAISED_SW.create(block, textures, this.modelOutput);

    this.createSimpleFlatItemModel(block);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(BlockStateProperties.RAIL_SHAPE)
            .select(RailShape.NORTH_SOUTH, Variant.variant()
                .with(VariantProperties.MODEL, flatModel))
            .select(RailShape.EAST_WEST, Variant.variant()
                .with(VariantProperties.MODEL, flatModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.ASCENDING_EAST, Variant.variant()
                .with(VariantProperties.MODEL, raisedNorthEastModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.ASCENDING_WEST, Variant.variant()
                .with(VariantProperties.MODEL, raisedSouthWestModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.ASCENDING_NORTH, Variant.variant()
                .with(VariantProperties.MODEL, raisedNorthEastModel))
            .select(RailShape.ASCENDING_SOUTH, Variant.variant()
                .with(VariantProperties.MODEL, raisedSouthWestModel))
            .select(RailShape.SOUTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, cornerModel))
            .select(RailShape.SOUTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.NORTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
  }

  private void createTurnoutTrack(Block block) {
    var northModel = this.createSuffixedVariant(block, "_north",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var northSwitchedModel = this.createSuffixedVariant(block, "_north_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var southModel = this.createSuffixedVariant(block, "_south",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var southSwitchedModel = this.createSuffixedVariant(block, "_south_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.properties(SwitchTrackBlock.SHAPE,
            ReversibleOutfittedTrackBlock.REVERSED, TurnoutTrackBlock.MIRRORED,
            SwitchTrackBlock.SWITCHED)
            .select(RailShape.NORTH_SOUTH, false, false, false, Variant.variant() // North
                .with(VariantProperties.MODEL, northModel))
            .select(RailShape.NORTH_SOUTH, false, false, true, Variant.variant() // North
                .with(VariantProperties.MODEL, northSwitchedModel))
            .select(RailShape.NORTH_SOUTH, true, false, false, Variant.variant() // South
                .with(VariantProperties.MODEL, northModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, true, false, true, Variant.variant() // South
                .with(VariantProperties.MODEL, northSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, false, true, false, Variant.variant() // North
                .with(VariantProperties.MODEL, southModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, false, true, true, Variant.variant() // North
                .with(VariantProperties.MODEL, southSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, true, true, false, Variant.variant() // South
                .with(VariantProperties.MODEL, southModel))
            .select(RailShape.NORTH_SOUTH, true, true, true, Variant.variant() // South
                .with(VariantProperties.MODEL, southSwitchedModel))
            .select(RailShape.EAST_WEST, false, false, false, Variant.variant() // East
                .with(VariantProperties.MODEL, northModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.EAST_WEST, false, false, true, Variant.variant() // East
                .with(VariantProperties.MODEL, northSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.EAST_WEST, true, false, false, Variant.variant() // West
                .with(VariantProperties.MODEL, northModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .select(RailShape.EAST_WEST, true, false, true, Variant.variant() // West
                .with(VariantProperties.MODEL, northSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .select(RailShape.EAST_WEST, false, true, false, Variant.variant() // East
                .with(VariantProperties.MODEL, southModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .select(RailShape.EAST_WEST, false, true, true, Variant.variant() // East
                .with(VariantProperties.MODEL, southSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .select(RailShape.EAST_WEST, true, true, false, Variant.variant() // West
                .with(VariantProperties.MODEL, southModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.EAST_WEST, true, true, true, Variant.variant() // West
                .with(VariantProperties.MODEL, southSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))));

    this.createSimpleFlatItemModel(block, "_north");
  }

  private void createWyeTrack(Block block) {
    var eastModel = this.createSuffixedVariant(block, "_east",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var eastSwitchedModel = this.createSuffixedVariant(block, "_east_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var westModel = this.createSuffixedVariant(block, "_west",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var westSwitchedModel = this.createSuffixedVariant(block, "_west_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.properties(SwitchTrackBlock.SHAPE,
            ReversibleOutfittedTrackBlock.REVERSED, SwitchTrackBlock.SWITCHED)
            .generate((railShape, reversed, switched) -> {
              Direction facing = ReversibleOutfittedTrackBlock.getDirection(railShape, reversed);
              switch (facing) {
                case NORTH:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, switched ? eastSwitchedModel : eastModel)
                      .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                case SOUTH:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, switched ? westSwitchedModel : westModel)
                      .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                case EAST:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, switched ? westSwitchedModel : westModel);
                case WEST:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, switched ? eastSwitchedModel : eastModel);
                default:
                  throw new UnsupportedOperationException();
              }
            })));

    this.createSimpleFlatItemModel(block, "_east");
  }

  private void createJunctionTrack(Block block) {
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant()
        .with(VariantProperties.MODEL,
            this.createPassiveRail(ForgeRegistries.BLOCKS.getKey(block).getPath()))));
    this.createSimpleFlatItemModel(block);
  }

  private void createLockingTrack(Block block, ResourceLocation trackModel) {
    var generator = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModel))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));

    this.addLockingMode(LockingMode.LOCKDOWN, this.lockingTrackLockdownModel,
        this.lockingTrackActiveLockdownModel,
        generator);
    this.addLockingMode(LockingMode.TRAIN_LOCKDOWN, this.lockingTrackTrainLockdownModel,
        this.lockingTrackActiveTrainLockdownModel, generator);
    this.addLockingMode(LockingMode.HOLDING, this.lockingTrackHoldingModel,
        this.lockingTrackActiveHoldingModel,
        generator);
    this.addLockingMode(LockingMode.TRAIN_HOLDING, this.lockingTrackTrainHoldingModel,
        this.lockingTrackActiveTrainHoldingModel, generator);
    this.addLockingMode(LockingMode.BOARDING, this.lockingTrackBoardingModel,
        this.lockingTrackActiveBoardingModel,
        generator);
    this.addLockingMode(LockingMode.BOARDING_REVERSED, this.lockingTrackBoardingReversedModel,
        this.lockingTrackActiveBoardingReversedModel, generator);
    this.addLockingMode(LockingMode.TRAIN_BOARDING, this.lockingTrackTrainBoardingModel,
        this.lockingTrackActiveTrainBoardingModel, generator);
    this.addLockingMode(LockingMode.TRAIN_BOARDING_REVERSED,
        this.lockingTrackTrainBoardingReversedModel,
        this.lockingTrackActiveTrainBoardingReversedModel, generator);

    this.blockStateOutput.accept(generator);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void addLockingMode(LockingMode lockingMode, ResourceLocation model,
      ResourceLocation poweredModel, MultiPartGenerator blockState) {
    blockState
        .with(
            Condition.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, false)
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, model))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, true)
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, poweredModel))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, false)
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, true)
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, poweredModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
  }

  private void createBufferStopTrack(Block block, ResourceLocation trackModel) {
    this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false), // North
            Variant.variant()
                .with(VariantProperties.MODEL, RailcraftModelTemplates.BUFFER_STOP))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true), // South
            Variant.variant()
                .with(VariantProperties.MODEL, RailcraftModelTemplates.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false), // East
            Variant.variant()
                .with(VariantProperties.MODEL, RailcraftModelTemplates.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true), // West
            Variant.variant()
                .with(VariantProperties.MODEL, RailcraftModelTemplates.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModel))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createActiveOutfittedTrack(
      Block block,
      boolean allowedOnSlopes,
      boolean reversible,
      StraightTrackModelSet trackModels,
      StraightTrackModelSet trackKitModels,
      StraightTrackModelSet activeTrackKitModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false);
    if (reversible) {
      trackKitModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, false)
              .term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, false)
              .term(PoweredOutfittedTrackBlock.POWERED, true));
      trackKitModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, true,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, true)
              .term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, true,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, true)
              .term(PoweredOutfittedTrackBlock.POWERED, true));
    } else {
      trackKitModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition().term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition().term(PoweredOutfittedTrackBlock.POWERED, true));
    }
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
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
