package mods.railcraft.client.renderer.blockentity;

import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.renderer.blockentity.state.TankBlockRenderState;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TankRenderer implements BlockEntityRenderer<TankBlockEntity, TankBlockRenderState> {

  @Override
  public boolean shouldRenderOffScreen() {
    return true;
  }

  @Override
  public boolean shouldRender(TankBlockEntity blockEntity, Vec3 cameraPos) {
    return blockEntity.isMaster() && BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
  }

  @Override
  public TankBlockRenderState createRenderState() {
    return new TankBlockRenderState();
  }

  @Override
  public void extractRenderState(TankBlockEntity blockEntity,
      TankBlockRenderState renderState, float partialTick, Vec3 cameraPos,
      @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);

    final float twoPixels = RenderUtil.SCALED_PIXEL * 2.0F;
    renderState.fluidMaxX = blockEntity.getMaxX() - twoPixels;
    renderState.fluidMaxZ = blockEntity.getMaxZ() - twoPixels;
    renderState.maxY = blockEntity.getMaxY();
    renderState.tank = blockEntity.getModule().getTank();
  }

  @Override
  public void submit(TankBlockRenderState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraState) {
    var fluidStack = state.tank.getFluid();
    if (fluidStack.isEmpty()) {
      return;
    }
    poseStack.translate(RenderUtil.SCALED_PIXEL - 1.0F, 1.0F, RenderUtil.SCALED_PIXEL - 1.0F);

    float capacity = state.tank.getCapacity();
    var level = Math.min(fluidStack.getAmount() / capacity, 1.0F);
    var fluidMaxY = (state.maxY - 2.0F) * level;

    var model = FluidRenderer.getFluidModel(fluidStack, state.fluidMaxX, fluidMaxY,
        state.fluidMaxZ, FluidRenderer.FluidType.STILL);

    poseStack.pushPose();
    model.setPackedLight(RenderUtil.calculateGlowLight(state.lightCoords, fluidStack));
    model.setPackedOverlay(OverlayTexture.NO_OVERLAY);
    collector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, vertexConsumer) -> {
      CuboidModelRenderer.render(model, pose, vertexConsumer,
          RenderUtil.getColorARGB(fluidStack, 1.0F),
          CuboidModelRenderer.FaceDisplay.FRONT, true);
    });
    poseStack.popPose();
  }

  @Override
  public AABB getRenderBoundingBox(TankBlockEntity blockEntity) {
    return AABB.INFINITE;
  }
}
