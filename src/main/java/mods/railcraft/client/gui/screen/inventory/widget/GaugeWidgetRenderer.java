package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.gui.widget.GaugeWidget;
import net.minecraft.util.text.ITextProperties;

public class GaugeWidgetRenderer extends WidgetRenderer<GaugeWidget> {

  public GaugeWidgetRenderer(GaugeWidget widget) {
    super(widget);
  }

  @Override
  public void draw(RailcraftMenuScreen<?> gui, MatrixStack matrixStack, int guiX, int guiY,
      int mouseX, int mouseY) {
    int scale = Math.round((float) (this.widget.getMeasurement()
        * (double) (this.widget.isVertical() ? this.widget.h : this.widget.w)));
    if (this.widget.isVertical())
      this.blit(matrixStack, guiX + this.widget.x, guiY + this.widget.y + this.widget.h - scale,
          this.widget.u, this.widget.v + this.widget.h - scale, this.widget.w, scale);
    else
      this.blit(matrixStack, guiX + this.widget.x, guiY + this.widget.y, this.widget.u,
          this.widget.v, scale, this.widget.h);
  }

  @Override
  public List<? extends ITextProperties> getTooltip() {
    return this.widget.getGauge().getTooltip();
  }
}
