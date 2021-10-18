package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.FluidLoaderBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class FluidLoaderRenderer extends FluidManipulatorRenderer<FluidLoaderBlockEntity> {

  private static final float PIPE_OFFSET = 5 * RenderUtil.PIXEL;
  public static final ResourceLocation PIPE_SIDE_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/fluid_loader/pipe_side");
  public static final ResourceLocation PIPE_END_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/fluid_loader/pipe_end");
  public static final CuboidModel pipeModel = new CuboidModel(PIPE_OFFSET, 0.0F, PIPE_OFFSET,
      1.0F - PIPE_OFFSET, RenderUtil.PIXEL, 1.0F - PIPE_OFFSET);

  public FluidLoaderRenderer(TileEntityRendererDispatcher renderer) {
    super(renderer);
  }

  @Override
  public void render(FluidLoaderBlockEntity blockEntity, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    super.render(blockEntity, partialTicks, matrixStack, renderTypeBuffer, packedLight,
        packedOverlay);
    Minecraft minecraft = Minecraft.getInstance();

    CuboidModel.Face sideFace = pipeModel.new Face()
        .setSprite(minecraft.getTextureAtlas(PlayerContainer.BLOCK_ATLAS)
            .apply(PIPE_SIDE_TEXTURE_LOCATION));
    pipeModel.set(Direction.NORTH, sideFace);
    pipeModel.set(Direction.SOUTH, sideFace);
    pipeModel.set(Direction.EAST, sideFace);
    pipeModel.set(Direction.WEST, sideFace);

    CuboidModel.Face endFace = pipeModel.new Face()
        .setSprite(minecraft.getTextureAtlas(PlayerContainer.BLOCK_ATLAS)
            .apply(PIPE_END_TEXTURE_LOCATION));
    pipeModel.set(Direction.UP, endFace);
    pipeModel.set(Direction.DOWN, endFace);

    pipeModel.setPackedLight(packedLight);
    pipeModel.setPackedOverlay(packedOverlay);

    matrixStack.pushPose();
    {
      pipeModel.setMinY(RenderUtil.PIXEL - blockEntity.getPipeLength(partialTicks));
      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(RenderType.entityCutout(PlayerContainer.BLOCK_ATLAS));
      CuboidModelRenderer.render(pipeModel, matrixStack, vertexBuilder, 0xFFFFFFFF,
          CuboidModelRenderer.FaceDisplay.BOTH, false);
    }
    matrixStack.popPose();
  }
}
