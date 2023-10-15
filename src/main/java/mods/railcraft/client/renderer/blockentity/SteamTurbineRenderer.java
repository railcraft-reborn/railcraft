package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.renderer.RailcraftRenderTypes;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.SteamTurbineBlock;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.util.Mth;

public class SteamTurbineRenderer implements BlockEntityRenderer<SteamTurbineBlockEntity> {

  @Override
  public void render(SteamTurbineBlockEntity blockEntity, float partialTick, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    var membership = blockEntity.getUnresolvedMembership().orElse(null);
    if (membership == null || membership.patternElement().marker() != 'W') {
      // not a gauge block
      return;
    }

    float xx = 0;
    float zz = 0;

    float halfWidth = 0.5F * RenderUtil.PIXEL; // half width of the needle
    float len = 0.26F; // length of the needle (along the center)
    float zOffset = RenderUtil.SCALED_PIXEL; // offset to prevent z-fighting

    // average the value over time to smooth the needle
    float value = blockEntity.getAndSmoothGaugeValue();

    // set the needle angle between 45° (= 0%) and 135° (= 100%)
    float angle = (90 * value + 45) * Mth.DEG_TO_RAD;

    int fx = 0, fz = 0; // vector towards the front of the gauge
    int rx = 0, rz = 0; // vector to the right when looking at the gauge

    var patternPos = membership.patternElement().relativePos();

    if (blockEntity.getBlockState().getValue(SteamTurbineBlock.ROTATED)) {
      if (patternPos.getX() == 0) {
        fx = -1;
        rz = 1;
      } else if (patternPos.getX() == 1) {
        xx++;
        zz++;
        fx = 1;
        rz = -1;
      } 
    } else {
      if (patternPos.getZ() == 0) {
        xx++;
        fz = -1;
        rx = -1;
      } else if (patternPos.getZ() == 1) {
        zz++;
        fz = 1;
        rx = 1;
      }
    }

    if (fx == 0 && fz == 0 || rx == 0 && rz == 0)
      throw new IllegalStateException("can't detect gauge orientation");

    // fix lightmap coords to use the brightness value in front of the block, not inside it (which
    // would be just 0)
    packedLight = LevelRenderer.getLightColor(blockEntity.getLevel(),
        blockEntity.getBlockPos().offset(fx, 0, fz));

    var vertexBuffer = bufferSource.getBuffer(RailcraftRenderTypes.POSITION_COLOR_LIGHTMAP);

    poseStack.pushPose();
    // move the origin to the center of the gauge
    poseStack.translate(xx + rx * 0.5 + fx * zOffset, 0.5, zz + rz * 0.5 + fz * zOffset);

    var matrix = poseStack.last().pose();

    float cosA = Mth.cos(angle);
    float sinA = Mth.sin(angle);

    // displacement along the length of the needle
    float glx = cosA * len;
    float gly = sinA * len;

    // displacement along the width of the needle
    float gwx = sinA * halfWidth;
    float gwy = cosA * halfWidth;

    // half width of the horizontal needle part where it connects to the "case"
    float baseOffset = 1.0F / Mth.sin(angle) * halfWidth;

    // set the needle color to dark-ish red
    int red = 100;
    int green = 0;
    int blue = 0;
    int alphaOne = 255;

    vertexBuffer
        .vertex(matrix, -rx * baseOffset, 0, -rz * baseOffset)
        .color(red, green, blue, alphaOne)
        .uv2(packedLight)
        .endVertex();
    vertexBuffer
        .vertex(matrix, rx * baseOffset, 0, rz * baseOffset)
        .color(red, green, blue, alphaOne)
        .uv2(packedLight)
        .endVertex();
    vertexBuffer
        .vertex(matrix, -rx * glx + rx * gwx, gly + gwy, -rz * glx + rz * gwx)
        .color(red, green, blue, alphaOne)
        .uv2(packedLight)
        .endVertex();
    vertexBuffer
        .vertex(matrix, -rx * glx - rx * gwx, gly - gwy, -rz * glx - rz * gwx)
        .color(red, green, blue, alphaOne)
        .uv2(packedLight)
        .endVertex();
    poseStack.popPose();
  }
}
