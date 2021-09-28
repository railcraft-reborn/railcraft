package mods.railcraft.client.renderer.blockentity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.api.signals.BlockSignal;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.TokenSignal;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractSignalRenderer<T extends AbstractSignalBlockEntity>
    extends TileEntityRenderer<T> {

  public static final Map<SignalAspect, ResourceLocation> ASPECT_TEXTURE_LOCATIONS = Util
      .make(new EnumMap<>(SignalAspect.class), map -> {
        map.put(SignalAspect.OFF, new ResourceLocation(Railcraft.ID, "entity/signal/off_aspect"));
        map.put(SignalAspect.RED, new ResourceLocation(Railcraft.ID, "entity/signal/red_aspect"));
        map.put(SignalAspect.YELLOW,
            new ResourceLocation(Railcraft.ID, "entity/signal/yellow_aspect"));
        map.put(SignalAspect.GREEN,
            new ResourceLocation(Railcraft.ID, "entity/signal/green_aspect"));
      });

  private static final Vector3f CENTER = new Vector3f(0.5F, 0.5F, 0.5F);

  private final CuboidModel signalAspectModel = new CuboidModel(1.0F);

  public AbstractSignalRenderer(TileEntityRendererDispatcher disptacher) {
    super(disptacher);
  }

  @Override
  public void render(T blockEntity, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay) {
    if (blockEntity instanceof SignalController) {
      Collection<BlockPos> peers =
          ((SignalController) blockEntity).getSignalControllerNetwork().getPeers();
      if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.TUNING)) {
        renderLines(blockEntity, matrixStack, renderTypeBuffer, peers, ColorProfile.COORD_RAINBOW);
      } else if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderLines(blockEntity, matrixStack, renderTypeBuffer, peers,
            ColorProfile.CONTROLLER_ASPECT);
      }
    }
    if (blockEntity instanceof BlockSignal) {
      Collection<BlockPos> pairs = ((BlockSignal) blockEntity).getSignalNetwork().getPeers();
      if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SURVEYING)) {
        renderLines(blockEntity, matrixStack, renderTypeBuffer, pairs, ColorProfile.COORD_RAINBOW);
      } else if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderLines(blockEntity, matrixStack, renderTypeBuffer, pairs, ColorProfile.CONSTANT_BLUE);
      }
    } else if (blockEntity instanceof TokenSignal) {
      TokenSignal tokenSignal = (TokenSignal) blockEntity;
      Collection<BlockPos> centroid = Collections.singletonList(tokenSignal.getTokenRingCentroid());
      if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SURVEYING)) {
        renderLines(blockEntity, matrixStack, renderTypeBuffer, centroid,
            (t, s, d) -> tokenSignal.getTokenRingId().hashCode());
      } else if (ClientEffects.INSTANCE.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)) {
        renderLines(blockEntity, matrixStack, renderTypeBuffer, centroid,
            ColorProfile.CONSTANT_BLUE);
      }
    }
    final ITextComponent name = blockEntity.getPrimaryNetworkName();
    if (name != null) {
      Minecraft minecraft = Minecraft.getInstance();
      Entity player = minecraft.getCameraEntity();
      if (player != null) {
        final float viewDist = 8.0F;
        double distanceSquared = player.blockPosition().distSqr(blockEntity.getBlockPos());
        if (distanceSquared <= viewDist * viewDist) {
          if (minecraft.hitResult != null
              && minecraft.hitResult.getType() == RayTraceResult.Type.BLOCK
              && player.level.getBlockEntity(
                  ((BlockRayTraceResult) minecraft.hitResult).getBlockPos()) == blockEntity) {
            matrixStack.pushPose();
            {
              RenderUtil.renderWorldText(minecraft.font, name,
                  matrixStack, renderTypeBuffer, packedLight);
            }
            matrixStack.popPose();
          }
        }
      }
    }
  }

  private static void renderLines(
      TileEntity blockEntity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      Collection<BlockPos> endPoints, IColorSupplier colorProfile) {
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
            .vertex(matrix, target.getX(), target.getY(), target.getZ())
            .color(c1, c2, c3, 1.0F)
            .endVertex();

        float endX = CENTER.x() + target.getX() - blockEntity.getBlockPos().getX();
        float endY = CENTER.y() + target.getY() - blockEntity.getBlockPos().getY();
        float endZ = CENTER.z() + target.getZ() - blockEntity.getBlockPos().getZ();

        builder.vertex(matrix, endX, endY, endZ).color(c1, c2, c3, 1.0F).endVertex();
      }
    }
    matrixStack.popPose();
  }

  @FunctionalInterface
  public interface IColorSupplier {
    int getColor(TileEntity tile, BlockPos source, BlockPos target);
  }

  public enum ColorProfile implements IColorSupplier {
    COORD_RAINBOW {
      private final BlockPos[] coords = new BlockPos[2];

      @Override
      public int getColor(TileEntity tile, BlockPos source, BlockPos target) {
        coords[0] = source;
        coords[1] = target;
        Arrays.sort(coords);
        return Arrays.hashCode(coords);
      }
    },
    CONSTANT_BLUE {
      @Override
      public int getColor(TileEntity tile, BlockPos source, BlockPos target) {
        return DyeColor.BLUE.getColorValue();
      }
    },
    CONTROLLER_ASPECT {
      @Override
      public int getColor(TileEntity tile, BlockPos source, BlockPos target) {
        if (tile instanceof SignalController) {
          SignalAspect aspect =
              ((SignalController) tile).getSignalControllerNetwork().getAspectFor(target);
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
        return CONSTANT_BLUE.getColor(tile, source, target);
      }
    };

    @Override
    public abstract int getColor(TileEntity tile, BlockPos source, BlockPos target);
  }

  protected void renderSignalAspect(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight,
      int packedOverlay, SignalAspect signalAspect, Direction direction) {

    Function<ResourceLocation, TextureAtlasSprite> spriteGetter = Minecraft.getInstance()
        .getTextureAtlas(PlayerContainer.BLOCK_ATLAS);

    final int skyLight = LightTexture.sky(packedLight);
    packedLight = LightTexture.pack(signalAspect.getLampLight(), skyLight);

    this.signalAspectModel.clear();
    this.signalAspectModel.set(direction,
        new CuboidModel.Face()
            .setSprite(spriteGetter.apply(ASPECT_TEXTURE_LOCATIONS.get(signalAspect)))
            .setSize(16).setPackedLight(packedLight).setPackedOverlay(packedOverlay));

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(RenderType.entityCutout(PlayerContainer.BLOCK_ATLAS));
    CuboidModelRenderer.render(this.signalAspectModel, matrixStack, vertexBuilder, 0xFFFFFFFF,
        FaceDisplay.FRONT,
        false);
  }
}
