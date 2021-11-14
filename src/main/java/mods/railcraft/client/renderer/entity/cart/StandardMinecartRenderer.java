package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.RailcraftMinecartModel;
import mods.railcraft.season.Seasons;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.ResourceLocation;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class StandardMinecartRenderer<T extends AbstractMinecartEntity>
    extends CustomMinecartRenderer<T> {

  public static final ResourceLocation SNOW_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/carts/cart_snow.png");

  public static final EntityModel<AbstractMinecartEntity> STANDARD_BODY_MODEL =
      new RailcraftMinecartModel<>();

  public static final EntityModel<AbstractMinecartEntity> STANDARD_SNOW_MODEL =
      new RailcraftMinecartModel<>(0.125F);

  public StandardMinecartRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void renderBody(T cart, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight,
      float red, float green, float blue, float alpha) {
    matrixStack.pushPose();
    {
      matrixStack.scale(-1.0F, -1.0F, 1.0F);
      EntityModel<? super T> bodyModel = this.getBodyModel(cart);
      IVertexBuilder bodyVertexBuilder =
          renderTypeBuffer.getBuffer(bodyModel.renderType(this.getTextureLocation(cart)));
      bodyModel.setupAnim(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
      bodyModel.renderToBuffer(matrixStack, bodyVertexBuilder, packedLight,
          OverlayTexture.NO_OVERLAY, red, green, blue, alpha);

      if (Seasons.isPolarExpress(cart)) {
        EntityModel<? super T> snowModel = this.getSnowModel(cart);
        IVertexBuilder snowVertexBuilder =
            renderTypeBuffer.getBuffer(snowModel.renderType(SNOW_TEXTURE));
        snowModel.setupAnim(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        snowModel.renderToBuffer(matrixStack, snowVertexBuilder, packedLight,
            OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      }
    }
    matrixStack.popPose();

    this.renderContents(cart, partialTicks, matrixStack, renderTypeBuffer, packedLight,
        red, green, blue, alpha);
  }

  protected abstract void renderContents(T cart, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight,
      float red, float green, float blue, float alpha);

  protected abstract EntityModel<? super T> getBodyModel(T cart);

  protected abstract EntityModel<? super T> getSnowModel(T cart);
}
