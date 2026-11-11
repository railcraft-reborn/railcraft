package mods.railcraft.client.renderer.blockentity;

import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.model.SteamEngineModel;
import mods.railcraft.client.renderer.blockentity.state.SteamEngineRenderState;
import mods.railcraft.world.level.block.SteamEngineBlock;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class SteamEngineRenderer
    implements BlockEntityRenderer<SteamEngineBlockEntity, SteamEngineRenderState> {

  private final SteamEngineModel model;

  public SteamEngineRenderer(BlockEntityRendererProvider.Context context) {
    this.model = new SteamEngineModel(context.bakeLayer(RailcraftModelLayers.STEAM_ENGINE));
  }

  public static Identifier getTexture(SteamEngineBlock.Tier tier) {
    return RailcraftConstants.id(
        "textures/entity/steam_engine/" + tier.getSerializedName() + ".png");
  }

  @Override
  public SteamEngineRenderState createRenderState() {
    return new SteamEngineRenderState();
  }

  @Override
  public void extractRenderState(SteamEngineBlockEntity engine, SteamEngineRenderState state,
      float partialTick, Vec3 cameraPosition,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(engine, state, partialTick, cameraPosition,
        breakProgress);
    state.tier = engine.getTier();
    state.facing = engine.getFacing();
    state.stage = engine.getEnergyStage();
    state.progress = engine.getPistonProgress();
  }

  @Override
  public void submit(SteamEngineRenderState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
    poseStack.pushPose();
    poseStack.translate(0.5F, 0.5F, 0.5F);
    poseStack.rotate(state.facing.getRotation());
    poseStack.translate(-0.5F, -0.5F, -0.5F);
    collector.submitModel(
        this.model,
        new SteamEngineModel.State(state.stage, state.progress),
        poseStack,
        this.model.renderType(getTexture(state.tier)),
        state.lightCoords,
        OverlayTexture.NO_OVERLAY,
        -1,
        null,
        0);
    if (state.breakProgress != null) {
      collector.submitCrumblingOverlay(this.model, new SteamEngineModel.State(state.stage, state.progress), poseStack, this.model.renderType(getTexture(state.tier)), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, state.breakProgress);
    }
    poseStack.popPose();
  }
}
