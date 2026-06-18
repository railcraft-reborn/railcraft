package mods.railcraft.client.gui.screen.inventory;

import java.util.List;
import mods.railcraft.gui.widget.Widget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

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

  public void render(Identifier widgetLocation, GuiGraphicsExtractor graphics, int centreX, int centreY,
      int mouseX, int mouseY) {
    graphics.blit(RenderPipelines.GUI_TEXTURED, widgetLocation, centreX + this.widget.x, centreY + this.widget.y,
        this.widget.u, this.widget.v, this.widget.w, this.widget.h, 256, 256);
  }

  public List<ClientTooltipComponent> getTooltip() {
    return List.of();
  }
}
