package mods.railcraft.client.renderer.blockentity;

import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
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
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractSignalRenderer<T extends AbstractSignalBlockEntity,
    S extends AbstractSignalRenderState> implements BlockEntityRenderer<T, S> {

  private static final Map<SignalAspect, ResourceLocation> ASPECT_TEXTURE_LOCATIONS = Map.of(
      SignalAspect.OFF, RailcraftConstants.rl("entity/signal_aspect/off"),
      SignalAspect.RED, RailcraftConstants.rl("entity/signal_aspect/red"),
      SignalAspect.YELLOW, RailcraftConstants.rl("entity/signal_aspect/yellow"),
      SignalAspect.GREEN, RailcraftConstants.rl("entity/signal_aspect/green"));

  private final CuboidModel signalAspectModel = new CuboidModel(1.0F);

  @Override
  public void extractRenderState(T blockEntity, S renderState, float partialTick, Vec3 cameraPos,
      @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);
    renderState.customName = Optional.ofNullable(blockEntity.getCustomName());
    renderState.level = blockEntity.getLevel();
  }

  @Override
  public void submit(S state, PoseStack poseStack, SubmitNodeCollector collector,
      CameraRenderState cameraState) {

    collector.submitCustomGeometry(poseStack, RenderType.lines(), (pose, vertexConsumer) -> {
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

    final int skyLight = LightTexture.sky(state.lightCoords);
    state.lightCoords = LightTexture.pack(signalAspect.getLampLight(), skyLight);

    this.signalAspectModel.clear();
    this.signalAspectModel.setPackedLight(state.lightCoords);
    this.signalAspectModel.setPackedOverlay(OverlayTexture.NO_OVERLAY);
    this.signalAspectModel.set(direction,
        this.signalAspectModel.new Face()
            .setSprite(textureAtlas.getSprite(ASPECT_TEXTURE_LOCATIONS.get(signalAspect)))
            .setSize(16));

    collector.submitCustomGeometry(poseStack, RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS),
        (pose, vertexConsumer) -> {
      CuboidModelRenderer.render(this.signalAspectModel, pose, vertexConsumer,
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
