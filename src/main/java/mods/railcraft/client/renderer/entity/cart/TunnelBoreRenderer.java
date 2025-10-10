package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.model.TunnelBoreModel;
import mods.railcraft.client.renderer.entity.state.TunnelBoreRendererState;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class TunnelBoreRenderer extends EntityRenderer<TunnelBore, TunnelBoreRendererState> {

  private static final ResourceLocation TEXTURE =
      RailcraftConstants.rl("textures/entity/tunnel_bore/tunnel_bore.png");

  protected TunnelBoreModel modelTunnelBore;

  public TunnelBoreRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.shadowRadius = 0.5F;
    this.modelTunnelBore = new TunnelBoreModel(context.bakeLayer(RailcraftModelLayers.TUNNEL_BORE));
  }

  // A lot of this is copied from the minecart renderer.
  @Override
  public void submit(TunnelBoreRendererState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraState) {
    poseStack.pushPose();
    long i = state.offsetSeed;
    float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    poseStack.translate(f, f1, f2);

    float yaw = state.yRot;
    poseStack.translate(0F, 0.375F, 0F);
    poseStack.mulPose(Axis.YP.rotationDegrees(180 - yaw));
    poseStack.mulPose(Axis.YP.rotationDegrees(90));

    float roll = state.hurtTime;
    if (roll > 0) {
      poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(roll) * roll * state.damageTime / 10.0F * (float)state.hurtDir));
    }

    // float light = bore.getBrightness();
    // light = light + ((1.0f - light) * 0.4f);

    boolean ghostTrain = Seasons.isGhostTrain(state);
    float colorIntensity = ghostTrain ? 0.5F : 1.0F;

    var head = state.head;
    modelTunnelBore.setRenderBoreHead(head != null);

    poseStack.scale(-1, -1, 1);

    this.modelTunnelBore.setBoreHeadRotation(state.rotationAngle);
    this.modelTunnelBore.setBoreActive(state.isMinecartPowered);
    var textureLocation = head != null ? head.getTextureLocation() : TEXTURE;
    collector.submitModel(
        this.modelTunnelBore,
        state,
        poseStack,
        this.modelTunnelBore.renderType(textureLocation),
        state.lightCoords,
        OverlayTexture.NO_OVERLAY,
        ARGB.colorFromFloat(ghostTrain ? 0.8F : 1, colorIntensity, colorIntensity, colorIntensity),
        null,
        0,
        null
    );
    poseStack.popPose();
  }

  @Override
  public void extractRenderState(TunnelBore entity, TunnelBoreRendererState reusedState,
      float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    reusedState.xRot = entity.getXRot(partialTick);
    reusedState.yRot = entity.getYRot(partialTick);
    reusedState.head = entity.getBoreHead();
    reusedState.rotationAngle = entity.getBoreRotationAngle();
    reusedState.isMinecartPowered = entity.isMinecartPowered();
  }

  @Override
  public TunnelBoreRendererState createRenderState() {
    return new TunnelBoreRendererState();
  }
}
