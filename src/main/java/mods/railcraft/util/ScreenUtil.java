package mods.railcraft.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class ScreenUtil {
  public static void drawCenteredString(PoseStack poseStack, Component component, Font font,
      int width, int y, int color, boolean shadow) {
    int length = font.width(component);
    int x = width / 2 - length / 2;
    if (shadow) {
      font.drawShadow(poseStack, component, x, y, color);
    } else {
      font.draw(poseStack, component, x, y, color);
    }
  }
}
