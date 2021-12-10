package mods.railcraft.client.model;

import mods.railcraft.world.entity.cart.TunnelBoreEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;

public class TunnelBoreModel extends HierarchicalModel<TunnelBoreEntity> {

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
    int offset = 3;

    int baseYOffset = 2;
    int baseXOffset = 0;
    int baseLength = 80;
    int baseWidth = 16;
    int baseHeight = 2;

    int rearYOffset = 0;
    int rearXOffset = 27;
    int rearLength = 42;
    int rearWidth = 24;
    int rearHeight = 24;

    int boxXOffset = -6;
    int boxLength = 24;
    int boxWidth = 32;
    int boxHeight = 32;

    int furnaceXOffset = -6;
    int furnaceYOffset = 6;
    int furnace1ZOffset = 17;
    int furnace2ZOffset = -17;
    int furnaceLength = 16;
    int furnaceWidth = 2;
    int furnaceHeight = 16;

    int stackXOffset = -6;
    int stackYOffset = 22;
    int stack1ZOffset = 15;
    int stack2ZOffset = -15;
    int stackLength = 4;
    int stackWidth = 4;
    int stackHeight = 16;

    int frameXOffset = -18;
    int frameLength = 32;
    int frameWidth = 48;
    int frameHeight = 43;

    int headXOffset = -50;
    int headLength = 2;
    int headWidth = 32;
    int headHeight = 32;
    int headYOffset = headHeight / 2 - frameHeight / 2 + offset;

    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    root.addOrReplaceChild("base",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-baseLength / 2 + baseXOffset, -baseHeight + offset + baseYOffset,
                -baseWidth / 2,
                baseLength, baseHeight, baseWidth),
        PartPose.ZERO);
    root.addOrReplaceChild("rear",
        CubeListBuilder.create()
            .texOffs(0, 18)
            .addBox(-rearLength / 2 + rearXOffset, -rearHeight + offset + rearYOffset,
                -rearWidth / 2,
                rearLength, rearHeight, rearWidth),
        PartPose.ZERO);
    root.addOrReplaceChild("furnaceBox",
        CubeListBuilder.create()
            .texOffs(99, 112)
            .addBox(-boxLength / 2 + boxXOffset, -boxHeight + offset, -boxWidth / 2, boxLength,
                boxHeight, boxWidth),
        PartPose.ZERO);
    root.addOrReplaceChild("boreHead",
        CubeListBuilder.create()
            .texOffs(59, 78)
            .addBox(-headLength + headXOffset, -headHeight / 2, -headWidth / 2, headLength,
                headHeight, headWidth),
        PartPose.offset(0, -headHeight / 2 + headYOffset, 0));
    root.addOrReplaceChild("boreFrame",
        CubeListBuilder.create()
            .texOffs(96, 18)
            .addBox(-frameLength + frameXOffset, -frameHeight + offset, -frameWidth / 2,
                frameLength, frameHeight, frameWidth),
        PartPose.ZERO);
    root.addOrReplaceChild("furnaceIdle",
        CubeListBuilder.create()
            .texOffs(0, 66)
            .addBox(-furnaceLength / 2 + furnaceXOffset,
                -furnaceHeight + offset - furnaceYOffset, -furnaceWidth / 2 + furnace1ZOffset,
                furnaceLength, furnaceHeight, furnaceWidth)
            .addBox(-furnaceLength / 2 + furnaceXOffset,
                -furnaceHeight + offset - furnaceYOffset, -furnaceWidth / 2 + furnace2ZOffset,
                furnaceLength, furnaceHeight, furnaceWidth),
        PartPose.ZERO);
    root.addOrReplaceChild("furnaceActive",
        CubeListBuilder.create()
            .texOffs(37, 66)
            .addBox(-furnaceLength / 2 + furnaceXOffset,
                -furnaceHeight + offset - furnaceYOffset, -furnaceWidth / 2 + furnace1ZOffset,
                furnaceLength, furnaceHeight, furnaceWidth)
            .addBox(-furnaceLength / 2 + furnaceXOffset,
                -furnaceHeight + offset - furnaceYOffset, -furnaceWidth / 2 + furnace2ZOffset,
                furnaceLength, furnaceHeight, furnaceWidth),
        PartPose.ZERO);
    root.addOrReplaceChild("stack",
        CubeListBuilder.create()
            .texOffs(74, 66)
            .addBox(-stackLength / 2 + stackXOffset, -stackHeight + offset - stackYOffset,
                -stackWidth / 2 + stack1ZOffset, stackLength, stackHeight, stackWidth)
            .addBox(-stackLength / 2 + stackXOffset, -stackHeight + offset - stackYOffset,
                -stackWidth / 2 + stack2ZOffset, stackLength, stackHeight, stackWidth),
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
  public void setupAnim(TunnelBoreEntity p_225597_1_, float p_225597_2_, float p_225597_3_,
      float p_225597_4_, float p_225597_5_, float p_225597_6_) {}

  @Override
  public ModelPart root() {
    return this.root;
  }
}
