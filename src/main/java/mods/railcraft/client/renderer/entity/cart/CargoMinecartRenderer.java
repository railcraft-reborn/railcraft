package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.world.entity.vehicle.CargoMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;

public class CargoMinecartRenderer extends ContentsMinecartRenderer<CargoMinecart> {

  private static final RandomSource RANDOM = RandomSource.create();

  private final LowSidesMinecartModel<CargoMinecart> bodyModel;
  private final LowSidesMinecartModel<CargoMinecart> snowModel;

  public CargoMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
  }

  @Override
  protected EntityModel<CargoMinecart> getBodyModel(CargoMinecart cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<CargoMinecart> getSnowModel(CargoMinecart cart) {
    return this.snowModel;
  }

  @Override
  protected void renderContents(CargoMinecart cart, float partialTicks, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int color) {
    poseStack.pushPose();
    poseStack.translate(0.5, 0.5 + 0.3125, 0.5);
    poseStack.mulPose(Axis.YP.rotationDegrees(90F));
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    this.renderCargo(cart, poseStack, bufferSource, packedLight);
    poseStack.popPose();
  }

  private void renderCargo(CargoMinecart cart, PoseStack poseStack,
      MultiBufferSource renderTypeBuffer, int packedLight) {
    if (!cart.hasFilter()) {
      return;
    }

    poseStack.pushPose();
    RenderSystem.disableBlend();

    var itemStack = cart.getFilterItem().copyWithCount(1);
    var model = Minecraft.getInstance().getItemRenderer()
        .getModel(itemStack, cart.level(), null, 0);

    int numIterations;
    float yOffset;
    float scale;
    float xScale;
    float zScale;
    if (model.isGui3d()) {
      xScale = 0.3f;
      zScale = 0.2F;
      scale = 2.5F;
      int slotsFilled = cart.getSlotsFilled();
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
      numIterations = cart.getSlotsFilled();
      yOffset = -0.8F;
    }
    poseStack.translate(0, yOffset, 0);
    poseStack.scale(scale, scale, scale);
    poseStack.mulPose(Axis.YP.rotationDegrees(90F));
    RANDOM.setSeed(cart.getId());
    for (int i = 0; i < numIterations; i++) {
      poseStack.pushPose();
      float tx = (float) (RANDOM.nextDouble() - 0.5F) * xScale;
      float ty = (float) (RANDOM.nextDouble() - 0.5F) * 0.15F;
      float tz = (float) (RANDOM.nextDouble() - 0.5F) * zScale;
      poseStack.translate(tx, ty, tz);
      Minecraft.getInstance().getItemRenderer().renderStatic(itemStack,
          ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
          poseStack, renderTypeBuffer, cart.level(), 0);
      poseStack.popPose();
    }
    poseStack.popPose();
  }
}
