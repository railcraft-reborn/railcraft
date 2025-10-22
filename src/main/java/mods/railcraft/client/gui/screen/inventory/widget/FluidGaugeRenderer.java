package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
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
  public void render(ResourceLocation widgetLocation, GuiComponent guiComponent, PoseStack poseStack,
                     int centreX, int centreY, int mouseX, int mouseY) {
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
    var alpha = FastColor.ARGB32.alpha(color) / 255.0F;
    var red = FastColor.ARGB32.red(color) / 255.0F;
    var green = FastColor.ARGB32.green(color) / 255.0F;
    var blue = FastColor.ARGB32.blue(color) / 255.0F;

    for (var col = 0; col < this.widget.w / 16; col++) {
      for (var row = 0; row <= this.widget.h / 16; row++) {
        tintedBlit(poseStack,
            centreX + this.widget.x + col * 16,
            centreY + this.widget.y + row * 16 - 1,
            0, 16, 16, fluidIcon, red, green, blue, alpha);
      }
    }

    var mask = Mth.floor(this.widget.h * scale);
    if (mask == 0 && fluidStack.getAmount() > 0) {
      mask = 1;
    }
    RenderSystem.setShaderTexture(0, widgetLocation);
    guiComponent.blit(poseStack, centreX + this.widget.x, centreY + this.widget.y - 1,
        this.widget.x,
        this.widget.y - 1, this.widget.w, this.widget.h - mask + 1);
    guiComponent.blit(poseStack, centreX + this.widget.x, centreY + this.widget.y,
        this.widget.u,
        this.widget.v, this.widget.w, this.widget.h);
  }

  private void tintedBlit(PoseStack pose, int x, int y, int blitOffset, int width, int height,
      TextureAtlasSprite sprite, float ped, float green, float blue, float alpha) {
    ResourceLocation atlasLocation = sprite.atlas().location();
    float minU = sprite.getU0();
    float maxU = sprite.getU1();
    float minV = sprite.getV0();
    float maxV = sprite.getV1();

    RenderSystem.setShaderTexture(0, atlasLocation);
    RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
    RenderSystem.enableBlend();
    Matrix4f matrix4f = pose.last().pose();
    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
    bufferbuilder.vertex(matrix4f, (float) x, (float) y, (float) blitOffset)
        .color(ped, green, blue, alpha).uv(minU, minV).endVertex();
    bufferbuilder.vertex(matrix4f, (float) x, (float) (y + height), (float) blitOffset)
        .color(ped, green, blue, alpha).uv(minU, maxV).endVertex();
    bufferbuilder.vertex(matrix4f, (float) (x + width), (float) (y + height), (float) blitOffset)
        .color(ped, green, blue, alpha).uv(maxU, maxV).endVertex();
    bufferbuilder.vertex(matrix4f, (float) (x + width), (float) y, (float) blitOffset)
        .color(ped, green, blue, alpha).uv(maxU, minV).endVertex();
    BufferUploader.drawWithShader(bufferbuilder.end());
    RenderSystem.disableBlend();
  }
}
