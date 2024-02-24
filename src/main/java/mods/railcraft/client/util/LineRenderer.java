package mods.railcraft.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;

public interface LineRenderer {
  default void renderLine(PoseStack poseStack, int color, Vec3 from, Vec3 to) {
    renderLine(poseStack,
        RenderUtil.getRed(color), RenderUtil.getGreen(color), RenderUtil.getBlue(color), 1,
        (float) from.x, (float) from.y, (float) from.z,
        (float) to.x,   (float) to.y,   (float) to.z
    );
  }

  void renderLine(PoseStack poseStack, float r, float g, float b, float a, float x0, float y0, float z0, float x1, float y1, float z1);
}
