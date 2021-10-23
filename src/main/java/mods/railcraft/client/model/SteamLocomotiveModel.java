package mods.railcraft.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SteamLocomotiveModel extends EntityModel<LocomotiveEntity> {

  private final ModelRenderer loco;

  public SteamLocomotiveModel() {
    this(0F);
  }

  public SteamLocomotiveModel(float scale) {
    super(RenderType::entityTranslucentCull);
    this.loco = new ModelRenderer(this);
    this.loco.setTexSize(128, 64);

    // wheels
    this.loco.texOffs(1, 23).addBox(-20F, -5F, -16F, 23, 2, 16, scale);
    // frame
    this.loco.texOffs(1, 1).addBox(-21F, -7F, -17F, 25, 2, 18, scale);
    // boiler
    this.loco.texOffs(67, 38).addBox(-20F, -18F, -15F, 16, 11, 14, scale);
    // cab
    this.loco.texOffs(81, 8).addBox(-4F, -19F, -16F, 7, 12, 16, scale);
    // cowcatcher
    this.loco.texOffs(1, 43).addBox(-22F, -8F, -14F, 3, 5, 12, scale);
    // stack
    this.loco.texOffs(49, 43).addBox(-17F, -24F, -10F, 4, 6, 4, scale);
    // dome
    this.loco.texOffs(23, 43).addBox(-11F, -20F, -11F, 6, 2, 6, scale);

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
