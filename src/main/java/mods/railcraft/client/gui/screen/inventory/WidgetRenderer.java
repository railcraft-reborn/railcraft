package mods.railcraft.client.gui.screen.inventory;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.gui.widget.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WidgetRenderer<T extends Widget> {

  protected final T widget;

  public WidgetRenderer(T widget) {
    this.widget = widget;
  }

  public final boolean isMouseOver(double mouseX, double mouseY) {
    return mouseX >= this.widget.x - 1 && mouseX < this.widget.x + this.widget.w + 1
        && mouseY >= this.widget.y - 1
        && mouseY < this.widget.y + this.widget.h + 1;
  }

  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return false;
  }

  public void render(ResourceLocation widgetLocation, GuiGraphics guiGraphics, int centreX, int centreY,
      int mouseX, int mouseY) {
    guiGraphics.blit(widgetLocation, centreX + this.widget.x, centreY + this.widget.y, this.widget.u,
        this.widget.v, this.widget.w, this.widget.h);
  }

  @Nullable
  public List<Component> getTooltip() {
    return null;
  }
}
