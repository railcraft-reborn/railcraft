/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.cart.LocomotiveEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class DefaultLocomotiveRenderer extends LocomotiveRenderer<LocomotiveEntity> {

  protected final String modelTag;
  private final EntityModel<? super LocomotiveEntity> model;
  private final EntityModel<? super LocomotiveEntity> snowLayer;
  private final ResourceLocation[] textures;
  private final int[] color = new int[3];
  // protected final IIcon[] itemIcons = new IIcon[3];
  private float emblemSize = 0.15F;
  private float emblemOffsetX = 0.47F;
  private float emblemOffsetY = -0.17F;
  private float emblemOffsetZ = -0.515F;

  public DefaultLocomotiveRenderer(EntityRendererManager dispatcher, String modelTag,
      EntityModel<? super LocomotiveEntity> model,
      EntityModel<? super LocomotiveEntity> snowLayer) {
    // Notice: do NOT remove the .png on these ones, they are needed.
    this(dispatcher, modelTag, model, snowLayer, new ResourceLocation[] {
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

  public DefaultLocomotiveRenderer(EntityRendererManager dispatcher, String modelTag,
      EntityModel<? super LocomotiveEntity> model,
      EntityModel<? super LocomotiveEntity> snowLayer, ResourceLocation[] textures) {
    super(dispatcher);
    this.modelTag = modelTag;
    this.model = model;
    this.snowLayer = snowLayer;
    this.textures = textures;
    color[2] = 0xFFFFFF;
  }

  public void setEmblemPosition(float size, float offsetX, float offsetY, float offsetZ) {
    this.emblemSize = size;
    this.emblemOffsetX = offsetX;
    this.emblemOffsetY = offsetY;
    this.emblemOffsetZ = offsetZ;
  }

  @Override
  public void renderBody(LocomotiveEntity cart, float time, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, float red, float green, float blue,
      float alpha) {
    matrixStack.pushPose();

    matrixStack.scale(-1, -1, 1);

    color[0] = this.getPrimaryColor(cart);
    color[1] = this.getSecondaryColor(cart);

    for (int pass = 0; pass < 3; pass++) {
      int c = color[pass];
      float dim = 1.0F;
      float c1 = (float) (c >> 16 & 255) / 255.0F;
      float c2 = (float) (c >> 8 & 255) / 255.0F;
      float c3 = (float) (c & 255) / 255.0F;
      this.model.setupAnim(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(this.model.renderType(this.textures[pass]));
      this.model.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY,
          c1 * dim, c2 * dim, c3 * dim, alpha);
    }

    if (Seasons.isPolarExpress(cart)) {
      this.model.setupAnim(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(this.model.renderType(this.textures[3]));
      this.model.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY,
          1.0F, 1.0F, 1.0F, 1.0F);
    }

    ResourceLocation emblemTexture = this.getEmblemTexture(cart);
    if (emblemTexture != null) {
      IVertexBuilder vertexBuilder =
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
    }
    // OpenGL.glDisable(GL11.GL_BLEND);
    matrixStack.popPose();
  }

}
