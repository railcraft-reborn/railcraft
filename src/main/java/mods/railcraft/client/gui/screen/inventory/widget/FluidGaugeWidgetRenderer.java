package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.fluids.FluidStack;

public class FluidGaugeWidgetRenderer extends WidgetRenderer<FluidGaugeWidget> {

  public FluidGaugeWidgetRenderer(FluidGaugeWidget widget) {
    super(widget);
  }

  @Override
  public List<? extends ITextProperties> getTooltip() {
    return this.widget.tank.getTooltip();
  }

  // TODO: test render
  @SuppressWarnings("deprecation")
  @Override
  public void draw(RailcraftMenuScreen<?> gui, MatrixStack matrixStack, int guiX, int guiY,
      int mouseX, int mouseY) {
    if (this.widget.tank == null)
      return;

    FluidStack fluidStack = this.widget.tank.getFluid();
    if (fluidStack == null || fluidStack.getAmount() <= 0)
      return;

    TextureAtlasSprite fluidIcon =
        FluidRenderer.getFluidTexture(fluidStack, FluidRenderer.FlowState.STILL);
    if (fluidIcon == null)
      return;

    float scale = Math.min(fluidStack.getAmount(), this.widget.tank.getCapacity())
        / (float) this.widget.tank.getCapacity();

    gui.bindTexture(FluidRenderer.getFluidSheet(fluidStack));

    int color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
    float alpha = (float) (color >> 24 & 255) / 255.0F;
    float red = (float) (color >> 16 & 255) / 255.0F;
    float green = (float) (color >> 8 & 255) / 255.0F;
    float blue = (float) (color & 255) / 255.0F;
    RenderSystem.color4f(red, green, blue, alpha);

    for (int col = 0; col < this.widget.w / 16; col++) {
      for (int row = 0; row <= this.widget.h / 16; row++) {
        blit(matrixStack, guiX + this.widget.x + col * 16,
            guiY + this.widget.y + row * 16 - 1, this.getBlitOffset(), 16, 16, fluidIcon);
      }
    }

    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    gui.bindTexture(gui.getTextureLocation());
    int mask = (int) Math.floor(this.widget.h * scale);
    if (mask == 0 && fluidStack.getAmount() > 0)
      mask = 1;
    gui.blit(matrixStack, guiX + this.widget.x, guiY + this.widget.y - 1, this.widget.x,
        this.widget.y - 1, this.widget.w, this.widget.h - mask + 1);
    gui.blit(matrixStack, guiX + this.widget.x, guiY + this.widget.y, this.widget.u, this.widget.v,
        this.widget.w, this.widget.h);
  }
}
