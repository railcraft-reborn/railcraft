/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.client.model.ElectricLocomotiveModel;
import mods.railcraft.client.model.SimpleModel;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.entity.LocomotiveEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class ElectricLocomotiveRenderer extends DefaultLocomotiveRenderer {

  private static final ModelLamp LAMP = new ModelLamp();
  private final ResourceLocation lampTextureOn;
  private final ResourceLocation lampTextureOff;

  public ElectricLocomotiveRenderer(EntityRendererManager dispatcher) {
    super(dispatcher, "electric", new ElectricLocomotiveModel(),
        new ElectricLocomotiveModel(0.125F));
    this.lampTextureOn = new ResourceLocation(Railcraft.ID,
        "textures/entity/locomotive/" + modelTag + "/lamp_on.png");
    this.lampTextureOff = new ResourceLocation(Railcraft.ID,
        "textures/entity/locomotive/" + modelTag + "/lamp_off.png");
    this.setEmblemPosition(0.2F, -0.03F, -0.41F, -0.505F);
  }

  @Override
  public void renderBody(LocomotiveEntity cart, float time, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, float red, float green, float blue,
      float alpha) {
    super.renderBody(cart, time, matrixStack, renderTypeBuffer, packedLight, red, green, blue,
        alpha);
    matrixStack.pushPose();

    matrixStack.scale(-1, -1, 1);
    matrixStack.translate(0.05F, 0.0F, 0.0F);

    boolean bright = ((LocomotiveEntity) cart).getMode() == LocomotiveEntity.Mode.RUNNING;

    IVertexBuilder vertexBuilder = bright
        ? renderTypeBuffer.getBuffer(LAMP.renderType(lampTextureOn))
        : renderTypeBuffer.getBuffer(LAMP.renderType(lampTextureOff));

    LAMP.renderToBuffer(matrixStack, vertexBuilder, bright ? RenderUtil.FULL_LIGHT : packedLight,
        OverlayTexture.NO_OVERLAY, red, green, blue, alpha);

    matrixStack.popPose();
  }

  private static class ModelLamp extends SimpleModel {

    public ModelLamp() {
      this.renderer.setTexSize(16, 16);
      this.renderer.addBox("bulb", -22F, -17F, -9F, 1, 2, 2, 0.0F, 1, 1);
      this.renderer.setPos(8F, 8F, 8F);
    }
  }
}
