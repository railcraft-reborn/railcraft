package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.ElectricLocomotiveLampModel;
import mods.railcraft.client.model.ElectricLocomotiveModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElectricLocomotiveRenderer extends DefaultLocomotiveRenderer {

  private final ElectricLocomotiveLampModel lampModel;
  private final ResourceLocation lampTextureOn;
  private final ResourceLocation lampTextureOff;

  public ElectricLocomotiveRenderer(EntityRendererProvider.Context context) {
    super(context, "electric",
        new ElectricLocomotiveModel(context.bakeLayer(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE)),
        new ElectricLocomotiveModel(
            context.bakeLayer(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE_SNOW)));

    this.lampModel =
        new ElectricLocomotiveLampModel(
            context.bakeLayer(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE_LAMP));

    this.lampTextureOn = new ResourceLocation(Railcraft.ID,
        "textures/entity/locomotive/" + modelTag + "/lamp_on.png");
    this.lampTextureOff = new ResourceLocation(Railcraft.ID,
        "textures/entity/locomotive/" + modelTag + "/lamp_off.png");
    this.setEmblemPosition(0.2F, -0.03F, -0.41F, -0.505F);
  }

  @Override
  public void renderBody(Locomotive cart, float time, PoseStack poseStack,
      MultiBufferSource renderTypeBuffer, int packedLight, float red, float green, float blue,
      float alpha) {
    super.renderBody(cart, time, poseStack, renderTypeBuffer, packedLight, red, green, blue,
        alpha);
    poseStack.pushPose();
    {
      poseStack.scale(-1, -1, 1);
      poseStack.translate(0.05F, 0.0F, 0.0F);

      boolean bright = ((Locomotive) cart).getMode() == Locomotive.Mode.RUNNING;

      VertexConsumer vertexBuilder = bright
          ? renderTypeBuffer.getBuffer(this.lampModel.renderType(this.lampTextureOn))
          : renderTypeBuffer.getBuffer(this.lampModel.renderType(this.lampTextureOff));

      this.lampModel.renderToBuffer(poseStack, vertexBuilder,
          bright ? RenderUtil.FULL_LIGHT : packedLight, OverlayTexture.NO_OVERLAY,
          red, green, blue, alpha);

    }
    poseStack.popPose();
  }


}
