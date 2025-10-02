package mods.railcraft.client.renderer.entity.cart;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import mods.railcraft.client.renderer.entity.state.LocomotiveRenderState;
import mods.railcraft.season.Seasons;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.NewMinecartBehavior;
import net.minecraft.world.entity.vehicle.OldMinecartBehavior;
import net.minecraft.world.phys.Vec3;

public abstract class CustomMinecartRenderer<T extends AbstractMinecart, S extends MinecartRenderState>
    extends EntityRenderer<T, S> {

  private static final Logger LOGGER = LogUtils.getLogger();

  public CustomMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  public void render(S renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
    super.render(renderState, poseStack, multiBufferSource, packedLight);
    poseStack.pushPose();
    long i = renderState.offsetSeed;
    float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    poseStack.translate(f, f1, f2);
    if (renderState.isNewRender) {
      //newRender(renderState, poseStack);
      LOGGER.warn("Tried to render a cart with new rendering, but that is not yet implemented");
      this.oldRender(renderState, poseStack, multiBufferSource, packedLight);
    } else {
      this.oldRender(renderState, poseStack, multiBufferSource, packedLight);
    }

    float roll = renderState.hurtTime;
    if (roll > 0) {
      poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(roll) * roll * renderState.damageTime / 10.0F * (float)renderState.hurtDir));
    }

    boolean ghostTrain = Seasons.isGhostTrain(renderState);
    float colorIntensity = ghostTrain ? 0.5F : 1.0F;

    this.renderBody(renderState, poseStack, multiBufferSource, packedLight,
        ARGB.colorFromFloat(ghostTrain ? 0.8F : 1.0F,
            colorIntensity, colorIntensity, colorIntensity));

    if (ghostTrain) {
      poseStack.pushPose();
      float scale = 1.1F;
      poseStack.scale(scale, scale, scale);
      this.renderBody(renderState, poseStack, multiBufferSource, packedLight,
          ARGB.colorFromFloat(0.4F, 1.0F, 1.0F, 1.0F));
      poseStack.popPose();
    }

    poseStack.popPose();
  }

  private void oldRender(S renderState, PoseStack poseStack,
      MultiBufferSource multiBufferSource, int packedLight) {
    double d0 = renderState.x;
    double d1 = renderState.y;
    double d2 = renderState.z;
    float pitch = renderState.xRot;
    float yaw = renderState.yRot;
    if (renderState.posOnRail != null && renderState.frontPos != null && renderState.backPos != null) {
      Vec3 vector3d1 = renderState.frontPos;
      Vec3 vector3d2 = renderState.backPos;
      poseStack.translate(renderState.posOnRail.x - d0, (vector3d1.y + vector3d2.y) / 2.0 - d1,
          renderState.posOnRail.z - d2);
      Vec3 vector3d3 = vector3d2.add(-vector3d1.x, -vector3d1.y, -vector3d1.z);
      if (vector3d3.length() != 0.0) {
        vector3d3 = vector3d3.normalize();
        yaw = (float)(Math.atan2(vector3d3.z, vector3d3.x) * 180.0 / Math.PI);
        pitch = (float)(Math.atan(vector3d3.y) * 73.0);
      }
    }
    /*yaw %= 360;
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
    }*/

    //FIXME
    /*if (cart instanceof Directional directional) {
      directional.setRenderYaw(yaw);
    }*/
    poseStack.translate(0, 0.375F, 0);

    boolean renderName = false;
    if (renderState.nameTag != null) {
      var customName = renderState.nameTag.getString();
      if (!Seasons.GHOST_TRAIN.equals(customName) && !Seasons.POLAR_EXPRESS.equals(customName)) {
        this.renderNameTag(renderState, renderState.nameTag, poseStack, multiBufferSource, packedLight);
        renderName = true;
      }
    }

    if (renderState instanceof LocomotiveRenderState locomotiveRenderState) {
      String dest = locomotiveRenderState.destination;
      if (!StringUtils.isBlank(dest)) {
        poseStack.pushPose();
        if (renderName) {
          poseStack.translate(0, 0.3F, 0);
        }
        var destination = Component.literal(dest)
            .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC);
        this.renderNameTag(renderState, destination, poseStack, multiBufferSource, packedLight);
        poseStack.popPose();
      }
    }

    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
    poseStack.mulPose(Axis.ZP.rotationDegrees(-pitch));
  }

  public void extractRenderState(T entity, S reusedState, float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    if (entity.getBehavior() instanceof NewMinecartBehavior behavior) {
      newExtractState(entity, behavior, reusedState, partialTick);
      reusedState.isNewRender = true;
    } else if (entity.getBehavior() instanceof OldMinecartBehavior behavior) {
      oldExtractState(entity, behavior, reusedState, partialTick);
      reusedState.isNewRender = false;
    }

    long i = (long)entity.getId() * 493286711L;
    reusedState.offsetSeed = i * i * 4392167121L + i * 98761L;
    reusedState.hurtTime = (float)entity.getHurtTime() - partialTick;
    reusedState.hurtDir = entity.getHurtDir();
    reusedState.damageTime = Math.max(entity.getDamage() - partialTick, 0.0F);
    reusedState.displayOffset = entity.getDisplayOffset();
    reusedState.displayBlockState = entity.getDisplayBlockState();
  }

  private static <T extends AbstractMinecart, S extends MinecartRenderState> void newExtractState(T entity,
      NewMinecartBehavior behavior, S reusedState, float partialTick) {
    if (behavior.cartHasPosRotLerp()) {
      reusedState.renderPos = behavior.getCartLerpPosition(partialTick);
      reusedState.xRot = behavior.getCartLerpXRot(partialTick);
      reusedState.yRot = behavior.getCartLerpYRot(partialTick);
    } else {
      reusedState.renderPos = null;
      reusedState.xRot = entity.getXRot();
      reusedState.yRot = entity.getYRot();
    }
  }

  private static <T extends AbstractMinecart, S extends MinecartRenderState> void oldExtractState(T entity,
      OldMinecartBehavior behavior, S reusedState, float partialTick) {
    reusedState.xRot = entity.getXRot(partialTick);
    reusedState.yRot = entity.getYRot(partialTick);
    double d0 = reusedState.x;
    double d1 = reusedState.y;
    double d2 = reusedState.z;
    Vec3 vec3 = behavior.getPos(d0, d1, d2);
    if (vec3 != null) {
      reusedState.posOnRail = vec3;
      Vec3 vec31 = behavior.getPosOffs(d0, d1, d2, 0.3F);
      Vec3 vec32 = behavior.getPosOffs(d0, d1, d2, -0.3F);
      reusedState.frontPos = Objects.requireNonNullElse(vec31, vec3);
      reusedState.backPos = Objects.requireNonNullElse(vec32, vec3);
    } else {
      reusedState.posOnRail = null;
      reusedState.frontPos = null;
      reusedState.backPos = null;
    }
  }

  protected abstract void renderBody(S renderState, PoseStack poseStack,
      MultiBufferSource multiBufferSource, int packedLight, int color);
}
