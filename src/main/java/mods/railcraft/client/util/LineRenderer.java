package mods.railcraft.client.util;

import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;

public interface LineRenderer {

  static LineRenderer simple(MultiBufferSource bufferSource) {
    return new SimpleLineRenderer(bufferSource);
  }

  default void renderLine(PoseStack poseStack, int color, Vec3 from, Vec3 to) {
    this.renderLine(poseStack,
        FastColor.ARGB32.red(color),
        FastColor.ARGB32.green(color),
        FastColor.ARGB32.blue(color),
        FastColor.ARGB32.alpha(color),
        (float) from.x, (float) from.y, (float) from.z,
        (float) to.x, (float) to.y, (float) to.z);
  }

  default void renderLine(PoseStack poseStack, int color, Vector3f from, Vector3f to) {
    this.renderLine(poseStack,
        FastColor.ARGB32.red(color),
        FastColor.ARGB32.green(color),
        FastColor.ARGB32.blue(color),
        FastColor.ARGB32.alpha(color),
        from.x, from.y, from.z,
        to.x, to.y, to.z);
  }

  void renderLine(PoseStack poseStack, int r, int g, int b, int a, float x0, float y0, float z0,
      float x1, float y1, float z1);
}
