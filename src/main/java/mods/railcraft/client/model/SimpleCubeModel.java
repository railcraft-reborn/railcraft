package mods.railcraft.client.model;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleCubeModel extends SimpleTexturedModel {

  public SimpleCubeModel() {
    this.renderer.setTexSize(64, 32);
    this.renderer.addBox(-8F, -8F, -8F, 16, 16, 16);
    this.renderer.xRot = 8F;
    this.renderer.yRot = 8F;
    this.renderer.zRot = 8F;
  }
}
