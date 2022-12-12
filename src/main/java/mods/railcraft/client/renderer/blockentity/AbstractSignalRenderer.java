package mods.railcraft.client.renderer.blockentity;

import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.renderer.RailcraftSheets;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractSignalRenderer<T extends AbstractSignalBlockEntity>
    implements BlockEntityRenderer<T> {

  public static final Map<SignalAspect, ResourceLocation> ASPECT_TEXTURE_LOCATIONS = Map.of(
      SignalAspect.OFF, new ResourceLocation(Railcraft.ID, "entity/signal_aspect/off"),
      SignalAspect.RED, new ResourceLocation(Railcraft.ID, "entity/signal_aspect/red"),
      SignalAspect.YELLOW, new ResourceLocation(Railcraft.ID, "entity/signal_aspect/yellow"),
      SignalAspect.GREEN, new ResourceLocation(Railcraft.ID, "entity/signal_aspect/green"));

  private final CuboidModel signalAspectModel = new CuboidModel(1.0F);

  @Override
  public void render(T blockEntity, float partialTick, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

    SignalAuraRenderUtil.tryRenderSignalAura(blockEntity, poseStack, bufferSource);

    if (blockEntity.hasCustomName()) {
      RenderUtil.renderBlockHoverText(blockEntity.getBlockPos(),
          blockEntity.getCustomName(), poseStack, bufferSource, packedLight);
    }
  }

  protected void renderSignalAspect(PoseStack poseStack, MultiBufferSource bufferSource,
      int packedLight, int packedOverlay, SignalAspect signalAspect, Direction direction) {

    var spriteGetter = Minecraft.getInstance()
        .getTextureAtlas(RailcraftSheets.SIGNAL_ASPECTS_SHEET);

    final int skyLight = LightTexture.sky(packedLight);
    packedLight = LightTexture.pack(signalAspect.getLampLight(), skyLight);

    this.signalAspectModel.clear();
    this.signalAspectModel.setPackedLight(packedLight);
    this.signalAspectModel.setPackedOverlay(packedOverlay);
    this.signalAspectModel.set(direction,
        this.signalAspectModel.new Face()
            .setSprite(spriteGetter.apply(ASPECT_TEXTURE_LOCATIONS.get(signalAspect)))
            .setSize(16));

    var vertexConsumer =
        bufferSource.getBuffer(RailcraftSheets.SIGNAL_ASPECTS_TYPE);
    CuboidModelRenderer.render(this.signalAspectModel, poseStack, vertexConsumer,
        0xFFFFFFFF, FaceDisplay.FRONT, false);
  }
}
