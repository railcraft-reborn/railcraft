package mods.railcraft.client.renderer.blockentity;

import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.blockentity.state.FluidManipulatorRenderState;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.manipulator.FluidManipulatorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public abstract class FluidManipulatorRenderer<T extends FluidManipulatorBlockEntity,
    S extends FluidManipulatorRenderState> implements BlockEntityRenderer<T, S> {

  public static final Identifier INTERIOR_TEXTURE_LOCATION =
      RailcraftConstants.id("entity/fluid_manipulator/interior");

  private static final CuboidModel interiorModel =
      new CuboidModel(0.011F, 0.01F, 0.011F, 0.989F, 0.99F, 0.989F);

  @Override
  public void extractRenderState(T blockEntity, S renderState, float partialTick, Vec3 cameraPos,
      ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);
    renderState.tank = blockEntity.getTankManager().get(0);
  }

  @Override
  public void submit(S state, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState) {
    var textureAtlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);

    interiorModel.setAll(interiorModel.new Face()
        .setSprite(textureAtlas.getSprite(INTERIOR_TEXTURE_LOCATION)));
    interiorModel.setPackedLight(state.lightCoords);
    interiorModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);

    collector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, vertexConsumer) -> {
      CuboidModelRenderer.render(interiorModel, pose, vertexConsumer,
          0xFFFFFFFF, CuboidModelRenderer.FaceDisplay.FRONT, true);
    });

    var fluidStack = state.tank.getFluidStack();
    if (fluidStack.getAmount() > 0) {
      float capacity = state.tank.getCapacity();
      var level = Math.min(fluidStack.getAmount() / capacity , 1);
      var fluidMaxY = fluidStack.getFluidType().isLighterThanAir()
          ? 1
          : level;

      var model = FluidRenderer.getFluidModel(fluidStack, 1 - (RenderUtil.SCALED_PIXEL * 2),
          fluidMaxY - (RenderUtil.SCALED_PIXEL * 2), 1 - (RenderUtil.SCALED_PIXEL * 2),
          FluidRenderer.FluidType.STILL);
      poseStack.pushPose();
      poseStack.translate(RenderUtil.SCALED_PIXEL, RenderUtil.SCALED_PIXEL, RenderUtil.SCALED_PIXEL);
      model.setPackedLight(RenderUtil.calculateGlowLight(state.lightCoords, fluidStack));
      model.setPackedOverlay(OverlayTexture.NO_OVERLAY);

      collector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, vertexConsumer) -> {
        CuboidModelRenderer.render(model, pose, vertexConsumer,
            RenderUtil.getColorARGB(fluidStack, 1),
            CuboidModelRenderer.FaceDisplay.FRONT, true);
      });
      poseStack.popPose();
    }
  }
}
