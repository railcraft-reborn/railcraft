package mods.railcraft.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.world.entity.cart.TunnelBoreEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class TunnelBoreModel extends EntityModel<TunnelBoreEntity> {

  private ModelRenderer base;
  private ModelRenderer rear;
  private ModelRenderer boreHead;
  private ModelRenderer boreFrame;
  private ModelRenderer furnaceBox;
  private ModelRenderer furnaceIdle;
  private ModelRenderer furnaceActive;
  private ModelRenderer stack;
  private boolean active;
  private boolean renderBoreHead;

  public TunnelBoreModel() {
    super(RenderType::entityTranslucentCull);
    base = new ModelRenderer(this, 0, 0).setTexSize(256, 256);
    rear = new ModelRenderer(this, 0, 18).setTexSize(256, 256);
    furnaceBox = new ModelRenderer(this, 99, 112).setTexSize(256, 256);
    boreHead = new ModelRenderer(this, 59, 78).setTexSize(256, 256);
    boreFrame = new ModelRenderer(this, 96, 18).setTexSize(256, 256);
    furnaceIdle = new ModelRenderer(this, 0, 66).setTexSize(256, 256);
    furnaceActive = new ModelRenderer(this, 37, 66).setTexSize(256, 256);
    stack = new ModelRenderer(this, 74, 66).setTexSize(256, 256);
    // sideModels[3] = new ModelRenderer(0, 0);
    // sideModels[4] = new ModelRenderer(0, 0);
    // sideModels[5] = new ModelRenderer(44, 10);
    int offset = 3;

    int baseYOffset = 2;
    int baseXOffset = 0;
    int baseLength = 80;
    int baseWidth = 16;
    int baseHeight = 2;

    int rearYOffset = 0;
    int rearXOffset = 27;
    int rearLength = 42;
    int rearWidth = 24;
    int rearHeight = 24;

    int boxXOffset = -6;
    int boxLength = 24;
    int boxWidth = 32;
    int boxHeight = 32;

    int furnaceXOffset = -6;
    int furnaceYOffset = 6;
    int furnace1ZOffset = 17;
    int furnace2ZOffset = -17;
    int furnaceLength = 16;
    int furnaceWidth = 2;
    int furnaceHeight = 16;

    int stackXOffset = -6;
    int stackYOffset = 22;
    int stack1ZOffset = 15;
    int stack2ZOffset = -15;
    int stackLength = 4;
    int stackWidth = 4;
    int stackHeight = 16;

    int frameXOffset = -18;
    int frameLength = 32;
    int frameWidth = 48;
    int frameHeight = 43;

    int headXOffset = -50;
    int headLength = 2;
    int headWidth = 32;
    int headHeight = 32;
    int headYOffset = headHeight / 2 - frameHeight / 2 + offset;

    base.addBox(-baseLength / 2 + baseXOffset, -baseHeight + offset + baseYOffset, -baseWidth / 2,
        baseLength, baseHeight, baseWidth, 0.0F);
    rear.addBox(-rearLength / 2 + rearXOffset, -rearHeight + offset + rearYOffset, -rearWidth / 2,
        rearLength, rearHeight, rearWidth, 0.0F);
    furnaceBox.addBox(-boxLength / 2 + boxXOffset, -boxHeight + offset, -boxWidth / 2, boxLength,
        boxHeight, boxWidth, 0.0F);
    furnaceIdle.addBox(-furnaceLength / 2 + furnaceXOffset,
        -furnaceHeight + offset - furnaceYOffset, -furnaceWidth / 2 + furnace1ZOffset,
        furnaceLength, furnaceHeight, furnaceWidth, 0.0F);
    furnaceIdle.addBox(-furnaceLength / 2 + furnaceXOffset,
        -furnaceHeight + offset - furnaceYOffset, -furnaceWidth / 2 + furnace2ZOffset,
        furnaceLength, furnaceHeight, furnaceWidth, 0.0F);
    furnaceActive.addBox(-furnaceLength / 2 + furnaceXOffset,
        -furnaceHeight + offset - furnaceYOffset, -furnaceWidth / 2 + furnace1ZOffset,
        furnaceLength, furnaceHeight, furnaceWidth, 0.0F);
    furnaceActive.addBox(-furnaceLength / 2 + furnaceXOffset,
        -furnaceHeight + offset - furnaceYOffset, -furnaceWidth / 2 + furnace2ZOffset,
        furnaceLength, furnaceHeight, furnaceWidth, 0.0F);
    stack.addBox(-stackLength / 2 + stackXOffset, -stackHeight + offset - stackYOffset,
        -stackWidth / 2 + stack1ZOffset, stackLength, stackHeight, stackWidth, 0.0F);
    stack.addBox(-stackLength / 2 + stackXOffset, -stackHeight + offset - stackYOffset,
        -stackWidth / 2 + stack2ZOffset, stackLength, stackHeight, stackWidth, 0.0F);
    boreFrame.addBox(-frameLength + frameXOffset, -frameHeight + offset, -frameWidth / 2,
        frameLength, frameHeight, frameWidth, 0.0F);

    boreHead.addBox(-headLength + headXOffset, -headHeight / 2, -headWidth / 2, headLength,
        headHeight, headWidth, 0.0F);
    boreHead.setPos(0, -headHeight / 2 + headYOffset, 0);

    // boreHead.rotateAngleX = 1;
    // boreHead.addBox(-25 / 2, -25 / 2, 0, 25, 25, 28, 1);
    // boreHead.setRotationPoint(-25 / 2, 0 + byte3, 0.0F);
    // sideModels[5].addBox(-byte0 / 2 + 1, -byte2 / 2 + 1, -1F, byte0 - 2, byte2 - 2, 1, 0.0F);
    // sideModels[5].setRotationPoint(0.0F, 0 + byte3, 0.0F);

    // boreFrame.setRotationPoint(-baseLength / 2 + 1, 0 + byte3, 0.0F);
    // sideModels[2].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
    // sideModels[2].setRotationPoint(byte0 / 2 - 1, 0 + byte3, 0.0F);
    // sideModels[3].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
    // sideModels[3].setRotationPoint(0.0F, 0 + byte3, -byte2 / 2 + 1);
    // sideModels[4].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
    // sideModels[4].setRotationPoint(0.0F, 0 + byte3, byte2 / 2 - 1);
    // base.rotateAngleX = 1.570796F;
    // furnaceBox.rotateAngleX = 1.570796F;
    // boreHead.rotateAngleY = 4.712389F;
    // boreFrame.rotateAngleY = 4.712389F;
    // sideModels[2].rotateAngleY = 1.570796F;
    // sideModels[3].rotateAngleY = 3.141593F;
    // sideModels[5].rotateAngleX = -1.570796F;
  }

  @Override
  public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    // sideModels[5].rotationPointY = 4F - f2;
    base.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    rear.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    if (renderBoreHead) {
      boreHead.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue,
          alpha);
    }
    boreFrame.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue,
        alpha);
    furnaceBox.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue,
        alpha);
    if (active) {
      furnaceActive.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue,
          alpha);
    } else {
      furnaceIdle.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue,
          alpha);
    }
    stack.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);

  }

  public void setBoreHeadRotation(float rotation) {
    boreHead.xRot = rotation;
  }

  public void setBoreActive(boolean active) {
    this.active = active;
  }

  public void setRenderBoreHead(boolean render) {
    renderBoreHead = render;
  }

  @Override
  public void setupAnim(TunnelBoreEntity p_225597_1_, float p_225597_2_, float p_225597_3_,
      float p_225597_4_, float p_225597_5_, float p_225597_6_) {}
}
