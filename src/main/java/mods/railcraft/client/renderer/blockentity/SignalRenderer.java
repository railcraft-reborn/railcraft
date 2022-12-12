package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBlock;
import net.minecraft.client.renderer.MultiBufferSource;

public class SignalRenderer extends AbstractSignalRenderer<AbstractSignalBlockEntity> {

  @Override
  public void render(AbstractSignalBlockEntity blockEntity, float partialTick,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    var direction = blockEntity.getBlockState().getValue(SignalBlock.FACING);
    var signalAspect = blockEntity.getPrimarySignalAspect().getDisplayAspect();
    poseStack.pushPose();
    {
      var zOffset = -0.175D;
      poseStack.translate(zOffset * direction.getStepX(), 0.19D,
          zOffset * direction.getStepZ());
      this.renderSignalAspect(
          poseStack, bufferSource, packedLight, packedOverlay, signalAspect, direction);
    }
    poseStack.popPose();
  }
}
