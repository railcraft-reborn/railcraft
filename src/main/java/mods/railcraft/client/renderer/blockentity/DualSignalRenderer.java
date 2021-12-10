package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualSignalBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public final class DualSignalRenderer<T extends AbstractSignalBlockEntity & DualSignalBlockEntity>
    extends AbstractSignalRenderer<T> {

  @Override
  public void render(
      T blockEntity, float partialTicks, PoseStack matrixStack,
      MultiBufferSource renderTypeBuffer, int packedLight, int packedOverlay) {
    super.render(blockEntity, partialTicks, matrixStack, renderTypeBuffer, packedLight,
        packedOverlay);

    Direction direction = blockEntity.getBlockState().getValue(SignalBlock.FACING);
    matrixStack.pushPose();
    {
      double zOffset = -0.175D;
      matrixStack.translate(zOffset * direction.getStepX(), 0.0D,
          zOffset * direction.getStepZ());

      SignalAspect signalAspect = blockEntity.getPrimarySignalAspect().getDisplayAspect();
      matrixStack.pushPose();
      {
        matrixStack.translate(0.0D, 0.19D, 0.0D);
        this.renderSignalAspect(
            matrixStack, renderTypeBuffer, packedLight, packedOverlay, signalAspect, direction);
      }
      matrixStack.popPose();

      matrixStack.pushPose();
      {
        matrixStack.translate(0.0D, -0.19D, 0.0D);
        signalAspect = blockEntity.getSecondarySignalAspect().getDisplayAspect();
        this.renderSignalAspect(
            matrixStack, renderTypeBuffer, packedLight, packedOverlay, signalAspect, direction);
      }
      matrixStack.popPose();
    }
    matrixStack.popPose();
  }
}
