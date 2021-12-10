package mods.railcraft.client.model;

import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class SteamLocomotiveModel extends HierarchicalModel<LocomotiveEntity> {

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
            .addBox(-20F, -5F, -16F, 23, 2, 16, deformation),
        PartPose.offset(8F, 8F, 8F));
    root.addOrReplaceChild("frame",
        CubeListBuilder.create()
            .texOffs(1, 1)
            .addBox(-21F, -7F, -17F, 25, 2, 18, deformation),
        PartPose.offset(8F, 8F, 8F));
    root.addOrReplaceChild("boiler",
        CubeListBuilder.create()
            .texOffs(67, 38)
            .addBox(-20F, -18F, -15F, 16, 11, 14, deformation),
        PartPose.offset(8F, 8F, 8F));
    root.addOrReplaceChild("cab",
        CubeListBuilder.create()
            .texOffs(81, 8)
            .addBox(-4F, -19F, -16F, 7, 12, 16, deformation),
        PartPose.offset(8F, 8F, 8F));
    root.addOrReplaceChild("cowcatcher",
        CubeListBuilder.create()
            .texOffs(1, 43)
            .addBox(-22F, -8F, -14F, 3, 5, 12, deformation),
        PartPose.offset(8F, 8F, 8F));
    root.addOrReplaceChild("stack",
        CubeListBuilder.create()
            .texOffs(49, 43)
            .addBox(-17F, -24F, -10F, 4, 6, 4, deformation),
        PartPose.offset(8F, 8F, 8F));
    root.addOrReplaceChild("dome",
        CubeListBuilder.create()
            .texOffs(23, 43)
            .addBox(-11F, -20F, -11F, 6, 2, 6, deformation),
        PartPose.offset(8F, 8F, 8F));
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
