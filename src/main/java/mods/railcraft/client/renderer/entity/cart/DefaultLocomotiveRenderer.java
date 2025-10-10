package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.entity.state.LocomotiveRenderState;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

public abstract class DefaultLocomotiveRenderer extends LocomotiveRenderer<Locomotive, LocomotiveRenderState> {

  protected final String modelTag;
  private final EntityModel<LocomotiveRenderState> model;
  private final EntityModel<LocomotiveRenderState> snowLayer;
  private final ResourceLocation[] textures;

  public DefaultLocomotiveRenderer(EntityRendererProvider.Context context, String modelTag,
      EntityModel<LocomotiveRenderState> model,
      EntityModel<LocomotiveRenderState> snowLayer) {
    this(context, modelTag, model, snowLayer, new ResourceLocation[] {
        RailcraftConstants.rl("textures/entity/locomotive/" + modelTag + "/primary.png"),
        RailcraftConstants.rl("textures/entity/locomotive/" + modelTag + "/secondary.png"),
        RailcraftConstants.rl("textures/entity/locomotive/" + modelTag + "/nocolor.png"),
        RailcraftConstants.rl("textures/entity/locomotive/" + modelTag + "/snow.png")
    });
  }

  public DefaultLocomotiveRenderer(EntityRendererProvider.Context context, String modelTag,
      EntityModel<LocomotiveRenderState> model,
      EntityModel<LocomotiveRenderState> snowLayer, ResourceLocation[] textures) {
    super(context);
    this.modelTag = modelTag;
    this.model = model;
    this.snowLayer = snowLayer;
    this.textures = textures;
  }

  @Override
  protected void renderBody(LocomotiveRenderState renderState, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraState, int color) {
    poseStack.pushPose();

    poseStack.scale(-1, -1, 1);

    var alpha = ARGB.alpha(color);
    var primaryColor = this.getPrimaryColor(renderState);
    var secondaryColor = this.getSecondaryColor(renderState);

    for (int pass = 0; pass <= 2; pass++) {
      var selectedColor = ARGB.color(alpha, switch (pass) {
        case 0 -> primaryColor;
        case 1 -> secondaryColor;
        default -> 1;
      });
      this.model.setupAnim(renderState);

      collector.submitModel(
          model,
          renderState,
          poseStack,
          this.model.renderType(this.textures[pass]),
          renderState.lightCoords,
          OverlayTexture.NO_OVERLAY,
          selectedColor,
          null,
          0,
          null);
    }

    if (Seasons.isPolarExpress(renderState)) {
      collector.submitModel(
          this.snowLayer,
          renderState,
          poseStack,
          this.snowLayer.renderType(this.textures[3]),
          renderState.lightCoords,
          OverlayTexture.NO_OVERLAY,
          0,
          null
      );
    }
    poseStack.popPose();
  }
}
