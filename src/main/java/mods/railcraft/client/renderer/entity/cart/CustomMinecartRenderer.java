package mods.railcraft.client.renderer.entity.cart;

import org.apache.commons.lang3.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.api.carts.IAlternateCartTexture;
import mods.railcraft.api.carts.IRoutableCart;
import mods.railcraft.plugins.SeasonPlugin;
import mods.railcraft.world.entity.IDirectionalCart;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;

public abstract class CustomMinecartRenderer<T extends AbstractMinecartEntity>
    extends MinecartRenderer<T> {

  public CustomMinecartRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(T cart, float yaw, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight) {
    matrixStack.pushPose();
    long i = (long) cart.getId() * 493286711L;
    i = i * i * 4392167121L + i * 98761L;
    float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    matrixStack.translate((double) f, (double) f1, (double) f2);
    double d0 = MathHelper.lerp((double) partialTicks, cart.xOld, cart.getX());
    double d1 = MathHelper.lerp((double) partialTicks, cart.yOld, cart.getY());
    double d2 = MathHelper.lerp((double) partialTicks, cart.zOld, cart.getZ());
    Vector3d vector3d = cart.getPos(d0, d1, d2);
    float pitch = MathHelper.lerp(partialTicks, cart.xRotO, cart.xRot);
    if (vector3d != null) {
      Vector3d vector3d1 = cart.getPosOffs(d0, d1, d2, (double) 0.3F);
      Vector3d vector3d2 = cart.getPosOffs(d0, d1, d2, (double) -0.3F);
      if (vector3d1 == null) {
        vector3d1 = vector3d;
      }

      if (vector3d2 == null) {
        vector3d2 = vector3d;
      }

      matrixStack.translate(vector3d.x - d0, (vector3d1.y + vector3d2.y) / 2.0D - d1,
          vector3d.z - d2);
      Vector3d vector3d3 = vector3d2.add(-vector3d1.x, -vector3d1.y, -vector3d1.z);
      if (vector3d3.length() != 0.0D) {
        vector3d3 = vector3d3.normalize();
        yaw = (float) (Math.atan2(vector3d3.z, vector3d3.x) * 180.0D / Math.PI);
        pitch = (float) (Math.atan(vector3d3.y) * 73.0D);
      }
    }
    yaw %= 360;
    if (yaw < 0)
      yaw += 360;
    yaw += 360;

    double serverYaw = cart.yRot;
    serverYaw += 180;
    serverYaw %= 360;
    if (serverYaw < 0)
      serverYaw += 360;
    serverYaw += 360;

    if (Math.abs(yaw - serverYaw) > 90) {
      yaw += 180;
      pitch = -pitch;
    }

    if (cart instanceof IDirectionalCart) {
      ((IDirectionalCart) cart).setRenderYaw(yaw);
    }
    matrixStack.translate(0.0D, 0.375D, 0.0D);

    if (cart.hasCustomName() && !SeasonPlugin.GHOST_TRAIN.equals(cart.getCustomName().getContents())
        && !SeasonPlugin.POLAR_EXPRESS.equals(cart.getCustomName().getContents())) {
      this.renderNameTag(cart, cart.getCustomName(), matrixStack, renderTypeBuffer, packedLight);
    }

    if (cart instanceof IRoutableCart) {
      String dest = ((IRoutableCart) cart).getDestination();
      if (!StringUtils.isBlank(dest))
        this.renderNameTag(cart, new StringTextComponent(dest), matrixStack, renderTypeBuffer,
            packedLight);
    }

    matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-pitch));

    float roll = (float) cart.getHurtTime() - partialTicks;
    float damage = cart.getDamage() - partialTicks;
    if (damage < 0.0F)
      damage = 0.0F;
    if (roll > 0.0F) {
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(
          MathHelper.sin(roll) * roll * damage / 10.0F * (float) cart.getHurtDir()));
    }


    // TODO Ghost train

    // boolean ghost = SeasonPlugin.isGhostTrain(cart);
    // if (ghost) {
    // GlStateManager.enableBlend();
    // GlStateManager.disableLighting();
    // GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
    // GlStateManager.DestFactor.DST_COLOR);
    // float color = 0.5F;
    // GlStateManager.color(color, color, color, 0.8F);
    // }


    matrixStack.pushPose();
    {
      // getBodyRenderer(cart.getClass()).render(this, cart, partialTicks, matrixStack,
      // renderTypeBuffer, packedLight, 1.0F, 1.0F, 1.0F, 1.0F);
      this.renderBody(cart, partialTicks, matrixStack, renderTypeBuffer, packedLight, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.popPose();

    // if (ghost) {
    // float scale = 1.1F;
    // p_225623_4_.scale(scale, scale, scale);
    // GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
    // GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
    // float color = 1F;
    // GlStateManager.color(color, color, color, 0.15F);
    // doRender(cart, light, partialTicks, p_225623_4_, renderTypeBuffer, packedLight);
    // GlStateManager.color(1F, 1F, 1F, 1F);
    //
    // GlStateManager.disableBlend();
    // GlStateManager.enableLighting();
    // GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
    // GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    // }


    matrixStack.popPose();
  }

  protected abstract void renderBody(T cart, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, float red, float green, float blue,
      float alpha);

  @Override
  public ResourceLocation getTextureLocation(T cart) {
    if (cart instanceof IAlternateCartTexture)
      return ((IAlternateCartTexture) cart).getTextureFile();
    return super.getTextureLocation(cart);
  }

  public EntityModel<T> getMinecartModel() {
    return this.model;
  }
}
