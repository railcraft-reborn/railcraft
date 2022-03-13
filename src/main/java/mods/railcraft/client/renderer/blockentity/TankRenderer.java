package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.TankGaugeBlock;
import mods.railcraft.world.level.block.entity.multiblock.TankBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class TankRenderer implements BlockEntityRenderer<TankBlockEntity> {

  @Override
  public void render(TankBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

    var consumer = bufferSource.getBuffer(Sheets.cutoutBlockSheet());


    if (blockEntity.getBlockState().getBlock() instanceof TankGaugeBlock) {

    }
    
//    System.out.println(blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));

    if (blockEntity.isMaster()) {
      var maxY = blockEntity.getMaxY();

      var tank = blockEntity.getModule().getTank();
      var fluidStack = tank.getFluid();

      if (!fluidStack.isEmpty()) {
        poseStack.translate(0F, 1.0F, 0F);

        var fluidMaxX = blockEntity.getMaxX() - 2.0F;
        var fluidMaxZ = blockEntity.getMaxZ() - 2.0F;

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
}
