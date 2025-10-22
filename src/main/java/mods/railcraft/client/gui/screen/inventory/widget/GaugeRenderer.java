package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.gui.widget.GaugeWidget;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GaugeRenderer extends WidgetRenderer<GaugeWidget> {

  public GaugeRenderer(GaugeWidget widget) {
    super(widget);
  }

  @Override
  public void render(ResourceLocation widgetLocation, GuiComponent guiComponent, PoseStack poseStack,
                     int centreX, int centreY, int mouseX, int mouseY) {
    int scale = Math.round(this.widget.getMeasurement() *
        (this.widget.isVertical() ? this.widget.h : this.widget.w));
    if (this.widget.isVertical()) {
      RenderSystem.setShaderTexture(0, widgetLocation);
      guiComponent.blit(poseStack, centreX + this.widget.x,
          centreY + this.widget.y + this.widget.h - scale,
          this.widget.u, this.widget.v + this.widget.h - scale, this.widget.w, scale);
    } else {
      RenderSystem.setShaderTexture(0, widgetLocation);
      guiComponent.blit(poseStack, centreX + this.widget.x, centreY + this.widget.y,
          this.widget.u, this.widget.v, scale, this.widget.h);
    }
  }

  @Override
  public List<Component> getTooltip() {
    return this.widget.getGauge().getTooltip();
  }
}
