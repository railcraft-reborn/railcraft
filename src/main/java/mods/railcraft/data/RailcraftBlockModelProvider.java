package mods.railcraft.data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.google.gson.JsonElement;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.post.Column;
import mods.railcraft.world.level.block.post.Connection;
import mods.railcraft.world.level.block.post.PostBlock;
import net.minecraft.block.Block;
import net.minecraft.data.BlockModelDefinition;
import net.minecraft.data.BlockModelFields;
import net.minecraft.data.BlockModelFields.Rotation;
import net.minecraft.data.BlockModelWriter;
import net.minecraft.data.FinishedMultiPartBlockState;
import net.minecraft.data.IFinishedBlockState;
import net.minecraft.data.IMultiPartPredicateBuilder;
import net.minecraft.data.ModelTextures;
import net.minecraft.data.ModelsResourceUtil;
import net.minecraft.util.ResourceLocation;

public class RailcraftBlockModelProvider {

  private final Consumer<IFinishedBlockState> blockStateOutput;
  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;

  public RailcraftBlockModelProvider(Consumer<IFinishedBlockState> blockStateOutput,
      BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
    this.blockStateOutput = blockStateOutput;
    this.modelOutput = modelOutput;
  }

  public void run() {
    this.createPost(RailcraftBlocks.BLACK_METAL_POST.get());
    this.createPost(RailcraftBlocks.RED_METAL_POST.get());
    this.createPost(RailcraftBlocks.GREEN_METAL_POST.get());
    this.createPost(RailcraftBlocks.BROWN_METAL_POST.get());
    this.createPost(RailcraftBlocks.BLUE_METAL_POST.get());
    this.createPost(RailcraftBlocks.PURPLE_METAL_POST.get());
    this.createPost(RailcraftBlocks.CYAN_METAL_POST.get());
    this.createPost(RailcraftBlocks.LIGHT_GRAY_METAL_POST.get());
    this.createPost(RailcraftBlocks.GRAY_METAL_POST.get());
    this.createPost(RailcraftBlocks.PINK_METAL_POST.get());
    this.createPost(RailcraftBlocks.LIME_METAL_POST.get());
    this.createPost(RailcraftBlocks.YELLOW_METAL_POST.get());
    this.createPost(RailcraftBlocks.LIGHT_BLUE_METAL_POST.get());
    this.createPost(RailcraftBlocks.MAGENTA_METAL_POST.get());
    this.createPost(RailcraftBlocks.ORANGE_METAL_POST.get());
    this.createPost(RailcraftBlocks.WHITE_METAL_POST.get());
  }

  private void delegateItemModel(Block block, ResourceLocation modelLocation) {
    this.modelOutput.accept(ModelsResourceUtil.getModelLocation(block.asItem()),
        new BlockModelWriter(modelLocation));
  }

  private void createPost(Block block) {
    ModelTextures textures = ModelTextures.defaultTexture(block);
    ResourceLocation columnModel = Models.POST_COLUMN.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
    ResourceLocation doubleConnectionModel = Models.POST_DOUBLE_CONNECTION.create(block,
        textures, RailcraftBlockModelProvider.this.modelOutput);
    // ResourceLocation platformModel = RailcraftModels.POST_PLATFORM.create(block,
    // textures, RailcraftBlockModelProvider.this.modelOutput);
    ResourceLocation shortColumnModel = Models.POST_SHORT_COLUMN.create(block,
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
                    .term(PostBlock.COLUMN, Column.SHORT),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, shortColumnModel))
            .with(
                IMultiPartPredicateBuilder.condition()
                    .term(PostBlock.COLUMN, Column.NORMAL),
                BlockModelDefinition.variant()
                    .with(BlockModelFields.MODEL, columnModel))
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
}
