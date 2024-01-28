package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.season.Seasons;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public abstract class StandardMinecartRenderer<T extends AbstractMinecart>
    extends CustomMinecartRenderer<T> {

  public static final ResourceLocation SNOW_TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/carts/cart_snow.png");

  public static final ResourceLocation MINECART_TEXTURE_LOCATION =
      new ResourceLocation("textures/entity/minecart.png");

  public StandardMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  protected void renderBody(T cart, float partialTicks, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight,
      float red, float green, float blue, float alpha) {
    poseStack.pushPose();
    poseStack.scale(-1, -1, 1);
    var bodyModel = this.getBodyModel(cart);
    var bodyVertexConsumer =
        bufferSource.getBuffer(bodyModel.renderType(this.getTextureLocation(cart)));
    bodyModel.setupAnim(cart, 0, 0, -0.1F, 0, 0);
    bodyModel.renderToBuffer(poseStack, bodyVertexConsumer, packedLight,
        OverlayTexture.NO_OVERLAY, red, green, blue, alpha);

    if (Seasons.isPolarExpress(cart)) {
      var snowModel = this.getSnowModel(cart);
      var snowVertexConsumer = bufferSource.getBuffer(snowModel.renderType(SNOW_TEXTURE_LOCATION));
      snowModel.setupAnim(cart, 0, 0, -0.1F, 0, 0);
      snowModel.renderToBuffer(poseStack, snowVertexConsumer, packedLight,
          OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
    poseStack.popPose();
  }

  @Override
  public ResourceLocation getTextureLocation(T cart) {
    return MINECART_TEXTURE_LOCATION;
  }

  protected abstract EntityModel<? super T> getBodyModel(T cart);

  protected abstract EntityModel<? super T> getSnowModel(T cart);
}
