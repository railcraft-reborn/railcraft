package mods.railcraft.client.model;

import java.util.function.Function;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CubeModel extends SimpleModel {

  public CubeModel(ModelPart root) {
    super(root);
  }

  public CubeModel(Function<ResourceLocation, RenderType> renderTypeFactory, ModelPart root) {
    super(renderTypeFactory, root);
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    root.addOrReplaceChild("cube",
        CubeListBuilder.create().addBox(-8F, -8F, -8F, 16, 16, 16),
        PartPose.offset(8.0F, 8.0F, 8.0F));
    return LayerDefinition.create(mesh, 64, 32);
  }
}
