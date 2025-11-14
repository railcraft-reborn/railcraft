package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.renderer.entity.state.CargoMinecartRendererState;
import mods.railcraft.world.entity.vehicle.CargoMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;

public class CargoMinecartRenderer extends ContentsMinecartRenderer<CargoMinecart, CargoMinecartRendererState> {

  private static final RandomSource RANDOM = RandomSource.create();

  private final LowSidesMinecartModel<CargoMinecartRendererState> bodyModel;
  private final LowSidesMinecartModel<CargoMinecartRendererState> snowModel;
  private final ItemModelResolver itemModelResolver;

  public CargoMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
    this.itemModelResolver = context.getItemModelResolver();
  }

  @Override
  protected EntityModel<CargoMinecartRendererState> getBodyModel(CargoMinecartRendererState cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<CargoMinecartRendererState> getSnowModel(CargoMinecartRendererState cart) {
    return this.snowModel;
  }

  @Override
  public CargoMinecartRendererState createRenderState() {
    return new CargoMinecartRendererState();
  }

  @Override
  public void extractRenderState(CargoMinecart entity, CargoMinecartRendererState reusedState,
      float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    reusedState.hasFilter = entity.hasFilter();
    reusedState.slotsFilled = entity.getSlotsFilled();
    reusedState.isBlock = entity.getFilterItem().getItem() instanceof BlockItem;
    this.itemModelResolver.updateForTopItem(reusedState.itemState,
        entity.getFilterItem().copyWithCount(1),
        ItemDisplayContext.GROUND, entity.level(), null, 0);
    RANDOM.setSeed(entity.getId());
  }

  @Override
  protected void renderContents(CargoMinecartRendererState rendererState, PoseStack poseStack,
      SubmitNodeCollector collector, int color) {
    poseStack.pushPose();
    poseStack.translate(0.5, 0.5 + 0.3125, 0.5);
    poseStack.mulPose(Axis.YP.rotationDegrees(90F));
    this.renderCargo(rendererState, poseStack, collector);
    poseStack.popPose();
  }

  private void renderCargo(CargoMinecartRendererState rendererState, PoseStack poseStack,
      SubmitNodeCollector collector) {
    if (!rendererState.hasFilter) {
      return;
    }

    poseStack.pushPose();

    int numIterations;
    float yOffset;
    float scale;
    float xScale;
    float zScale;
    if (rendererState.isBlock) {
      xScale = 0.3f;
      zScale = 0.2F;
      scale = 2.5F;
      int slotsFilled = rendererState.slotsFilled;
      if (slotsFilled <= 0) {
        numIterations = 0;
      } else {
        numIterations = (int) Math.ceil(slotsFilled / 2f);
        numIterations = Mth.clamp(numIterations, 1, 6);
      }
      yOffset = -1.1F;
    } else {
      xScale = 0.5f;
      zScale = 0.6F;
      scale = 1.6F;
      numIterations = rendererState.slotsFilled;
      yOffset = -0.8F;
    }
    poseStack.translate(0, yOffset, 0);
    poseStack.scale(scale, scale, scale);
    poseStack.mulPose(Axis.YP.rotationDegrees(90F));
    for (int i = 0; i < numIterations; i++) {
      poseStack.pushPose();
      float tx = (float) (RANDOM.nextDouble() - 0.5F) * xScale;
      float ty = (float) (RANDOM.nextDouble() - 0.5F) * 0.15F;
      float tz = (float) (RANDOM.nextDouble() - 0.5F) * zScale;
      poseStack.translate(tx, ty, tz);
      rendererState.itemState.submit(poseStack, collector, rendererState.lightCoords,
          OverlayTexture.NO_OVERLAY, 0);
      poseStack.popPose();
    }
    poseStack.popPose();
  }
}
