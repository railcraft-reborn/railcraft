package mods.railcraft.client.renderer.blockentity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.api.signal.BlockSignal;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.TokenSignal;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;

public class SignalAuraRenderer {

  public static void tryRenderSignalAura(TileEntity blockEntity, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer) {
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
      TileEntity blockEntity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      Collection<BlockPos> endPoints, ColorSupplier colorProfile) {
    if (endPoints.isEmpty()) {
      return;
    }

    matrixStack.pushPose();
    {
      IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderType.lines());
      Matrix4f matrix = matrixStack.last().pose();

      for (BlockPos target : endPoints) {
        int color = colorProfile.getColor(blockEntity, blockEntity.getBlockPos(), target);
        float c1 = (float) (color >> 16 & 255) / 255.0F;
        float c2 = (float) (color >> 8 & 255) / 255.0F;
        float c3 = (float) (color & 255) / 255.0F;

        builder
            .vertex(matrix, 0.5F, 0.5F, 0.5F)
            .color(c1, c2, c3, 1.0F)
            .endVertex();

        float endX = 0.5F + target.getX() - blockEntity.getBlockPos().getX();
        float endY = 0.5F + target.getY() - blockEntity.getBlockPos().getY();
        float endZ = 0.5F + target.getZ() - blockEntity.getBlockPos().getZ();

        builder.vertex(matrix, endX, endY, endZ).color(c1, c2, c3, 1.0F).endVertex();
      }
    }
    matrixStack.popPose();
  }

  @FunctionalInterface
  public interface ColorSupplier {

    int getColor(TileEntity blockEntity, BlockPos source, BlockPos target);
  }

  public enum ColorProfile implements ColorSupplier {
    COORD_RAINBOW {
      private final BlockPos[] coords = new BlockPos[2];

      @Override
      public int getColor(TileEntity blockEntity, BlockPos source, BlockPos target) {
        this.coords[0] = source;
        this.coords[1] = target;
        Arrays.sort(this.coords);
        return Arrays.hashCode(coords);
      }
    },
    CONSTANT_BLUE {
      @Override
      public int getColor(TileEntity blockEntity, BlockPos source, BlockPos target) {
        return DyeColor.BLUE.getColorValue();
      }
    },
    CONTROLLER_ASPECT {
      @Override
      public int getColor(TileEntity blockEntity, BlockPos source, BlockPos target) {
        if (blockEntity instanceof SignalControllerProvider) {
          SignalAspect aspect =
              ((SignalControllerProvider) blockEntity).getSignalController().getSignalAspect();
          switch (aspect) {
            case GREEN:
              return DyeColor.LIME.getColorValue();
            case YELLOW:
            case BLINK_YELLOW:
              return DyeColor.YELLOW.getColorValue();
            default:
              return DyeColor.RED.getColorValue();
          }
        }
        return CONSTANT_BLUE.getColor(blockEntity, source, target);
      }
    };
  }
}
