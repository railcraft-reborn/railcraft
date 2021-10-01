package mods.railcraft.client.renderer.blockentity;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public abstract class AbstractSignalBoxRenderer
    extends TileEntityRenderer<AbstractSignalBoxBlockEntity> {

  public static final Map<SignalAspect, ResourceLocation> ASPECT_TEXTURE_LOCATIONS =
      Util.make(new EnumMap<>(SignalAspect.class), map -> {
        map.put(SignalAspect.OFF,
            new ResourceLocation(Railcraft.ID, "entity/signal_box/off_aspect"));
        map.put(SignalAspect.RED,
            new ResourceLocation(Railcraft.ID, "entity/signal_box/red_aspect"));
        map.put(SignalAspect.YELLOW,
            new ResourceLocation(Railcraft.ID, "entity/signal_box/yellow_aspect"));
        map.put(SignalAspect.GREEN,
            new ResourceLocation(Railcraft.ID, "entity/signal_box/green_aspect"));
      });

  public static final ResourceLocation SIDE_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/signal_box/side");
  public static final ResourceLocation CONNECTED_SIDE_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/signal_box/connected_side");
  public static final ResourceLocation BOTTOM_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/signal_box/bottom");

  private final CuboidModel model =
      new CuboidModel(2 / 16.0F, 0, 2 / 16.0F, 14 / 16.0F, 15 / 16.0F, 14 / 16.0F);

  protected abstract ResourceLocation getTopTextureLocation();

  public AbstractSignalBoxRenderer(TileEntityRendererDispatcher dispatcher) {
    super(dispatcher);
  }

  @Override
  public void render(AbstractSignalBoxBlockEntity blockEntity, float partialTicks,
      MatrixStack poseStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {

    SignalAuraRenderer.tryRenderSignalAura(blockEntity, poseStack, renderTypeBuffer);

    if (blockEntity.hasCustomName()) {
      RenderUtil.renderBlockHoverText(blockEntity.getBlockPos(),
          blockEntity.getCustomName(), poseStack, renderTypeBuffer, packedLight);
    }

    Function<ResourceLocation, TextureAtlasSprite> spriteGetter =
        Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS);

    this.model.set(Direction.UP, new CuboidModel.Face()
        .setSprite(spriteGetter.apply(this.getTopTextureLocation()))
        .setSize(16)
        .setPackedLight(packedLight)
        .setPackedOverlay(packedOverlay));
    this.model.set(Direction.DOWN, new CuboidModel.Face()
        .setSprite(spriteGetter.apply(BOTTOM_TEXTURE_LOCATION))
        .setSize(16)
        .setPackedLight(packedLight)
        .setPackedOverlay(packedOverlay));

    for (Direction direction : Direction.Plane.HORIZONTAL) {
      if (SignalBoxBlock.isConnected(blockEntity.getBlockState(), direction)) {
        this.model.set(direction, new CuboidModel.Face()
            .setSprite(spriteGetter.apply(CONNECTED_SIDE_TEXTURE_LOCATION))
            .setSize(16)
            .setPackedLight(packedLight)
            .setPackedOverlay(packedOverlay));
      } else {
        this.model.set(direction, new CuboidModel.Face()
            .setSprite(spriteGetter.apply(SIDE_TEXTURE_LOCATION))
            .setSize(16)
            .setPackedLight(packedLight)
            .setPackedOverlay(packedOverlay));
      }
    }

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(RenderType.entityCutout(PlayerContainer.BLOCK_ATLAS));
    CuboidModelRenderer.render(
        this.model, poseStack, vertexBuilder, 0xFFFFFFFF, FaceDisplay.BOTH, false);

    for (Direction direction : Direction.Plane.HORIZONTAL) {
      if (SignalBoxBlock.isConnected(blockEntity.getBlockState(), direction)) {
        this.model.disable(direction);
      } else {
        SignalAspect aspect = blockEntity.getSignalAspect(direction).getDisplayAspect();
        final int skyLight = LightTexture.sky(packedLight);
        final int facePackedLight = LightTexture.pack(aspect.getLampLight(), skyLight);
        this.model.set(direction, new CuboidModel.Face()
            .setSprite(spriteGetter.apply(ASPECT_TEXTURE_LOCATIONS.get(aspect)))
            .setSize(16)
            .setPackedLight(facePackedLight)
            .setPackedOverlay(packedOverlay));
      }
    }

    CuboidModelRenderer.render(
        this.model, poseStack, vertexBuilder, 0xFFFFFFFF, FaceDisplay.BOTH, false);
  }
}
