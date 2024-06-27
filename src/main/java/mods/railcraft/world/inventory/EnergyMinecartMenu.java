package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.EnergyStorageBatteryIndicator;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.entity.vehicle.EnergyMinecart;
import net.minecraft.world.entity.player.Inventory;

public class EnergyMinecartMenu extends RailcraftMenu {

  private final GaugeWidget energyGauge;

  public EnergyMinecartMenu(int id, Inventory inventory, EnergyMinecart energyMinecart) {
    super(RailcraftMenuTypes.ENERGY_MINECART.get(), id, inventory.player, energyMinecart::stillValid);
    var chargeIndicator = new EnergyStorageBatteryIndicator(energyMinecart.getCartBattery());
    this.addWidget(
        this.energyGauge = new GaugeWidget(chargeIndicator, 57, 38, 176, 0, 62, 8, false));
  }

  public GaugeWidget getEnergyGauge() {
    return this.energyGauge;
  }
}
