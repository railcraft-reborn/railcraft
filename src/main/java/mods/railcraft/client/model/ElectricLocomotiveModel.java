package mods.railcraft.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.world.entity.cart.LocomotiveEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ElectricLocomotiveModel extends EntityModel<LocomotiveEntity> {

  private final ModelRenderer loco;

  public ElectricLocomotiveModel() {
    this(0f);
  }

  public ElectricLocomotiveModel(float scale) {
    this.loco = new ModelRenderer(this);
    this.loco.setTexSize(128, 64);
    // wheels
    this.loco.texOffs(1, 25).addBox(-20F, -5F, -16F, 23, 2, 16, scale);
    // frame
    this.loco.texOffs(1, 1).addBox(-21F, -10F, -17F, 25, 5, 18, scale);
    // engine
    this.loco.texOffs(67, 37).addBox(-15F, -19F, -16F, 13, 9, 16, scale);
    // sideA
    this.loco.texOffs(35, 45).addBox(-20F, -17F, -13F, 5, 7, 10, scale);
    // sideB
    this.loco.texOffs(35, 45).addBox(-2F, -17F, -13F, 5, 7, 10, scale);
    // lightA
    // loco.setTextureOffset( 1, 45).addBox( -2F, -18F, -10F, 6, 4, 4, scale);
    // lightB
    this.loco.texOffs(1, 55).addBox(-21F, -18F, -10F, 6, 4, 4, scale);

    this.loco.setPos(8, 8, 8);
  }

  @Override
  public void setupAnim(LocomotiveEntity p_225597_1_, float p_225597_2_, float p_225597_3_,
      float p_225597_4_, float p_225597_5_, float p_225597_6_) {}

  @Override
  public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    this.loco.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }
}
