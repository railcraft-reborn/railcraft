package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.gui.widget.GaugeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class GaugeRenderer extends WidgetRenderer<GaugeWidget> {

  public GaugeRenderer(GaugeWidget widget) {
    super(widget);
  }

  @Override
  public void render(Identifier widgetLocation, GuiGraphics guiGraphics, int centreX,
      int centreY, int mouseX, int mouseY) {
    int scale = Math.round(this.widget.getMeasurement() *
        (this.widget.isVertical() ? this.widget.h : this.widget.w));
    if (this.widget.isVertical()) {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, widgetLocation, centreX + this.widget.x,
          centreY + this.widget.y + this.widget.h - scale,
          this.widget.u, this.widget.v + this.widget.h - scale, this.widget.w, scale, 256, 256);
    } else {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, widgetLocation, centreX + this.widget.x,
          centreY + this.widget.y, this.widget.u, this.widget.v, scale, this.widget.h, 256, 256);
    }
  }

  @Override
  public List<ClientTooltipComponent> getTooltip() {
    return this.widget.getGauge().getTooltip();
  }
}
