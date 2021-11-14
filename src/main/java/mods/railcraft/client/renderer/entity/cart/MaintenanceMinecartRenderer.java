package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.MaintenanceLampModel;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.cart.MaintenanceMinecartEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public abstract class MaintenanceMinecartRenderer
    extends ContentModelMinecartRenderer<MaintenanceMinecartEntity> {

  private static final ResourceLocation LAMP_ON_TEX =
      new ResourceLocation(Railcraft.ID, "textures/entity/minecart/maintenance_lamp_on.png");
  private static final ResourceLocation LAMP_OFF_TEX =
      new ResourceLocation(Railcraft.ID, "textures/entity/minecart/maintenance_lamp_off.png");
  private static final ResourceLocation LAMP_DISABLED_TEX =
      new ResourceLocation(Railcraft.ID, "textures/entity/minecart/maintenance_lamp_disabled.png");

  private static final Model lampModel = new MaintenanceLampModel();

  public MaintenanceMinecartRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void renderContents(MaintenanceMinecartEntity cart, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      float red, float green, float blue, float alpha) {
    super.renderContents(cart, partialTicks, matrixStack, renderTypeBuffer, packedLight, red, green,
        blue, alpha);
    matrixStack.pushPose();
    {
      matrixStack.translate(-0.5F, -0.5F, -0.5F);
      
      boolean blinking = cart.isBlinking();
      ResourceLocation textureLocation;
      if (blinking) {
        textureLocation = LAMP_ON_TEX;
      } else if (cart.getMode() == MaintenanceMinecartEntity.Mode.TRANSPORT) {
        textureLocation = LAMP_DISABLED_TEX;
      } else {
        textureLocation = LAMP_OFF_TEX;
      }
      MaintenanceLampModel lampModel = new MaintenanceLampModel();
      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(lampModel.renderType(textureLocation));
      lampModel.renderToBuffer(matrixStack, vertexBuilder,
          blinking ? RenderUtil.FULL_LIGHT : packedLight,
          OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }
    matrixStack.popPose();
  }

  @Override
  protected EntityModel<? super MaintenanceMinecartEntity> getBodyModel(
      MaintenanceMinecartEntity cart) {
    return STANDARD_BODY_MODEL;
  }

  @Override
  protected EntityModel<? super MaintenanceMinecartEntity> getSnowModel(
      MaintenanceMinecartEntity cart) {
    return STANDARD_SNOW_MODEL;
  }
}
