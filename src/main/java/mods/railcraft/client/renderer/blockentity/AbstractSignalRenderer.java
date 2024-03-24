package mods.railcraft.client.renderer.blockentity;

import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;

public abstract class AbstractSignalRenderer<T extends AbstractSignalBlockEntity>
    implements BlockEntityRenderer<T> {

  private static final Map<SignalAspect, ResourceLocation> ASPECT_TEXTURE_LOCATIONS = Map.of(
      SignalAspect.OFF, RailcraftConstants.rl("entity/signal_aspect/off"),
      SignalAspect.RED, RailcraftConstants.rl("entity/signal_aspect/red"),
      SignalAspect.YELLOW, RailcraftConstants.rl("entity/signal_aspect/yellow"),
      SignalAspect.GREEN, RailcraftConstants.rl("entity/signal_aspect/green"));

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

    var spriteGetter = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);

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
        bufferSource.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
    CuboidModelRenderer.render(this.signalAspectModel, poseStack, vertexConsumer,
        0xFFFFFFFF, FaceDisplay.FRONT, false);
  }

  @Override
  public AABB getRenderBoundingBox(T blockEntity) {
    var pos = blockEntity.getBlockPos();
    return new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1,
        pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
  }
}
