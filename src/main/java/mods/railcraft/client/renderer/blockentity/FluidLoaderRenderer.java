package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.Railcraft;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.manipulator.FluidLoaderBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class FluidLoaderRenderer extends FluidManipulatorRenderer<FluidLoaderBlockEntity> {

  private static final float PIPE_OFFSET = 5 * RenderUtil.PIXEL;
  public static final ResourceLocation PIPE_SIDE_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/fluid_loader/pipe_side");
  public static final ResourceLocation PIPE_END_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/fluid_loader/pipe_end");
  public static final CuboidModel pipeModel = new CuboidModel(PIPE_OFFSET, 0.0F, PIPE_OFFSET,
      1.0F - PIPE_OFFSET, RenderUtil.PIXEL, 1.0F - PIPE_OFFSET);

  @Override
  public void render(FluidLoaderBlockEntity blockEntity, float partialTicks,
      PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int packedLight,
      int packedOverlay) {
    super.render(blockEntity, partialTicks, matrixStack, renderTypeBuffer, packedLight,
        packedOverlay);
    Minecraft minecraft = Minecraft.getInstance();

    CuboidModel.Face sideFace = pipeModel.new Face()
        .setSprite(minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(PIPE_SIDE_TEXTURE_LOCATION));
    pipeModel.set(Direction.NORTH, sideFace);
    pipeModel.set(Direction.SOUTH, sideFace);
    pipeModel.set(Direction.EAST, sideFace);
    pipeModel.set(Direction.WEST, sideFace);

    CuboidModel.Face endFace = pipeModel.new Face()
        .setSprite(minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(PIPE_END_TEXTURE_LOCATION));
    pipeModel.set(Direction.UP, endFace);
    pipeModel.set(Direction.DOWN, endFace);

    pipeModel.setPackedLight(packedLight);
    pipeModel.setPackedOverlay(packedOverlay);

    matrixStack.pushPose();
    {
      pipeModel.setMinY(RenderUtil.PIXEL - blockEntity.getPipeLength(partialTicks));
      VertexConsumer vertexBuilder =
          renderTypeBuffer.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
      CuboidModelRenderer.render(pipeModel, matrixStack, vertexBuilder, 0xFFFFFFFF,
          CuboidModelRenderer.FaceDisplay.BOTH, false);
    }
    matrixStack.popPose();
  }
}
