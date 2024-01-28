package mods.railcraft.client.model;

import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class SteamLocomotiveModel extends HierarchicalModel<Locomotive> {

  private final ModelPart root;

  public SteamLocomotiveModel(ModelPart root) {
    this.root = root;
  }

  public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    root.addOrReplaceChild("wheels",
        CubeListBuilder.create()
            .texOffs(1, 23)
            .addBox(-20, -5, -16, 23, 2, 16, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("frame",
        CubeListBuilder.create()
            .texOffs(1, 1)
            .addBox(-21, -7, -17, 25, 2, 18, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("boiler",
        CubeListBuilder.create()
            .texOffs(67, 38)
            .addBox(-20, -18, -15, 16, 11, 14, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("cab",
        CubeListBuilder.create()
            .texOffs(81, 8)
            .addBox(-4, -19, -16, 7, 12, 16, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("cowcatcher",
        CubeListBuilder.create()
            .texOffs(1, 43)
            .addBox(-22, -8, -14, 3, 5, 12, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("stack",
        CubeListBuilder.create()
            .texOffs(49, 43)
            .addBox(-17, -24, -10, 4, 6, 4, deformation),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("dome",
        CubeListBuilder.create()
            .texOffs(23, 43)
            .addBox(-11, -20, -11, 6, 2, 6, deformation),
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
