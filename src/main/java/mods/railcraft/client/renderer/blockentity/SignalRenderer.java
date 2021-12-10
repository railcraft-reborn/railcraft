package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public class SignalRenderer extends AbstractSignalRenderer<AbstractSignalBlockEntity> {

  @Override
  public void render(AbstractSignalBlockEntity blockEntity, float partialTicks,
      PoseStack matrixStack,
      MultiBufferSource renderTypeBuffer, int packedLight, int packedOverlay) {
    super.render(blockEntity, partialTicks, matrixStack, renderTypeBuffer, packedLight,
        packedOverlay);
    Direction direction = blockEntity.getBlockState().getValue(SignalBlock.FACING);
    SignalAspect signalAspect = blockEntity.getPrimarySignalAspect().getDisplayAspect();
    matrixStack.pushPose();
    {
      var zOffset = -0.175D;
      matrixStack.translate(zOffset * direction.getStepX(), 0.19D,
          zOffset * direction.getStepZ());
      this.renderSignalAspect(
          matrixStack, renderTypeBuffer, packedLight, packedOverlay, signalAspect, direction);
    }
    matrixStack.popPose();
  }
}
