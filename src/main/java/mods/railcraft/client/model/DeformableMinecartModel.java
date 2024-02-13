package mods.railcraft.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * Copy of {@link MinecartModel} that accepts a {@link CubeDeformation}.
 *
 * @author Sm0keySa1m0n
 *
 * @param <T> - entity type
 */
public class DeformableMinecartModel<T extends Entity> extends HierarchicalModel<T> {

  private final ModelPart root;

  public DeformableMinecartModel(ModelPart root) {
    this.root = root;
  }

  public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    partdefinition.addOrReplaceChild("bottom",
        CubeListBuilder.create()
            .texOffs(0, 10)
            .addBox(-10, -8, -1, 20, 16, 2, cubeDeformation),
        PartPose.offsetAndRotation(0, 4, 0, Mth.HALF_PI, 0, 0));
    partdefinition.addOrReplaceChild("front",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8, -9, -1, 16, 8, 2, cubeDeformation),
        PartPose.offsetAndRotation(-9, 4, 0, 0, Mth.PI * 1.5F, 0));
    partdefinition.addOrReplaceChild("back",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8, -9, -1, 16, 8, 2, cubeDeformation),
        PartPose.offsetAndRotation(9, 4, 0, 0, Mth.HALF_PI, 0));
    partdefinition.addOrReplaceChild("left",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8, -9, -1, 16, 8, 2, cubeDeformation),
        PartPose.offsetAndRotation(0, 4, -7, 0, Mth.PI, 0));
    partdefinition.addOrReplaceChild("right",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8, -9, -1, 16, 8, 2, cubeDeformation),
        PartPose.offset(0, 4, 7));
    return LayerDefinition.create(meshdefinition, 64, 32);
  }

  @Override
  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {}

  @Override
  public ModelPart root() {
    return this.root;
  }
}
