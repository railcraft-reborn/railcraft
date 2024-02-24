package mods.railcraft.client.renderer.blockentity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.signal.BlockSignalEntity;
import mods.railcraft.api.signal.TokenSignalEntity;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SignalAuraRenderUtil {

  public static void tryRenderSignalAura(BlockEntity blockEntity,
      PoseStack poseStack, MultiBufferSource bufferSource) {
    if (blockEntity instanceof SignalControllerEntity provider) {
      var peers = provider.getSignalController().peers();
      if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.TUNING)) {
        renderSignalAura(blockEntity, poseStack, bufferSource, peers,
            ColorProfile.COORD_RAINBOW);
      } else if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderSignalAura(blockEntity, poseStack, bufferSource, peers,
            ColorProfile.CONTROLLER_ASPECT);
      }
    }
    if (blockEntity instanceof BlockSignalEntity blockSignal) {
      var peers = blockSignal.signalNetwork().peers();
      if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SURVEYING)) {
        renderSignalAura(blockEntity, poseStack, bufferSource, peers,
            ColorProfile.COORD_RAINBOW);
      } else if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderSignalAura(blockEntity, poseStack, bufferSource, peers,
            ColorProfile.CONSTANT_BLUE);
      }
    } else if (blockEntity instanceof TokenSignalEntity tokenSignal) {
      var centroid = Collections.singletonList(tokenSignal.ringCentroidPos());
      if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SURVEYING)) {
        renderSignalAura(blockEntity, poseStack, bufferSource, centroid,
            (t, s, d) -> tokenSignal.ringId().hashCode());
      } else if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderSignalAura(blockEntity, poseStack, bufferSource, centroid,
            ColorProfile.CONSTANT_BLUE);
      }
    }
  }

  private static void renderSignalAura(
      BlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource,
      Collection<BlockPos> endPoints, ColorSupplier colorProfile) {
    if (endPoints.isEmpty()) {
      return;
    }

    poseStack.pushPose();
    var consumer = bufferSource.getBuffer(RenderType.lines());
    var matrix = poseStack.last().pose();
    var normal = poseStack.last().normal();

    for (BlockPos target : endPoints) {
      int color = colorProfile.getColor(blockEntity, blockEntity.getBlockPos(), target);
      float red = RenderUtil.getRed(color);
      float green = RenderUtil.getGreen(color);
      float blue = RenderUtil.getBlue(color);

      consumer
          .vertex(matrix, 0.5F, 0.5F, 0.5F)
          .color(red, green, blue, 1)
          .normal(normal, 1, 0, 0)
          .endVertex();

      float endX = 0.5F + target.getX() - blockEntity.getBlockPos().getX();
      float endY = 0.5F + target.getY() - blockEntity.getBlockPos().getY();
      float endZ = 0.5F + target.getZ() - blockEntity.getBlockPos().getZ();

      consumer
          .vertex(matrix, endX, endY, endZ)
          .color(red, green, blue, 1)
          .normal(normal, 1, 0, 0)
          .endVertex();
    }
    poseStack.popPose();
  }

  @FunctionalInterface
  public interface ColorSupplier {

    int getColor(BlockEntity blockEntity, BlockPos source, BlockPos target);
  }

  public enum ColorProfile implements ColorSupplier {
    COORD_RAINBOW {

      private final BlockPos[] coords = new BlockPos[2];

      @Override
      public int getColor(BlockEntity blockEntity, BlockPos source, BlockPos target) {
        this.coords[0] = source;
        this.coords[1] = target;
        Arrays.sort(this.coords);
        return Arrays.hashCode(coords);
      }
    },
    CONSTANT_BLUE {
      @Override
      public int getColor(BlockEntity blockEntity, BlockPos source, BlockPos target) {
        return DyeColor.BLUE.getFireworkColor();
      }
    },
    CONTROLLER_ASPECT {
      @Override
      public int getColor(BlockEntity blockEntity, BlockPos source, BlockPos target) {
        if (blockEntity instanceof SignalControllerEntity signalControllerEntity) {
          var aspect = signalControllerEntity.getSignalController().aspect();
          return switch (aspect) {
            case GREEN -> DyeColor.LIME.getFireworkColor();
            case YELLOW, BLINK_YELLOW -> DyeColor.YELLOW.getFireworkColor();
            default -> DyeColor.RED.getFireworkColor();
          };
        }
        return CONSTANT_BLUE.getColor(blockEntity, source, target);
      }
    }
  }
}
