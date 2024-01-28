package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.manipulator.FluidManipulatorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class FluidManipulatorRenderer<T extends FluidManipulatorBlockEntity>
    implements BlockEntityRenderer<T> {

  public static final ResourceLocation INTERIOR_TEXTURE_LOCATION =
      RailcraftConstants.rl("entity/fluid_manipulator/interior");

  private static final CuboidModel interiorModel =
      new CuboidModel(0.011F, 0.01F, 0.011F, 0.989F, 0.99F, 0.989F);

  @Override
  public void render(T blockEntity, float partialTick,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
      int packedOverlay) {
    interiorModel.setAll(interiorModel.new Face()
        .setSprite(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(INTERIOR_TEXTURE_LOCATION)));
    interiorModel.setPackedLight(packedLight);
    interiorModel.setPackedOverlay(packedOverlay);

    var consumer = bufferSource.getBuffer(Sheets.cutoutBlockSheet());
    CuboidModelRenderer.render(interiorModel, poseStack, consumer,
        0xFFFFFFFF, CuboidModelRenderer.FaceDisplay.FRONT, true);

    var tank = blockEntity.getTankManager().get(0);
    var fluidStack = tank.getFluid();
    if (fluidStack != null && fluidStack.getAmount() > 0) {

      float capacity = tank.getCapacity();
      var level = Math.min(fluidStack.getAmount() / capacity , 1);
      var fluidMaxY = fluidStack.getFluid().getFluidType().isLighterThanAir()
          ? 1
          : level;

      var model = FluidRenderer.getFluidModel(fluidStack, 1 - (RenderUtil.SCALED_PIXEL * 2),
          fluidMaxY - (RenderUtil.SCALED_PIXEL * 2), 1 - (RenderUtil.SCALED_PIXEL * 2),
          FluidRenderer.FluidType.STILL);
      if (model != null) {
        poseStack.pushPose();
        poseStack.translate(RenderUtil.SCALED_PIXEL, RenderUtil.SCALED_PIXEL,
            RenderUtil.SCALED_PIXEL);
        model.setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
        model.setPackedOverlay(packedOverlay);
        CuboidModelRenderer.render(model, poseStack, consumer,
            RenderUtil.getColorARGB(fluidStack, 1),
            CuboidModelRenderer.FaceDisplay.FRONT, true);
        poseStack.popPose();
      }
    }
  }
}
