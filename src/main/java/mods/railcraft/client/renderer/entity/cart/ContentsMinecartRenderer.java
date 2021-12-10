package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.RenderShape;

/**
 * @author Sm0keySa1m0n
 */
public abstract class ContentsMinecartRenderer<T extends AbstractMinecart>
    extends StandardMinecartRenderer<T> {

  public ContentsMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void renderBody(T cart, float partialTicks, PoseStack poseStack,
      MultiBufferSource renderTypeBuffer, int packedLight,
      float red, float green, float blue, float alpha) {
    super.renderBody(cart, partialTicks, poseStack, renderTypeBuffer, packedLight,
        red, green, blue, alpha);
    poseStack.pushPose();
    {
      var displayOffset = cart.getDisplayOffset();
      var scale = 0.75F;
      poseStack.scale(scale, scale, scale);
      poseStack.translate(-0.5D, (displayOffset - 8.0F) / 16.0F, -0.5D);

      this.renderContents(cart, partialTicks, poseStack, renderTypeBuffer, packedLight, red, green,
          blue, alpha);
    }
    poseStack.popPose();
  }

  @SuppressWarnings("deprecation")
  protected void renderContents(T cart, float partialTicks, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight,
      float red, float green, float blue, float alpha) {
    var blockstate = cart.getDisplayBlockState();
    if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
      poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
      Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, poseStack,
          bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
    }
  }
}
