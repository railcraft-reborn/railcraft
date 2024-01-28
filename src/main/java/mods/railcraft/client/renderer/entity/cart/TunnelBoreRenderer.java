package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mods.railcraft.api.carts.TunnelBoreHead;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.model.TunnelBoreModel;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TunnelBoreRenderer extends EntityRenderer<TunnelBore> {

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
  public void render(TunnelBore bore, float yaw, float partialTicks,
      PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int packedLight) {
    matrixStack.pushPose();
    long var10 = (long) bore.getId() * 493286711L;
    var10 = var10 * var10 * 4392167121L + var10 * 98761L;
    float tx = (((float) (var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float ty = (((float) (var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float tz = (((float) (var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    matrixStack.translate(tx, ty, tz);

    matrixStack.translate(0F, 0.375F, 0F);

    matrixStack.mulPose(Axis.YP.rotationDegrees(180 - yaw));
    matrixStack.mulPose(Axis.YP.rotationDegrees(90));

    float roll = (float) bore.getHurtTime() - partialTicks;
    float damage = bore.getDamage() - partialTicks;
    if (damage < 0)
      damage = 0;
    if (roll > 0) {
      matrixStack.mulPose(Axis.XP.rotationDegrees(
          Mth.sin(roll) * roll * damage / 10.0F * (float) bore.getHurtDir()));
    }

    // float light = bore.getBrightness();
    // light = light + ((1.0f - light) * 0.4f);

    boolean ghostTrain = Seasons.isGhostTrain(bore);
    float colorIntensity = ghostTrain ? 0.5F : 1.0F;

    TunnelBoreHead head = bore.getBoreHead();
    ResourceLocation textureLocation;
    if (head != null) {
      textureLocation = head.getTextureLocation();
      modelTunnelBore.setRenderBoreHead(true);
    } else {
      textureLocation = TEXTURE;
      modelTunnelBore.setRenderBoreHead(false);
    }

    matrixStack.scale(-1, -1, 1);

    this.modelTunnelBore.setBoreHeadRotation(bore.getBoreRotationAngle());
    this.modelTunnelBore.setBoreActive(bore.isMinecartPowered());
    this.modelTunnelBore.setupAnim(bore, 0, 0, -0.1F, 0, 0);
    VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(
        this.modelTunnelBore.renderType(textureLocation));
    this.modelTunnelBore.renderToBuffer(matrixStack, vertexBuilder, packedLight,
        OverlayTexture.NO_OVERLAY, colorIntensity, colorIntensity, colorIntensity,
        ghostTrain ? 0.8F : 1);
    matrixStack.popPose();
  }

  @Override
  public ResourceLocation getTextureLocation(TunnelBore entity) {
    TunnelBoreHead head = entity.getBoreHead();
    return head == null ? TEXTURE : head.getTextureLocation();
  }
}
