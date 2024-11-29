package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.DeformableMinecartModel;
import mods.railcraft.client.model.MaintenanceLampModel;
import mods.railcraft.client.model.MaintenanceModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.renderer.entity.state.MaintenanceMinecartRendererState;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public abstract class MaintenanceMinecartRenderer
    extends ContentsMinecartRenderer<MaintenanceMinecart, MaintenanceMinecartRendererState> {

  private static final ResourceLocation LAMP_ON_TEX =
      RailcraftConstants.rl("textures/entity/minecart/maintenance_lamp_on.png");
  private static final ResourceLocation LAMP_OFF_TEX =
      RailcraftConstants.rl("textures/entity/minecart/maintenance_lamp_off.png");
  private static final ResourceLocation LAMP_DISABLED_TEX =
      RailcraftConstants.rl("textures/entity/minecart/maintenance_lamp_disabled.png");

  private final ResourceLocation maintenanceTextureLocation;

  private final DeformableMinecartModel<MaintenanceMinecartRendererState> bodyModel;
  private final DeformableMinecartModel<MaintenanceMinecartRendererState> snowModel;

  private final Model maintenanceModel;
  private final Model lampModel;

  public MaintenanceMinecartRenderer(EntityRendererProvider.Context context,
      ResourceLocation maintenanceTextureLocation) {
    super(context);

    this.maintenanceTextureLocation = maintenanceTextureLocation;

    this.bodyModel = new DeformableMinecartModel<>(context.bakeLayer(ModelLayers.MINECART));
    this.snowModel =
        new DeformableMinecartModel<>(context.bakeLayer(RailcraftModelLayers.MINECART_SNOW));
    this.maintenanceModel =
        new MaintenanceModel(context.bakeLayer(RailcraftModelLayers.MAINTENANCE));
    this.lampModel =
        new MaintenanceLampModel(context.bakeLayer(RailcraftModelLayers.MAINTENANCE_LAMP));
  }

  @Override
  protected void renderContents(MaintenanceMinecartRendererState renderState, PoseStack poseStack,
      MultiBufferSource multiBufferSource, int packedLight, int color) {
    var maintenanceVertexConsumer =
        multiBufferSource.getBuffer(this.maintenanceModel.renderType(this.maintenanceTextureLocation));
    this.maintenanceModel.renderToBuffer(poseStack, maintenanceVertexConsumer, packedLight,
        OverlayTexture.NO_OVERLAY, color);

    poseStack.pushPose();
    //poseStack.translate(-0.5F, -0.5F, -0.5F);

    boolean blinking = renderState.isBlinking;
    ResourceLocation textureLocation;
    if (blinking) {
      textureLocation = LAMP_ON_TEX;
    } else if (renderState.mode == MaintenanceMinecart.Mode.OFF) {
      textureLocation = LAMP_DISABLED_TEX;
    } else {
      textureLocation = LAMP_OFF_TEX;
    }
    var lampVertexConsumer =
        multiBufferSource.getBuffer(this.lampModel.renderType(textureLocation));
    this.lampModel.renderToBuffer(poseStack, lampVertexConsumer,
        blinking ? RenderUtil.FULL_LIGHT : packedLight, OverlayTexture.NO_OVERLAY, color);
    poseStack.popPose();
  }

  @Override
  protected EntityModel<MaintenanceMinecartRendererState> getBodyModel(MaintenanceMinecartRendererState cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<MaintenanceMinecartRendererState> getSnowModel(MaintenanceMinecartRendererState cart) {
    return this.snowModel;
  }

  @Override
  public void extractRenderState(MaintenanceMinecart entity,
      MaintenanceMinecartRendererState reusedState, float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    reusedState.isBlinking = entity.isBlinking();
    reusedState.mode = entity.mode();
  }
}
