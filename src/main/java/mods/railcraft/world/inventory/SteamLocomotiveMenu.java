package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.inventory.slot.LimitedWaterSlot;
import mods.railcraft.world.inventory.slot.OutputSlot;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.world.entity.player.Inventory;

public class SteamLocomotiveMenu extends LocomotiveMenu<SteamLocomotive> {

  public static final int HEIGHT = 205;

  public SteamLocomotiveMenu(int id, Inventory inventory,  SteamLocomotive locomotive) {
    super(RailcraftMenuTypes.STEAM_LOCOMOTIVE.get(), id, inventory, locomotive, HEIGHT);

    this.addWidget(
        new FluidGaugeWidget(locomotive.getTankManager().get(0), 53, 23, 176, 0, 16, 47));
    this.addWidget(
        new FluidGaugeWidget(locomotive.getTankManager().get(1), 17, 23, 176, 0, 16, 47));
    this.addWidget(new GaugeWidget(
        locomotive.boiler().getTemperatureGauge(), 40, 25, 176, 61, 6, 43));

    this.addDataSlot(new SimpleDataSlot(
        () -> Math.round(locomotive.boiler().getBurnTime()),
        locomotive.boiler()::setBurnTime));
    this.addDataSlot(new SimpleDataSlot(
        () -> Math.round(locomotive.boiler().getCurrentItemBurnTime()),
        locomotive.boiler()::setCurrentItemBurnTime));
  }

  @Override
  protected void addSlots(SteamLocomotive locomotive) {
    this.addSlot(new LimitedWaterSlot(locomotive, 0, 152, 20));
    this.addSlot(new OutputSlot(locomotive, 1, 152, 56));
    this.addSlot(new OutputSlot(locomotive, 2, 116, 56));
    this.addSlot(new RailcraftSlot(locomotive, 3, 116, 20)); // Burn
    this.addSlot(new RailcraftSlot(locomotive, 4, 80, 20)); // Fuel
    this.addSlot(new RailcraftSlot(locomotive, 5, 80, 38)); // Fuel
    this.addSlot(new RailcraftSlot(locomotive, 6, 80, 56)); // Fuel
  }
}
