package mods.railcraft.client.model;

import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity.EnergyStage;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SteamEngineModel {

  private static final float MAX_STROKE = 7.99F;
  private static final float PISTON_SPACING = 2.0F / 16.0F;

  private final ModelPart base;
  private final ModelPart frame;
  private final ModelPart piston;
  private final Map<EnergyStage, ModelPart> trunks;

  public SteamEngineModel(ModelPart root) {
    this.base = root.getChild("base");
    this.frame = root.getChild("frame");
    this.piston = root.getChild("piston");
    this.trunks = Map.of(
        EnergyStage.BLUE, root.getChild("trunk_blue"),
        EnergyStage.GREEN, root.getChild("trunk_green"),
        EnergyStage.YELLOW, root.getChild("trunk_yellow"),
        EnergyStage.ORANGE, root.getChild("trunk_orange"),
        EnergyStage.RED, root.getChild("trunk_red"));
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    var pose = PartPose.offset(8.0F, 8.0F, 8.0F);

    root.addOrReplaceChild("base", CubeListBuilder.create()
        .texOffs(1, 1).addBox(-8F, -8F, -8F, 16, 4, 16), pose);

    root.addOrReplaceChild("frame", CubeListBuilder.create()
        .texOffs(65, 1).addBox(-6F, -4F, -6F, 12, 4, 12)
        .texOffs(1, 23).addBox(-3F, -4F, -8F, 6, 4, 16)
        .texOffs(1, 45).addBox(-8F, -4F, -3F, 16, 4, 6), pose);

    root.addOrReplaceChild("piston", CubeListBuilder.create()
        .texOffs(32, 24)
        .addBox(-5F, -4F, -5F, 10, 2, 10)
        .addBox(-7F, -4F, -1F, 1, 2, 2)
        .addBox(-1F, -4F, -7F, 2, 2, 1)
        .addBox(6F, -4F, -1F, 1, 2, 2)
        .addBox(-1F, -4F, 6F, 2, 2, 1), pose);

    addTrunk(root, "trunk_blue", 1, 57);
    addTrunk(root, "trunk_green", 35, 57);
    addTrunk(root, "trunk_yellow", 69, 57);
    addTrunk(root, "trunk_orange", 1, 79);
    addTrunk(root, "trunk_red", 35, 79);

    return LayerDefinition.create(mesh, 128, 128);
  }

  private static void addTrunk(PartDefinition root,
      String name, int u, int v) {
    root.addOrReplaceChild(name, CubeListBuilder.create()
        .texOffs(u, v).addBox(-4F, -4F, -4F, 8, 12, 8), PartPose.offset(8.0F, 8.0F, 8.0F));
  }

  /**
   * Renders the engine pointing up.
   *
   * @param progress piston progress of the current stroke, 0 to 1.
   */
  public void render(PoseStack poseStack, VertexConsumer consumer, EnergyStage stage,
      float progress, int packedLight, int packedOverlay) {
    var trunk = this.trunks.getOrDefault(stage, this.trunks.get(EnergyStage.RED));
    trunk.render(poseStack, consumer, packedLight, packedOverlay);
    this.base.render(poseStack, consumer, packedLight, packedOverlay);

    float step = progress > 0.5F
        ? MAX_STROKE - (progress - 0.5F) * 2.0F * MAX_STROKE
        : progress * 2.0F * MAX_STROKE;

    poseStack.pushPose();
    poseStack.translate(0, step / 16.0F, 0);
    this.frame.render(poseStack, consumer, packedLight, packedOverlay);
    poseStack.popPose();

    poseStack.pushPose();
    poseStack.translate(0, -0.01F, 0);
    for (int i = 0; i <= step + 2; i += 2) {
      this.piston.render(poseStack, consumer, packedLight, packedOverlay);
      poseStack.translate(0, PISTON_SPACING, 0);
    }
    poseStack.popPose();
  }
}
