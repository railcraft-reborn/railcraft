package mods.railcraft.gui.widget;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.Translations;
import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyStorageBatteryIndicator implements Gauge {

  private float charge;
  private final IEnergyStorage battery;

  private final List<Component> tooltip = new ArrayList<>(2);

  public EnergyStorageBatteryIndicator(IEnergyStorage battery) {
    this.battery = battery;
  }

  @Override
  public void refresh() {
    int capacity = this.battery.getMaxEnergyStored();
    float chargeLevel = charge * 100.0F;
    float energyStorage = charge * capacity;
    this.tooltip.clear();
    this.tooltip.add(Component.translatable(Translations.Tips.PERCENTAGE)
        .withStyle(ChatFormatting.GREEN)
        .append(" ")
        .append(Component.literal(String.format("%.0f%%", chargeLevel))
            .withStyle(ChatFormatting.WHITE)));
    this.tooltip.add(Component.translatable(Translations.Tips.ENERGY)
        .withStyle(ChatFormatting.GREEN)
        .append(" ")
        .append(Component.literal(String.format("%sFE / %sFE",
            HumanReadableNumberFormatter.format(energyStorage),
            HumanReadableNumberFormatter.format(capacity)))
            .withStyle(ChatFormatting.WHITE)));
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
