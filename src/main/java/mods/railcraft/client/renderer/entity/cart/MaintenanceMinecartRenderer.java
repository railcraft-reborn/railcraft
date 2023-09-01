package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.DeformableMinecartModel;
import mods.railcraft.client.model.MaintenanceLampModel;
import mods.railcraft.client.model.MaintenanceModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public abstract class MaintenanceMinecartRenderer
    extends ContentsMinecartRenderer<MaintenanceMinecart> {

  private static final ResourceLocation LAMP_ON_TEX =
      new ResourceLocation(Railcraft.ID, "textures/entity/minecart/maintenance_lamp_on.png");
  private static final ResourceLocation LAMP_OFF_TEX =
      new ResourceLocation(Railcraft.ID, "textures/entity/minecart/maintenance_lamp_off.png");
  private static final ResourceLocation LAMP_DISABLED_TEX =
      new ResourceLocation(Railcraft.ID, "textures/entity/minecart/maintenance_lamp_disabled.png");

  private final ResourceLocation maintenanceTextureLocation;

  private final MinecartModel<MaintenanceMinecart> bodyModel;
  private final DeformableMinecartModel<MaintenanceMinecart> snowModel;

  private final Model maintenanceModel;
  private final Model lampModel;

  public MaintenanceMinecartRenderer(EntityRendererProvider.Context context,
      ResourceLocation maintenanceTextureLocation) {
    super(context);

    this.maintenanceTextureLocation = maintenanceTextureLocation;

    this.bodyModel = new MinecartModel<>(context.bakeLayer(ModelLayers.MINECART));
    this.snowModel =
        new DeformableMinecartModel<>(context.bakeLayer(RailcraftModelLayers.MINECART_SNOW));
    this.maintenanceModel =
        new MaintenanceModel(context.bakeLayer(RailcraftModelLayers.MAINTENANCE));
    this.lampModel =
        new MaintenanceLampModel(context.bakeLayer(RailcraftModelLayers.MAINTENANCE_LAMP));
  }

  @Override
  public void renderContents(MaintenanceMinecart cart, float partialTicks,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
      float red, float green, float blue, float alpha) {
    var maintenanceVertexConsumer =
        bufferSource.getBuffer(this.maintenanceModel.renderType(this.maintenanceTextureLocation));
    this.maintenanceModel.renderToBuffer(poseStack, maintenanceVertexConsumer, packedLight,
        OverlayTexture.NO_OVERLAY, red, green, blue, alpha);

    poseStack.pushPose();
    //poseStack.translate(-0.5F, -0.5F, -0.5F);

    var blinking = cart.isBlinking();
    ResourceLocation textureLocation;
    if (blinking) {
      textureLocation = LAMP_ON_TEX;
    } else if (cart.mode() == MaintenanceMinecart.Mode.OFF) {
      textureLocation = LAMP_DISABLED_TEX;
    } else {
      textureLocation = LAMP_OFF_TEX;
    }
    var lampVertexConsumer =
        bufferSource.getBuffer(this.lampModel.renderType(textureLocation));
    this.lampModel.renderToBuffer(poseStack, lampVertexConsumer,
        blinking ? RenderUtil.FULL_LIGHT : packedLight,
        OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    poseStack.popPose();
  }

  @Override
  protected EntityModel<? super MaintenanceMinecart> getBodyModel(MaintenanceMinecart cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<? super MaintenanceMinecart> getSnowModel(MaintenanceMinecart cart) {
    return this.snowModel;
  }
}
