package mods.railcraft.client.renderer.blockentity;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.Railcraft;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import mods.railcraft.client.util.CuboidModel.Face;

public abstract class AbstractSignalRenderer<T extends AbstractSignalBlockEntity>
    implements BlockEntityRenderer<T> {

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

  @Override
  public void render(T blockEntity, float partialTicks, PoseStack matrixStack,
      MultiBufferSource renderTypeBuffer, int packedLight, int packedOverlay) {

    SignalAuraRenderUtil.tryRenderSignalAura(blockEntity, matrixStack, renderTypeBuffer);

    if (blockEntity.hasCustomName()) {
      RenderUtil.renderBlockHoverText(blockEntity.getBlockPos(),
          blockEntity.getCustomName(), matrixStack, renderTypeBuffer, packedLight);
    }
  }

  protected void renderSignalAspect(PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
      int packedLight, int packedOverlay, SignalAspect signalAspect, Direction direction) {

    Function<ResourceLocation, TextureAtlasSprite> spriteGetter = Minecraft.getInstance()
        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS);

    final int skyLight = LightTexture.sky(packedLight);
    packedLight = LightTexture.pack(signalAspect.getLampLight(), skyLight);

    this.signalAspectModel.clear();
    this.signalAspectModel.setPackedLight(packedLight);
    this.signalAspectModel.setPackedOverlay(packedOverlay);
    this.signalAspectModel.set(direction,
        this.signalAspectModel.new Face()
            .setSprite(spriteGetter.apply(ASPECT_TEXTURE_LOCATIONS.get(signalAspect)))
            .setSize(16));

    VertexConsumer vertexBuilder =
        renderTypeBuffer.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
    CuboidModelRenderer.render(this.signalAspectModel, matrixStack, vertexBuilder, 0xFFFFFFFF,
        FaceDisplay.FRONT, false);
  }
}
