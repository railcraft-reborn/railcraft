package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.RailcraftMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public abstract class StandardMinecartRenderer<T extends RailcraftMinecart, S extends RailcraftMinecartRenderState>
    extends CustomMinecartRenderer<T, S> {

  public static final Identifier SNOW_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/carts/cart_snow.png");

  public static final Identifier MINECART_TEXTURE_LOCATION =
      Identifier.withDefaultNamespace("textures/entity/minecart.png");

  public StandardMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  protected void renderBody(S renderState, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState, int color) {
    poseStack.pushPose();
    poseStack.scale(-1, -1, 1);
    var bodyModel = this.getBodyModel(renderState);
    bodyModel.setupAnim(renderState);
    collector.submitModel(
        bodyModel,
        renderState,
        poseStack,
        bodyModel.renderType(MINECART_TEXTURE_LOCATION),
        renderState.lightCoords,
        OverlayTexture.NO_OVERLAY,
        color,
        null,
        0,
        null
    );

    if (Seasons.isPolarExpress(renderState)) {
      var snowModel = this.getSnowModel(renderState);
      snowModel.setupAnim(renderState);
      collector.submitModel(
          snowModel,
          renderState,
          poseStack,
          snowModel.renderType(SNOW_TEXTURE_LOCATION),
          renderState.lightCoords,
          OverlayTexture.NO_OVERLAY,
          ARGB.colorFromFloat(1, 1, 1, 1),
          null,
          0,
          null
      );
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
