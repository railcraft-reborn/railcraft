package mods.railcraft.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class SimpleLineRenderer implements LineRenderer {
  private final VertexConsumer consumer;

  public SimpleLineRenderer(MultiBufferSource bufferSource) {
    consumer = bufferSource.getBuffer(RenderType.lines());
  }

  public void renderLine(PoseStack poseStack, float r, float g, float b, float a, float x0, float y0, float z0, float x1, float y1, float z1) {
    poseStack.pushPose();
    var matrix = poseStack.last().pose();
    var normal = poseStack.last().normal();
    consumer
        .vertex(matrix, x0, y0, z0)
        .color(r, g, b, a)
        .normal(normal, 0, 0, 0)
        .endVertex();
    consumer
        .vertex(matrix, x1, y1, z1)
        .color(r, g, b, a)
        .normal(normal, 0, 0, 0)
        .endVertex();
    poseStack.popPose();
  }
}
