package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.RailcraftMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

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
  protected void renderBody(S renderState, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState, int color) {
    poseStack.pushPose();
    poseStack.scale(-1, -1, 1);
    var bodyModel = this.getBodyModel(renderState);
    collector.submitModel(
        bodyModel,
        renderState,
        poseStack,
        bodyModel.renderType(MINECART_TEXTURE_LOCATION),
        renderState.lightCoords,
        OverlayTexture.NO_OVERLAY,
        0,
        null
    );

    if (Seasons.isPolarExpress(renderState)) {
      var snowModel = this.getSnowModel(renderState);
      collector.submitModel(
          snowModel,
          renderState,
          poseStack,
          snowModel.renderType(SNOW_TEXTURE_LOCATION),
          renderState.lightCoords,
          OverlayTexture.NO_OVERLAY,
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
