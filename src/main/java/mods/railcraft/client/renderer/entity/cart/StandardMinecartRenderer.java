package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.RailcraftMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

public abstract class StandardMinecartRenderer<T extends RailcraftMinecart, S extends RailcraftMinecartRenderState>
    extends CustomMinecartRenderer<T, S> {

  public static final ResourceLocation SNOW_TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/carts/cart_snow.png");

  public static final ResourceLocation MINECART_TEXTURE_LOCATION =
      ResourceLocation.withDefaultNamespace("textures/entity/minecart.png");

  public StandardMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  protected void renderBody(S renderState, PoseStack poseStack, MultiBufferSource multiBufferSource,
      int packedLight, int color) {
    poseStack.pushPose();
    poseStack.scale(-1, -1, 1);
    var bodyModel = this.getBodyModel(renderState);
    bodyModel.setupAnim(renderState);
    var bodyVertexConsumer =
        multiBufferSource.getBuffer(bodyModel.renderType(MINECART_TEXTURE_LOCATION));
    bodyModel.setupAnim(renderState);
    bodyModel.renderToBuffer(poseStack, bodyVertexConsumer, packedLight,
        OverlayTexture.NO_OVERLAY, color);

    if (Seasons.isPolarExpress(renderState)) {
      var snowModel = this.getSnowModel(renderState);
      var snowVertexConsumer = multiBufferSource.getBuffer(snowModel.renderType(SNOW_TEXTURE_LOCATION));
      snowModel.setupAnim(renderState);
      snowModel.renderToBuffer(poseStack, snowVertexConsumer, packedLight,
          OverlayTexture.NO_OVERLAY, ARGB.color(1, 1, 1, 1));
    }
    poseStack.popPose();
  }

  @Override
  public void extractRenderState(T entity, S reusedState, float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    reusedState.season = entity.getSeason();
  }

  protected abstract EntityModel<S> getBodyModel(S cart);

  protected abstract EntityModel<S> getSnowModel(S cart);
}
