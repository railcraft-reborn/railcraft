package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.world.entity.vehicle.RailcraftMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;

/**
 * @author Sm0keySa1m0n
 */
public abstract class ContentsMinecartRenderer<T extends RailcraftMinecart, S extends RailcraftMinecartRenderState>
    extends StandardMinecartRenderer<T, S> {

  public ContentsMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  protected void renderBody(S renderState, PoseStack poseStack,
      MultiBufferSource multiBufferSource, int packedLight, int color) {
    super.renderBody(renderState, poseStack, multiBufferSource, packedLight, color);
    poseStack.pushPose();
    var displayOffset = renderState.displayOffset;
    var scale = 0.75F;
    poseStack.scale(scale, scale, scale);
    if (!renderState.displayBlockState.is(Blocks.AIR)) {
      poseStack.translate(-0.5F, (displayOffset - 8.0F) / 16.0F, 0.5F);
    } else {
      poseStack.translate(-0.5F, (displayOffset - 8.0F) / 16.0F, -0.5F);
    }

    this.renderContents(renderState, poseStack, multiBufferSource, packedLight, color);
    poseStack.popPose();
  }

  @SuppressWarnings("deprecation")
  protected void renderContents(S renderState, PoseStack poseStack,
      MultiBufferSource multiBufferSource, int packedLight, int color) {
    var blockstate = renderState.displayBlockState;
    if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
      poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
      Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, poseStack,
          multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY);
    }
  }
}
