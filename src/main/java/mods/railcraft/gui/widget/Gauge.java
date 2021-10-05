package mods.railcraft.gui.widget;

import java.util.Collections;
import java.util.List;
import net.minecraft.util.text.ITextProperties;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface Gauge {

  void refresh();

  default List<? extends ITextProperties> getTooltip() {
    return Collections.emptyList();
  }

  default float getMeasurement() {
    return this.getClientValue();
  }

  float getServerValue();

  float getClientValue();

  void setClientValue(float value);
}
