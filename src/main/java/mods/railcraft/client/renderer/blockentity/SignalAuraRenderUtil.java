package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.signal.BlockSignal;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.TokenSignal;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class SignalAuraRenderUtil {

  public static void tryRenderSignalAura(BlockEntity blockEntity, PoseStack matrixStack,
      MultiBufferSource renderTypeBuffer) {
    if (blockEntity instanceof SignalControllerProvider) {
      Collection<BlockPos> peers =
          ((SignalControllerProvider) blockEntity).getSignalController().getPeers();
      if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.TUNING)) {
        renderSignalAura(blockEntity, matrixStack, renderTypeBuffer, peers,
            ColorProfile.COORD_RAINBOW);
      } else if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderSignalAura(blockEntity, matrixStack, renderTypeBuffer, peers,
            ColorProfile.CONTROLLER_ASPECT);
      }
    }
    if (blockEntity instanceof BlockSignal) {
      Collection<BlockPos> peers = ((BlockSignal) blockEntity).getSignalNetwork().getPeers();
      if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SURVEYING)) {
        renderSignalAura(blockEntity, matrixStack, renderTypeBuffer, peers,
            ColorProfile.COORD_RAINBOW);
      } else if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderSignalAura(blockEntity, matrixStack, renderTypeBuffer, peers,
            ColorProfile.CONSTANT_BLUE);
      }
    } else if (blockEntity instanceof TokenSignal) {
      TokenSignal tokenSignal = (TokenSignal) blockEntity;
      Collection<BlockPos> centroid = Collections.singletonList(tokenSignal.getTokenRingCentroid());
      if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SURVEYING)) {
        renderSignalAura(blockEntity, matrixStack, renderTypeBuffer, centroid,
            (t, s, d) -> tokenSignal.getTokenRingId().hashCode());
      } else if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderSignalAura(blockEntity, matrixStack, renderTypeBuffer, centroid,
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
    {
      var consumer = bufferSource.getBuffer(RenderType.lines());
      var matrix = poseStack.last().pose();
      var normal = poseStack.last().normal();

      for (BlockPos target : endPoints) {
        int color = colorProfile.getColor(blockEntity, blockEntity.getBlockPos(), target);
        float c1 = (float) (color >> 16 & 255) / 255.0F;
        float c2 = (float) (color >> 8 & 255) / 255.0F;
        float c3 = (float) (color & 255) / 255.0F;

        consumer
            .vertex(matrix, 0.5F, 0.5F, 0.5F)
            .color(c1, c2, c3, 1.0F)
            .normal(normal, 1.0F, 0.0F, 0.0F)
            .endVertex();

        float endX = 0.5F + target.getX() - blockEntity.getBlockPos().getX();
        float endY = 0.5F + target.getY() - blockEntity.getBlockPos().getY();
        float endZ = 0.5F + target.getZ() - blockEntity.getBlockPos().getZ();

        consumer.vertex(matrix, endX, endY, endZ).color(c1, c2, c3, 1.0F)
            .normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
      }
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
        if (blockEntity instanceof SignalControllerProvider) {
          SignalAspect aspect =
              ((SignalControllerProvider) blockEntity).getSignalController().getSignalAspect();
          switch (aspect) {
            case GREEN:
              return DyeColor.LIME.getFireworkColor();
            case YELLOW:
            case BLINK_YELLOW:
              return DyeColor.YELLOW.getFireworkColor();
            default:
              return DyeColor.RED.getFireworkColor();
          }
        }
        return CONSTANT_BLUE.getColor(blockEntity, source, target);
      }
    };
  }
}
