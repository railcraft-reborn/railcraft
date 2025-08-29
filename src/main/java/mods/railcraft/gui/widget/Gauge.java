package mods.railcraft.gui.widget;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public interface Gauge {

  void refresh();

  default List<ClientTooltipComponent> getTooltip() {
    return Collections.emptyList();
  }

  default float getMeasurement() {
    return this.getClientValue();
  }

  float getServerValue();

  float getClientValue();

  void setClientValue(float value);
}
