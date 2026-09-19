package mods.railcraft.client.renderer.blockentity;

import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.client.renderer.blockentity.state.RitualBlockRenderState;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RitualBlock;
import mods.railcraft.world.level.block.entity.RitualBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RitualBlockRenderer implements BlockEntityRenderer<RitualBlockEntity, RitualBlockRenderState> {

  private final ItemModelResolver itemModelResolver;

  public RitualBlockRenderer(BlockEntityRendererProvider.Context context) {
    this.itemModelResolver = context.itemModelResolver();
  }

  @Override
  public RitualBlockRenderState createRenderState() {
    return new RitualBlockRenderState();
  }

  @Override
  public void extractRenderState(RitualBlockEntity blockEntity,
      RitualBlockRenderState renderState, float partialTick, Vec3 cameraPos,
      ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);

    renderState.yOffset = blockEntity.getYOffset(partialTick);
    renderState.yaw = blockEntity.getRotationYaw(partialTick);
    renderState.id = (int) blockEntity.getBlockPos().asLong();

    var firestone = new ItemStack(blockEntity.getBlockState().getValue(RitualBlock.CRACKED)
        ? RailcraftItems.CRACKED_FIRESTONE.get()
        : RailcraftItems.REFINED_FIRESTONE.get());

    this.itemModelResolver.updateForTopItem(renderState.itemState, firestone,
        ItemDisplayContext.FIXED, blockEntity.getLevel(), null, renderState.id);
  }

  @Override
  public void submit(RitualBlockRenderState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraState) {
    poseStack.pushPose();
    poseStack.translate(0.5F, 0.95F + state.yOffset, 0.5F);

    poseStack.mulPose(Axis.YP.rotation(state.yaw));

    poseStack.scale(0.6F, 0.6F, 0.6F);

    state.itemState.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.id);
    poseStack.popPose();
  }
}
