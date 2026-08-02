package mods.railcraft.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
      return ARGB.color(alpha, ARGB.red(color), ARGB.green(color), ARGB.blue(color));
    }
    return color;
  }

  public static int getColorARGB(FluidStack fluidStack) {
    var fluidModel = Minecraft.getInstance()
        .getModelManager()
        .getFluidStateModelSet()
        .get(fluidStack.getFluid().defaultFluidState());
    var tintSource = fluidModel.fluidTintSource();
    if (tintSource == null) {
      return -1;
    }
    return tintSource.colorAsStack(fluidStack);
  }

  public static int calculateGlowLight(int combinedLight, FluidStack fluid) {
    return fluid.isEmpty() ? combinedLight
        : calculateGlowLight(combinedLight, fluid.getFluidType().getLightLevel(fluid));
  }

  public static int calculateGlowLight(int combinedLight, int glow) {
    // Only factor the glow into the block light portion
    return (combinedLight & 0xFFFF0000) | Math.max(Math.min(glow, 15) << 4, combinedLight & 0xFFFF);
  }

  public static void renderBlockHoverText(SubmitNodeCollector collector, BlockPos blockPos,
      Component text, PoseStack poseStack, int packedLight) {
    if (minecraft.hitResult != null
        && minecraft.hitResult.getType() == HitResult.Type.BLOCK
        && ((BlockHitResult) minecraft.hitResult).getBlockPos().equals(blockPos)) {
      poseStack.pushPose();
      poseStack.translate(0.5F, 1.5F, 0.5F);
      renderWorldText(collector, minecraft.font, text, poseStack, packedLight);
      poseStack.popPose();
    }
  }

  public static void renderWorldText(SubmitNodeCollector collector, Font font, Component text,
      PoseStack poseStack, int packedLight) {
    poseStack.pushPose();
    poseStack.mulPose(minecraft.gameRenderer.getMainCamera().rotation());
    poseStack.scale(0.025F, -0.025F, 0.025F);
    float backgroundOpacity = minecraft.options.getBackgroundOpacity(0.25F);
    int packedOverlay = (int) (backgroundOpacity * 255.0F) << 24;
    float x = (float) (-font.width(text) / 2);
    collector.submitText(poseStack, x, 0, text.getVisualOrderText(), false,
        Font.DisplayMode.SEE_THROUGH, packedLight, 0x20FFFFFF, packedOverlay, 0);
    collector.submitText(poseStack, x, 0, text.getVisualOrderText(), false,
        Font.DisplayMode.NORMAL, packedLight, -1, 0, 0);
    poseStack.popPose();
  }
}
