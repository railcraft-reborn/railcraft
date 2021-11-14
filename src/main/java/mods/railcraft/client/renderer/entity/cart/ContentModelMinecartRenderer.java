package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.client.model.SimpleTexturedModel;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.vector.Vector3f;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class ContentModelMinecartRenderer<T extends AbstractMinecartEntity>
    extends StandardMinecartRenderer<T> {

  public static final SimpleTexturedModel EMPTY_MODEL = new SimpleTexturedModel();

  public ContentModelMinecartRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderContents(T cart, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight,
      float red, float green, float blue, float alpha) {
    matrixStack.pushPose();
    {
      int blockOffset = cart.getDisplayOffset();
      float scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-0.5D, (blockOffset - 8.0F) / 16.0F, -0.5D);

      BlockState blockstate = cart.getDisplayBlockState();
      if (blockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, matrixStack,
            renderTypeBuffer, packedLight, OverlayTexture.NO_OVERLAY);
      }

      SimpleTexturedModel contents = this.getContentsModel(cart);
      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(contents.renderType(contents.getTexture()));
      contents.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY,
          red, green, blue, alpha);
    }
    matrixStack.popPose();
  }

  protected abstract SimpleTexturedModel getContentsModel(T cart);
}
