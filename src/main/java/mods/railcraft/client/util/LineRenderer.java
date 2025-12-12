package mods.railcraft.client.util;

import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

public interface LineRenderer {

  static LineRenderer simple(MultiBufferSource bufferSource) {
    return new SimpleLineRenderer(bufferSource.getBuffer(RenderTypes.lines()));
  }

  static LineRenderer simple(VertexConsumer vertexConsumer) {
    return new SimpleLineRenderer(vertexConsumer);
  }

  default void renderLine(PoseStack poseStack, int color, Vec3 from, Vec3 to) {
    poseStack.pushPose();
    this.renderLine(poseStack.last(),
        ARGB.red(color),
        ARGB.green(color),
        ARGB.blue(color),
        ARGB.alpha(color),
        (float) from.x, (float) from.y, (float) from.z,
        (float) to.x, (float) to.y, (float) to.z);
    poseStack.popPose();
  }

  default void renderLine(PoseStack poseStack, int color, Vector3f from, Vector3f to) {
    poseStack.pushPose();
    this.renderLine(poseStack.last(),
        ARGB.red(color),
        ARGB.green(color),
        ARGB.blue(color),
        ARGB.alpha(color),
        from.x, from.y, from.z,
        to.x, to.y, to.z);
    poseStack.popPose();
  }

  void renderLine(PoseStack.Pose pose, int r, int g, int b, int a, float x0, float y0, float z0,
      float x1, float y1, float z1);
}
