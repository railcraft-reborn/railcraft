package mods.railcraft.client.model;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class MaintenanceLampModel extends SimpleModel {

  public MaintenanceLampModel() {
    this.renderer.setTexSize(16, 16);
    this.renderer.addBox("lamp", -2, 10.75F, -2, 4, 4, 4, -0.5F, 0, 1);
    this.renderer.setPos(8.0F, 8.0F, 8.0F);
  }
}
