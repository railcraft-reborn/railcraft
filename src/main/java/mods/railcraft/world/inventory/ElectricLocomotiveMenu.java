package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.EnergyStorageBatteryIndicator;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import net.minecraft.world.entity.player.Inventory;

public class ElectricLocomotiveMenu extends LocomotiveMenu<ElectricLocomotive> {

  private final GaugeWidget energyGauge;

  public ElectricLocomotiveMenu(int id, Inventory playerInv, ElectricLocomotive loco) {
    super(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(), id, playerInv, loco);
    var chargeIndicator = new EnergyStorageBatteryIndicator(loco.getBatteryCart());
    this.addWidget(
        this.energyGauge = new GaugeWidget(chargeIndicator, 57, 20, 176, 0, 62, 8, false));
    this.addInventorySlots(playerInv);
  }

  public GaugeWidget getEnergyGauge() {
    return energyGauge;
  }
}
