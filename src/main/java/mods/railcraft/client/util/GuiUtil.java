package mods.railcraft.client.util;

import mods.railcraft.client.gui.screen.IngameWindowScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

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
}
