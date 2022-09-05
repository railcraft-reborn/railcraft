package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.inventory.slots.ItemFilterSlot;
import mods.railcraft.world.inventory.slots.OutputSlot;
import mods.railcraft.world.inventory.slots.RailcraftSlot;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.module.SolidFueledSteamBoilerModule;
import mods.railcraft.world.module.SteamBoilerModule;
import net.minecraft.world.entity.player.Inventory;

public class SolidFueledSteamBoilerMenu extends RailcraftMenu {

  private final SolidFueledSteamBoilerModule module;
  private final GaugeWidget temperatureGauge;
  private final FluidGaugeWidget waterGauge;
  private final FluidGaugeWidget steamGauge;

  public SolidFueledSteamBoilerMenu(int id, Inventory inventory,
      SteamBoilerBlockEntity steamBoiler) {
    super(RailcraftMenuTypes.SOLID_FUELED_STEAM_BOILER.get(), id, inventory.player,
        steamBoiler::stillValid);

    this.module = steamBoiler.getModule(SolidFueledSteamBoilerModule.class).get();

    this.addWidget(this.waterGauge =
        new FluidGaugeWidget(this.module.getWaterTank(), 116, 23, 176, 0, 16, 47));
    this.addWidget(this.steamGauge =
        new FluidGaugeWidget(this.module.getSteamTank(), 17, 23, 176, 0, 16, 47));

    this.addWidget(this.temperatureGauge =
        new GaugeWidget(this.module.getBoiler().getTemperatureGauge(), 40, 25, 176, 61, 6, 43));

    this.addSlot(new ItemFilterSlot(
        StackFilter.FLUID_CONTAINER, this.module, SteamBoilerModule.SLOT_LIQUID_INPUT, 143, 20));
    this.addSlot(new OutputSlot(this.module, SteamBoilerModule.SLOT_LIQUID_PROCESSING, 143, 38));
    this.addSlot(new OutputSlot(this.module, SteamBoilerModule.SLOT_LIQUID_OUTPUT, 143, 56));


    this.addSlot(new RailcraftSlot(this.module, 3, 62, 39)); // Fuel
    this.addSlot(new RailcraftSlot(this.module, 4, 89, 20)); // Fuel
    this.addSlot(new RailcraftSlot(this.module, 5, 89, 38)); // Fuel
    this.addSlot(new RailcraftSlot(this.module, 6, 89, 56)); // Fuel

    this.addPlayerSlots(inventory, 166);

    this.addDataSlot(new SimpleDataSlot(
        () -> (int) Math.round(this.module.getBoiler().getBurnTime()),
        this.module.getBoiler()::setBurnTime));

    this.addDataSlot(new SimpleDataSlot(
        () -> (int) Math.round(this.module.getBoiler().getCurrentItemBurnTime()),
        this.module.getBoiler()::setCurrentItemBurnTime));
  }

  public SolidFueledSteamBoilerModule getModule() {
    return this.module;
  }

  public GaugeWidget getTemperatureGauge() {
    return this.temperatureGauge;
  }

  public FluidGaugeWidget getWaterGauge() {
    return this.waterGauge;
  }

  public FluidGaugeWidget getSteamGauge() {
    return this.steamGauge;
  }
}
