package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.ElectricLocomotiveLampModel;
import mods.railcraft.client.model.ElectricLocomotiveModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.renderer.entity.state.LocomotiveRenderState;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class ElectricLocomotiveRenderer extends DefaultLocomotiveRenderer {

  private final ElectricLocomotiveLampModel<LocomotiveRenderState> lampModel;
  private final Identifier lampTextureOn;
  private final Identifier lampTextureOff;

  public ElectricLocomotiveRenderer(EntityRendererProvider.Context context) {
    super(context, "electric",
        new ElectricLocomotiveModel(context.bakeLayer(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE)),
        new ElectricLocomotiveModel(
            context.bakeLayer(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE_SNOW)));

    this.lampModel =
        new ElectricLocomotiveLampModel<>(
            context.bakeLayer(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE_LAMP));

    this.lampTextureOn =
        RailcraftConstants.id("textures/entity/locomotive/" + modelTag + "/lamp_on.png");
    this.lampTextureOff =
        RailcraftConstants.id("textures/entity/locomotive/" + modelTag + "/lamp_off.png");
  }

  @Override
  protected void renderBody(LocomotiveRenderState renderState, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraState, int color) {
    super.renderBody(renderState, poseStack, collector, cameraState, color);
    poseStack.pushPose();
    poseStack.scale(-1, -1, 1);
    poseStack.translate(0.05F, 0, 0);

    boolean bright = renderState.mode == Locomotive.Mode.RUNNING;

    collector.submitModel(
        this.lampModel,
        renderState,
        poseStack,
        this.lampModel.renderType(bright ? this.lampTextureOn : this.lampTextureOff),
        bright ? RenderUtil.FULL_LIGHT : renderState.lightCoords,
        OverlayTexture.NO_OVERLAY,
        color,
        null,
        0,
        null
    );
    poseStack.popPose();
  }

  @Override
  public LocomotiveRenderState createRenderState() {
    return new LocomotiveRenderState();
  }
}
