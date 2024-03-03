package mods.railcraft.client.renderer.blockentity;

import java.util.Collection;
import java.util.Objects;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.signal.BlockSignalEntity;
import mods.railcraft.api.signal.TokenSignalEntity;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.client.util.LineRenderer;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SignalAuraRenderUtil {

  public static void tryRenderSignalAura(BlockEntity blockEntity,
      PoseStack poseStack, MultiBufferSource bufferSource) {
    var lineRenderer = LineRenderer.simple(bufferSource);
    if (blockEntity instanceof SignalControllerEntity provider) {
      renderControllerAura(blockEntity.getBlockPos(), poseStack, lineRenderer, provider);
    }
    if (blockEntity instanceof BlockSignalEntity blockSignal) {
      renderBlockSignalAura(blockEntity.getBlockPos(), poseStack, lineRenderer, blockSignal);
    } else if (blockEntity instanceof TokenSignalEntity tokenSignal) {
      renderTokenSignalAura(blockEntity.getBlockPos(), poseStack, lineRenderer, tokenSignal);
    }
  }

  private static void renderControllerAura(BlockPos blockPos, PoseStack poseStack,
      LineRenderer lineRenderer, SignalControllerEntity provider) {
    var peers = provider.getSignalController().peers();
    if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.TUNING)) {
      renderSignalAura(blockPos, poseStack, lineRenderer, peers, SignalAuraRenderUtil::rainbow);
    } else if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
      renderSignalAura(blockPos, poseStack, lineRenderer, peers,
          ColorSupplier.of(provider.getSignalController().aspect().color()));
    }
  }

  private static void renderBlockSignalAura(BlockPos blockPos, PoseStack poseStack,
      LineRenderer lineRenderer, BlockSignalEntity blockSignal) {
    var peers = blockSignal.signalNetwork().peers();
    if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SURVEYING)) {
      renderSignalAura(blockPos, poseStack, lineRenderer, peers, SignalAuraRenderUtil::rainbow);
    } else if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
      renderSignalAura(blockPos, poseStack, lineRenderer, peers, ColorSupplier.CONSTANT_BLUE);
    }
  }

  private static void renderTokenSignalAura(BlockPos blockPos, PoseStack poseStack,
      LineRenderer lineRenderer, TokenSignalEntity tokenSignal) {
    if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SURVEYING)) {
      renderAuraLine(lineRenderer, poseStack, tokenSignal.ringId().hashCode(), blockPos,
          tokenSignal.ringCentroidPos());
    } else if (GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
      renderAuraLine(lineRenderer, poseStack, DyeColor.BLUE.getFireworkColor(), blockPos,
          tokenSignal.ringCentroidPos());
    }
  }

  private static void renderAuraLine(LineRenderer renderer, PoseStack poseStack, int color,
      BlockPos source, Position target) {
    var red = FastColor.ARGB32.red(color);
    var green = FastColor.ARGB32.green(color);
    var blue = FastColor.ARGB32.blue(color);

    var endX = (float) (target.x() - source.getX());
    var endY = (float) (target.y() - source.getY());
    var endZ = (float) (target.z() - source.getZ());

    renderer.renderLine(poseStack,
        red, green, blue, 255,
        0.5F, 0.5F, 0.5F,
        endX, endY, endZ);
  }

  private static void renderSignalAura(BlockPos source, PoseStack poseStack,
      LineRenderer lineRenderer, Collection<BlockPos> endPoints, ColorSupplier colorProfile) {
    for (var target : endPoints) {
      int color = colorProfile.getColor(source, target);
      renderAuraLine(lineRenderer, poseStack, color, source, target.getCenter());
    }
  }

  public static int rainbow(BlockPos source, BlockPos target) {
    var comparison = source.compareTo(target);
    return comparison < 0
        ? Objects.hash(source, target)
        : Objects.hash(target, source);
  }

  @FunctionalInterface
  public interface ColorSupplier {

    ColorSupplier CONSTANT_BLUE = of(DyeColor.BLUE);

    int getColor(BlockPos source, BlockPos target);

    static ColorSupplier of(DyeColor color) {
      return of(color.getFireworkColor());
    }

    static ColorSupplier of(int color) {
      return (source, target) -> color;
    }
  }
}
