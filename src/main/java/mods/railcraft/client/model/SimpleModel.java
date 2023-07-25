package mods.railcraft.client.model;

import java.util.function.Function;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SimpleModel extends Model {

  protected final ModelPart root;

  public SimpleModel(ModelPart root) {
    this(RenderType::entityCutout, root);
  }

  public SimpleModel(Function<ResourceLocation, RenderType> renderTypeFactory, ModelPart root) {
    super(renderTypeFactory);
    this.root = root;
  }

  @Override
  public void renderToBuffer(
      PoseStack poseStack, VertexConsumer vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    this.root.render(poseStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
  }

  public void setRotation(float x, float y, float z) {
    root.xRot = x;
    root.yRot = y;
    root.zRot = z;
  }

  public void rotateY(float degrees) {
    root.yRot += degrees;
  }

  public void resetRotation() {
    root.xRot = 0;
    root.yRot = 0;
    root.zRot = 0;
  }
}
