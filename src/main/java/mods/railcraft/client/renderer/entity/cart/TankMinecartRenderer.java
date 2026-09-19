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
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;

public class TankMinecartRenderer extends ContentsMinecartRenderer<TankMinecart, TankMinecartRendererState> {

  private static final Identifier TANK_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/entity/minecart/tank.png");

  private final LowSidesMinecartModel<TankMinecartRendererState> bodyModel;
  private final LowSidesMinecartModel<TankMinecartRendererState> snowModel;
  private final CubeModel<TankMinecartRendererState> tankModel;
  private final ItemModelResolver itemModelResolver;

  public TankMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
    this.tankModel = new CubeModel<>(RenderTypes::entityTranslucent,
        context.bakeLayer(RailcraftModelLayers.CUBE));
    this.itemModelResolver = context.getItemModelResolver();
  }

  @Override
  protected void renderContents(TankMinecartRendererState renderState, PoseStack poseStack,
      SubmitNodeCollector collector, int color) {
    collector.submitModel(
        this.tankModel,
        renderState,
        poseStack,
        this.tankModel.renderType(TANK_TEXTURE_LOCATION),
        renderState.lightCoords,
        OverlayTexture.NO_OVERLAY,
        color,
        null,
        0,
        null
    );
    this.renderTank(renderState, poseStack, collector);
    if (renderState.hasFilter) {
      this.renderFilterItem(renderState, poseStack, collector);
    }
  }

  private void renderTank(TankMinecartRendererState renderState, PoseStack poseStack,
      SubmitNodeCollector collector) {
    var tank = renderState.tankManager;
    var fluidStack = tank.getFluidStack();
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

      poseStack.translate(RenderUtil.SCALED_PIXEL, RenderUtil.SCALED_PIXEL, RenderUtil.SCALED_PIXEL);
      fluidModel.setPackedLight(RenderUtil.calculateGlowLight(renderState.lightCoords, fluidStack));
      fluidModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
      collector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, vertexConsumer) -> {
        CuboidModelRenderer.render(fluidModel, pose, vertexConsumer,
            RenderUtil.getColorARGB(fluidStack, level),
            CuboidModelRenderer.FaceDisplay.FRONT, true);
      });
      poseStack.popPose();

      if (renderState.isFilling) {
        poseStack.pushPose();
        final var size = 0.3F;
        poseStack.translate(0.5F - size / 2, 0F, 0.5F - size / 2);

        var fillingFluidModel =
            FluidRenderer.getFluidModel(fluidStack, size, 1 - RenderUtil.SCALED_PIXEL, size,
                FluidRenderer.FluidType.FLOWING);
        fillingFluidModel.setPackedLight(RenderUtil.calculateGlowLight(renderState.lightCoords, fluidStack));
        fillingFluidModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
        collector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, vertexConsumer) -> {
          CuboidModelRenderer.render(fillingFluidModel, pose, vertexConsumer,
              RenderUtil.getColorARGB(fluidStack, 1),
              CuboidModelRenderer.FaceDisplay.FRONT, true);
        });
        poseStack.popPose();
      }
    }
  }

  private void renderFilterItem(TankMinecartRendererState rendererState, PoseStack poseStack,
      SubmitNodeCollector collector) {
    poseStack.pushPose();

    final float scale = 1.2F;

    poseStack.pushPose();
    poseStack.mulPose(Axis.YP.rotationDegrees(90));
    poseStack.translate(0, -0.9F, 0.68F);
    poseStack.scale(scale, scale, scale);
    rendererState.itemState.submit(poseStack, collector, rendererState.lightCoords,
        OverlayTexture.NO_OVERLAY, 0);
    poseStack.popPose();

    poseStack.mulPose(Axis.YN.rotationDegrees(90));
    poseStack.translate(0, -0.9F, 0.68F);
    poseStack.scale(scale, scale, scale);
    rendererState.itemState.submit(poseStack, collector, rendererState.lightCoords,
        OverlayTexture.NO_OVERLAY, 0);
    poseStack.popPose();
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

    this.itemModelResolver.updateForTopItem(reusedState.itemState, entity.getFilterItem().copy(),
        ItemDisplayContext.GROUND, entity.level(), null, 0);
  }
}
