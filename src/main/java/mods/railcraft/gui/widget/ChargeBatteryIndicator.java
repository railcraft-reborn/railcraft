package mods.railcraft.gui.widget;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.api.charge.Battery;
import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class ChargeBatteryIndicator implements Gauge {

  private float charge;
  private final Battery battery;

  private final List<ITextProperties> tooltip = new ArrayList<>();

  public ChargeBatteryIndicator(Battery battery) {
    this.battery = battery;
  }

  @Override
  public void refresh() {
    float capacity = this.battery.getCapacity();
    float current = Math.min(this.charge, capacity);
    float chargeLevel = capacity <= 0.0F ? 0.0F : (current / capacity) * 100.0F;
    this.tooltip.clear();
    this.tooltip.add(new StringTextComponent(String.format("%.0f%%", chargeLevel)));
    this.tooltip.add(new StringTextComponent(HumanReadableNumberFormatter.format(current)));
    this.tooltip.add(new StringTextComponent("/ " + HumanReadableNumberFormatter.format(capacity)));
  }

  @Override
  public List<? extends ITextProperties> getTooltip() {
    return this.tooltip;
  }

  @Override
  public float getMeasurement() {
    float capacity = battery.getCapacity();
    if (capacity <= 0.0)
      return 0.0F;
    return Math.min(charge, capacity) / capacity;
  }

  @Override
  public float getServerValue() {
    return battery.getCharge();
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
