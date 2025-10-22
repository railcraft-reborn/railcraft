package mods.railcraft.client.util;

import java.util.Collection;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class GuiUtil {
  public static void drawCenteredString(PoseStack poseStack, Font font, Component component,
                                        int windowWidth, int y, boolean shadow, int color) {
    int length = font.width(component);
    int x = windowWidth / 2 - length / 2;
    if (shadow) {
      font.drawShadow(poseStack, component, x, y, color);
    } else {
      font.draw(poseStack, component, x, y, color);
    }
  }

  public static void drawCenteredString(PoseStack poseStack, Font font, Component component, int windowWidth, int y) {
    drawCenteredString(poseStack, font, component, windowWidth, y, false, IngameWindowScreen.TEXT_COLOR);
  }

  public static void calculateHorizontalLayout(
      Collection<? extends AbstractWidget> widgets, int offsetX, int offsetY, int width) {
    if (widgets.isEmpty()) {
      return;
    }
    if (widgets.size() == 1) {
      widgets.forEach(
          widget -> {
            widget.x = offsetX;
            widget.y = offsetY;
          });
      return;
    }

    int totalUsedWidth = 0;
    for (AbstractWidget widget : widgets) {
      totalUsedWidth += widget.getWidth();
    }

    int totalAvailableWidth = Math.max(width - totalUsedWidth, 0);
    int spacing = totalAvailableWidth / (widgets.size() - 1);

    int currentPosX = offsetX;
    for (AbstractWidget widget : widgets) {
      widget.x = currentPosX;
      currentPosX += widget.getWidth() + spacing;
      widget.y = offsetY;
    }
  }
}
