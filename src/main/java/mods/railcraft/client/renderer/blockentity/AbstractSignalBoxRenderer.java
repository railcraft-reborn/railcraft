package mods.railcraft.client.renderer.blockentity;

import java.util.EnumMap;
import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.CuboidModelRenderer.FaceDisplay;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public abstract class AbstractSignalBoxRenderer
    implements BlockEntityRenderer<AbstractSignalBoxBlockEntity> {

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

  @Override
  public int getViewDistance() {
    return 512;
  }

  @Override
  public void render(AbstractSignalBoxBlockEntity blockEntity, float partialTicks,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
      int packedOverlay) {

    SignalAuraRenderUtil.tryRenderSignalAura(blockEntity, poseStack, bufferSource);

    if (blockEntity.hasCustomName()) {
      RenderUtil.renderBlockHoverText(blockEntity.getBlockPos(),
          blockEntity.getCustomName(), poseStack, bufferSource, packedLight);
    }

    var spriteGetter = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);

    this.model.setPackedLight(packedLight);
    this.model.setPackedOverlay(packedOverlay);

    this.model.set(Direction.UP, this.model.new Face()
        .setSprite(spriteGetter.apply(this.getTopTextureLocation()))
        .setSize(16));
    this.model.set(Direction.DOWN, this.model.new Face()
        .setSprite(spriteGetter.apply(BOTTOM_TEXTURE_LOCATION))
        .setSize(16));

    for (var direction : Direction.Plane.HORIZONTAL) {
      if (SignalBoxBlock.isConnected(blockEntity.getBlockState(), direction)) {
        this.model.set(direction, this.model.new Face()
            .setSprite(spriteGetter.apply(CONNECTED_SIDE_TEXTURE_LOCATION))
            .setSize(16));
      } else {
        this.model.set(direction, this.model.new Face()
            .setSprite(spriteGetter.apply(SIDE_TEXTURE_LOCATION))
            .setSize(16));
      }
    }

    var consumer =
        bufferSource.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
    CuboidModelRenderer.render(this.model, poseStack, consumer, 0xFFFFFFFF, FaceDisplay.BOTH,
        false);

    for (var direction : Direction.Plane.HORIZONTAL) {
      if (SignalBoxBlock.isConnected(blockEntity.getBlockState(), direction)) {
        this.model.disable(direction);
      } else {
        var aspect = blockEntity.getSignalAspect(direction).getDisplayAspect();
        final int skyLight = LightTexture.sky(packedLight);
        final int facePackedLight = LightTexture.pack(aspect.getLampLight(), skyLight);
        this.model.set(direction, this.model.new Face()
            .setSprite(spriteGetter.apply(ASPECT_TEXTURE_LOCATIONS.get(aspect)))
            .setSize(16)
            .setPackedLight(facePackedLight)
            .setPackedOverlay(packedOverlay));
      }
    }

    CuboidModelRenderer.render(
        this.model, poseStack, consumer, 0xFFFFFFFF, FaceDisplay.BOTH, false);
  }
}
