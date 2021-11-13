package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.SimpleCubeModel;
import mods.railcraft.client.model.SimpleTexturedModel;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.cart.TankMinecartEntity;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidStack;

public class TankMinecartRenderer extends ContentModelMinecartRenderer<TankMinecartEntity> {

  private static final LowSidesMinecartModel<TankMinecartEntity> BODY_MODEL =
      new LowSidesMinecartModel<>();
  private static final LowSidesMinecartModel<TankMinecartEntity> SNOW_MODEL =
      new LowSidesMinecartModel<>(0.125F);
  private static final SimpleCubeModel TANK_MODEL =
      Util.make(new SimpleCubeModel(RenderType::entityTranslucentCull), model -> model.setTexture(
          new ResourceLocation(Railcraft.ID, "textures/entity/minecart/tank.png")));

  public TankMinecartRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void renderContents(TankMinecartEntity cart, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, float red,
      float green, float blue, float alpha) {
    super.renderContents(cart, partialTicks, matrixStack, renderTypeBuffer, packedLight, red, green,
        blue, alpha);
    this.renderTank(cart, partialTicks, matrixStack, renderTypeBuffer, packedLight, red, green,
        blue, alpha);
    if (cart.hasFilter()) {
      this.renderFilterItem(cart, partialTicks, matrixStack, renderTypeBuffer, packedLight, red,
          green, blue, alpha);
    }
  }

  private void renderTank(TankMinecartEntity cart, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, float red,
      float green, float blue, float alpha) {
    StandardTank tank = cart.getTankManager().get(0);
    if (tank != null) {
      FluidStack fluidStack = tank.getFluid();
      float capacity = tank.getCapacity();
      if (capacity > 0 && fluidStack != null && fluidStack.getAmount() > 0) {
        matrixStack.pushPose();
        {
          matrixStack.translate(-0.5F, -0.5F, -0.5F);
          matrixStack.translate(0.0D, RenderUtil.PIXEL / 16.0D, 0.0D);

          float level = Math.min(fluidStack.getAmount() / capacity, capacity);
          int stage = fluidStack.getFluid().getAttributes().isGaseous(fluidStack)
              ? FluidRenderer.STAGES - 1
              : Math.min(FluidRenderer.STAGES - 1, (int) (level * (FluidRenderer.STAGES - 1)));

          CuboidModel fluidModel =
              FluidRenderer.getFluidModel(fluidStack, stage, FluidRenderer.FluidType.STILL);
          if (fluidModel != null) {
            fluidModel.setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
            fluidModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
            IVertexBuilder builder = renderTypeBuffer.getBuffer(Atlases.cutoutBlockSheet());
            CuboidModelRenderer.render(fluidModel, matrixStack, builder,
                RenderUtil.getColorARGB(fluidStack, level),
                CuboidModelRenderer.FaceDisplay.FRONT, true);
          }
        }
        matrixStack.popPose();

        if (cart.isFilling()) {
          matrixStack.pushPose();
          {
            matrixStack.translate(-0.5F, -0.5F, -0.5F);
            matrixStack.translate(0.0D, RenderUtil.PIXEL / 16.0D, 0.0D);

            float scale = 6F / 16F;
            matrixStack.translate(0.5F, 0F, 0.5F);
            matrixStack.scale(scale, 1F, scale);
            matrixStack.translate(-0.5F, 0F, -0.5F);

            CuboidModel fillingFluidModel =
                FluidRenderer.getFluidModel(fluidStack, FluidRenderer.STAGES,
                    FluidRenderer.FluidType.FLOWING);
            if (fillingFluidModel != null) {
              fillingFluidModel
                  .setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
              fillingFluidModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
              IVertexBuilder builder = renderTypeBuffer.getBuffer(Atlases.cutoutBlockSheet());
              CuboidModelRenderer.render(fillingFluidModel, matrixStack, builder,
                  RenderUtil.getColorARGB(fluidStack, scale),
                  CuboidModelRenderer.FaceDisplay.FRONT, true);
            }
          }
          matrixStack.popPose();
        }

      }
    }
  }

  private void renderFilterItem(TankMinecartEntity cart, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, float red,
      float green, float blue, float alpha) {
    matrixStack.pushPose();
    {
      ItemStack itemStack = cart.getFilterItem().copy();

      final float scale = 1.2F;

      matrixStack.pushPose();
      {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        matrixStack.translate(0.0F, -0.9F, 0.68F);
        matrixStack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack,
            ItemCameraTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
            matrixStack, renderTypeBuffer);
      }
      matrixStack.popPose();

      matrixStack.mulPose(Vector3f.YN.rotationDegrees(90.0F));
      matrixStack.translate(0.0F, -0.9F, 0.68F);
      matrixStack.scale(scale, scale, scale);
      Minecraft.getInstance().getItemRenderer().renderStatic(itemStack,
          ItemCameraTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
          matrixStack, renderTypeBuffer);
    }
    matrixStack.popPose();
  }

  @Override
  protected SimpleTexturedModel getContentModel(TankMinecartEntity cart) {
    return TANK_MODEL;
  }

  @Override
  protected EntityModel<TankMinecartEntity> getBodyModel(TankMinecartEntity cart) {
    return BODY_MODEL;
  }

  @Override
  protected EntityModel<TankMinecartEntity> getSnowModel(TankMinecartEntity cart) {
    return SNOW_MODEL;
  }
}
