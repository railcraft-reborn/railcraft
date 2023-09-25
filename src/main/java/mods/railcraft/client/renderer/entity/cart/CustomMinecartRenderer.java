package mods.railcraft.client.renderer.entity.cart;

import org.apache.commons.lang3.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.api.carts.Routable;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.Directional;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.phys.Vec3;

public abstract class CustomMinecartRenderer<T extends AbstractMinecart>
    extends EntityRenderer<T> {

  public CustomMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(T cart, float yaw, float partialTicks,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
    poseStack.pushPose();
    long i = (long) cart.getId() * 493286711L;
    i = i * i * 4392167121L + i * 98761L;
    float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    poseStack.translate(f, f1, f2);
    double d0 = Mth.lerp(partialTicks, cart.xOld, cart.getX());
    double d1 = Mth.lerp(partialTicks, cart.yOld, cart.getY());
    double d2 = Mth.lerp(partialTicks, cart.zOld, cart.getZ());
    Vec3 vector3d = cart.getPos(d0, d1, d2);
    float pitch = Mth.lerp(partialTicks, cart.xRotO, cart.getXRot());
    if (vector3d != null) {
      Vec3 vector3d1 = cart.getPosOffs(d0, d1, d2, 0.3F);
      Vec3 vector3d2 = cart.getPosOffs(d0, d1, d2, -0.3F);
      if (vector3d1 == null) {
        vector3d1 = vector3d;
      }

      if (vector3d2 == null) {
        vector3d2 = vector3d;
      }

      poseStack.translate(vector3d.x - d0, (vector3d1.y + vector3d2.y) / 2.0D - d1,
          vector3d.z - d2);
      Vec3 vector3d3 = vector3d2.add(-vector3d1.x, -vector3d1.y, -vector3d1.z);
      if (vector3d3.length() != 0.0D) {
        vector3d3 = vector3d3.normalize();
        yaw = (float) (Math.atan2(vector3d3.z, vector3d3.x) * 180 / Math.PI);
        pitch = (float) (Math.atan(vector3d3.y) * 73);
      }
    }
    yaw %= 360;
    if (yaw < 0)
      yaw += 360;
    yaw += 360;

    float serverYaw = cart.getYRot();
    serverYaw += 180;
    serverYaw %= 360;
    if (serverYaw < 0)
      serverYaw += 360;
    serverYaw += 360;

    if (Math.abs(yaw - serverYaw) > 90) {
      yaw += 180;
      pitch = -pitch;
    }

    if (cart instanceof Directional directional) {
      directional.setRenderYaw(yaw);
    }
    poseStack.translate(0.0D, 0.375D, 0.0D);

    if (cart.hasCustomName() && !Seasons.GHOST_TRAIN.equals(cart.getCustomName().getString())
        && !Seasons.POLAR_EXPRESS.equals(cart.getCustomName().getString())) {
      this.renderNameTag(cart, cart.getCustomName(), poseStack, bufferSource, packedLight);
    }

    if (cart instanceof Routable routable) {
      String dest = routable.getDestination();
      if (!StringUtils.isBlank(dest))
        this.renderNameTag(cart, Component.literal(dest), poseStack, bufferSource,
            packedLight);
    }

    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
    poseStack.mulPose(Axis.ZP.rotationDegrees(-pitch));

    float roll = (float) cart.getHurtTime() - partialTicks;
    float damage = cart.getDamage() - partialTicks;
    if (damage < 0.0F)
      damage = 0.0F;
    if (roll > 0.0F) {
      poseStack.mulPose(Axis.XP.rotationDegrees(
          Mth.sin(roll) * roll * damage / 10.0F * (float) cart.getHurtDir()));
    }

    boolean ghostTrain = Seasons.isGhostTrain(cart);
    float colorIntensity = ghostTrain ? 0.5F : 1.0F;

    this.renderBody(cart, partialTicks, poseStack, bufferSource, packedLight,
        colorIntensity, colorIntensity, colorIntensity, ghostTrain ? 0.8F : 1.0F);

    if (ghostTrain) {
      poseStack.pushPose();
      float scale = 1.1F;
      poseStack.scale(scale, scale, scale);
      this.renderBody(cart, partialTicks, poseStack, bufferSource, packedLight,
          1.0F, 1.0F, 1.0F, 0.4F);
      poseStack.popPose();
    }

    poseStack.popPose();
  }

  protected abstract void renderBody(T cart, float partialTicks, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight,
      float red, float green, float blue, float alpha);
}
