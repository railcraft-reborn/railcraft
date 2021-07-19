package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.SimpleCubeModel;
import mods.railcraft.client.model.SimpleTexturedModel;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class CartContentRenderer<T extends AbstractMinecartEntity>
    extends StandardCartBodyRenderer<T> {

  public static final SimpleTexturedModel emptyModel = new SimpleTexturedModel();

  public CartContentRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  static {
    SimpleTexturedModel tank = new SimpleCubeModel();
    tank.setTexture(new ResourceLocation(Railcraft.ID, "textures/carts/cart_tank.png"));
    tank.doBackFaceCulling(false);
    // modelsContents.put(EntityCartTank.class, tank);

    // modelsContents.put(EntityCartGift.class, new ModelGift());

    // ModelTextured<AbstractMinecartEntity> maint = new ModelMaintance<>();
    // maint.setTexture(new ResourceLocation(Railcraft.ID, "cart_undercutter.png"));
    // modelsContents.put(EntityCartUndercutter.class, maint);
    //
    // maint = new ModelMaintance<>();
    // maint.setTexture(new ResourceLocation(Railcraft.ID, "cart_track_relayer.png"));
    // modelsContents.put(EntityCartTrackRelayer.class, maint);
    //
    // maint = new ModelMaintance<>();
    // maint.setTexture(new ResourceLocation(Railcraft.ID, "cart_track_layer.png"));
    // modelsContents.put(EntityCartTrackLayer.class, maint);
    //
    // maint = new ModelMaintance<>();
    // maint.setTexture(new ResourceLocation(Railcraft.ID, "cart_track_remover.png"));
    // modelsContents.put(EntityCartTrackRemover.class, maint);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderContents(T cart, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, float red,
      float green, float blue, float alpha) {

    int blockOffset = cart.getDisplayOffset();
    BlockState blockstate = cart.getDisplayBlockState();
    if (blockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
      matrixStack.pushPose();
      {
        float scale = 0.75F;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-0.5D, (blockOffset - 8.0F) / 16.0F, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, matrixStack,
            renderTypeBuffer, packedLight, OverlayTexture.NO_OVERLAY);
      }
      matrixStack.popPose();
    }

    SimpleTexturedModel contents = this.getContentModel(cart);

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(contents.renderType(contents.getTexture()));
    contents.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, red,
        green, blue, alpha);
  }

  protected abstract SimpleTexturedModel getContentModel(T cart);
}
