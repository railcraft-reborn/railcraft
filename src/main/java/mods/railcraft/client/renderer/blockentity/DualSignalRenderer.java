package mods.railcraft.client.renderer.blockentity;

import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.renderer.blockentity.state.DualSignalRenderState;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualSignalBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBlock;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class DualSignalRenderer<T extends AbstractSignalBlockEntity & DualSignalBlockEntity>
    extends AbstractSignalRenderer<T, DualSignalRenderState> {

  @Override
  public DualSignalRenderState createRenderState() {
    return new DualSignalRenderState();
  }

  @Override
  public void extractRenderState(T blockEntity, DualSignalRenderState renderState,
      float partialTick, Vec3 cameraPos,
      @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);
    renderState.direction =
        blockEntity.getBlockState().getValue(SignalBlock.FACING);
    renderState.primarySignalAspect = blockEntity.getPrimarySignalAspect().getDisplayAspect();
    renderState.secondarySignalAspect = blockEntity.getSecondarySignalAspect().getDisplayAspect();
  }

  @Override
  public void submit(DualSignalRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState) {
    super.submit(state, poseStack, collector, cameraState);
    var direction = state.direction;

    poseStack.pushPose();
    {
      double zOffset = -0.175D;
      poseStack.translate(zOffset * direction.getStepX(), 0.0D,
          zOffset * direction.getStepZ());

      poseStack.pushPose();
      {
        poseStack.translate(0, 0.19F, 0);
        this.renderSignalAspect(state, poseStack, collector, cameraState,
            state.primarySignalAspect, direction);
      }
      poseStack.popPose();

      poseStack.pushPose();
      {
        poseStack.translate(0, -0.19F, 0);
        this.renderSignalAspect(state, poseStack, collector, cameraState,
            state.secondarySignalAspect, direction);
      }
      poseStack.popPose();
    }
    poseStack.popPose();
  }
}
