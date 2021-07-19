package mods.railcraft.client.util;

public class RenderUtil {

  public static final int FULL_LIGHT = 0xF000F0;
  public static final float PIXEL = 0.0625F;

  public static float getRed(int color) {
    return (color >> 16 & 0xFF) / 255.0F;
  }

  public static float getGreen(int color) {
    return (color >> 8 & 0xFF) / 255.0F;
  }

  public static float getBlue(int color) {
    return (color & 0xFF) / 255.0F;
  }

  public static float getAlpha(int color) {
    return (color >> 24 & 0xFF) / 255.0F;
  }
}
