/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.Railcraft;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class DefaultLocomotiveRenderer extends LocomotiveRenderer<Locomotive> {

  protected final String modelTag;
  private final EntityModel<? super Locomotive> model;
  private final EntityModel<? super Locomotive> snowLayer;
  private final ResourceLocation[] textures;
  private final float[][] color = new float[3][];
  private float emblemSize = 0.15F;
  private float emblemOffsetX = 0.47F;
  private float emblemOffsetY = -0.17F;
  private float emblemOffsetZ = -0.515F;

  public DefaultLocomotiveRenderer(EntityRendererProvider.Context context, String modelTag,
      EntityModel<? super Locomotive> model,
      EntityModel<? super Locomotive> snowLayer) {
    // Notice: do NOT remove the .png on these ones, they are needed.
    this(context, modelTag, model, snowLayer, new ResourceLocation[] {
        new ResourceLocation(Railcraft.ID,
            "textures/entity/locomotive/" + modelTag + "/primary.png"),
        new ResourceLocation(Railcraft.ID,
            "textures/entity/locomotive/" + modelTag + "/secondary.png"),
        new ResourceLocation(Railcraft.ID,
            "textures/entity/locomotive/" + modelTag + "/nocolor.png"),
        new ResourceLocation(Railcraft.ID,
            "textures/entity/locomotive/" + modelTag + "/snow.png")
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

  public void setEmblemPosition(float size, float offsetX, float offsetY, float offsetZ) {
    this.emblemSize = size;
    this.emblemOffsetX = offsetX;
    this.emblemOffsetY = offsetY;
    this.emblemOffsetZ = offsetZ;
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
      this.model.setupAnim(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
      VertexConsumer vertexBuilder =
          renderTypeBuffer.getBuffer(this.model.renderType(this.textures[pass]));
      this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY,
          color[0], color[1], color[2], alpha);
    }

    if (Seasons.isPolarExpress(cart)) {
      this.snowLayer.setupAnim(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
      VertexConsumer vertexBuilder =
          renderTypeBuffer.getBuffer(this.snowLayer.renderType(this.textures[3]));
      this.snowLayer.renderToBuffer(poseStack, vertexBuilder, packedLight,
          OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    this.getEmblemTexture(cart).ifPresent(emblemTexture -> {

      VertexConsumer vertexBuilder =
          renderTypeBuffer.getBuffer(RenderType.entityTranslucent(emblemTexture));

      // float size = 0.22F;
      // float offsetX = -0.25F;
      // float offsetY = -0.25F;
      // float offsetZ = -0.46F;
      // TODO: Test this!


      vertexBuilder.vertex(emblemOffsetX - emblemSize, emblemOffsetY - emblemSize, emblemOffsetZ)
          .uv(0, 0).endVertex();
      vertexBuilder.vertex(emblemOffsetX - emblemSize, emblemOffsetY + emblemSize, emblemOffsetZ)
          .uv(0, 1).endVertex();
      vertexBuilder.vertex(emblemOffsetX + emblemSize, emblemOffsetY + emblemSize, emblemOffsetZ)
          .uv(1, 1).endVertex();
      vertexBuilder.vertex(emblemOffsetX + emblemSize, emblemOffsetY + -emblemSize, emblemOffsetZ)
          .uv(1, 0).endVertex();

      vertexBuilder.vertex(emblemOffsetX + emblemSize, emblemOffsetY + -emblemSize, -emblemOffsetZ)
          .uv(0, 0).endVertex();
      vertexBuilder.vertex(emblemOffsetX + emblemSize, emblemOffsetY + emblemSize, -emblemOffsetZ)
          .uv(0, 1).endVertex();
      vertexBuilder.vertex(emblemOffsetX - emblemSize, emblemOffsetY + emblemSize, -emblemOffsetZ)
          .uv(1, 1).endVertex();
      vertexBuilder.vertex(emblemOffsetX - emblemSize, emblemOffsetY - emblemSize, -emblemOffsetZ)
          .uv(1, 0).endVertex();

    });
    // OpenGL.glDisable(GL11.GL_BLEND);
    poseStack.popPose();
  }

  @Override
  public ResourceLocation getTextureLocation(Locomotive loco) {
    throw new IllegalStateException();
  }
}
