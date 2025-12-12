package mods.railcraft.client.renderer.blockentity;

import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.blockentity.state.FluidLoaderRenderState;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.manipulator.FluidLoaderBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FluidLoaderRenderer extends FluidManipulatorRenderer<FluidLoaderBlockEntity, FluidLoaderRenderState> {

  private static final float PIPE_OFFSET = 5 * RenderUtil.PIXEL;
  private static final Identifier PIPE_SIDE_TEXTURE_LOCATION =
      RailcraftConstants.id("entity/fluid_loader/pipe_side");
  private static final Identifier PIPE_END_TEXTURE_LOCATION =
      RailcraftConstants.id("entity/fluid_loader/pipe_end");
  private static final CuboidModel PIPE_MODEL = new CuboidModel(PIPE_OFFSET, 0, PIPE_OFFSET,
      1 - PIPE_OFFSET, RenderUtil.PIXEL, 1 - PIPE_OFFSET);

  @Override
  public FluidLoaderRenderState createRenderState() {
    return new FluidLoaderRenderState();
  }

  @Override
  public void extractRenderState(FluidLoaderBlockEntity blockEntity,
      FluidLoaderRenderState renderState, float partialTick, Vec3 cameraPos,
      ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
    super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);
    renderState.pipeLength = blockEntity.getPipeLength(partialTick);
  }

  @Override
  public void submit(FluidLoaderRenderState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraState) {
    super.submit(state, poseStack, collector, cameraState);
    var textureAtlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);

    CuboidModel.Face sideFace = PIPE_MODEL.new Face()
        .setSprite(textureAtlas.getSprite(PIPE_SIDE_TEXTURE_LOCATION));
    PIPE_MODEL.set(Direction.NORTH, sideFace);
    PIPE_MODEL.set(Direction.SOUTH, sideFace);
    PIPE_MODEL.set(Direction.EAST, sideFace);
    PIPE_MODEL.set(Direction.WEST, sideFace);

    CuboidModel.Face endFace = PIPE_MODEL.new Face()
        .setSprite(textureAtlas.getSprite(PIPE_END_TEXTURE_LOCATION));
    PIPE_MODEL.set(Direction.UP, endFace);
    PIPE_MODEL.set(Direction.DOWN, endFace);

    PIPE_MODEL.setPackedLight(state.lightCoords);
    PIPE_MODEL.setPackedOverlay(OverlayTexture.NO_OVERLAY);

    poseStack.pushPose();
    PIPE_MODEL.setMinY(RenderUtil.PIXEL - state.pipeLength);
    collector.submitCustomGeometry(poseStack,
        RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS), (pose, vertexConsumer) -> {
      CuboidModelRenderer.render(PIPE_MODEL, pose, vertexConsumer, 0xFFFFFFFF,
          CuboidModelRenderer.FaceDisplay.BOTH, false);
    });
    poseStack.popPose();
  }

  @Override
  public AABB getRenderBoundingBox(FluidLoaderBlockEntity blockEntity) {
    var pos = blockEntity.getBlockPos();
    return new AABB(pos.getX(), pos.getY() - 1, pos.getZ(),
        pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
  }
}
