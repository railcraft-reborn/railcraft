package mods.railcraft.client.model;

import net.minecraft.util.ResourceLocation;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SimpleTexturedModel extends SimpleModel {

  private ResourceLocation texture;
  private boolean backFaceCulling = true;

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

  /**
   * @return the backFaceCulling
   */
  public boolean cullBackFaces() {
    return backFaceCulling;
  }

  /**
   * @param backFaceCulling the backFaceCulling to set
   */
  public void doBackFaceCulling(boolean backFaceCulling) {
    this.backFaceCulling = backFaceCulling;
  }
}
