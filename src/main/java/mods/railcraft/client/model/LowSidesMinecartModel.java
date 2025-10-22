package mods.railcraft.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.util.Mth;

public class LowSidesMinecartModel<T extends MinecartRenderState> extends EntityModel<T> {

  public LowSidesMinecartModel(ModelPart root) {
    super(root, RenderType::entityTranslucent);
  }

  public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    float length = 20;
    int heightEnds = 8;
    int heightSides = 6;
    float width = 16;
    int yOffset = 4;
    root.addOrReplaceChild("bottom",
        CubeListBuilder.create()
            .texOffs(0, 10)
            .addBox(-length / 2, -width / 2, -1, length, width, 2, deformation),
        PartPose.offsetAndRotation(0, yOffset, 0, Mth.HALF_PI, 0, 0));
    root.addOrReplaceChild("front",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-width / 2, (-length / 2) + 1, -1, width, heightEnds, 2,
                deformation),
        PartPose.offsetAndRotation(-9, yOffset, 0, 0, Mth.PI * 1.5F, 0));
    root.addOrReplaceChild("back",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-width / 2, (-length / 2) + 1, -1, width, heightEnds, 2,
                deformation),
        PartPose.offsetAndRotation((length / 2) - 1, yOffset, 0, 0, Mth.HALF_PI, 0));
    root.addOrReplaceChild("left",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-width / 2, -heightSides - 1, -1, width, heightSides, 2, deformation),
        PartPose.offsetAndRotation(0, yOffset, -7, 0, Mth.PI, 0));
    root.addOrReplaceChild("right",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-width / 2, -heightSides - 1, -1, width, heightSides, 2, deformation),
        PartPose.offset(0, yOffset, 7));
    return LayerDefinition.create(mesh, 64, 32);
  }

  @Override
  public void setupAnim(MinecartRenderState renderState) {
  }
}
