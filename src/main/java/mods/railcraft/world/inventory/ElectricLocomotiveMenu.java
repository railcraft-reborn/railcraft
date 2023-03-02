package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.EnergyStorageBatteryIndicator;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import net.minecraft.world.entity.player.Inventory;

public class ElectricLocomotiveMenu extends LocomotiveMenu<ElectricLocomotive> {

  private final EnergyStorageBatteryIndicator chargeIndicator;

  public ElectricLocomotiveMenu(int id, Inventory playerInv,
      ElectricLocomotive loco) {
    super(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(), id, playerInv, loco);
    this.chargeIndicator = new EnergyStorageBatteryIndicator(loco.getBatteryCart());
    this.addWidget(new GaugeWidget(this.chargeIndicator, 57, 20, 176, 0, 62, 8, false));
  }
}
