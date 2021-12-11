package mods.railcraft.data;

import com.google.gson.JsonElement;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.AdvancedItemLoaderBlock;
import mods.railcraft.world.level.block.CokeOvenBricksBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.post.Column;
import mods.railcraft.world.level.block.post.Connection;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversibleOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.blockstates.VariantProperties.Rotation;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class RailcraftBlockModelProvider {

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

  private final StraightTrackModels transitionTrackModels;
  private final StraightTrackModels activeTransitionTrackModels;
  private final StraightTrackModels activatorTrackModels;
  private final StraightTrackModels activeActivatorTrackModels;
  private final StraightTrackModels boosterTrackModels;
  private final StraightTrackModels activeBoosterTrackModels;
  private final StraightTrackModels controlTrackModels;
  private final StraightTrackModels detectorTrackModels;
  private final StraightTrackModels activeDetectorTrackModels;
  private final StraightTrackModels travelDetectorTrackModels;
  private final StraightTrackModels activeTravelDetectotTrackModels;

  public RailcraftBlockModelProvider(Consumer<BlockStateGenerator> blockStateOutput,
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

    this.transitionTrackModels = this.createTrackModels("transition_track");
    this.activeTransitionTrackModels = this.createActiveTrackModels("transition_track");

    this.activatorTrackModels = this.createTrackModels("activator_track");
    this.activeActivatorTrackModels = this.createActiveTrackModels("activator_track");

    this.boosterTrackModels = this.createTrackModels("booster_track");
    this.activeBoosterTrackModels = this.createActiveTrackModels("booster_track");

    this.controlTrackModels = this.createTrackModels("control_track");

    this.detectorTrackModels = this.createTrackModels("detector_track");
    this.activeDetectorTrackModels = this.createActiveTrackModels("detector_track");
    this.travelDetectorTrackModels = this.createTrackModels("detector_track_travel");
    this.activeTravelDetectotTrackModels = this.createActiveTrackModels("detector_track_travel");
  }

  public void run() {
    this.skipAutoItemBlock(RailcraftBlocks.FORCE_TRACK_EMITTER.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_RECEIVER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.BLOCK_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DISTANT_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.TOKEN_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_BLOCK_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_DISTANT_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_TOKEN_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.SWITCH_TRACK_LEVER.get());
    this.skipAutoItemBlock(RailcraftBlocks.SWITCH_TRACK_MOTOR.get());
    this.skipAutoItemBlock(RailcraftBlocks.CREOSOTE.get()); // let mc stitch this

    this.createTrivialBlock(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get(),
        TexturedModel.CUBE_TOP_BOTTOM);

    // TODO cleanup, this is disgusting.
    final Block cokeBrick = RailcraftBlocks.COKE_OVEN_BRICKS.get();
    final ResourceLocation cokeDefaultState =
        TexturedModel.CUBE.create(cokeBrick, this.modelOutput);
    final ResourceLocation cokeParentState =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(cokeBrick,
          "_parent",
          ((new TextureMapping()).put(
            TextureSlot.SIDE,
            TextureMapping.getBlockTexture(cokeBrick, "_parent"))
              .put(TextureSlot.TOP, TextureMapping.getBlockTexture(cokeBrick))
              .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(cokeBrick))
          ),
          this.modelOutput);
    final ResourceLocation cokeParentStateOn =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(cokeBrick,
          "_parent_on",
          ((new TextureMapping()).put(
            TextureSlot.SIDE,
            TextureMapping.getBlockTexture(cokeBrick, "_parent_on"))
              .put(TextureSlot.TOP, TextureMapping.getBlockTexture(cokeBrick))
              .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(cokeBrick))
          ),
          this.modelOutput);

    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(cokeBrick).with(
            PropertyDispatch.properties(CokeOvenBricksBlock.LIT,
                CokeOvenBricksBlock.PARENT)
            .select(false, false, Variant.variant()
                .with(VariantProperties.MODEL, cokeDefaultState))
            .select(true, false, Variant.variant()
                .with(VariantProperties.MODEL, cokeDefaultState))
            .select(false, true, Variant.variant()
                .with(VariantProperties.MODEL, cokeParentState))
            .select(true, true, Variant.variant()
              .with(VariantProperties.MODEL, cokeParentStateOn))
            ));

    // this.skipAutoItemBlock(RailcraftBlocks.ELEVATOR_TRACK.get());
    // this.createSimpleFlatItemModel(RailcraftBlocks.ELEVATOR_TRACK.get(), "_off");

    this.createPost(RailcraftBlocks.BLACK_POST.get());
    this.createPost(RailcraftBlocks.RED_POST.get());
    this.createPost(RailcraftBlocks.GREEN_POST.get());
    this.createPost(RailcraftBlocks.BROWN_POST.get());
    this.createPost(RailcraftBlocks.BLUE_POST.get());
    this.createPost(RailcraftBlocks.PURPLE_POST.get());
    this.createPost(RailcraftBlocks.CYAN_POST.get());
    this.createPost(RailcraftBlocks.LIGHT_GRAY_POST.get());
    this.createPost(RailcraftBlocks.GRAY_POST.get());
    this.createPost(RailcraftBlocks.PINK_POST.get());
    this.createPost(RailcraftBlocks.LIME_POST.get());
    this.createPost(RailcraftBlocks.YELLOW_POST.get());
    this.createPost(RailcraftBlocks.LIGHT_BLUE_POST.get());
    this.createPost(RailcraftBlocks.MAGENTA_POST.get());
    this.createPost(RailcraftBlocks.ORANGE_POST.get());
    this.createPost(RailcraftBlocks.WHITE_POST.get());

    this.createFluidManipulator(RailcraftBlocks.FLUID_LOADER.get());
    this.createFluidManipulator(RailcraftBlocks.FLUID_UNLOADER.get());
    this.createManipulator(RailcraftBlocks.ITEM_LOADER.get());
    this.createManipulator(RailcraftBlocks.ITEM_UNLOADER.get());
    this.createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_LOADER.get(),
        AdvancedItemLoaderBlock.FACING);
    this.createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get(),
        AdvancedItemLoaderBlock.FACING);

    this.createElevatorTrack(RailcraftBlocks.ELEVATOR_TRACK.get());

    this.createTurnoutTrack(RailcraftBlocks.TURNOUT_TRACK.get());
    this.createWyeTrack(RailcraftBlocks.WYE_TRACK.get());

    this.createAbandonedTracks(
        RailcraftBlocks.ABANDONED_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
        RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
        RailcraftBlocks.ABANDONED_GATED_TRACK.get(),
        RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get());
    this.createTracks(
        RailcraftBlocks.ELECTRIC_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
        RailcraftBlocks.ELECTRIC_GATED_TRACK.get(),
        RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get());
    this.createHighSpeedTracks(
        RailcraftBlocks.HIGH_SPEED_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get());
    this.createHighSpeedTracks(
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get());
    this.createOutfittedTracks(Blocks.RAIL,
        RailcraftBlocks.IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.IRON_CONTROL_TRACK.get(),
        RailcraftBlocks.IRON_GATED_TRACK.get(),
        RailcraftBlocks.IRON_DETECTOR_TRACK.get());
    this.createTracks(
        RailcraftBlocks.REINFORCED_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
        RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
        RailcraftBlocks.REINFORCED_GATED_TRACK.get(),
        RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get());
    this.createTracks(
        RailcraftBlocks.STRAP_IRON_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get());
  }

  private void skipAutoItemBlock(Block block) {
    this.skippedAutoModelsOutput.accept(block.asItem());
  }

  private void createTrivialBlock(Block block, TexturedModel.Provider textureFactory) {
    this.blockStateOutput.accept(
        createSimpleBlock(block, textureFactory.create(block, this.modelOutput)));
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

  private void createElevatorTrack(Block block) {
    ResourceLocation model =
        Models.ELEVATOR_TRACK.create(block, TextureMapping.defaultTexture(block), this.modelOutput);
    ResourceLocation activeModel = this.createSuffixedVariant(block, "_on", Models.ELEVATOR_TRACK,
        TextureMapping::defaultTexture);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch
            .properties(ElevatorTrackBlock.POWERED, ElevatorTrackBlock.FACING)
            .generate((powered, facing) -> {
              VariantProperties.Rotation yRot = VariantProperties.Rotation.R0;
              switch (facing) {
                case SOUTH:
                  yRot = VariantProperties.Rotation.R180;
                  break;
                case EAST:
                  yRot = VariantProperties.Rotation.R90;
                  break;
                case WEST:
                  yRot = VariantProperties.Rotation.R270;
                  break;
                default:
                  break;
              }
              return Variant.variant()
                  .with(VariantProperties.MODEL, powered ? activeModel : model)
                  .with(VariantProperties.Y_ROT, yRot);
            })));

    this.createSimpleFlatItemModel(block);
  }

  private void createFluidManipulator(Block block) {
    this.createManipulator(block);
    ResourceLocation model =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_inventory",
            new TextureMapping()
                .put(TextureSlot.SIDE,
                    new ResourceLocation(Railcraft.ID, "block/fluid_manipulator_side_inventory"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom")),
            this.modelOutput);
    this.delegateItemModel(block, model);
  }

  private void createManipulator(Block block) {
    ResourceLocation model =
        ModelTemplates.CUBE_BOTTOM_TOP.create(block, TextureMapping.cubeBottomTop(block),
            this.modelOutput);
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block,
        Variant.variant()
            .with(VariantProperties.MODEL, model)));
  }

  private void createDirectionalManipulator(Block block, DirectionProperty facingProperty) {
    ResourceLocation horizontalModel =
        ModelTemplates.CUBE_ORIENTABLE.create(block, TextureMapping.orientableCubeOnlyTop(block),
            this.modelOutput);
    ResourceLocation upModel =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_up",
            new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_front"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_top")),
            this.modelOutput);
    ResourceLocation downModel =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_down",
            new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_front")),
            this.modelOutput);
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(facingProperty)
            .generate(facing -> {
              VariantProperties.Rotation yRot = VariantProperties.Rotation.R0;
              VariantProperties.Rotation xRot = VariantProperties.Rotation.R0;
              switch (facing) {
                case SOUTH:
                  yRot = VariantProperties.Rotation.R180;
                  break;
                case EAST:
                  yRot = VariantProperties.Rotation.R90;
                  break;
                case WEST:
                  yRot = VariantProperties.Rotation.R270;
                  break;
                case UP:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, upModel);
                case DOWN:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, downModel);
                default:
                  break;
              }
              return Variant.variant()
                  .with(VariantProperties.MODEL, horizontalModel)
                  .with(VariantProperties.Y_ROT, yRot)
                  .with(VariantProperties.X_ROT, xRot);
            })));
  }

  private void createPost(Block block) {
    TextureMapping textures = TextureMapping.defaultTexture(block);
    ResourceLocation fullColumnModel = Models.POST_COLUMN.create(block,
        textures, this.modelOutput);
    ResourceLocation doubleConnectionModel = Models.POST_DOUBLE_CONNECTION.create(block,
        textures, this.modelOutput);
    ResourceLocation topColumnModel = Models.POST_TOP_COLUMN.create(block,
        textures, this.modelOutput);
    ResourceLocation middleColumnModel = Models.POST_SMALL_COLUMN.create(block,
        textures, this.modelOutput);
    ResourceLocation platformModel = Models.POST_PLATFORM.create(block,
        textures, this.modelOutput);
    ResourceLocation singleConnectionModel = Models.POST_SINGLE_CONNECTION.create(block,
        textures, this.modelOutput);
    ResourceLocation inventoryModel = Models.POST_INVENTORY.create(block,
        textures, this.modelOutput);
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
                    .with(VariantProperties.Y_ROT, Rotation.R180)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.SOUTH, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R180)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.EAST, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R90)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.EAST, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R90)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.WEST, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R270)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.WEST, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R270)
                    .with(VariantProperties.UV_LOCK, true)));
  }

  private void createAbandonedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock) {
    this.createAbandonedFlexTrack(block);
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock);
  }

  private void createAbandonedFlexTrack(Block block) {
    TextureMapping texture0 =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_0"));
    TextureMapping texture1 =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_1"));
    TextureMapping cornerTextures =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));
    ResourceLocation flatModel0 =
        ModelTemplates.RAIL_FLAT.createWithSuffix(block, "_0", texture0, this.modelOutput);
    ResourceLocation flatModel1 =
        ModelTemplates.RAIL_FLAT.createWithSuffix(block, "_1", texture1, this.modelOutput);
    ResourceLocation cornerModel =
        ModelTemplates.RAIL_CURVED.create(block, cornerTextures, this.modelOutput);
    ResourceLocation raisedNorthEastModel =
        ModelTemplates.RAIL_RAISED_NE.create(block, texture0, this.modelOutput);
    ResourceLocation raisedSouthWestModel =
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
      Block gatedTrackBlock, Block detectorTrackBlock) {
    this.createFlexTrack(block);
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock);
  }

  private void createOutfittedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock) {
    StraightTrackModels outfittedTrackModels = this.createOutfittedTrackModels(block);
    this.createLockingTrack(lockingTrackBlock, outfittedTrackModels.getFlatModel());
    this.createBufferStopTrack(bufferStopTrackBlock, outfittedTrackModels.getFlatModel());
    this.createActiveOutfittedTrack(activatorTrackBlock, true, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createControlTrack(controlTrackBlock, outfittedTrackModels);
    this.createGatedTrack(gatedTrackBlock, outfittedTrackModels);
    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels);
  }

  private void createHighSpeedTracks(Block block, Block transitionTrackBlock,
      Block lockingTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block detectorTrackBlock) {
    this.createFlexTrack(block);
    StraightTrackModels outfittedTrackModels = this.createOutfittedTrackModels(block);
    this.createTransitionTrack(transitionTrackBlock, outfittedTrackModels);
    this.createLockingTrack(lockingTrackBlock, outfittedTrackModels.getFlatModel());
    this.createActiveOutfittedTrack(activatorTrackBlock, true, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels);
  }

  private void createFlexTrack(Block block) {
    TextureMapping textures = TextureMapping.rail(block);
    TextureMapping cornerTextures =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));
    ResourceLocation flatModel =
        ModelTemplates.RAIL_FLAT.create(block, textures, this.modelOutput);
    ResourceLocation cornerModel =
        ModelTemplates.RAIL_CURVED.create(block, cornerTextures, this.modelOutput);
    ResourceLocation raisedNorthEastModel =
        ModelTemplates.RAIL_RAISED_NE.create(block, textures, this.modelOutput);
    ResourceLocation raisedSouthWestModel =
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
    ResourceLocation northModel = this.createSuffixedVariant(block, "_north",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    ResourceLocation northSwitchedModel = this.createSuffixedVariant(block, "_north_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    ResourceLocation southModel = this.createSuffixedVariant(block, "_south",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    ResourceLocation southSwitchedModel = this.createSuffixedVariant(block, "_south_switched",
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
                .with(VariantProperties.MODEL, southModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, true, false, true, Variant.variant() // South
                .with(VariantProperties.MODEL, southSwitchedModel)
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
    ResourceLocation eastModel = this.createSuffixedVariant(block, "_east",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    ResourceLocation eastSwitchedModel = this.createSuffixedVariant(block, "_east_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    ResourceLocation westModel = this.createSuffixedVariant(block, "_west",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    ResourceLocation westSwitchedModel = this.createSuffixedVariant(block, "_west_switched",
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

  private void createLockingTrack(Block block, ResourceLocation trackModel) {
    MultiPartGenerator blockState = MultiPartGenerator.multiPart(block)
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
        blockState);
    this.addLockingMode(LockingMode.TRAIN_LOCKDOWN, this.lockingTrackTrainLockdownModel,
        this.lockingTrackActiveTrainLockdownModel, blockState);
    this.addLockingMode(LockingMode.HOLDING, this.lockingTrackHoldingModel,
        this.lockingTrackActiveHoldingModel,
        blockState);
    this.addLockingMode(LockingMode.TRAIN_HOLDING, this.lockingTrackTrainHoldingModel,
        this.lockingTrackActiveTrainHoldingModel, blockState);
    this.addLockingMode(LockingMode.BOARDING, this.lockingTrackBoardingModel,
        this.lockingTrackActiveBoardingModel,
        blockState);
    this.addLockingMode(LockingMode.BOARDING_REVERSED, this.lockingTrackBoardingReversedModel,
        this.lockingTrackActiveBoardingReversedModel, blockState);
    this.addLockingMode(LockingMode.TRAIN_BOARDING, this.lockingTrackTrainBoardingModel,
        this.lockingTrackActiveTrainBoardingModel, blockState);
    this.addLockingMode(LockingMode.TRAIN_BOARDING_REVERSED,
        this.lockingTrackTrainBoardingReversedModel,
        this.lockingTrackActiveTrainBoardingReversedModel, blockState);

    this.blockStateOutput.accept(blockState);

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
                .with(VariantProperties.MODEL, Models.BUFFER_STOP))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true), // South
            Variant.variant()
                .with(VariantProperties.MODEL, Models.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false), // East
            Variant.variant()
                .with(VariantProperties.MODEL, Models.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true), // West
            Variant.variant()
                .with(VariantProperties.MODEL, Models.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, Rotation.R270))
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

  private void createActiveOutfittedTrack(Block block, boolean allowedOnSlopes,
      StraightTrackModels trackModels, StraightTrackModels trackKitModels,
      StraightTrackModels activeTrackKitModels) {

    MultiPartGenerator blockState = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, activeTrackKitModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, activeTrackKitModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));

    if (allowedOnSlopes) {
      blockState
          .with(
              Condition.condition()
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
              Variant.variant()
                  .with(VariantProperties.MODEL, trackModels.getRaisedNorthEastModel()))
          .with(
              Condition.condition()
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
              Variant.variant()
                  .with(VariantProperties.MODEL, trackModels.getRaisedSouthWestModel()))
          .with(
              Condition.condition()
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
              Variant.variant()
                  .with(VariantProperties.MODEL, trackModels.getRaisedNorthEastModel())
                  .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
          .with(
              Condition.condition()
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
              Variant.variant()
                  .with(VariantProperties.MODEL, trackModels.getRaisedSouthWestModel())
                  .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
          .with(
              Condition.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, false)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
              Variant.variant()
                  .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel()))
          .with(
              Condition.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, false)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
              Variant.variant()
                  .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel()))
          .with(
              Condition.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, false)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
              Variant.variant()
                  .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel())
                  .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
          .with(
              Condition.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, false)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
              Variant.variant()
                  .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel())
                  .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
          .with(
              Condition.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, true)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
              Variant.variant()
                  .with(VariantProperties.MODEL, activeTrackKitModels.getRaisedNorthEastModel()))
          .with(
              Condition.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, true)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
              Variant.variant()
                  .with(VariantProperties.MODEL, activeTrackKitModels.getRaisedSouthWestModel()))
          .with(
              Condition.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, true)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
              Variant.variant()
                  .with(VariantProperties.MODEL, activeTrackKitModels.getRaisedNorthEastModel())
                  .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
          .with(
              Condition.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, true)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
              Variant.variant()
                  .with(VariantProperties.MODEL, activeTrackKitModels.getRaisedSouthWestModel())
                  .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }

    this.blockStateOutput.accept(blockState);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createControlTrack(Block block, StraightTrackModels trackModels) {
    this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createTransitionTrack(Block block, StraightTrackModels trackModels) {
    MultiPartGenerator blockState = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));

    this.addTransitionVariants(blockState, false, this.transitionTrackModels);
    this.addTransitionVariants(blockState, true, this.activeTransitionTrackModels);

    this.blockStateOutput.accept(blockState);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void addTransitionVariants(MultiPartGenerator blockState, boolean powered,
      StraightTrackModels trackKitModels) {
    blockState
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
  }

  private void createGatedTrack(Block block, StraightTrackModels trackModels) {
    ResourceLocation closedGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE);
    ResourceLocation openGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_open");
    ResourceLocation closedWallGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall");
    ResourceLocation openWallGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall_open");

    MultiPartGenerator blockState = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));

    this.addGateVariants(blockState, false, false, closedGateModel);
    this.addGateVariants(blockState, true, false, openGateModel);
    this.addGateVariants(blockState, false, true, closedWallGateModel);
    this.addGateVariants(blockState, true, true, openWallGateModel);

    this.blockStateOutput.accept(blockState);

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

  private void createDetectorTrack(Block block, StraightTrackModels trackModels) {
    this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.detectorTrackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.detectorTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.detectorTrackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.detectorTrackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.detectorTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.detectorTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.activeDetectorTrackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.activeDetectorTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeDetectorTrackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeDetectorTrackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeDetectorTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeDetectorTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.travelDetectorTrackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.travelDetectorTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.travelDetectorTrackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.travelDetectorTrackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.travelDetectorTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.travelDetectorTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.activeTravelDetectotTrackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.activeTravelDetectotTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeTravelDetectotTrackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeTravelDetectotTrackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeTravelDetectotTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeTravelDetectotTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.travelDetectorTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.travelDetectorTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.travelDetectorTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.travelDetectorTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.travelDetectorTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, false),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.travelDetectorTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.activeTravelDetectotTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.activeTravelDetectotTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeTravelDetectotTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeTravelDetectotTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeTravelDetectotTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)
                .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
                .term(DetectorTrackBlock.POWERED, true),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    this.activeTravelDetectotTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));

    this.createSimpleFlatItemModel(block.asItem());
  }

  private ResourceLocation createPassiveRail(String name) {
    return this.createVariant(name, ModelTemplates.RAIL_FLAT, TextureMapping::rail);
  }

  private ResourceLocation createActiveRail(String name) {
    return this.createVariant(name + "_on", ModelTemplates.RAIL_FLAT, TextureMapping::rail);
  }

  private StraightTrackModels createOutfittedTrackModels(Block block) {
    return this.createTrackModels(block.getRegistryName().getPath() + "_outfitted");
  }

  private StraightTrackModels createTrackModels(String name) {
    return new StraightTrackModels()
        .setFlatModel(this.createPassiveRail(name))
        .setRaisedNorthEastModel(
            this.createVariant(name, ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail))
        .setRaisedSouthWestModel(
            this.createVariant(name, ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
  }

  private StraightTrackModels createActiveTrackModels(String name) {
    return new StraightTrackModels()
        .setFlatModel(this.createActiveRail(name))
        .setRaisedNorthEastModel(this.createVariant(name + "_on",
            ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail))
        .setRaisedSouthWestModel(this.createVariant(name + "_on",
            ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
  }

  private class StraightTrackModels {

    private ResourceLocation flatModel;
    private ResourceLocation raisedNorthEastModel;
    private ResourceLocation raisedSouthWestModel;

    private ResourceLocation getFlatModel() {
      return this.flatModel;
    }

    private StraightTrackModels setFlatModel(ResourceLocation flatModel) {
      this.flatModel = flatModel;
      return this;
    }

    private ResourceLocation getRaisedNorthEastModel() {
      return this.raisedNorthEastModel;
    }

    private StraightTrackModels setRaisedNorthEastModel(ResourceLocation raisedNorthEastModel) {
      this.raisedNorthEastModel = raisedNorthEastModel;
      return this;
    }

    private ResourceLocation getRaisedSouthWestModel() {
      return this.raisedSouthWestModel;
    }

    private StraightTrackModels setRaisedSouthWestModel(ResourceLocation raisedSouthWestModel) {
      this.raisedSouthWestModel = raisedSouthWestModel;
      return this;
    }
  }
}
