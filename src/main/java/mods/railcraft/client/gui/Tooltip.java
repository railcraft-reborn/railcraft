package mods.railcraft.client.gui;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class Tooltip {
  private final Component component;
  @Nullable
  private List<FormattedCharSequence> cachedTooltip;

  private Tooltip(Component component) {
    this.component = component;
  }

  public List<FormattedCharSequence> toCharSequence(Screen screen) {
    if (this.cachedTooltip == null) {
      this.cachedTooltip = screen.getMinecraft().font.split(component, 170);
    }
    return this.cachedTooltip;
  }

  public static Tooltip create(Component component) {
    return new Tooltip(component);
  }
}
