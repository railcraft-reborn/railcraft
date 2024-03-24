package mods.railcraft.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class RenderUtil {

  public static final int FULL_LIGHT = 0xF000F0;
  public static final float PIXEL = 0.0625F;
  public static final float SCALED_PIXEL = PIXEL / 16.0F;

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static int replaceAlpha(int color, int alpha) {
    return (color & 0x00FFFFFF) | (alpha << 24);
  }

  public static int getColorARGB(FluidStack fluidStack, float fluidScale) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    int color = getColorARGB(fluidStack);
    if (fluidStack.getFluidType().isLighterThanAir()) {
      // TODO: We probably want to factor in the fluid's alpha value somehow
      var alpha = (int) (Math.min(1, fluidScale + 0.2F) * 255);
      return FastColor.ARGB32.color(
          alpha,
          FastColor.ARGB32.red(color),
          FastColor.ARGB32.green(color),
          FastColor.ARGB32.blue(color));
    }
    return color;
  }

  public static int getColorARGB(FluidStack fluidStack) {
    return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
  }

  public static int calculateGlowLight(int combinedLight, FluidStack fluid) {
    return fluid.isEmpty() ? combinedLight
        : calculateGlowLight(combinedLight, fluid.getFluidType().getLightLevel(fluid));
  }

  public static int calculateGlowLight(int combinedLight, int glow) {
    // Only factor the glow into the block light portion
    return (combinedLight & 0xFFFF0000) | Math.max(Math.min(glow, 15) << 4, combinedLight & 0xFFFF);
  }

  public static void renderBlockHoverText(BlockPos blockPos, Component text, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight) {
    if (minecraft.hitResult != null
        && minecraft.hitResult.getType() == HitResult.Type.BLOCK
        && ((BlockHitResult) minecraft.hitResult).getBlockPos().equals(blockPos)) {
      poseStack.pushPose();
      poseStack.translate(0.5F, 1.5F, 0.5F);
      renderWorldText(minecraft.font, text, poseStack, bufferSource, packedLight);
      poseStack.popPose();
    }
  }

  public static void renderWorldText(Font font, Component text, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight) {
    poseStack.pushPose();
    poseStack.mulPose(minecraft.gameRenderer.getMainCamera().rotation());
    poseStack.scale(-0.025F, -0.025F, 0.025F);
    var matrix = poseStack.last().pose();
    float backgroundOpacity = minecraft.options.getBackgroundOpacity(0.25F);
    int packedOverlay = (int) (backgroundOpacity * 255.0F) << 24;
    float x = (float) (-font.width(text) / 2);
    font.drawInBatch(text, x, 0, 0x20FFFFFF, false, matrix, bufferSource,
        Font.DisplayMode.SEE_THROUGH, packedOverlay, packedLight);
    font.drawInBatch(text, x, 0, -1, false, matrix, bufferSource,
        Font.DisplayMode.NORMAL, 0, packedLight);
    poseStack.popPose();
  }
}
