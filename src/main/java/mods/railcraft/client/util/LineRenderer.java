package mods.railcraft.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public interface LineRenderer {
  static LineRenderer create(MultiBufferSource bufferSource) {
    return new SimpleLineRenderer(bufferSource);
  }

  default void renderLine(PoseStack poseStack, int color, Vec3 from, Vec3 to) {
    renderLine(poseStack,
        RenderUtil.getRed(color), RenderUtil.getGreen(color), RenderUtil.getBlue(color),
        (float) from.x, (float) from.y, (float) from.z,
        (float) to.x,   (float) to.y,   (float) to.z
    );
  }

  default void renderLine(PoseStack poseStack, int color, Vector3f from, Vector3f to) {
    renderLine(poseStack,
        RenderUtil.getRed(color), RenderUtil.getGreen(color), RenderUtil.getBlue(color),
        from.x, from.y, from.z,
        to.x,   to.y,   to.z
    );
  }

  void renderLine(PoseStack poseStack, float r, float g, float b, float x0, float y0, float z0, float x1, float y1, float z1);
}
