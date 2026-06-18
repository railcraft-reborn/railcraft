package mods.railcraft.client.renderer.blockentity;

import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.renderer.RailcraftRenderTypes;
import mods.railcraft.client.renderer.blockentity.state.SteamTurbineRenderState;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.SteamTurbineBlock;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SteamTurbineRenderer implements BlockEntityRenderer<SteamTurbineBlockEntity, SteamTurbineRenderState> {

  @Override
  public SteamTurbineRenderState createRenderState() {
    return new SteamTurbineRenderState();
  }

  @Override
  public void extractRenderState(SteamTurbineBlockEntity  blockEntity,
      SteamTurbineRenderState renderState, float partialTick, Vec3 cameraPos,
      ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);

    var membership = blockEntity.getUnresolvedMembership().orElse(null);
    if (membership == null || membership.patternElement().marker() != 'W') {
      // not a gauge block
      return;
    }

    float xx = 0;
    float zz = 0;

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

    if (fx == 0 && fz == 0) {
      throw new IllegalStateException("can't detect gauge orientation");
    }

    // fix lightmap coords to use the brightness value in front of the block, not inside it (which
    // would be just 0)
    renderState.lightCoords = LevelRenderer.getLightCoords(blockEntity.getLevel(),
        blockEntity.getBlockPos().offset(fx, 0, fz));

    renderState.gaugeValue = blockEntity.getAndSmoothGaugeValue();
    renderState.xx = xx;
    renderState.rx = rx;
    renderState.fx = fx;
    renderState.zz = zz;
    renderState.rz = rz;
    renderState.fz = fz;
  }

  @Override
  public void submit(SteamTurbineRenderState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraState) {

    float halfWidth = 0.5F * RenderUtil.PIXEL; // half width of the needle
    float len = 0.26F; // length of the needle (along the center)
    float zOffset = RenderUtil.SCALED_PIXEL; // offset to prevent z-fighting

    // average the value over time to smooth the needle
    float value = state.gaugeValue;

    // set the needle angle between 45° (= 0%) and 135° (= 100%)
    float angle = (90 * value + 45) * Mth.DEG_TO_RAD;

    poseStack.pushPose();
    // move the origin to the center of the gauge
    poseStack.translate(state.xx + state.rx * 0.5 + state.fx * zOffset, 0.5, state.zz + state.rz * 0.5 + state.fz * zOffset);

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

    collector.submitCustomGeometry(poseStack, RailcraftRenderTypes.POSITION_COLOR_LIGHTMAP,
        (pose, vertexConsumer) -> {
          vertexConsumer
              .addVertex(pose, -state.rx * baseOffset, 0, -state.rz * baseOffset)
              .setColor(red, green, blue, alphaOne)
              .setUv(0.0F, 1.0F)
              .setLight(state.lightCoords);
          vertexConsumer
              .addVertex(pose, state.rx * baseOffset, 0, state.rz * baseOffset)
              .setColor(red, green, blue, alphaOne)
              .setUv(1.0F, 1.0F)
              .setLight(state.lightCoords);
          vertexConsumer
              .addVertex(pose, -state.rx * glx + state.rx * gwx, gly + gwy, -state.rz * glx + state.rz * gwx)
              .setColor(red, green, blue, alphaOne)
              .setUv(1.0F, 0.0F)
              .setLight(state.lightCoords);
          vertexConsumer
              .addVertex(pose, -state.rx * glx - state.rx * gwx, gly - gwy, -state.rz * glx - state.rz * gwx)
              .setColor(red, green, blue, alphaOne)
              .setUv(0.0F, 0.0F)
              .setLight(state.lightCoords);
    });
    poseStack.popPose();
  }
}
