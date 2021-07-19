package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.RailcraftMinecartModel;
import mods.railcraft.plugins.SeasonPlugin;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.ResourceLocation;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class StandardCartBodyRenderer<T extends AbstractMinecartEntity>
    extends CustomMinecartRenderer<T> {

  public static final ResourceLocation SNOW_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/carts/cart_snow.png");

  public static final EntityModel<AbstractMinecartEntity> modelSnow =
      new RailcraftMinecartModel<>(0.125f);


  public StandardCartBodyRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void renderBody(
      T cart,
      float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, float red, float green, float blue, float alpha) {
    matrixStack.pushPose();
    {
      matrixStack.scale(-1F, -1F, 1.0F);

      EntityModel<T> core = this.getBodyModel(cart);
      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(core.renderType(this.getTextureLocation(cart)));
      core.setupAnim(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
      core.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, red,
          green, blue, alpha);

      if (SeasonPlugin.isPolarExpress(cart)) {
        EntityModel<T> snow = this.getSnowModel(cart);
        IVertexBuilder snowVertexBuilder =
            renderTypeBuffer.getBuffer(snow.renderType(SNOW_TEXTURE));
        snow.setupAnim(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        core.renderToBuffer(matrixStack, snowVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY,
            1.0F, 1.0F, 1.0F, 1.0F);
      }
    }
    matrixStack.popPose();

    float blockScale = 0.74F;
    matrixStack.scale(blockScale, blockScale, blockScale);
    this.renderContents(cart, partialTicks, matrixStack,
        renderTypeBuffer, packedLight, red, green, blue, alpha);
  }

  protected abstract void renderContents(
      T cart,
      float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, float red, float green, float blue, float alpha);

  protected abstract EntityModel<T> getBodyModel(T cart);

  protected abstract EntityModel<T> getSnowModel(T cart);
}
