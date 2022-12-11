package mods.railcraft.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class RenderUtil {

  public static final int FULL_LIGHT = 0xF000F0;
  public static final float PIXEL = 0.0625F;
  public static final float SCALED_PIXEL = PIXEL / 16.0F;

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static int getColorARGB(FluidStack fluidStack, float fluidScale) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    int color = getColorARGB(fluidStack);
    if (fluidStack.getFluid().getFluidType().isLighterThanAir()) {
      // TODO: We probably want to factor in the fluid's alpha value somehow
      return getColorARGB(getRed(color), getGreen(color), getBlue(color),
          Math.min(1, fluidScale + 0.2F));
    }
    return color;
  }

  public static int getColorARGB(FluidStack fluidStack) {
    return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
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
        : calculateGlowLight(combinedLight, fluid.getFluid().getFluidType().getLightLevel(fluid));
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

  public static void renderBlockHoverText(BlockPos blockPos, Component text,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
    if (minecraft.hitResult != null
        && minecraft.hitResult.getType() == HitResult.Type.BLOCK
        && ((BlockHitResult) minecraft.hitResult).getBlockPos().equals(blockPos)) {
      poseStack.pushPose();
      {
        poseStack.translate(0.5D, 1.5D, 0.5D);
        renderWorldText(minecraft.font, text, poseStack, bufferSource, packedLight);
      }
      poseStack.popPose();
    }
  }

  public static void renderWorldText(Font font, Component text,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
    poseStack.pushPose();
    {
      poseStack.mulPose(minecraft.gameRenderer.getMainCamera().rotation());
      poseStack.scale(-0.025F, -0.025F, 0.025F);
      var matrix = poseStack.last().pose();
      float backgroundOpacity = minecraft.options.getBackgroundOpacity(0.25F);
      int packedOverlay = (int) (backgroundOpacity * 255.0F) << 24;
      float x = (float) (-font.width(text) / 2);
      font.drawInBatch(text, x, 0, 0x20FFFFFF, false, matrix, bufferSource,
          true, packedOverlay, packedLight);
      font.drawInBatch(text, x, 0, -1, false, matrix, bufferSource, false,
          0, packedLight);
    }
    poseStack.popPose();
  }
}
