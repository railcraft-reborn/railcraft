package mods.railcraft.client.model;

import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SimpleTexturedModel extends SimpleModel {

  private ResourceLocation texture;

  public SimpleTexturedModel(Function<ResourceLocation, RenderType> renderTypeFactory) {
    super(renderTypeFactory);
  }

  public SimpleTexturedModel() {
    super();
  }

  public void setTexture(ResourceLocation texture) {
    this.texture = texture;
  }

  public void setTexture(String texture) {
    this.texture = new ResourceLocation(texture);
  }

  public ResourceLocation getTexture() {
    return texture;
  }
}
