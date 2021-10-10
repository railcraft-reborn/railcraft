package mods.railcraft.data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.google.gson.JsonElement;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.post.Column;
import mods.railcraft.world.level.block.post.Connection;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ActivatorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BufferStopTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
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

  private final ResourceLocation activatorTrackModel;
  private final ResourceLocation activeActivatorTrackModel;

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

    this.activatorTrackModel = this.createPassiveRail("activator_track");
    this.activeActivatorTrackModel = this.createActiveRail("activator_track");
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

    this.createAbandonedTracks(
        RailcraftBlocks.ABANDONED_FLEX_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get());
    this.createTracks(
        RailcraftBlocks.ELECTRIC_FLEX_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get());
    this.createTracks(
        RailcraftBlocks.HIGH_SPEED_FLEX_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get());
    this.createTracks(
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_FLEX_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get());
    this.createOutfittedTracks(ModelsResourceUtil.getModelLocation(Blocks.RAIL),
        RailcraftBlocks.IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.IRON_ACTIVATOR_TRACK.get());
    this.createTracks(
        RailcraftBlocks.REINFORCED_FLEX_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get());
    this.createTracks(
        RailcraftBlocks.STRAP_IRON_FLEX_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get());
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

  private void createSimpleFlatItemModel(Block block, String textureSuffix) {
    Item item = block.asItem();
    StockModelShapes.FLAT_ITEM.create(ModelsResourceUtil.getModelLocation(item),
        ModelTextures.layer0(ModelTextures.getBlockTexture(block, textureSuffix)),
        this.modelOutput);
  }

  private ResourceLocation createPassiveRail(String name) {
    return StockModelShapes.RAIL_FLAT.create(
        new ResourceLocation(Railcraft.ID, "block/" + name),
        ModelTextures.rail(
            new ResourceLocation(Railcraft.ID, "block/" + name)),
        this.modelOutput);
  }

  private void createAbandonedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock) {
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
    this.createOutfittedTracks(flatModel0, lockingTrackBlock, bufferStopTrackBlock,
        activatorTrackBlock);

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

  private void createOutfittedTracks(ResourceLocation trackModel, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock) {
    this.createLockingTrack(trackModel, lockingTrackBlock);
    this.createBufferStopTrack(trackModel, bufferStopTrackBlock);
    this.createActivatorTrack(trackModel, activatorTrackBlock);
  }

  private void createTracks(Block block, Block lockingTrackBlock, Block bufferStopTrackBlock,
      Block activatorTrackBlock) {
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
    this.createOutfittedTracks(flatModel, lockingTrackBlock, bufferStopTrackBlock,
        activatorTrackBlock);

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

  private ResourceLocation createActiveRail(String name) {
    return StockModelShapes.RAIL_FLAT.create(
        new ResourceLocation(Railcraft.ID, "block/" + name + "_on"),
        ModelTextures.rail(
            new ResourceLocation(Railcraft.ID, "block/" + name + "_on")),
        this.modelOutput);
  }

  private void createPost(Block block) {
    ModelTextures textures = ModelTextures.defaultTexture(block);
    ResourceLocation fullColumnModel = Models.POST_COLUMN.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
    ResourceLocation doubleConnectionModel = Models.POST_DOUBLE_CONNECTION.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
    ResourceLocation topColumnModel = Models.POST_TOP_COLUMN.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
    ResourceLocation middleColumnModel = Models.POST_SMALL_COLUMN.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
    ResourceLocation platformModel = Models.POST_PLATFORM.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
    ResourceLocation singleConnectionModel = Models.POST_SINGLE_CONNECTION.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
    ResourceLocation inventoryModel = Models.POST_INVENTORY.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
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

    this.createSimpleFlatItemModel(block);
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
                .term(BufferStopTrackBlock.FACING, Direction.NORTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, Models.BUFFER_STOP))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BufferStopTrackBlock.FACING, Direction.SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, Models.BUFFER_STOP)
                .with(BlockModelFields.Y_ROT, Rotation.R180))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BufferStopTrackBlock.FACING, Direction.EAST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, Models.BUFFER_STOP)
                .with(BlockModelFields.Y_ROT, Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(BufferStopTrackBlock.FACING, Direction.WEST),
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

    this.createSimpleFlatItemModel(block);
  }

  private void createActivatorTrack(ResourceLocation trackModel, Block block) {
    this.blockStateOutput.accept(FinishedMultiPartBlockState.multiPart(block)
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(ActivatorTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(ActivatorTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, trackModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(ActivatorTrackBlock.POWERED, false)
                .term(ActivatorTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, this.activatorTrackModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(ActivatorTrackBlock.POWERED, false)
                .term(ActivatorTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, this.activatorTrackModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(ActivatorTrackBlock.POWERED, true)
                .term(ActivatorTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, this.activeActivatorTrackModel))
        .with(
            IMultiPartPredicateBuilder.condition()
                .term(ActivatorTrackBlock.POWERED, true)
                .term(ActivatorTrackBlock.SHAPE, RailShape.EAST_WEST),
            BlockModelDefinition.variant()
                .with(BlockModelFields.MODEL, this.activeActivatorTrackModel)
                .with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90)));

    this.createSimpleFlatItemModel(block);
  }
}
