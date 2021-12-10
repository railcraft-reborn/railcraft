package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.CubeModel;
import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TankMinecartRenderer extends ContentsMinecartRenderer<TankMinecart> {

  private static final ResourceLocation TANK_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/entity/minecart/tank.png");

  private final LowSidesMinecartModel<TankMinecart> bodyModel;
  private final LowSidesMinecartModel<TankMinecart> snowModel;
  private final CubeModel tankModel;

  public TankMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
    this.tankModel = new CubeModel(RenderType::entityTranslucentCull,
        context.bakeLayer(RailcraftModelLayers.CUBE));
  }

  @Override
  protected void renderContents(TankMinecart cart, float partialTicks,
      PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int packedLight, float red,
      float green, float blue, float alpha) {
    VertexConsumer vertexBuilder =
        renderTypeBuffer.getBuffer(this.tankModel.renderType(TANK_TEXTURE_LOCATION));
    this.tankModel.renderToBuffer(matrixStack, vertexBuilder, packedLight,
        OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    this.renderTank(cart, partialTicks, matrixStack, renderTypeBuffer, packedLight, red, green,
        blue, alpha);
    if (cart.hasFilter()) {
      this.renderFilterItem(cart, partialTicks, matrixStack, renderTypeBuffer, packedLight, red,
          green, blue, alpha);
    }
  }

  private void renderTank(TankMinecart cart, float partialTicks, PoseStack matrixStack,
      MultiBufferSource renderTypeBuffer, int packedLight, float red,
      float green, float blue, float alpha) {
    StandardTank tank = cart.getTankManager().get(0);
    if (tank != null) {
      FluidStack fluidStack = tank.getFluid();
      float capacity = tank.getCapacity();
      if (capacity > 0 && fluidStack != null && fluidStack.getAmount() > 0) {
        matrixStack.pushPose();
        {
          float level = Math.min(fluidStack.getAmount() / capacity, capacity);
          int stage = fluidStack.getFluid().getAttributes().isGaseous(fluidStack)
              ? FluidRenderer.STAGES - 1
              : Math.min(FluidRenderer.STAGES - 1, (int) (level * (FluidRenderer.STAGES - 1)));

          CuboidModel fluidModel =
              FluidRenderer.getFluidModel(fluidStack, stage, FluidRenderer.FluidType.STILL);
          if (fluidModel != null) {
            fluidModel.setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
            fluidModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
            VertexConsumer builder = renderTypeBuffer.getBuffer(Sheets.cutoutBlockSheet());
            CuboidModelRenderer.render(fluidModel, matrixStack, builder,
                RenderUtil.getColorARGB(fluidStack, level),
                CuboidModelRenderer.FaceDisplay.FRONT, true);
          }
        }
        matrixStack.popPose();

        if (cart.isFilling()) {
          matrixStack.pushPose();
          {
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
              VertexConsumer builder = renderTypeBuffer.getBuffer(Sheets.cutoutBlockSheet());
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

  private void renderFilterItem(TankMinecart cart, float partialTicks,
      PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int packedLight, float red,
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
            ItemTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
            matrixStack, renderTypeBuffer, 0);
      }
      matrixStack.popPose();

      matrixStack.mulPose(Vector3f.YN.rotationDegrees(90.0F));
      matrixStack.translate(0.0F, -0.9F, 0.68F);
      matrixStack.scale(scale, scale, scale);
      Minecraft.getInstance().getItemRenderer().renderStatic(itemStack,
          ItemTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
          matrixStack, renderTypeBuffer, 0);
    }
    matrixStack.popPose();
  }

  @Override
  protected EntityModel<TankMinecart> getBodyModel(TankMinecart cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<TankMinecart> getSnowModel(TankMinecart cart) {
    return this.snowModel;
  }
}
