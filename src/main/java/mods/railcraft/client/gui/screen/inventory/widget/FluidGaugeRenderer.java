package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FluidGaugeRenderer extends WidgetRenderer<FluidGaugeWidget> {

  public FluidGaugeRenderer(FluidGaugeWidget widget) {
    super(widget);
  }

  @Override
  public List<Component> getTooltip() {
    return this.widget.getTooltip();
  }

  @Override
  public void render(ResourceLocation widgetLocation, GuiGraphics guiGraphics, int centreX, int centreY,
      int mouseX, int mouseY) {
    if (this.widget.tank == null) {
      return;
    }

    var fluidStack = this.widget.tank.getFluid();
    if (fluidStack == null || fluidStack.getAmount() <= 0) {
      return;
    }

    var fluidIcon =
        FluidRenderer.getFluidTexture(fluidStack, FluidRenderer.FluidType.STILL);
    if (fluidIcon == null) {
      return;
    }

    var scale = Math.min(fluidStack.getAmount(), this.widget.tank.getCapacity())
        / (float) this.widget.tank.getCapacity();

    var color = RenderUtil.getColorARGB(fluidStack);
    var alpha = RenderUtil.getAlpha(color);
    var red = RenderUtil.getRed(color);
    var green = RenderUtil.getGreen(color);
    var blue = RenderUtil.getBlue(color);

    for (var col = 0; col < this.widget.w / 16; col++) {
      for (var row = 0; row <= this.widget.h / 16; row++) {
        guiGraphics.blit(centreX + this.widget.x + col * 16, centreY + this.widget.y + row * 16 - 1,
            0, 16, 16, fluidIcon, red, green, blue, alpha);
      }
    }

    var mask = Mth.floor(this.widget.h * scale);
    if (mask == 0 && fluidStack.getAmount() > 0) {
      mask = 1;
    }
    guiGraphics.blit(widgetLocation, centreX + this.widget.x, centreY + this.widget.y - 1, this.widget.x,
        this.widget.y - 1, this.widget.w, this.widget.h - mask + 1);
    guiGraphics.blit(widgetLocation, centreX + this.widget.x, centreY + this.widget.y, this.widget.u,
        this.widget.v, this.widget.w, this.widget.h);
  }
}
