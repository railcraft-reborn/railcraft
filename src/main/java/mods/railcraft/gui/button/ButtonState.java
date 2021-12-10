package mods.railcraft.gui.button;

import java.util.List;
import javax.annotation.Nullable;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import net.minecraft.network.chat.Component;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface ButtonState<T extends ButtonState<T>> {

  Component getLabel();

  TexturePosition getTexturePosition();

  @Nullable
  default List<Component> getTooltip() {
    return null;
  }

  T getNext();
}
