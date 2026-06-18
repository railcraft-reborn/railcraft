package mods.railcraft.client.renderer.blockentity;

import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.renderer.blockentity.state.SignalRenderState;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBlock;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public class SignalRenderer extends AbstractSignalRenderer<AbstractSignalBlockEntity, SignalRenderState> {

  @Override
  public SignalRenderState createRenderState() {
    return new SignalRenderState();
  }

  @Override
  public void extractRenderState(AbstractSignalBlockEntity blockEntity,
      SignalRenderState renderState, float partialTick, Vec3 cameraPos,
      ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
    super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);
    renderState.direction =
        blockEntity.getBlockState().getValue(SignalBlock.FACING);
    renderState.signalAspect = blockEntity.getPrimarySignalAspect().getDisplayAspect();
  }

  @Override
  public void submit(SignalRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState) {
    super.submit(state, poseStack, collector, cameraState);
    var direction = state.direction;
    var signalAspect = state.signalAspect;

    poseStack.pushPose();
    var zOffset = -0.175D;
    poseStack.translate(zOffset * direction.getStepX(), 0.19D, zOffset * direction.getStepZ());
    this.renderSignalAspect(state, poseStack, collector, cameraState, signalAspect, direction);
    poseStack.popPose();
  }
}
