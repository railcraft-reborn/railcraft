package mods.railcraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class MaintenanceModel extends SimpleModel {

  public MaintenanceModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    root.addOrReplaceChild("base",
        CubeListBuilder.create()
            .texOffs(0, 1)
            .addBox(-8, -8, -8, 16, 16, 16),
        PartPose.offset(8, 8, 8));
    root.addOrReplaceChild("bracket",
        CubeListBuilder.create()
            .texOffs(1, 35)
            .addBox(-3, 8, -3, 6, 1, 6),
        PartPose.offset(8, 8, 8));
    return LayerDefinition.create(mesh, 64, 64);
  }
}
