package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;

public class FluidGaugeWidgetRenderer extends WidgetRenderer<FluidGaugeWidget> {

  public FluidGaugeWidgetRenderer(FluidGaugeWidget widget) {
    super(widget);
  }

  @Override
  public List<Component> getTooltip() {
    return this.widget.tank.getTooltip();
  }

  // TODO: test render
  @Override
  public void draw(RailcraftMenuScreen<?> gui, PoseStack matrixStack, int guiX, int guiY,
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

    var color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
    var alpha = (float) (color >> 24 & 255) / 255.0F;
    var red = (float) (color >> 16 & 255) / 255.0F;
    var green = (float) (color >> 8 & 255) / 255.0F;
    var blue = (float) (color & 255) / 255.0F;

    RenderSystem.setShaderColor(red, green, blue, alpha);
    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

    for (var col = 0; col < this.widget.w / 16; col++) {
      for (var row = 0; row <= this.widget.h / 16; row++) {
        blit(matrixStack, guiX + this.widget.x + col * 16, guiY + this.widget.y + row * 16 - 1,
            this.getBlitOffset(), 16, 16, fluidIcon);
      }
    }

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, gui.getWidgetsTexture());

    var mask = (int) Math.floor(this.widget.h * scale);
    if (mask == 0 && fluidStack.getAmount() > 0) {
      mask = 1;
    }
    gui.blit(matrixStack, guiX + this.widget.x, guiY + this.widget.y - 1, this.widget.x,
        this.widget.y - 1, this.widget.w, this.widget.h - mask + 1);
    gui.blit(matrixStack, guiX + this.widget.x, guiY + this.widget.y, this.widget.u, this.widget.v,
        this.widget.w, this.widget.h);
  }
}
