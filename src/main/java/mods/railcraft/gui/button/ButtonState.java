package mods.railcraft.gui.button;

import java.util.Optional;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import net.minecraft.network.chat.Component;

public interface ButtonState<T extends ButtonState<T>> {

  Component label();

  TexturePosition texturePosition();

  default Optional<Component> tooltip() {
    return Optional.empty();
  }

  T next();
}
