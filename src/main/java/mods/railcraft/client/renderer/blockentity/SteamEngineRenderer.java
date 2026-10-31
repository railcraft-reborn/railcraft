package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.model.SteamEngineModel;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class SteamEngineRenderer implements BlockEntityRenderer<SteamEngineBlockEntity> {

  private final SteamEngineModel model;

  public SteamEngineRenderer(BlockEntityRendererProvider.Context context) {
    this.model = new SteamEngineModel(context.bakeLayer(RailcraftModelLayers.STEAM_ENGINE));
  }

  @Override
  public void render(SteamEngineBlockEntity engine, float partialTick, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    var texture = RailcraftConstants.rl(
        "textures/entity/steam_engine/" + engine.getTier().getSerializedName() + ".png");
    var consumer = bufferSource.getBuffer(RenderType.entityCutout(texture));

    poseStack.pushPose();
    poseStack.translate(0.5F, 0.5F, 0.5F);
    poseStack.mulPose(engine.getFacing().getRotation());
    poseStack.translate(-0.5F, -0.5F, -0.5F);
    this.model.render(poseStack, consumer, engine.getEnergyStage(), engine.getPistonProgress(),
        packedLight, packedOverlay);
    poseStack.popPose();
  }
}
