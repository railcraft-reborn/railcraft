package mods.railcraft.gui.button;

import java.util.List;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import net.minecraft.network.chat.Component;

public interface ButtonState<T extends ButtonState<T>> {

  Component getLabel();

  TexturePosition getTexturePosition();

  default List<Component> getTooltip() {
    return List.of();
  }

  T getNext();
}
