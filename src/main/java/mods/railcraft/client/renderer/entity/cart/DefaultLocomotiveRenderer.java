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

public class DefaultLocomotiveRenderer extends LocomotiveRenderer<Locomotive> {

  protected final String modelTag;
  private final EntityModel<? super Locomotive> model;
  private final EntityModel<? super Locomotive> snowLayer;
  private final ResourceLocation[] textures;
  private final float[][] color = new float[3][];

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
    this.color[2] = new float[] {1.0F, 1.0F, 1.0F};
  }

  @Override
  public void renderBody(Locomotive cart, float time, PoseStack poseStack,
      MultiBufferSource renderTypeBuffer, int packedLight, float red, float green, float blue,
      float alpha) {
    poseStack.pushPose();

    poseStack.scale(-1, -1, 1);

    this.color[0] = this.getPrimaryColor(cart);
    this.color[1] = this.getSecondaryColor(cart);

    for (int pass = 0; pass < 3; pass++) {
      float[] color = this.color[pass];
      this.model.setupAnim(cart, 0, 0, -0.1F, 0, 0);
      var vertexBuilder = renderTypeBuffer.getBuffer(this.model.renderType(this.textures[pass]));
      this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY,
          color[0], color[1], color[2], alpha);
    }

    if (Seasons.isPolarExpress(cart)) {
      this.snowLayer.setupAnim(cart, 0, 0, -0.1F, 0, 0);
      var vertexBuilder = renderTypeBuffer.getBuffer(this.snowLayer.renderType(this.textures[3]));
      this.snowLayer.renderToBuffer(poseStack, vertexBuilder, packedLight,
          OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
    poseStack.popPose();
  }

  @Override
  public ResourceLocation getTextureLocation(Locomotive locomotive) {
    throw new IllegalStateException();
  }
}
