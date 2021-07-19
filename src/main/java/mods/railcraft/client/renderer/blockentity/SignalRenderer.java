package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.world.level.block.AbstractSignalBlock;
import mods.railcraft.world.level.block.entity.AbstractSignalBlockEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class SignalRenderer extends AbstractSignalRenderer<AbstractSignalBlockEntity> {

  public SignalRenderer(TileEntityRendererDispatcher disptacher) {
    super(disptacher);
  }

  @Override
  public void render(AbstractSignalBlockEntity tile, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    super.render(tile, partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    Direction direction = tile.getBlockState().getValue(AbstractSignalBlock.FACING);
    SignalAspect aspect = tile.getSignalAspect().getDisplayAspect();
    matrixStack.pushPose();
    {
      double zOffset = -0.175D;
      matrixStack.translate(zOffset * direction.getStepX(), 0.19D,
          zOffset * direction.getStepZ());
      this.renderSignalAspect(
          matrixStack, renderTypeBuffer, packedLight, packedOverlay, aspect, direction);
    }
    matrixStack.popPose();
  }
}
