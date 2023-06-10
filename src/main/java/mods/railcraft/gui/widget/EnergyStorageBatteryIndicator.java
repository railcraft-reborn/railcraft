package mods.railcraft.gui.widget;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageBatteryIndicator implements Gauge {

  private float charge;
  private final IEnergyStorage battery;

  // alloc 2, charge% and nRF / mnRF CU
  private final List<Component> tooltip = new ArrayList<>(2);

  public EnergyStorageBatteryIndicator(IEnergyStorage battery) {
    this.battery = battery;
  }

  @Override
  public void refresh() {
    int capacity = this.battery.getMaxEnergyStored();
    float current = Math.min(this.charge, capacity);
    float chargeLevel = capacity <= 0.0F ? 0.0F : (current / capacity) * 100.0F;
    this.tooltip.clear();
    this.tooltip.add(Component.literal(String.format("%.0f%%", chargeLevel)));
    this.tooltip.add(Component.literal(String.format("%sFE / %sFE",
            HumanReadableNumberFormatter.format(current),
            HumanReadableNumberFormatter.format(capacity))));
  }

  @Override
  public List<Component> getTooltip() {
    return this.tooltip;
  }

  @Override
  public float getServerValue() {
    int energyStored = this.battery.getEnergyStored();
    int capacity = this.battery.getMaxEnergyStored();
    return (float) energyStored / capacity;
  }

  @Override
  public float getClientValue() {
    return this.charge;
  }

  @Override
  public void setClientValue(float value) {
    this.charge = value;
  }
}
