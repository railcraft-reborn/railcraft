package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class DefaultLocomotiveRenderer extends LocomotiveRenderer<Locomotive> {

  protected final String modelTag;
  private final EntityModel<? super Locomotive> model;
  private final EntityModel<? super Locomotive> snowLayer;
  private final ResourceLocation[] textures;

  public DefaultLocomotiveRenderer(EntityRendererProvider.Context context, String modelTag,
      EntityModel<? super Locomotive> model,
      EntityModel<? super Locomotive> snowLayer) {
    this(context, modelTag, model, snowLayer, new ResourceLocation[] {
        RailcraftConstants.rl("textures/entity/locomotive/" + modelTag + "/primary.png"),
        RailcraftConstants.rl("textures/entity/locomotive/" + modelTag + "/secondary.png"),
        RailcraftConstants.rl("textures/entity/locomotive/" + modelTag + "/nocolor.png"),
        RailcraftConstants.rl("textures/entity/locomotive/" + modelTag + "/snow.png")
    });
  }

  public DefaultLocomotiveRenderer(EntityRendererProvider.Context context, String modelTag,
      EntityModel<? super Locomotive> model,
      EntityModel<? super Locomotive> snowLayer, ResourceLocation[] textures) {
    super(context);
    this.modelTag = modelTag;
    this.model = model;
    this.snowLayer = snowLayer;
    this.textures = textures;
  }

  @Override
  public void renderBody(Locomotive cart, float time, PoseStack poseStack,
      MultiBufferSource renderTypeBuffer, int packedLight, int color) {
    poseStack.pushPose();

    poseStack.scale(-1, -1, 1);

    var alpha = FastColor.ARGB32.alpha(color);
    var primaryColor = this.getPrimaryColor(cart);
    var secondaryColor = this.getSecondaryColor(cart);

    for (int pass = 0; pass < 3; pass++) {
      var selectedColor = FastColor.ARGB32.color(alpha, switch (pass) {
        case 0 -> primaryColor;
        case 1 -> secondaryColor;
        default -> 1;
      });
      this.model.setupAnim(cart, 0, 0, -0.1F, 0, 0);
      var vertexBuilder = renderTypeBuffer.getBuffer(this.model.renderType(this.textures[pass]));
      this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, selectedColor);
    }

    if (Seasons.isPolarExpress(cart)) {
      this.snowLayer.setupAnim(cart, 0, 0, -0.1F, 0, 0);
      var vertexBuilder = renderTypeBuffer.getBuffer(this.snowLayer.renderType(this.textures[3]));
      this.snowLayer.renderToBuffer(poseStack, vertexBuilder, packedLight,
          OverlayTexture.NO_OVERLAY, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1));
    }
    poseStack.popPose();
  }

  @Override
  public ResourceLocation getTextureLocation(Locomotive locomotive) {
    throw new IllegalStateException();
  }
}
