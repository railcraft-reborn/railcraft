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
        new FluidGaugeWidget(this.getLocomotive().getTankManager().get(0), 53, 23, 176, 0, 16, 47));
    this.addWidget(
        new FluidGaugeWidget(this.getLocomotive().getTankManager().get(1), 17, 23, 176, 0, 16, 47));

    this.addWidget(new GaugeWidget(
        this.getLocomotive().getBoiler().getTemperatureGauge(), 40, 25, 176, 61, 6, 43));

    this.addSlot(new LimitedWaterSlot(this.getLocomotive(), 0, 152, 20));
    this.addSlot(new OutputSlot(this.getLocomotive(), 1, 152, 56));
    this.addSlot(new OutputSlot(this.getLocomotive(), 2, 116, 56));
    this.addSlot(new RailcraftSlot(this.getLocomotive(), 3, 116, 20)); // Burn
    this.addSlot(new RailcraftSlot(this.getLocomotive(), 4, 80, 20)); // Fuel
    this.addSlot(new RailcraftSlot(this.getLocomotive(), 5, 80, 38)); // Fuel
    this.addSlot(new RailcraftSlot(this.getLocomotive(), 6, 80, 56)); // Fuel

    this.addDataSlot(new SimpleDataSlot(
        () -> (int) Math.round(locomotive.getBoiler().getBurnTime()),
        this.getLocomotive().getBoiler()::setBurnTime));

    this.addDataSlot(new SimpleDataSlot(
        () -> (int) Math.round(locomotive.getBoiler().getCurrentItemBurnTime()),
        this.getLocomotive().getBoiler()::setCurrentItemBurnTime));
  }
}
