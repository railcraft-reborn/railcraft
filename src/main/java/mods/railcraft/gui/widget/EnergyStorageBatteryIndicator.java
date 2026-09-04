package mods.railcraft.gui.widget;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.Translations;
import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public class EnergyStorageBatteryIndicator implements Gauge {

  private float charge;
  private final EnergyHandler battery;

  private final List<Component> tooltip = new ArrayList<>(2);

  public EnergyStorageBatteryIndicator(EnergyHandler battery) {
    this.battery = battery;
  }

  @Override
  public void refresh() {
    int capacity = this.battery.getCapacityAsInt();
    float chargeLevel = charge * 100.0F;
    float energyStorage = charge * capacity;
    this.tooltip.clear();
    this.tooltip.add(Component.translatable(Translations.Tips.PERCENTAGE)
        .withStyle(ChatFormatting.GREEN)
        .append(CommonComponents.SPACE)
        .append(Component.literal(String.format("%.0f%%", chargeLevel))
            .withStyle(ChatFormatting.WHITE)));
    this.tooltip.add(Component.translatable(Translations.Tips.ENERGY)
        .withStyle(ChatFormatting.GREEN)
        .append(CommonComponents.SPACE)
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
    int energyStored = this.battery.getAmountAsInt();
    int capacity = this.battery.getCapacityAsInt();
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
