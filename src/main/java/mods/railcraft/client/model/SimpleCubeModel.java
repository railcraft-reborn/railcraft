package mods.railcraft.client.model;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SimpleCubeModel extends SimpleTexturedModel {

  public SimpleCubeModel() {
    this.renderer.setTexSize(64, 32);
    this.renderer.addBox(-8F, -8F, -8F, 16, 16, 16);
  }
}
