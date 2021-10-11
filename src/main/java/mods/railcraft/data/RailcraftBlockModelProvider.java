package mods.railcraft.data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.post.Column;
import mods.railcraft.world.level.block.post.Connection;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversibleOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TransitionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockModelDefinition;
import net.minecraft.data.BlockModelFields;
import net.minecraft.data.BlockModelFields.Rotation;
import net.minecraft.data.BlockModelWriter;
import net.minecraft.data.BlockStateVariantBuilder;
import net.minecraft.data.FinishedMultiPartBlockState;
import net.minecraft.data.FinishedVariantBlockState;
import net.minecraft.data.IFinishedBlockState;
import net.minecraft.data.IMultiPartPredicateBuilder;
import net.minecraft.data.ModelTextures;
import net.minecraft.data.ModelsResourceUtil;
import net.minecraft.data.ModelsUtil;
import net.minecraft.data.StockModelShapes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class RailcraftBlockModelProvider {

  private final Consumer<IFinishedBlockState> blockStateOutput;
  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;

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

  private final TrackModelSet transitionTrackModels;
  private final TrackModelSet activeTransitionTrackModels;
  private final TrackModelSet activatorTrackModels;
  private final TrackModelSet activeActivatorTrackModels;
  private final TrackModelSet boosterTrackModels;
  private final TrackModelSet activeBoosterTrackModels;
  private final TrackModelSet controlTrackModels;

  public RailcraftBlockModelProvider(Consumer<IFinishedBlockState> blockStateOutput,
      BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
    this.blockStateOutput = blockStateOutput;
    this.modelOutput = modelOutput;

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
  }

  public void run() {
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

    this.createTurnoutTrack(RailcraftBlocks.TURNOUT_TRACK.get());
    this.createWyeTrack(RailcraftBlocks.WYE_TRACK.get());

    this.createAbandonedTracks(
        RailcraftBlocks.ABANDONED_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
        RailcraftBlocks.ABANDONED_CONTROL_TRACK.get());
    this.createTracks(
        RailcraftBlocks.ELECTRIC_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get());
    this.createTracks(
        RailcraftBlocks.HIGH_SPEED_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_CONTROL_TRACK.get());
    this.createTracks(
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_CONTROL_TRACK.get());
    this.createOutfittedTracks(Blocks.RAIL,
        RailcraftBlocks.IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.IRON_CONTROL_TRACK.get());
    this.createTracks(
        RailcraftBlocks.REINFORCED_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
        RailcraftBlocks.REINFORCED_CONTROL_TRACK.get());
    this.createTracks(
        RailcraftBlocks.STRAP_IRON_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get());
  }

  private void delegateItemModel(Block block, ResourceLocation modelLocation) {
    this.modelOutput.accept(ModelsResourceUtil.getModelLocation(block.asItem()),
        new BlockModelWriter(modelLocation));
  }

  private void createSimpleFlatItemModel(Block block) {
    Item item = block.asItem();
    if (item != Items.AIR) {
      StockModelShapes.FLAT_ITEM.create(ModelsResourceUtil.getModelLocation(item),
          ModelTextures.layer0(block), this.modelOutput);
    }
  }

  private void createSimpleFlatItemModel(Item item) {
    StockModelShapes.FLAT_ITEM.create(ModelsResourceUtil.getModelLocation(item),
        ModelTextures.layer0(item), this.modelOutput);
  }

  private void createSimpleFlatItemModel(Block block, String textureSuffix) {
    Item item = block.asItem();
    StockModelShapes.FLAT_ITEM.create(ModelsResourceUtil.getModelLocation(item),
        ModelTextures.layer0(ModelTextures.getBlockTexture(block, textureSuffix)),
        this.modelOutput);
  }

  private ResourceLocation createSuffixedVariant(Block block, String suffix,
      ModelsUtil model, Function<ResourceLocation, ModelTextures> textureFactory) {
    return model.createWithSuffix(block, suffix,
        textureFactory.apply(ModelTextures.getBlockTexture(block, suffix)),
        this.modelOutput);
  }

  private ResourceLocation createVariant(String name,
      ModelsUtil model, Function<ResourceLocation, ModelTextures> textureFactory) {
    return model.create(new ResourceLocation(Railcraft.ID, name + model.suffix.orElse("")),
        textureFactory.apply(new ResourceLocation(Railcraft.ID, "block/" + name)),
        this.modelOutput);
  }

  private void createPost(Block block) {
    ModelTextures textures = ModelTextures.defaultTexture(block);
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
        FinishedMultiPartBlockState.multiPart(block)
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.COLUMN, Column.PLATFORM),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, platformModel))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.COLUMN, Column.TOP),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, topColumnModel))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.COLUMN, Column.SMALL),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, middleColumnModel))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.COLUMN, Column.FULL),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, fullColumnModel))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.NORTH, Connection.SINGLE),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, singleConnectionModel)
                    .with(BlockModelFields.UV_LOCK, true))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.NORTH, Connection.DOUBLE),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, doubleConnectionModel)
                    .with(BlockModelFields.UV_LOCK, true))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.SOUTH, Connection.SINGLE),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, singleConnectionModel)
                    .with(BlockModelFields.Y_ROT, Rotation.R180)
                    .with(BlockModelFields.UV_LOCK, true))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.SOUTH, Connection.DOUBLE),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, doubleConnectionModel)
                    .with(BlockModelFields.Y_ROT, Rotation.R180)
                    .with(BlockModelFields.UV_LOCK, true))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.EAST, Connection.SINGLE),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, singleConnectionModel)
                    .with(BlockModelFields.Y_ROT, Rotation.R90)
                    .with(BlockModelFields.UV_LOCK, true))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.EAST, Connection.DOUBLE),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, doubleConnectionModel)
                    .with(BlockModelFields.Y_ROT, Rotation.R90)
                    .with(BlockModelFields.UV_LOCK, true))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.WEST, Connection.SINGLE),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, singleConnectionModel)
                    .with(BlockModelFields.Y_ROT, Rotation.R270)
                    .with(BlockModelFields.UV_LOCK, true))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.WEST, Connection.DOUBLE),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, doubleConnectionModel)
                    .with(BlockModelFields.Y_ROT, Rotation.R270)
                    .with(BlockModelFields.UV_LOCK, true)));
  }

  private void createAbandonedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock) {
    ModelTextures texture0 =
        ModelTextures.rail(ModelTextures.getBlockTexture(block, "_0"));
    ModelTextures texture1 =
        ModelTextures.rail(ModelTextures.getBlockTexture(block, "_1"));
    ModelTextures cornerTextures =
        ModelTextures.rail(ModelTextures.getBlockTexture(block, "_corner"));
    ResourceLocation flatModel0 =
        StockModelShapes.RAIL_FLAT.createWithSuffix(block, "_0", texture0, this.modelOutput);
    ResourceLocation flatModel1 =
        StockModelShapes.RAIL_FLAT.createWithSuffix(block, "_1", texture1, this.modelOutput);
    ResourceLocation cornerModel =
        StockModelShapes.RAIL_CURVED.create(block, cornerTextures, this.modelOutput);
    ResourceLocation raisedNorthEastModel =
        StockModelShapes.RAIL_RAISED_NE.create(block, texture0, this.modelOutput);
    ResourceLocation raisedSouthWestModel =
        StockModelShapes.RAIL_RAISED_SW.create(block, texture0, this.modelOutput);

    this.createSimpleFlatItemModel(block, "_0");
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock,
        activatorTrackBlock, boosterTrackBlock, controlTrackBlock);

    this.blockStateOutput.accept(FinishedMultiPartBlockState.multiPart(block)
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(AbandonedTrackBlock.GRASS, true),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, new ResourceLocation("block/grass")))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant().with(BlockModelFields.MODEL, flatModel0),
            BlockModelDefinition.variant().with(BlockModelFields.MODEL, flatModel1))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, flatModel0)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, flatModel1)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, raisedNorthEastModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, raisedSouthWestModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, raisedNorthEastModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, raisedSouthWestModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, cornerModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, cornerModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, cornerModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, cornerModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270)));
  }

  private void createTracks(Block block, Block lockingTrackBlock, Block bufferStopTrackBlock,
      Block activatorTrackBlock, Block boosterTrackBlock, Block controlTrackBlock) {
    this.createTracks(block, null, lockingTrackBlock, bufferStopTrackBlock,
        activatorTrackBlock, boosterTrackBlock, controlTrackBlock);
  }

  private void createTracks(Block block, @Nullable Block transitionTrackBlock,
      Block lockingTrackBlock, Block bufferStopTrackBlock,
      Block activatorTrackBlock, Block boosterTrackBlock, Block controlTrackBlock) {
    ModelTextures textures = ModelTextures.rail(block);
    ModelTextures cornerTextures =
        ModelTextures.rail(ModelTextures.getBlockTexture(block, "_corner"));
    ResourceLocation flatModel =
        StockModelShapes.RAIL_FLAT.create(block, textures, this.modelOutput);
    ResourceLocation cornerModel =
        StockModelShapes.RAIL_CURVED.create(block, cornerTextures, this.modelOutput);
    ResourceLocation raisedNorthEastModel =
        StockModelShapes.RAIL_RAISED_NE.create(block, textures, this.modelOutput);
    ResourceLocation raisedSouthWestModel =
        StockModelShapes.RAIL_RAISED_SW.create(block, textures, this.modelOutput);

    this.createSimpleFlatItemModel(block);
    this.createOutfittedTracks(block, transitionTrackBlock, lockingTrackBlock, bufferStopTrackBlock,
        activatorTrackBlock, boosterTrackBlock, controlTrackBlock);

    this.blockStateOutput.accept(FinishedVariantBlockState.multiVariant(block)
        .with(BlockStateVariantBuilder.property(BlockStateProperties.RAIL_SHAPE)
            .select(RailShape.NORTH_SOUTH, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, flatModel))
            .select(RailShape.EAST_WEST, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, flatModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
            .select(RailShape.ASCENDING_EAST, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, raisedNorthEastModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
            .select(RailShape.ASCENDING_WEST, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, raisedSouthWestModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
            .select(RailShape.ASCENDING_NORTH, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, raisedNorthEastModel))
            .select(RailShape.ASCENDING_SOUTH, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, raisedSouthWestModel))
            .select(RailShape.SOUTH_EAST, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, cornerModel))
            .select(RailShape.SOUTH_WEST, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, cornerModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
            .select(RailShape.NORTH_WEST, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, cornerModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
            .select(RailShape.NORTH_EAST, BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, cornerModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))));
  }

  private void createOutfittedTracks(Block block,
      Block lockingTrackBlock, Block bufferStopTrackBlock, Block activatorTrackBlock,
      Block boosterTrackBlock, Block controlTrackBlock) {
    this.createOutfittedTracks(block, null, lockingTrackBlock, bufferStopTrackBlock,
        activatorTrackBlock, boosterTrackBlock, controlTrackBlock);
  }

  private void createOutfittedTracks(Block block, @Nullable Block transitionTrackBlock,
      Block lockingTrackBlock, Block bufferStopTrackBlock, Block activatorTrackBlock,
      Block boosterTrackBlock, Block controlTrackBlock) {
    TrackModelSet outfittedTrackModels =
        this.createTrackModels(block.getRegistryName().getPath() + "_outfitted");

    if (transitionTrackBlock != null) {
      this.createTransitionTrack(transitionTrackBlock, outfittedTrackModels,
          this.transitionTrackModels, this.activeTransitionTrackModels);
    }

    this.createLockingTrack(outfittedTrackModels.getFlatModel(), lockingTrackBlock);
    this.createBufferStopTrack(outfittedTrackModels.getFlatModel(), bufferStopTrackBlock);
    this.createActiveOutfittedTrack(activatorTrackBlock, true, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createControlTrack(controlTrackBlock, outfittedTrackModels, this.controlTrackModels);
  }

  private void createTurnoutTrack(Block block) {
    ResourceLocation northModel = this.createSuffixedVariant(block, "_north",
        StockModelShapes.RAIL_FLAT, ModelTextures::rail);
    ResourceLocation northSwitchedModel = this.createSuffixedVariant(block, "_north_switched",
        StockModelShapes.RAIL_FLAT, ModelTextures::rail);
    ResourceLocation southModel = this.createSuffixedVariant(block, "_south",
        StockModelShapes.RAIL_FLAT, ModelTextures::rail);
    ResourceLocation southSwitchedModel = this.createSuffixedVariant(block, "_south_switched",
        StockModelShapes.RAIL_FLAT, ModelTextures::rail);

    this.blockStateOutput.accept(FinishedVariantBlockState.multiVariant(block)
        .with(BlockStateVariantBuilder.properties(SwitchTrackBlock.SHAPE,
            ReversibleOutfittedTrackBlock.REVERSED, TurnoutTrackBlock.MIRRORED,
            SwitchTrackBlock.SWITCHED)
            .select(RailShape.NORTH_SOUTH, false, false, false, BlockModelDefinition.variant() // North
                .with(BlockModelFields.MODEL, northModel))
            .select(RailShape.NORTH_SOUTH, false, false, true, BlockModelDefinition.variant() // North
                .with(BlockModelFields.MODEL, northSwitchedModel))
            .select(RailShape.NORTH_SOUTH, true, false, false, BlockModelDefinition.variant() // South
                .with(BlockModelFields.MODEL, southModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, true, false, true, BlockModelDefinition.variant() // South
                .with(BlockModelFields.MODEL, southSwitchedModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, false, true, false, BlockModelDefinition.variant() // North
                .with(BlockModelFields.MODEL, southModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, false, true, true, BlockModelDefinition.variant() // North
                .with(BlockModelFields.MODEL, southSwitchedModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, true, true, false, BlockModelDefinition.variant() // South
                .with(BlockModelFields.MODEL, southModel))
            .select(RailShape.NORTH_SOUTH, true, true, true, BlockModelDefinition.variant() // South
                .with(BlockModelFields.MODEL, southSwitchedModel))
            .select(RailShape.EAST_WEST, false, false, false, BlockModelDefinition.variant() // East
                .with(BlockModelFields.MODEL, northModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
            .select(RailShape.EAST_WEST, false, false, true, BlockModelDefinition.variant() // East
                .with(BlockModelFields.MODEL, northSwitchedModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
            .select(RailShape.EAST_WEST, true, false, false, BlockModelDefinition.variant() // West
                .with(BlockModelFields.MODEL, northModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))
            .select(RailShape.EAST_WEST, true, false, true, BlockModelDefinition.variant() // West
                .with(BlockModelFields.MODEL, northSwitchedModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))
            .select(RailShape.EAST_WEST, false, true, false, BlockModelDefinition.variant() // East
                .with(BlockModelFields.MODEL, southModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))
            .select(RailShape.EAST_WEST, false, true, true, BlockModelDefinition.variant() // East
                .with(BlockModelFields.MODEL, southSwitchedModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))
            .select(RailShape.EAST_WEST, true, true, false, BlockModelDefinition.variant() // West
                .with(BlockModelFields.MODEL, southModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
            .select(RailShape.EAST_WEST, true, true, true, BlockModelDefinition.variant() // West
                .with(BlockModelFields.MODEL, southSwitchedModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))));

    this.createSimpleFlatItemModel(block, "_north");
  }

  private void createWyeTrack(Block block) {
    ResourceLocation eastModel = this.createSuffixedVariant(block, "_east",
        StockModelShapes.RAIL_FLAT, ModelTextures::rail);
    ResourceLocation eastSwitchedModel = this.createSuffixedVariant(block, "_east_switched",
        StockModelShapes.RAIL_FLAT, ModelTextures::rail);
    ResourceLocation westModel = this.createSuffixedVariant(block, "_west",
        StockModelShapes.RAIL_FLAT, ModelTextures::rail);
    ResourceLocation westSwitchedModel = this.createSuffixedVariant(block, "_west_switched",
        StockModelShapes.RAIL_FLAT, ModelTextures::rail);

    this.blockStateOutput.accept(FinishedVariantBlockState.multiVariant(block)
        .with(BlockStateVariantBuilder.properties(SwitchTrackBlock.SHAPE,
            ReversibleOutfittedTrackBlock.REVERSED, SwitchTrackBlock.SWITCHED)
            .generate((railShape, reversed, switched) -> {
              Direction facing = ReversibleOutfittedTrackBlock.getDirection(railShape, reversed);
              switch (facing) {
                case NORTH:
                  return BlockModelDefinition.variant()
                      .with(BlockModelFields.MODEL, switched ? eastSwitchedModel : eastModel)
                      .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90);
                case SOUTH:
                  return BlockModelDefinition.variant()
                      .with(BlockModelFields.MODEL, switched ? westSwitchedModel : westModel)
                      .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90);
                case EAST:
                  return BlockModelDefinition.variant()
                      .with(BlockModelFields.MODEL, switched ? westSwitchedModel : westModel);
                case WEST:
                  return BlockModelDefinition.variant()
                      .with(BlockModelFields.MODEL, switched ? eastSwitchedModel : eastModel);
                default:
                  throw new UnsupportedOperationException();
              }
            })));

    this.createSimpleFlatItemModel(block, "_east");
  }

  private void createLockingTrack(ResourceLocation trackModel, Block block) {
    FinishedMultiPartBlockState blockState = FinishedMultiPartBlockState.multiPart(block)
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90));

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
      ResourceLocation poweredModel, FinishedMultiPartBlockState blockState) {
    blockState
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, false)
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, model))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, true)
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, poweredModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, false)
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, model)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, true)
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, poweredModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90));
  }

  private void createBufferStopTrack(ResourceLocation trackModel, Block block) {
    this.blockStateOutput.accept(FinishedMultiPartBlockState.multiPart(block)
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false), // North
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, Models.BUFFER_STOP))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true), // South
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, Models.BUFFER_STOP)
                .with(BlockModelFields.Y_ROT, Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false), // East
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, Models.BUFFER_STOP)
                .with(BlockModelFields.Y_ROT, Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true), // West
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, Models.BUFFER_STOP)
                .with(BlockModelFields.Y_ROT, Rotation.R270))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90)));

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createActiveOutfittedTrack(Block block, boolean allowedOnSlopes,
      TrackModelSet trackModels, TrackModelSet trackKitModels,
      TrackModelSet activeTrackKitModels) {

    FinishedMultiPartBlockState blockState = FinishedMultiPartBlockState.multiPart(block)
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getFlatModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, activeTrackKitModels.getFlatModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, activeTrackKitModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90));

    if (allowedOnSlopes) {
      blockState
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, trackModels.getRaisedNorthEastModel()))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, trackModels.getRaisedSouthWestModel()))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, trackModels.getRaisedNorthEastModel())
                  .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, trackModels.getRaisedSouthWestModel())
                  .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, false)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel()))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, false)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel()))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, false)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel())
                  .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, false)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel())
                  .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, true)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, activeTrackKitModels.getRaisedNorthEastModel()))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, true)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, activeTrackKitModels.getRaisedSouthWestModel()))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, true)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, activeTrackKitModels.getRaisedNorthEastModel())
                  .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
          .with(
              IMultiPartPredicateBuilder.condition()
                  .term(PoweredOutfittedTrackBlock.POWERED, true)
                  .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
              BlockModelDefinition.variant()
                  .with(BlockModelFields.MODEL, activeTrackKitModels.getRaisedSouthWestModel())
                  .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90));
    }

    this.blockStateOutput.accept(blockState);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createControlTrack(Block block,
      TrackModelSet trackModels, TrackModelSet trackKitModels) {
    this.blockStateOutput.accept(FinishedMultiPartBlockState.multiPart(block)
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getFlatModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.or(
                IMultiPartPredicateBuilder.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                IMultiPartPredicateBuilder.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.or(
                IMultiPartPredicateBuilder.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                IMultiPartPredicateBuilder.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getRaisedNorthEastModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getRaisedSouthWestModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getRaisedNorthEastModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getRaisedSouthWestModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.or(
                IMultiPartPredicateBuilder.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
                IMultiPartPredicateBuilder.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.or(
                IMultiPartPredicateBuilder.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
                IMultiPartPredicateBuilder.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.or(
                IMultiPartPredicateBuilder.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
                IMultiPartPredicateBuilder.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))
        .with(
            IMultiPartPredicateBuilder.or(
                IMultiPartPredicateBuilder.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
                IMultiPartPredicateBuilder.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270)));

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createTransitionTrack(Block block,
      TrackModelSet trackModels, TrackModelSet trackKitModels, TrackModelSet activeTrackKitModels) {

    FinishedMultiPartBlockState blockState = FinishedMultiPartBlockState.multiPart(block)
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getFlatModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getRaisedNorthEastModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getRaisedSouthWestModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getRaisedNorthEastModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModels.getRaisedSouthWestModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90));

    this.addTransitionVariant(blockState, false, trackKitModels);
    this.addTransitionVariant(blockState, true, activeTrackKitModels);

    this.blockStateOutput.accept(blockState);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void addTransitionVariant(FinishedMultiPartBlockState blockState, boolean powered,
      TrackModelSet trackKitModels) {
    blockState
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getFlatModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel()))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(TransitionTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(
                    TransitionTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270));
  }

  private ResourceLocation createPassiveRail(String name) {
    return this.createVariant(name, StockModelShapes.RAIL_FLAT, ModelTextures::rail);
  }

  private ResourceLocation createActiveRail(String name) {
    return this.createVariant(name + "_on", StockModelShapes.RAIL_FLAT, ModelTextures::rail);
  }

  private TrackModelSet createTrackModels(String name) {
    return new TrackModelSet()
        .setFlatModel(this.createPassiveRail(name))
        .setRaisedNorthEastModel(
            this.createVariant(name, StockModelShapes.RAIL_RAISED_NE, ModelTextures::rail))
        .setRaisedSouthWestModel(
            this.createVariant(name, StockModelShapes.RAIL_RAISED_SW, ModelTextures::rail));
  }

  private TrackModelSet createActiveTrackModels(String name) {
    return new TrackModelSet()
        .setFlatModel(this.createActiveRail(name))
        .setRaisedNorthEastModel(this.createVariant(name + "_on",
            StockModelShapes.RAIL_RAISED_NE, ModelTextures::rail))
        .setRaisedSouthWestModel(this.createVariant(name + "_on",
            StockModelShapes.RAIL_RAISED_SW, ModelTextures::rail));
  }

  private class TrackModelSet {

    private ResourceLocation flatModel;
    private ResourceLocation raisedNorthEastModel;
    private ResourceLocation raisedSouthWestModel;

    private ResourceLocation getFlatModel() {
      return this.flatModel;
    }

    private TrackModelSet setFlatModel(ResourceLocation flatModel) {
      this.flatModel = flatModel;
      return this;
    }

    private ResourceLocation getRaisedNorthEastModel() {
      return this.raisedNorthEastModel;
    }

    private TrackModelSet setRaisedNorthEastModel(ResourceLocation raisedNorthEastModel) {
      this.raisedNorthEastModel = raisedNorthEastModel;
      return this;
    }

    private ResourceLocation getRaisedSouthWestModel() {
      return this.raisedSouthWestModel;
    }

    private TrackModelSet setRaisedSouthWestModel(ResourceLocation raisedSouthWestModel) {
      this.raisedSouthWestModel = raisedSouthWestModel;
      return this;
    }
  }
}
