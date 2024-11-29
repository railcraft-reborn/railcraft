package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.CubeModel;
import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.renderer.entity.state.TankMinecartRendererState;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class TankMinecartRenderer extends ContentsMinecartRenderer<TankMinecart, TankMinecartRendererState> {

  private static final ResourceLocation TANK_TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/entity/minecart/tank.png");

  private final LowSidesMinecartModel<TankMinecartRendererState> bodyModel;
  private final LowSidesMinecartModel<TankMinecartRendererState> snowModel;
  private final CubeModel tankModel;

  public TankMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
    this.tankModel = new CubeModel(RenderType::entityTranslucent,//entityTranslucentCull
        context.bakeLayer(RailcraftModelLayers.CUBE));
  }

  @Override
  protected void renderContents(TankMinecartRendererState renderState, PoseStack poseStack,
      MultiBufferSource multiBufferSource, int packedLight, int color) {
    var vertexBuilder =
        multiBufferSource.getBuffer(this.tankModel.renderType(TANK_TEXTURE_LOCATION));
    this.tankModel.renderToBuffer(poseStack, vertexBuilder, packedLight,
        OverlayTexture.NO_OVERLAY, color);
    this.renderTank(renderState, poseStack, multiBufferSource, packedLight);
    if (renderState.hasFilter) {
      this.renderFilterItem(renderState, poseStack, multiBufferSource, packedLight);
    }
  }

  private void renderTank(TankMinecartRendererState renderState, PoseStack poseStack,
      MultiBufferSource renderTypeBuffer, int packedLight) {
    var tank = renderState.tankManager;
    var fluidStack = tank.getFluid();
    float capacity = tank.getCapacity();
    if (capacity > 0 && fluidStack.getAmount() > 0) {
      poseStack.pushPose();
      var level = fluidStack.getAmount() / capacity;
      var fluidMaxY = fluidStack.getFluidType().isLighterThanAir()
          ? 1
          : Math.min(1, level);

      var fluidModel = FluidRenderer.getFluidModel(fluidStack,
          1 - (RenderUtil.SCALED_PIXEL * 2),
          fluidMaxY - (RenderUtil.SCALED_PIXEL * 2),
          1 - (RenderUtil.SCALED_PIXEL * 2),
          FluidRenderer.FluidType.STILL);

      poseStack.translate(RenderUtil.SCALED_PIXEL, RenderUtil.SCALED_PIXEL,
          RenderUtil.SCALED_PIXEL);
      fluidModel.setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
      fluidModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
      var builder = renderTypeBuffer.getBuffer(Sheets.cutoutBlockSheet());
      CuboidModelRenderer.render(fluidModel, poseStack, builder,
          RenderUtil.getColorARGB(fluidStack, level),
          CuboidModelRenderer.FaceDisplay.FRONT, true);
      poseStack.popPose();

      if (renderState.isFilling) {
        poseStack.pushPose();
        final var size = 0.3F;
        poseStack.translate(0.5F - size / 2, 0F, 0.5F - size / 2);

        var fillingFluidModel =
            FluidRenderer.getFluidModel(fluidStack, size, 1 - RenderUtil.SCALED_PIXEL, size,
                FluidRenderer.FluidType.FLOWING);
        fillingFluidModel.setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
        fillingFluidModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
        CuboidModelRenderer.render(fillingFluidModel, poseStack, builder,
            RenderUtil.getColorARGB(fluidStack, 1),
            CuboidModelRenderer.FaceDisplay.FRONT, true);
        poseStack.popPose();
      }
    }
  }

  private void renderFilterItem(TankMinecartRendererState rendererState, PoseStack matrixStack,
      MultiBufferSource renderTypeBuffer, int packedLight) {
    matrixStack.pushPose();
    var itemStack = rendererState.filterItem;
    var level = Minecraft.getInstance().level;

    final float scale = 1.2F;

    matrixStack.pushPose();
    matrixStack.mulPose(Axis.YP.rotationDegrees(90));
    matrixStack.translate(0, -0.9F, 0.68F);
    matrixStack.scale(scale, scale, scale);
    Minecraft.getInstance().getItemRenderer().renderStatic(itemStack,
        ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
        matrixStack, renderTypeBuffer, level, 0);
    matrixStack.popPose();

    matrixStack.mulPose(Axis.YN.rotationDegrees(90));
    matrixStack.translate(0, -0.9F, 0.68F);
    matrixStack.scale(scale, scale, scale);
    Minecraft.getInstance().getItemRenderer().renderStatic(itemStack,
        ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
        matrixStack, renderTypeBuffer, level, 0);
    matrixStack.popPose();
  }

  @Override
  protected EntityModel<TankMinecartRendererState> getBodyModel(TankMinecartRendererState cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<TankMinecartRendererState> getSnowModel(TankMinecartRendererState cart) {
    return this.snowModel;
  }


  @Override
  public TankMinecartRendererState createRenderState() {
    return new TankMinecartRendererState();
  }

  @Override
  public void extractRenderState(TankMinecart entity, TankMinecartRendererState reusedState,
      float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    reusedState.isFilling = entity.isFilling();
    reusedState.tankManager = entity.getTankManager();
    reusedState.hasFilter = entity.hasFilter();
    reusedState.filterItem = entity.getFilterItem().copy();
  }
}
