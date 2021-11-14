package mods.railcraft.client.model;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class MaintenanceModel extends SimpleTexturedModel {

  public MaintenanceModel() {
    this.renderer.setTexSize(64, 64);
    this.renderer.addBox("base", -8, -8, -8, 16, 16, 16, 0.0F, 0, 1);
    this.renderer.addBox("bracket", -3, 8, -3, 6, 1, 6, 0.0F, 1, 35);
    this.renderer.setPos(8.0F, 8.0F, 8.0F);
  }
}
