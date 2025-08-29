package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.gui.widget.AnalogGaugeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class AnalogGaugeRenderer extends WidgetRenderer<AnalogGaugeWidget> {

  public AnalogGaugeRenderer(AnalogGaugeWidget widget) {
    super(widget);
  }

  @Override
  public List<ClientTooltipComponent> getTooltip() {
    return this.widget.getGauge().getTooltip();
  }

  @Override
  public void render(ResourceLocation widgetLocation, GuiGraphics guiGraphics, int centreX, int centreY,
      int mouseX, int mouseY) {

    float halfWidth = 1; // half width of the needle
    float len = this.widget.h * 0.75F; // length of the needle (along the center)

    // average the value over time to smooth the needle (0.0 - 1.0)
    float value = this.widget.getMeasurement();

    // set the needle angle between 30° (= 0%) and 150° (= 100%)
    float angle = (120 * value + 30) * Mth.DEG_TO_RAD;

    float cosA = Mth.cos(angle);
    float sinA = Mth.sin(angle);

    // displacement along the length of the needle
    float glx = cosA * len;
    float gly = sinA * len;

    // displacement along the width of the needle
    float gwx = sinA * halfWidth;
    float gwy = cosA * halfWidth;

    // half width of the horizontal needle part where it connects to the "case"
    float baseOffset = 1.0F / sinA * halfWidth;

    // set the needle color to dark-ish red
    var color = ARGB.color(255, 100, 0, 0);

    float gx = centreX + this.widget.x;
    float gy = centreY + this.widget.y - 1;

    float bx = gx + this.widget.w * 0.5F;
    float by = gy + this.widget.h;

    guiGraphics.submitGuiElementRenderState(new GuiElementRenderState() {

      private final Matrix3x2f pose = new Matrix3x2f(guiGraphics.pose());
      @Nullable
      private final ScreenRectangle scissorArea = guiGraphics.peekScissorStack();

      @Override
      @Nullable
      public ScreenRectangle bounds() {
        var rectangle = new ScreenRectangle(
            (int) (bx - baseOffset),
            (int) ((int) by - (gly + gwy)),
            2 * (int)baseOffset,
            2 * (int)gwy
        );

        rectangle = rectangle.transformMaxBounds(this.pose);

        return this.scissorArea != null
            ? this.scissorArea.intersection(rectangle)
            : rectangle;
      }

      @Override
      @Nullable
      public ScreenRectangle scissorArea() {
        return this.scissorArea;
      }

      @Override
      public RenderPipeline pipeline() {
        return RenderPipelines.GUI;
      }

      @Override
      public TextureSetup textureSetup() {
        return TextureSetup.noTexture();
      }

      @Override
      public void buildVertices(VertexConsumer vertexConsumer, float z) {
        vertexConsumer
            .addVertexWith2DPose(this.pose, bx - baseOffset, by, z)
            .setColor(color);
        vertexConsumer
            .addVertexWith2DPose(this.pose, bx + baseOffset, by, z)
            .setColor(color);
        vertexConsumer
            .addVertexWith2DPose(this.pose, bx - glx + gwx, by - (gly + gwy), z)
            .setColor(color);
        vertexConsumer
            .addVertexWith2DPose(this.pose, bx - glx - gwx, by - (gly - gwy), z)
            .setColor(color);
      }
    });

    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, widgetLocation, centreX + this.widget.ox, centreY + this.widget.oy, this.widget.ou,
        this.widget.ov, 4, 3, 256, 256);
  }
}
