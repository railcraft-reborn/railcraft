package mods.railcraft.client.renderer.blockentity;

import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.renderer.blockentity.state.AbstractSignalRenderState;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractSignalRenderer<T extends AbstractSignalBlockEntity,
    S extends AbstractSignalRenderState> implements BlockEntityRenderer<T, S> {

  private static final Map<SignalAspect, Identifier> ASPECT_TEXTURE_LOCATIONS = Map.of(
      SignalAspect.OFF, RailcraftConstants.id("entity/signal_aspect/off"),
      SignalAspect.RED, RailcraftConstants.id("entity/signal_aspect/red"),
      SignalAspect.YELLOW, RailcraftConstants.id("entity/signal_aspect/yellow"),
      SignalAspect.GREEN, RailcraftConstants.id("entity/signal_aspect/green"));

  @Override
  public void extractRenderState(T blockEntity, S renderState, float partialTick, Vec3 cameraPos,
      ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);
    renderState.customName = Optional.ofNullable(blockEntity.getCustomName());
    renderState.level = blockEntity.getLevel();
  }

  @Override
  public void submit(S state, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState) {

    collector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, vertexConsumer) -> {
      if (state.level != null) {
        var blockEntity = state.level.getBlockEntity(state.blockPos);
        if (blockEntity == null) {
          return;
        }
        SignalAuraRenderUtil.tryRenderSignalAura(blockEntity, pose, vertexConsumer);
      }
    });

    state.customName.ifPresent(name -> {
      RenderUtil.renderBlockHoverText(collector, state.blockPos, name, poseStack, state.lightCoords);
    });
  }

  protected void renderSignalAspect(S state, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState, SignalAspect signalAspect, Direction direction) {

    var textureAtlas =
        Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);

    final int skyLight = LightCoordsUtil.sky(state.lightCoords);
    final int lightCoords = LightCoordsUtil.pack(signalAspect.getLampLight(), skyLight);

    var signalAspectModel = new CuboidModel(1.0F);
    signalAspectModel.setPackedLight(lightCoords);
    signalAspectModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
    signalAspectModel.set(direction,
        signalAspectModel.new Face()
            .setSprite(textureAtlas.getSprite(ASPECT_TEXTURE_LOCATIONS.get(signalAspect)))
            .setSize(16));

    collector.submitCustomGeometry(poseStack,
        RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS),
        (pose, vertexConsumer) -> {
      CuboidModelRenderer.render(signalAspectModel, pose, vertexConsumer,
          0xFFFFFFFF, FaceDisplay.FRONT, false);
    });
  }

  @Override
  public AABB getRenderBoundingBox(T blockEntity) {
    var pos = blockEntity.getBlockPos();
    return new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1,
        pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
  }
}
