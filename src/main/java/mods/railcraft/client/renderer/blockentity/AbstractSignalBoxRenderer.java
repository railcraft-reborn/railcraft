package mods.railcraft.client.renderer.blockentity;

import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.renderer.blockentity.state.AbstractSignalBoxRenderState;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
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

public abstract class AbstractSignalBoxRenderer
    implements BlockEntityRenderer<AbstractSignalBoxBlockEntity, AbstractSignalBoxRenderState> {

  private static final Map<SignalAspect, ResourceLocation> ASPECT_TEXTURE_LOCATIONS = Map.of(
      SignalAspect.OFF, RailcraftConstants.rl("entity/signal_box_aspect/off"),
      SignalAspect.RED, RailcraftConstants.rl("entity/signal_box_aspect/red"),
      SignalAspect.YELLOW, RailcraftConstants.rl("entity/signal_box_aspect/yellow"),
      SignalAspect.GREEN, RailcraftConstants.rl("entity/signal_box_aspect/green"));

  private static final ResourceLocation SIDE_TEXTURE_LOCATION =
      RailcraftConstants.rl("entity/signal_box/side");
  private static final ResourceLocation CONNECTED_SIDE_TEXTURE_LOCATION =
      RailcraftConstants.rl("entity/signal_box/connected_side");
  private static final ResourceLocation BOTTOM_TEXTURE_LOCATION =
      RailcraftConstants.rl("entity/signal_box/bottom");

  private final CuboidModel model =
      new CuboidModel(2 / 16F, 0, 2 / 16.0F, 14 / 16.0F, 15 / 16.0F, 14 / 16.0F);

  protected abstract ResourceLocation getTopTextureLocation();

  @Override
  public int getViewDistance() {
    return 512;
  }

  @Override
  public AbstractSignalBoxRenderState createRenderState() {
    return new AbstractSignalBoxRenderState();
  }

  @Override
  public void extractRenderState(AbstractSignalBoxBlockEntity blockEntity,
      AbstractSignalBoxRenderState renderState, float partialTick, Vec3 cameraPos,
      @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);
    renderState.customName = Optional.ofNullable(blockEntity.getCustomName());
    renderState.blockEntity = blockEntity;
    for (var direction : Direction.Plane.HORIZONTAL) {
      var isConnected = SignalBoxBlock.isConnected(blockEntity.getBlockState(), direction);
      renderState.directionConnections.put(direction, isConnected);

      var aspect = blockEntity.getSignalAspect(direction).getDisplayAspect();
      renderState.directionSignalAspects.put(direction, aspect);
    }
  }

  @Override
  public void submit(AbstractSignalBoxRenderState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
    collector.submitCustomGeometry(poseStack, RenderType.lines(), (pose, vertexConsumer) -> {
      SignalAuraRenderUtil.tryRenderSignalAura(state.blockEntity, pose, vertexConsumer);
    });


    state.customName.ifPresent(name -> {
      RenderUtil.renderBlockHoverText(collector, state.blockPos, name, poseStack, state.lightCoords);
    });

    var textureAtlas =
        Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);

    this.model.setPackedLight(state.lightCoords);
    this.model.setPackedOverlay(OverlayTexture.NO_OVERLAY);

    this.model.set(Direction.UP, this.model.new Face()
        .setSprite(textureAtlas.getSprite(this.getTopTextureLocation()))
        .setSize(16));
    this.model.set(Direction.DOWN, this.model.new Face()
        .setSprite(textureAtlas.getSprite(BOTTOM_TEXTURE_LOCATION))
        .setSize(16));

    var renderType = RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS);
    collector.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
      for (var direction : Direction.Plane.HORIZONTAL) {
        var isConnected = state.directionConnections.get(direction);
        this.model.set(direction, this.model.new Face()
            .setSprite(textureAtlas.getSprite(isConnected
                ? CONNECTED_SIDE_TEXTURE_LOCATION
                : SIDE_TEXTURE_LOCATION))
            .setSize(16));
      }
      CuboidModelRenderer.render(this.model, pose, vertexConsumer,
          0xFFFFFFFF, FaceDisplay.BOTH, false);
    });

    collector.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
      for (var direction : Direction.Plane.HORIZONTAL) {
        if (state.directionConnections.get(direction)) {
          this.model.disable(direction);
        } else {
          var aspect = state.directionSignalAspects.get(direction);
          final int skyLight = LightTexture.sky(state.lightCoords);
          final int facePackedLight = LightTexture.pack(aspect.getLampLight(), skyLight);
          this.model.set(direction, this.model.new Face()
                  .setSprite(textureAtlas.getSprite(ASPECT_TEXTURE_LOCATIONS.get(aspect)))
                  .setSize(16)
                  .setPackedLight(facePackedLight)
                  .setPackedOverlay(OverlayTexture.NO_OVERLAY));
        }
      }
      CuboidModelRenderer.render(this.model, pose, vertexConsumer,
          0xFFFFFFFF, FaceDisplay.BOTH, false);
    });
  }

  @Override
  public AABB getRenderBoundingBox(AbstractSignalBoxBlockEntity blockEntity) {
    var pos = blockEntity.getBlockPos();
    return new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1,
        pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
  }
}
