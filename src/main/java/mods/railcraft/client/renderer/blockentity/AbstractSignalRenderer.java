package mods.railcraft.client.renderer.blockentity;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.api.signals.AbstractNetwork;
import mods.railcraft.api.signals.IBlockSignal;
import mods.railcraft.api.signals.IControllerProvider;
import mods.railcraft.api.signals.IReceiverProvider;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.world.level.block.entity.AbstractSignalBlockEntity;
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

public abstract class AbstractSignalRenderer<T extends AbstractSignalBlockEntity>
    extends TileEntityRenderer<T> {

  public static final Map<SignalAspect, ResourceLocation> ASPECT_TEXTURE_LOCATIONS =
      Util.make(new EnumMap<>(SignalAspect.class), map -> {
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
  public void render(T tile, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    // if (tile instanceof IControllerTile) {
    // Collection<BlockPos> pairs = ((IControllerTile) tile).getController().getPairs();
    // if (ClientEffects.INSTANCE.isGoggleAuraActive(ItemGoggles.GoggleAura.TUNING)) {
    // renderLines(tile, x, y, z, pairs, ColorProfile.COORD_RAINBOW);
    // } else if (ClientEffects.INSTANCE.isGoggleAuraActive(ItemGoggles.GoggleAura.SIGNALLING)) {
    // renderLines(tile, x, y, z, pairs, ColorProfile.CONTROLLER_ASPECT);
    // }
    // }
    // if (tile instanceof ISignalTileBlock) {
    // Collection<BlockPos> pairs = ((ISignalTileBlock) tile).getSignalBlock().getPairs();
    // if (ClientEffects.INSTANCE.isGoggleAuraActive(ItemGoggles.GoggleAura.SURVEYING)) {
    // renderLines(tile, x, y, z, pairs, ColorProfile.COORD_RAINBOW);
    // } else if (ClientEffects.INSTANCE.isGoggleAuraActive(ItemGoggles.GoggleAura.SIGNALLING)) {
    // renderLines(tile, x, y, z, pairs, ColorProfile.CONSTANT_BLUE);
    // }
    // } else if (tile instanceof TileSignalToken) {
    // Collection<BlockPos> centroid =
    // Collections.singletonList(((TileSignalToken) tile).getTokenRingCentroid());
    // if (ClientEffects.INSTANCE.isGoggleAuraActive(ItemGoggles.GoggleAura.SURVEYING)) {
    // renderLines(tile, x, y, z, centroid,
    // (t, s, d) -> ((TileSignalToken) t).getTokenRingUUID().hashCode());
    // } else if (ClientEffects.INSTANCE.isGoggleAuraActive(ItemGoggles.GoggleAura.SIGNALLING)) {
    // renderLines(tile, x, y, z, centroid, ColorProfile.CONSTANT_BLUE);
    // }
    // }
    AbstractNetwork pair = null;
    if (tile instanceof IReceiverProvider) {
      pair = ((IReceiverProvider) tile).getReceiver();
    } else if (tile instanceof IControllerProvider) {
      pair = ((IControllerProvider) tile).getController();
    } else if (tile instanceof IBlockSignal) {
      pair = ((IBlockSignal) tile).getBlockSignal();
    }
    if (pair != null) {
      String name = pair.getName();
      if (name != null) {
        Entity player = Minecraft.getInstance().getCameraEntity();
        if (player != null) {
          final float viewDist = 8f;
          double dist = player.blockPosition().distSqr(tile.getBlockPos());

          if (dist <= (double) (viewDist * viewDist)) {
            RayTraceResult mop = player.pick(8, partialTicks, false);
            if (mop != null && mop.getType() == RayTraceResult.Type.BLOCK
                && player.level.getBlockEntity(((BlockRayTraceResult) mop).getBlockPos()) == tile) {
              // RenderTools.renderString(name, x + 0.5, y + 1.5, z + 0.5);
            }
          }
        }
      }
    }
  }

  private void renderLines(T tile, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      float x, float y, float z,
      Collection<BlockPos> endPoints,
      IColorSupplier colorProfile) {
    if (endPoints.isEmpty()) {
      return;
    }

    matrixStack.pushPose();

    Vector3f start = new Vector3f(x, y, z);
    start.add(CENTER);

    IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderType.lines());
    Matrix4f matrix = matrixStack.last().pose();

    for (BlockPos target : endPoints) {
      int color = colorProfile.getColor(tile, tile.getBlockPos(), target);
      float c1 = (float) (color >> 16 & 255) / 255.0F;
      float c2 = (float) (color >> 8 & 255) / 255.0F;
      float c3 = (float) (color & 255) / 255.0F;

      builder.vertex(matrix, target.getX(), target.getY(), target.getZ())
          .color(c1, c2, c3, 1.0F)
          .endVertex();

      float endX = start.x() + target.getX() - tile.getBlockPos().getX();
      float endY = start.y() + target.getY() - tile.getBlockPos().getY();
      float endZ = start.z() + target.getZ() - tile.getBlockPos().getZ();

      builder.vertex(matrix, endX, endY, endZ)
          .color(c1, c2, c3, 1.0F)
          .endVertex();
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
        if (tile instanceof IControllerProvider) {
          SignalAspect aspect = ((IControllerProvider) tile).getController().getAspectFor(target);
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
      int packedLight, int packedOverlay, SignalAspect signalAspect, Direction direction) {

    Function<ResourceLocation, TextureAtlasSprite> spriteGetter =
        Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS);

    final int skyLight = LightTexture.sky(packedLight);
    packedLight = LightTexture.pack(signalAspect.getLampLight(), skyLight);

    this.signalAspectModel.clear();
    this.signalAspectModel.set(direction,
        new CuboidModel.Face()
            .setSprite(spriteGetter.apply(ASPECT_TEXTURE_LOCATIONS.get(signalAspect)))
            .setSize(16)
            .setPackedLight(packedLight)
            .setPackedOverlay(packedOverlay));

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(RenderType.entityCutout(PlayerContainer.BLOCK_ATLAS));
    CuboidModelRenderer.render(this.signalAspectModel, matrixStack, vertexBuilder, 0xFFFFFFFF,
        FaceDisplay.FRONT, false);
  }
}
