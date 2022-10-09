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
            .addBox(-10.0F, -8.0F, -1.0F, 20.0F, 16.0F, 2.0F, cubeDeformation),
        PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));
    partdefinition.addOrReplaceChild("front",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F, cubeDeformation),
        PartPose.offsetAndRotation(-9.0F, 4.0F, 0.0F, 0.0F, ((float) Math.PI * 1.5F), 0.0F));
    partdefinition.addOrReplaceChild("back",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F, cubeDeformation),
        PartPose.offsetAndRotation(9.0F, 4.0F, 0.0F, 0.0F, ((float) Math.PI / 2F), 0.0F));
    partdefinition.addOrReplaceChild("left",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F, cubeDeformation),
        PartPose.offsetAndRotation(0.0F, 4.0F, -7.0F, 0.0F, (float) Math.PI, 0.0F));
    partdefinition.addOrReplaceChild("right",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F, cubeDeformation),
        PartPose.offset(0.0F, 4.0F, 7.0F));
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
