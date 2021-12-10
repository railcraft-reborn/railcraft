package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.Railcraft;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;

public class FluidManipulatorRenderer<T extends FluidManipulatorBlockEntity>
    implements BlockEntityRenderer<T> {

  public static final ResourceLocation INTERIOR_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/fluid_manipulator/interior");

  private static final CuboidModel interiorModel =
      new CuboidModel(0.011F, 0.01F, 0.011F, 0.989F, 0.99F, 0.989F);

  @Override
  public void render(T blockEntity, float partialTicks,
      PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int packedLight,
      int packedOverlay) {
    interiorModel.setAll(interiorModel.new Face()
        .setSprite(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(INTERIOR_TEXTURE_LOCATION)));
    interiorModel.setPackedLight(packedLight);
    interiorModel.setPackedOverlay(packedOverlay);

    VertexConsumer builder = renderTypeBuffer.getBuffer(Sheets.cutoutBlockSheet());
    CuboidModelRenderer.render(interiorModel, matrixStack, builder,
        0xFFFFFFFF, CuboidModelRenderer.FaceDisplay.FRONT, true);

    StandardTank tank = blockEntity.getTankManager().get(0);
    FluidStack fluidStack = tank.getFluid();
    if (fluidStack != null && fluidStack.getAmount() > 0) {

      float capacity = tank.getCapacity();
      float level = Math.min(fluidStack.getAmount(), capacity) / capacity;
      int stage = fluidStack.getFluid().getAttributes().isGaseous(fluidStack)
          ? FluidRenderer.STAGES - 1
          : Math.min(FluidRenderer.STAGES - 1, (int) (level * (FluidRenderer.STAGES - 1)));

      CuboidModel model =
          FluidRenderer.getFluidModel(fluidStack, stage, FluidRenderer.FluidType.STILL);
      if (model != null) {
        matrixStack.pushPose();
        {
          matrixStack.translate(0.0D, RenderUtil.PIXEL / 16.0D, 0.0D);
          model.setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
          model.setPackedOverlay(packedOverlay);
          CuboidModelRenderer.render(model, matrixStack, builder,
              RenderUtil.getColorARGB(fluidStack, level),
              CuboidModelRenderer.FaceDisplay.FRONT, true);
        }
        matrixStack.popPose();
      }
    }
  }
}
