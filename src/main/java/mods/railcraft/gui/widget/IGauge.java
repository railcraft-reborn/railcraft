package mods.railcraft.gui.widget;

import java.util.Collections;
import java.util.List;
import net.minecraft.util.text.ITextProperties;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IGauge {

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
