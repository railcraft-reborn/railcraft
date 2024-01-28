package mods.railcraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class MaintenanceLampModel extends SimpleModel {

  public MaintenanceLampModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    root.addOrReplaceChild("lamp",
        CubeListBuilder.create()
            .texOffs(0, 1)
            .addBox(-2, 10.75F, -2, 4, 4, 4),
        PartPose.offset(8, 6, 8));
    return LayerDefinition.create(mesh, 16, 16);
  }
}
