package mods.railcraft.client.model;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class MaintanceModel extends SimpleTexturedModel {

  public MaintanceModel() {
    this.renderer.setTexSize(64, 64);
    this.renderer.addBox("base", -8, -8, -8, 16, 16, 16, 0.0F, 0, 1);
    this.renderer.addBox("bracket", -3, 8, -3, 6, 1, 6, 0.0F, 1, 35);
    this.renderer.xRot = 8F;
    this.renderer.yRot = 8F;
    this.renderer.zRot = 8F;
  }
}
