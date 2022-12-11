package mods.railcraft.gui.button;

import java.util.Optional;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import net.minecraft.network.chat.Component;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface ButtonState<T extends ButtonState<T>> {

  Component getLabel();

  TexturePosition getTexturePosition();

  default Optional<Component> getTooltip() {
    return Optional.empty();
  }

  T getNext();
}
