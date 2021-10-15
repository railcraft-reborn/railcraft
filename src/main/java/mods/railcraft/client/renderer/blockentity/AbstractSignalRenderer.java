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
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
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

  private final CuboidModel signalAspectModel = new CuboidModel(1.0F);

  public AbstractSignalRenderer(TileEntityRendererDispatcher disptacher) {
    super(disptacher);
  }

  @Override
  public void render(T blockEntity, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {

    SignalAuraRenderUtil.tryRenderSignalAura(blockEntity, matrixStack, renderTypeBuffer);

    if (blockEntity.hasCustomName()) {
      RenderUtil.renderBlockHoverText(blockEntity.getBlockPos(),
          blockEntity.getCustomName(), matrixStack, renderTypeBuffer, packedLight);
    }
  }

  protected void renderSignalAspect(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay, SignalAspect signalAspect, Direction direction) {

    Function<ResourceLocation, TextureAtlasSprite> spriteGetter = Minecraft.getInstance()
        .getTextureAtlas(PlayerContainer.BLOCK_ATLAS);

    final int skyLight = LightTexture.sky(packedLight);
    packedLight = LightTexture.pack(signalAspect.getLampLight(), skyLight);

    this.signalAspectModel.clear();
    this.signalAspectModel.setPackedLight(packedLight);
    this.signalAspectModel.setPackedOverlay(packedOverlay);
    this.signalAspectModel.set(direction,
        this.signalAspectModel.new Face()
            .setSprite(spriteGetter.apply(ASPECT_TEXTURE_LOCATIONS.get(signalAspect)))
            .setSize(16));

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(RenderType.entityCutout(PlayerContainer.BLOCK_ATLAS));
    CuboidModelRenderer.render(this.signalAspectModel, matrixStack, vertexBuilder, 0xFFFFFFFF,
        FaceDisplay.FRONT, false);
  }
}
