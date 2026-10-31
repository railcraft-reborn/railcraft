package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.EnergyStorageBatteryIndicator;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.inventory.slot.ItemFilterSlot;
import mods.railcraft.world.inventory.slot.OutputSlot;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import mods.railcraft.world.level.block.entity.HobbyistSteamEngineBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class HobbyistSteamEngineMenu extends RailcraftMenu {

  private final HobbyistSteamEngineBlockEntity steamEngine;
  private final FluidGaugeWidget steamGauge;
  private final FluidGaugeWidget waterGauge;
  private final GaugeWidget temperatureGauge;
  private final GaugeWidget energyGauge;

  public HobbyistSteamEngineMenu(int id, Inventory inventory,
      HobbyistSteamEngineBlockEntity steamEngine) {
    super(RailcraftMenuTypes.HOBBYIST_STEAM_ENGINE.get(), id, inventory.player,
        steamEngine::isStillValid);
    this.steamEngine = steamEngine;
    var boiler = steamEngine.getBoiler();
    var container = steamEngine.getContainer();

    this.addWidget(this.steamGauge =
        new FluidGaugeWidget(steamEngine.getSteamTank(), 17, 23, 176, 0, 16, 47));
    this.addWidget(this.waterGauge =
        new FluidGaugeWidget(steamEngine.getWaterTank(), 107, 23, 176, 0, 16, 47));
    this.addWidget(this.temperatureGauge =
        new GaugeWidget(boiler.getTemperatureGauge(), 40, 25, 176, 61, 6, 43));
    this.addWidget(this.energyGauge = new GaugeWidget(
        new EnergyStorageBatteryIndicator(steamEngine.getEnergyStorage()),
        94, 25, 182, 61, 6, 43));

    this.addSlot(new RailcraftSlot(container, HobbyistSteamEngineBlockEntity.SLOT_FUEL, 62, 39));
    this.addSlot(new ItemFilterSlot(StackFilter.FLUID_CONTAINER, container,
        HobbyistSteamEngineBlockEntity.SLOT_LIQUID_INPUT, 143, 21));
    this.addSlot(new OutputSlot(container,
        HobbyistSteamEngineBlockEntity.SLOT_LIQUID_OUTPUT, 143, 56));

    this.addInventorySlots(inventory);

    this.addDataSlot(new SimpleDataSlot(
        () -> Math.round(boiler.getBurnTime()), boiler::setBurnTime));
    this.addDataSlot(new SimpleDataSlot(
        () -> Math.round(boiler.getCurrentItemBurnTime()), boiler::setCurrentItemBurnTime));
    this.addDataSlot(new SimpleDataSlot(
        () -> Math.round(steamEngine.getCurrentOutput() * 100),
        value -> steamEngine.setCurrentOutput(value / 100.0F)));
  }

  public HobbyistSteamEngineBlockEntity getSteamEngine() {
    return this.steamEngine;
  }

  public FluidGaugeWidget getSteamGauge() {
    return this.steamGauge;
  }

  public FluidGaugeWidget getWaterGauge() {
    return this.waterGauge;
  }

  public GaugeWidget getTemperatureGauge() {
    return this.temperatureGauge;
  }

  public GaugeWidget getEnergyGauge() {
    return this.energyGauge;
  }
}
