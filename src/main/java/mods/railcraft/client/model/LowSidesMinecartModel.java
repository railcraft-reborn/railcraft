package mods.railcraft.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class LowSidesMinecartModel<T extends Entity> extends HierarchicalModel<T> {

  private final ModelPart root;

  public LowSidesMinecartModel(ModelPart root) {
    super(RenderType::entityTranslucentCull);
    this.root = root;
  }

  public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    int length = 20;
    int heightEnds = 8;
    int heightSides = 6;
    int width = 16;
    int yOffset = 4;
    root.addOrReplaceChild("bottom",
        CubeListBuilder.create()
            .texOffs(0, 10)
            .addBox(-length / 2.0F, -width / 2.0F, -1.0F, length, width, 2.0F, deformation),
        PartPose.offsetAndRotation(0.0F, yOffset, 0.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));
    root.addOrReplaceChild("front",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-width / 2.0F, (-length / 2.0F) + 1, -1.0F, width, heightEnds, 2.0F,
                deformation),
        PartPose.offsetAndRotation(-9.0F, yOffset, 0.0F, 0.0F, ((float) Math.PI * 1.5F), 0.0F));
    root.addOrReplaceChild("back",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-width / 2.0F, (-length / 2.0F) + 1, -1.0F, width, heightEnds, 2.0F,
                deformation),
        PartPose.offsetAndRotation((length / 2.0F) - 1, yOffset, 0.0F, 0.0F, ((float) Math.PI / 2F),
            0.0F));
    root.addOrReplaceChild("left",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-width / 2.0F, -heightSides - 1, -1.0F, width, heightSides, 2.0F, deformation),
        PartPose.offsetAndRotation(0.0F, yOffset, -7.0F, 0.0F, (float) Math.PI, 0.0F));
    root.addOrReplaceChild("right",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-width / 2.0F, -heightSides - 1, -1.0F, width, heightSides, 2.0F, deformation),
        PartPose.offset(0.0F, yOffset, 7.0F));
    return LayerDefinition.create(mesh, 64, 32);
  }

  @Override
  public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_,
      float p_225597_5_, float p_225597_6_) {}

  @Override
  public ModelPart root() {
    return this.root;
  }
}
