package mods.railcraft.client.model;

import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class ElectricLocomotiveModel extends HierarchicalModel<Locomotive> {

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
            .addBox(-20, -5, -16, 23, 2, 16, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("frame",
        CubeListBuilder.create()
            .texOffs(1, 1)
            .addBox(-21, -10, -17, 25, 5, 18, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("engine",
        CubeListBuilder.create()
            .texOffs(67, 37)
            .addBox(-15, -19, -16, 13, 9, 16, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("sideA",
        CubeListBuilder.create()
            .texOffs(35, 45)
            .addBox(-20, -17, -13, 5, 7, 10, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("sideB",
        CubeListBuilder.create()
            .texOffs(35, 45)
            .addBox(-2, -17, -13, 5, 7, 10, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("light",
        CubeListBuilder.create()
            .texOffs(1, 55)
            .addBox(-21, -18, -10, 6, 4, 4, deformation),
        PartPose.offset(8, 8, 8));

    return LayerDefinition.create(mesh, 128, 64);
  }

  @Override
  public void setupAnim(Locomotive entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {}

  @Override
  public ModelPart root() {
    return this.root;
  }
}
