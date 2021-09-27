package mods.railcraft.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

public class RenderUtil {

  public static final int FULL_LIGHT = 0xF000F0;
  public static final float PIXEL = 0.0625F;

  private static final Minecraft minecraft = Minecraft.getInstance();

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

  public static void renderWorldText(FontRenderer font, ITextComponent text,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight) {
    matrixStack.pushPose();
    {
      matrixStack.mulPose(minecraft.gameRenderer.getMainCamera().rotation());
      matrixStack.scale(-0.025F, -0.025F, 0.025F);
      Matrix4f matrix = matrixStack.last().pose();
      float backgroundOpacity = minecraft.options.getBackgroundOpacity(0.25F);
      int packedOverlay = (int) (backgroundOpacity * 255.0F) << 24;
      float x = (float) (-font.width(text) / 2);
      font.drawInBatch(text, x, 0, 0x20FFFFFF, false, matrix, renderTypeBuffer,
          true, packedOverlay, packedLight);
      font.drawInBatch(text, x, 0, -1, false, matrix, renderTypeBuffer, false,
          0, packedLight);
    }
    matrixStack.popPose();
  }
}
