package mods.railcraft.client.model;

import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SimpleModel<S> extends Model<S> {

  protected final ModelPart root;

  public SimpleModel(ModelPart root) {
    this(RenderType::entityCutout, root);
  }

  public SimpleModel(Function<ResourceLocation, RenderType> renderTypeFactory, ModelPart root) {
    super(root, renderTypeFactory);
    this.root = root;
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
