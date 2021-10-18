package mods.railcraft.client.model;

import java.util.Arrays;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class RailcraftMinecartModel<T extends Entity> extends SegmentedModel<T> {

  public ModelRenderer[] sideModels = new ModelRenderer[7];

  public RailcraftMinecartModel() {
    this(0.0F);
  }

  public RailcraftMinecartModel(float scale) {
    super(RenderType::entityTranslucentCull);
    sideModels[0] = new ModelRenderer(this, 0, 10);
    sideModels[1] = new ModelRenderer(this, 0, 0);
    sideModels[2] = new ModelRenderer(this, 0, 0);
    sideModels[3] = new ModelRenderer(this, 0, 0);
    sideModels[4] = new ModelRenderer(this, 0, 0);
    sideModels[5] = new ModelRenderer(this, 44, 10);
    sideModels[0].addBox(-10.0F, -8.0F, -1.0F, 20, 16, 2, scale);
    sideModels[0].setPos(0.0F, 4.0F, 0.0F);
    sideModels[5].addBox(-9.0F, -7.0F, -1.0F, 18, 14, 1, scale);
    sideModels[5].setPos(0.0F, 4.0F, 0.0F);
    sideModels[1].addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, scale);
    sideModels[1].setPos(-9.0F, 4.0F, 0.0F);
    sideModels[2].addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, scale);
    sideModels[2].setPos(9.0F, 4.0F, 0.0F);
    sideModels[3].addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, scale);
    sideModels[3].setPos(0.0F, 4.0F, -7.0F);
    sideModels[4].addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, scale);
    sideModels[4].setPos(0.0F, 4.0F, 7.0F);
    sideModels[0].xRot = ((float) Math.PI / 2F);
    sideModels[1].yRot = ((float) Math.PI * 3F / 2F);
    sideModels[2].yRot = ((float) Math.PI / 2F);
    sideModels[3].yRot = (float) Math.PI;
    sideModels[5].xRot = -((float) Math.PI / 2F);
  }

  @Override
  public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
      float ageInTicks, float netHeadYaw, float headPitch) {
    sideModels[5].y = 4.0F - ageInTicks;
  }

  @Override
  public Iterable<ModelRenderer> parts() {
    return Arrays.asList(this.sideModels);
  }
}
