package mods.railcraft.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class RenderUtil {

  public static final int FULL_LIGHT = 0xF000F0;
  public static final float PIXEL = 0.0625F;

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static int getColorARGB(FluidStack fluidStack, float fluidScale) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    int color = getColorARGB(fluidStack);
    if (fluidStack.getFluid().getAttributes().isGaseous(fluidStack)) {
      // TODO: We probably want to factor in the fluid's alpha value somehow
      return getColorARGB(getRed(color), getGreen(color), getBlue(color),
          Math.min(1, fluidScale + 0.2F));
    }
    return color;
  }

  public static int getColorARGB(FluidStack fluidStack) {
    return fluidStack.getFluid().getAttributes().getColor(fluidStack);
  }

  public static int getColorARGB(float red, float green, float blue, float alpha) {
    return getColorARGB((int) (255 * red), (int) (255 * green), (int) (255 * blue), alpha);
  }

  public static int getColorARGB(int red, int green, int blue, float alpha) {
    if (alpha < 0) {
      alpha = 0;
    } else if (alpha > 1) {
      alpha = 1;
    }
    int argb = (int) (255 * alpha) << 24;
    argb |= red << 16;
    argb |= green << 8;
    argb |= blue;
    return argb;
  }


  public static int calculateGlowLight(int combinedLight, FluidStack fluid) {
    return fluid.isEmpty() ? combinedLight
        : calculateGlowLight(combinedLight, fluid.getFluid().getAttributes().getLuminosity(fluid));
  }

  public static int calculateGlowLight(int combinedLight, int glow) {
    // Only factor the glow into the block light portion
    return (combinedLight & 0xFFFF0000) | Math.max(Math.min(glow, 15) << 4, combinedLight & 0xFFFF);
  }


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

  public static void renderBlockHoverText(BlockPos blockPos, ITextComponent text,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight) {
    if (minecraft.hitResult != null
        && minecraft.hitResult.getType() == RayTraceResult.Type.BLOCK
        && ((BlockRayTraceResult) minecraft.hitResult).getBlockPos().equals(blockPos)) {
      matrixStack.pushPose();
      {
        matrixStack.translate(0.5D, 1.5D, 0.5D);
        renderWorldText(minecraft.font, text, matrixStack, renderTypeBuffer, packedLight);
      }
      matrixStack.popPose();
    }
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
