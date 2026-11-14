package mods.railcraft.client.util;

import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

class SimpleLineRenderer implements LineRenderer {

  private final VertexConsumer vertexConsumer;

  SimpleLineRenderer(VertexConsumer vertexConsumer) {
    this.vertexConsumer = vertexConsumer;
  }

  @Override
  public void renderLine(PoseStack.Pose pose, int red, int green, int blue, int alpha,
      float x0, float y0, float z0, float x1, float y1, float z1) {
    // The lines shader expands each segment along its normal, so the normal must be the line
    // direction, and every vertex needs a line width.
    var normal = new Vector3f(x1 - x0, y1 - y0, z1 - z0);
    if (normal.lengthSquared() == 0) {
      return;
    }
    normal.normalize();
    float width = Minecraft.getInstance().gameRenderer.getGameRenderState()
        .windowRenderState.appropriateLineWidth;

    this.vertexConsumer
        .addVertex(pose, x0, y0, z0)
        .setColor(red, green, blue, alpha)
        .setNormal(pose, normal)
        .setLineWidth(width);
    this.vertexConsumer
        .addVertex(pose, x1, y1, z1)
        .setColor(red, green, blue, alpha)
        .setNormal(pose, normal)
        .setLineWidth(width);
  }
}
