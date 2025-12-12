package mods.railcraft.client.model;

import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class SimpleModel<S> extends Model<S> {

  protected final ModelPart root;

  public SimpleModel(ModelPart root) {
    this(RenderTypes::entityCutout, root);
  }

  public SimpleModel(Function<Identifier, RenderType> renderTypeFactory, ModelPart root) {
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
