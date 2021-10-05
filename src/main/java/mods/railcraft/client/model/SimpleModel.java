package mods.railcraft.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SimpleModel extends Model {

  protected final ModelRenderer renderer;

  public SimpleModel() {
    super(RenderType::entityCutoutNoCull);
    this.renderer = new ModelRenderer(this);
  }

  @Override
  public void renderToBuffer(MatrixStack matrix, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    renderer.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }

  public void setRotation(float x, float y, float z) {
    renderer.xRot = x;
    renderer.yRot = y;
    renderer.zRot = z;
  }

  public void rotateY(float degrees) {
    renderer.yRot += degrees;
  }

  public void resetRotation() {
    renderer.xRot = 0;
    renderer.yRot = 0;
    renderer.zRot = 0;
  }
}
