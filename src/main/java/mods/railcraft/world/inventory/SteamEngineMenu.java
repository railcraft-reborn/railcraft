package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.EnergyStorageBatteryIndicator;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class SteamEngineMenu extends RailcraftMenu {

  private final SteamEngineBlockEntity steamEngine;
  private final FluidGaugeWidget steamGauge;
  private final GaugeWidget energyGauge;

  public SteamEngineMenu(int id, Inventory inventory, SteamEngineBlockEntity steamEngine) {
    super(RailcraftMenuTypes.STEAM_ENGINE.get(), id, inventory.player, steamEngine::isStillValid);
    this.steamEngine = steamEngine;

    this.addWidget(this.steamGauge =
        new FluidGaugeWidget(steamEngine.getSteamTank(), 71, 23, 176, 0, 16, 47));
    this.addWidget(this.energyGauge = new GaugeWidget(
        new EnergyStorageBatteryIndicator(steamEngine.getEnergyStorage()),
        94, 25, 176, 47, 6, 43));

    this.addInventorySlots(inventory);

    this.addDataSlot(new SimpleDataSlot(
        () -> Math.round(steamEngine.getCurrentOutput() * 100),
        value -> steamEngine.setCurrentOutput(value / 100.0F)));
  }

  public SteamEngineBlockEntity getSteamEngine() {
    return this.steamEngine;
  }

  public FluidGaugeWidget getSteamGauge() {
    return this.steamGauge;
  }

  public GaugeWidget getEnergyGauge() {
    return this.energyGauge;
  }
}
