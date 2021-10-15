package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.signal.AbstractSignalBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class SignalRenderer extends AbstractSignalRenderer<AbstractSignalBlockEntity> {

  public SignalRenderer(TileEntityRendererDispatcher disptacher) {
    super(disptacher);
  }

  @Override
  public void render(AbstractSignalBlockEntity blockEntity, float partialTicks,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    super.render(blockEntity, partialTicks, matrixStack, renderTypeBuffer, packedLight,
        packedOverlay);
    Direction direction = blockEntity.getBlockState().getValue(AbstractSignalBlock.FACING);
    SignalAspect signalAspect = blockEntity.getPrimarySignalAspect().getDisplayAspect();
    matrixStack.pushPose();
    {
      double zOffset = -0.175D;
      matrixStack.translate(zOffset * direction.getStepX(), 0.19D,
          zOffset * direction.getStepZ());
      this.renderSignalAspect(
          matrixStack, renderTypeBuffer, packedLight, packedOverlay, signalAspect, direction);
    }
    matrixStack.popPose();
  }
}
