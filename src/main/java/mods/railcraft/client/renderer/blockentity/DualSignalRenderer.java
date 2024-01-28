package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualSignalBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBlock;
import net.minecraft.client.renderer.MultiBufferSource;

public final class DualSignalRenderer<T extends AbstractSignalBlockEntity & DualSignalBlockEntity>
    extends AbstractSignalRenderer<T> {

  @Override
  public void render(
      T blockEntity, float partialTick, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight,
        packedOverlay);

    var direction = blockEntity.getBlockState().getValue(SignalBlock.FACING);
    poseStack.pushPose();
    {
      double zOffset = -0.175D;
      poseStack.translate(zOffset * direction.getStepX(), 0.0D,
          zOffset * direction.getStepZ());

      var signalAspect = blockEntity.getPrimarySignalAspect().getDisplayAspect();
      poseStack.pushPose();
      {
        poseStack.translate(0, 0.19F, 0);
        this.renderSignalAspect(
            poseStack, bufferSource, packedLight, packedOverlay, signalAspect, direction);
      }
      poseStack.popPose();

      poseStack.pushPose();
      {
        poseStack.translate(0, -0.19F, 0);
        signalAspect = blockEntity.getSecondarySignalAspect().getDisplayAspect();
        this.renderSignalAspect(
            poseStack, bufferSource, packedLight, packedOverlay, signalAspect, direction);
      }
      poseStack.popPose();
    }
    poseStack.popPose();
  }
}
