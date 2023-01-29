package mods.railcraft.client.model;

import mods.railcraft.world.entity.vehicle.TunnelBore;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;

public class TunnelBoreModel extends HierarchicalModel<TunnelBore> {

  private final ModelPart root;
  private final ModelPart boreHead;
  private final ModelPart furnaceActive;
  private final ModelPart furnaceIdle;

  public TunnelBoreModel(ModelPart root) {
    super(RenderType::entityTranslucentCull);
    this.root = root;
    this.boreHead = root.getChild("boreHead");
    this.furnaceActive = root.getChild("furnaceActive");
    this.furnaceIdle = root.getChild("furnaceIdle");
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    root.addOrReplaceChild("base",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-40, 3, -8, 80, 2, 16),
        PartPose.ZERO);
    root.addOrReplaceChild("rear",
        CubeListBuilder.create()
            .texOffs(0, 18)
            .addBox(6, -21, -12, 42, 24, 24),
        PartPose.ZERO);
    root.addOrReplaceChild("furnaceBox",
        CubeListBuilder.create()
            .texOffs(99, 112)
            .addBox(-18, -29, -16, 24, 32, 32),
        PartPose.ZERO);
    root.addOrReplaceChild("boreHead",
        CubeListBuilder.create()
            .texOffs(59, 78)
            .addBox(-52, -16, -16, 2, 32, 32),
        PartPose.offset(0, -18, 0));
    root.addOrReplaceChild("boreFrame",
        CubeListBuilder.create()
            .texOffs(96, 18)
            .addBox(-50, -40, -24, 32, 43, 48),
        PartPose.ZERO);
    root.addOrReplaceChild("furnaceIdle",
        CubeListBuilder.create()
            .texOffs(0, 66)
            .addBox(-14, -19, 16, 16, 16, 2)
            .addBox(-14, -19, -18, 16, 16, 2),
        PartPose.ZERO);
    root.addOrReplaceChild("furnaceActive",
        CubeListBuilder.create()
            .texOffs(37, 66)
            .addBox(-14, -19, 16, 16, 16, 2)
            .addBox(-14, -19, -18, 16, 16, 2),
        PartPose.ZERO);
    root.addOrReplaceChild("stack",
        CubeListBuilder.create()
            .texOffs(74, 66)
            .addBox(-8, -35, 13, 4, 16, 4)
            .addBox(-8, -35, -17, 4, 16, 4),
        PartPose.ZERO);
    return LayerDefinition.create(mesh, 255, 255);
  }

  public void setBoreHeadRotation(float rotation) {
    this.boreHead.xRot = rotation;
  }

  public void setBoreActive(boolean active) {
    this.furnaceActive.visible = active;
    this.furnaceIdle.visible = !active;
  }

  public void setRenderBoreHead(boolean visible) {
    this.boreHead.visible = visible;
  }

  @Override
  public void setupAnim(TunnelBore entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {}

  @Override
  public ModelPart root() {
    return this.root;
  }
}
