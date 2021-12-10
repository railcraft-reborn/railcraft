package mods.railcraft.client.model;

import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class ElectricLocomotiveModel extends HierarchicalModel<LocomotiveEntity> {

  private final ModelPart root;

  public ElectricLocomotiveModel(ModelPart root) {
    super(RenderType::entityTranslucentCull);
    this.root = root;
  }

  public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    root.addOrReplaceChild("wheels",
        CubeListBuilder.create()
            .texOffs(1, 25)
            .addBox(-20F, -5F, -16F, 23, 2, 16, deformation),
        PartPose.offset(8.0F, 8.0F, 8.0F));
    root.addOrReplaceChild("frame",
        CubeListBuilder.create()
            .texOffs(1, 1)
            .addBox(-21F, -10F, -17F, 25, 5, 18, deformation),
        PartPose.offset(8.0F, 8.0F, 8.0F));
    root.addOrReplaceChild("engine",
        CubeListBuilder.create()
            .texOffs(67, 37)
            .addBox(-15F, -19F, -16F, 13, 9, 16, deformation),
        PartPose.offset(8.0F, 8.0F, 8.0F));
    root.addOrReplaceChild("sideA",
        CubeListBuilder.create()
            .texOffs(35, 45)
            .addBox(-20F, -17F, -13F, 5, 7, 10, deformation),
        PartPose.offset(8.0F, 8.0F, 8.0F));
    root.addOrReplaceChild("sideB",
        CubeListBuilder.create()
            .texOffs(35, 45)
            .addBox(-2F, -17F, -13F, 5, 7, 10, deformation),
        PartPose.offset(8.0F, 8.0F, 8.0F));
    root.addOrReplaceChild("light",
        CubeListBuilder.create()
            .texOffs(1, 55)
            .addBox(-21F, -18F, -10F, 6, 4, 4, deformation),
        PartPose.offset(8.0F, 8.0F, 8.0F));

    return LayerDefinition.create(mesh, 128, 64);
  }

  @Override
  public void setupAnim(LocomotiveEntity p_225597_1_, float p_225597_2_, float p_225597_3_,
      float p_225597_4_, float p_225597_5_, float p_225597_6_) {}

  @Override
  public ModelPart root() {
    return this.root;
  }
}
