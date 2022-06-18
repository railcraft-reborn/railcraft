package mods.railcraft.client.gui.screen.inventory.widget;

import java.util.List;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.client.gui.screen.inventory.WidgetRenderer;
import mods.railcraft.gui.widget.AnalogGaugeWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class AnalogGaugeRenderer extends WidgetRenderer<AnalogGaugeWidget> {

  public AnalogGaugeRenderer(AnalogGaugeWidget widget) {
    super(widget);
  }

  @Override
  public List<Component> getTooltip() {
    return this.widget.getGauge().getTooltip();
  }

  @Override
  public void render(RailcraftMenuScreen<?> screen, PoseStack poseStack, int centreX, int centreY,
      int mouseX, int mouseY) {

    float halfWidth = 1; // half width of the needle
    float len = this.widget.h * 0.75F; // length of the needle (along the center)

    // average the value over time to smooth the needle (0.0 - 1.0)
    float value = this.widget.getMeasurement();

    // set the needle angle between 30° (= 0%) and 150° (= 100%)
    float angle = (120 * value + 30) * Mth.DEG_TO_RAD;

    var tesselator = Tesselator.getInstance();
    var builder = tesselator.getBuilder();

    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

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
    int red = 100;
    int green = 0;
    int blue = 0;
    int alpha = 255;

    float z = screen.getBlitOffset();
    float gx = centreX + this.widget.x;
    float gy = centreY + this.widget.y - 1;

    float bx = gx + this.widget.w * 0.5F;
    float by = gy + this.widget.h;

    var matrix = poseStack.last().pose();
    builder
        .vertex(matrix, bx - baseOffset, by, z)
        .color(red, green, blue, alpha)
        .endVertex();
    builder
        .vertex(matrix, bx + baseOffset, by, z)
        .color(red, green, blue, alpha)
        .endVertex();
    builder
        .vertex(matrix, bx - glx + gwx, by - (gly + gwy), z)
        .color(red, green, blue, alpha)
        .endVertex();
    builder
        .vertex(matrix, bx - glx - gwx, by - (gly - gwy), z)
        .color(red, green, blue, alpha)
        .endVertex();

    tesselator.end();

    this.blit(poseStack, centreX + this.widget.ox, centreY + this.widget.oy, this.widget.ou,
        this.widget.ov, 4, 3);
  }
}
