package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.api.signals.DualLamp;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.IDualSignal;
import mods.railcraft.world.level.block.signal.AbstractSignalBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public final class DualSignalRenderer<T extends AbstractSignalBlockEntity & IDualSignal>
    extends AbstractSignalRenderer<T> {

  public DualSignalRenderer(TileEntityRendererDispatcher disptacher) {
    super(disptacher);
  }

  @Override
  public void render(
      T blockEntity, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    super.render(blockEntity, partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);

    Direction direction = blockEntity.getBlockState().getValue(AbstractSignalBlock.FACING);
    matrixStack.pushPose();
    {
      double zOffset = -0.175D;
      matrixStack.translate(zOffset * direction.getStepX(), 0.0D,
          zOffset * direction.getStepZ());

      SignalAspect aspect = blockEntity.getSignalAspect(DualLamp.TOP).getDisplayAspect();
      matrixStack.pushPose();
      {
        matrixStack.translate(0.0D, 0.19D, 0.0D);
        this.renderSignalAspect(
            matrixStack, renderTypeBuffer, packedLight, packedOverlay, aspect, direction);
      }
      matrixStack.popPose();

      matrixStack.pushPose();
      {
        matrixStack.translate(0.0D, -0.19D, 0.0D);
        aspect = blockEntity.getSignalAspect(DualLamp.BOTTOM).getDisplayAspect();
        this.renderSignalAspect(
            matrixStack, renderTypeBuffer, packedLight, packedOverlay, aspect, direction);
      }
      matrixStack.popPose();
    }
    matrixStack.popPose();
  }
}
