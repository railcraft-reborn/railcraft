package mods.railcraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class ElectricLocomotiveLampModel extends SimpleModel {

  public ElectricLocomotiveLampModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    root.addOrReplaceChild("bulb",
        CubeListBuilder.create()
            .texOffs(1, 1)
            .addBox(-22F, -17F, -9F, 1, 2, 2),
        PartPose.offset(8F, 8F, 8F));
    return LayerDefinition.create(mesh, 16, 16);
  }
}
