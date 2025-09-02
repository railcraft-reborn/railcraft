package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.model.TunnelBoreModel;
import mods.railcraft.client.renderer.entity.state.TunnelBoreRendererState;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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
  public void render(TunnelBoreRendererState renderState, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight) {
    poseStack.pushPose();
    long i = renderState.offsetSeed;
    float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    poseStack.translate(f, f1, f2);

    float yaw = renderState.yRot;
    poseStack.translate(0F, 0.375F, 0F);
    poseStack.mulPose(Axis.YP.rotationDegrees(180 - yaw));
    poseStack.mulPose(Axis.YP.rotationDegrees(90));

    float roll = renderState.hurtTime;
    if (roll > 0) {
      poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(roll) * roll * renderState.damageTime / 10.0F * (float)renderState.hurtDir));
    }

    // float light = bore.getBrightness();
    // light = light + ((1.0f - light) * 0.4f);

    boolean ghostTrain = Seasons.isGhostTrain(renderState);
    float colorIntensity = ghostTrain ? 0.5F : 1.0F;

    var head = renderState.head;
    modelTunnelBore.setRenderBoreHead(head != null);

    poseStack.scale(-1, -1, 1);

    this.modelTunnelBore.setBoreHeadRotation(renderState.rotationAngle);
    this.modelTunnelBore.setBoreActive(renderState.isMinecartPowered);
    this.modelTunnelBore.setupAnim(renderState);
    var textureLocation = head != null ? head.getTextureLocation() : TEXTURE;
    var vertexBuilder = bufferSource.getBuffer(this.modelTunnelBore.renderType(textureLocation));
    this.modelTunnelBore.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY,
        ARGB.colorFromFloat(ghostTrain ? 0.8F : 1, colorIntensity, colorIntensity, colorIntensity));
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
