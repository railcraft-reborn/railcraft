package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FluidGaugeRenderer extends WidgetRenderer<FluidGaugeWidget> {

  public FluidGaugeRenderer(FluidGaugeWidget widget) {
    super(widget);
  }

  @Override
  public List<ClientTooltipComponent> getTooltip() {
    return this.widget.getTooltip().stream()
        .map(Component::getVisualOrderText)
        .map(ClientTooltipComponent::create)
        .toList();
  }

  @Override
  public void render(ResourceLocation widgetLocation, GuiGraphics guiGraphics, int centreX,
      int centreY,
      int mouseX, int mouseY) {
    if (this.widget.tank == null) {
      return;
    }

    var fluidStack = this.widget.tank.getFluid();
    if (fluidStack.getAmount() <= 0) {
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

    for (var col = 0; col < this.widget.w / 16; col++) {
      for (var row = 0; row <= this.widget.h / 16; row++) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, fluidIcon.atlasLocation(), centreX + this.widget.x + col * 16,
            centreY + this.widget.y + row * 16 - 1, 0, 16, 16, color, 256, 256);
      }
    }

    var mask = Mth.floor(this.widget.h * scale);
    if (mask == 0 && fluidStack.getAmount() > 0) {
      mask = 1;
    }
    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, widgetLocation, centreX + this.widget.x,
        centreY + this.widget.y - 1, this.widget.x, this.widget.y - 1, this.widget.w,
        this.widget.h - mask + 1, 256, 256);
    guiGraphics.blit(RenderPipelines.GUI_TEXTURED,  widgetLocation, centreX + this.widget.x,
        centreY + this.widget.y, this.widget.u, this.widget.v, this.widget.w,
        this.widget.h, 256, 256);
  }
}
