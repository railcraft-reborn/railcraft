package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.gui.widget.GaugeWidget;
import net.minecraft.network.chat.Component;

public class GaugeWidgetRenderer extends WidgetRenderer<GaugeWidget> {

  public GaugeWidgetRenderer(GaugeWidget widget) {
    super(widget);
  }

  @Override
  public void render(RailcraftMenuScreen<?> screen, PoseStack poseStack, int centreX, int centreY,
      int mouseX, int mouseY) {
    int scale = Math.round((float) (this.widget.getMeasurement()
        * (double) (this.widget.isVertical() ? this.widget.h : this.widget.w)));
    if (this.widget.isVertical())
      this.blit(poseStack, centreX + this.widget.x, centreY + this.widget.y + this.widget.h - scale,
          this.widget.u, this.widget.v + this.widget.h - scale, this.widget.w, scale);
    else
      this.blit(poseStack, centreX + this.widget.x, centreY + this.widget.y, this.widget.u,
          this.widget.v, scale, this.widget.h);
  }

  @Override
  public List<Component> getTooltip() {
    return this.widget.getGauge().getTooltip();
  }
}
