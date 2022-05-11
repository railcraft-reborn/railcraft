package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class TankRenderer implements BlockEntityRenderer<TankBlockEntity> {

  @Override
  public void render(TankBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    if (!blockEntity.isMaster()) {
      return;
    }

    var consumer = bufferSource.getBuffer(Sheets.cutoutBlockSheet());
    var maxY = blockEntity.getMaxY();

    var tank = blockEntity.getModule().getTank();
    var fluidStack = tank.getFluid();

    if (!fluidStack.isEmpty()) {
      poseStack.translate(RenderUtil.SCALED_PIXEL - 1.0F, 1.0F, RenderUtil.SCALED_PIXEL - 1.0F);

      final var twoPixels = RenderUtil.SCALED_PIXEL * 2.0F;
      var fluidMaxX = blockEntity.getMaxX() - twoPixels;
      var fluidMaxZ = blockEntity.getMaxZ() - twoPixels;

      float capacity = tank.getCapacity();
      var level = Math.min(fluidStack.getAmount() / capacity, 1.0F);
      var fluidMaxY = (maxY - 2.0F) * level;

      var model = FluidRenderer.getFluidModel(fluidStack, fluidMaxX, fluidMaxY, fluidMaxZ,
          FluidRenderer.FluidType.STILL);

      if (model != null) {
        poseStack.pushPose();
        {
          model.setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
          model.setPackedOverlay(packedOverlay);
          CuboidModelRenderer.render(model, poseStack, consumer,
              RenderUtil.getColorARGB(fluidStack, 1.0F),
              CuboidModelRenderer.FaceDisplay.FRONT, true);
        }
        poseStack.popPose();
      }
    }
  }
}
