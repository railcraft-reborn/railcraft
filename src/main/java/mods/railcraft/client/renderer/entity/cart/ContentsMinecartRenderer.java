package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.world.entity.vehicle.RailcraftMinecart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * @author Sm0keySa1m0n
 */
public abstract class ContentsMinecartRenderer<T extends RailcraftMinecart, S extends RailcraftMinecartRenderState>
    extends StandardMinecartRenderer<T, S> {
  public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
  private final BlockModelResolver blockModelResolver;

  public ContentsMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.blockModelResolver = context.getBlockModelResolver();
  }

  @Override
  protected void renderBody(S renderState, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState, int color) {
    super.renderBody(renderState, poseStack, collector, cameraState, color);
    poseStack.pushPose();
    var displayOffset = renderState.displayOffset;
    var scale = 0.75F;
    poseStack.scale(scale, scale, scale);
    if (!renderState.displayBlockModel.isEmpty()) {
      poseStack.translate(-0.5F, (displayOffset - 8.0F) / 16.0F, 0.5F);
    } else {
      poseStack.translate(-0.5F, (displayOffset - 8.0F) / 16.0F, -0.5F);
    }

    this.renderContents(renderState, poseStack, collector, color);
    poseStack.popPose();
  }

  @Override
  public void extractRenderState(T entity, S reusedState, float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    this.blockModelResolver.update(reusedState.displayBlockModel, entity.getDisplayBlockState(),
        BLOCK_DISPLAY_CONTEXT);
  }

  protected void renderContents(S renderState, PoseStack poseStack,
      SubmitNodeCollector collector, int color) {
    var displayBlockModel = renderState.displayBlockModel;
    if (!displayBlockModel.isEmpty()) {
      poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
      displayBlockModel.submitMultiLayer(poseStack, collector, renderState.lightCoords,
          OverlayTexture.NO_OVERLAY, renderState.outlineColor);
    }
  }
}
