package mods.railcraft.client.util;

import mods.railcraft.client.gui.screen.IngameWindowScreen;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class GuiUtil {

  public static void drawCenteredString(GuiGraphics guiGraphics, Font font, Component component,
      int windowWidth, int y, boolean shadow) {
    int length = font.width(component);
    int x = windowWidth / 2 - length / 2;
    guiGraphics.drawString(font, component, x, y, IngameWindowScreen.TEXT_COLOR, shadow);
  }

  public static void drawCenteredString(GuiGraphics guiGraphics, Font font, Component component,
      int windowWidth, int y) {
    drawCenteredString(guiGraphics, font, component, windowWidth, y, false);
  }

  /**
   * Draws left aligned text without shadow, scrolling it back and forth when it is wider than the
   * given area, like vanilla buttons do. Coordinates are absolute screen coordinates, as the scissor
   * ignores the pose transformation.
   */
  public static void drawScrollingString(GuiGraphics guiGraphics, Font font, Component text,
      int minX, int minY, int maxX, int maxY, int color) {
    int textWidth = font.width(text);
    int y = (minY + maxY - font.lineHeight) / 2 + 1;
    int areaWidth = maxX - minX;
    if (textWidth <= areaWidth) {
      guiGraphics.drawString(font, text, minX, y, color, false);
      return;
    }
    // Same easing as AbstractWidget#renderScrollingString
    int overflow = textWidth - areaWidth;
    double seconds = Util.getMillis() / 1000.0;
    double period = Math.max(overflow * 0.5, 3.0);
    double progress =
        Math.sin(Math.PI / 2 * Math.cos(Math.PI * 2 * seconds / period)) / 2.0 + 0.5;
    int offset = (int) Mth.lerp(progress, 0.0, overflow);
    guiGraphics.enableScissor(minX, minY, maxX, maxY);
    guiGraphics.drawString(font, text, minX - offset, y, color, false);
    guiGraphics.disableScissor();
  }
}
