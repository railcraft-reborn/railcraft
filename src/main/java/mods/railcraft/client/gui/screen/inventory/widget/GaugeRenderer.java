package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.gui.widget.GaugeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GaugeRenderer extends WidgetRenderer<GaugeWidget> {

  public GaugeRenderer(GaugeWidget widget) {
    super(widget);
  }

  @Override
  public void render(ResourceLocation widgetLocation, GuiGraphics guiGraphics, int centreX,
      int centreY, int mouseX, int mouseY) {
    int scale = Math.round(this.widget.getMeasurement() *
        (this.widget.isVertical() ? this.widget.h : this.widget.w));
    if (this.widget.isVertical()) {
      guiGraphics.blit(widgetLocation, centreX + this.widget.x,
          centreY + this.widget.y + this.widget.h - scale,
          this.widget.u, this.widget.v + this.widget.h - scale, this.widget.w, scale);
    } else {
      guiGraphics.blit(widgetLocation, centreX + this.widget.x, centreY + this.widget.y,
          this.widget.u, this.widget.v, scale, this.widget.h);
    }
  }

  @Override
  public List<Component> getTooltip() {
    return this.widget.getGauge().getTooltip();
  }
}
