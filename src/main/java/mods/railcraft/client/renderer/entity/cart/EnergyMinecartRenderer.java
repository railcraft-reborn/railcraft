package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.world.entity.vehicle.EnergyMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class EnergyMinecartRenderer extends ContentsMinecartRenderer<EnergyMinecart> {

  private static final ResourceLocation FRAME =
      RailcraftConstants.rl("entity/minecart/energy_minecart_flux_frame");

  private static final ResourceLocation CORE =
      RailcraftConstants.rl("entity/minecart/energy_minecart_flux_core");

  private static final float PIXEL_OFFSET = 0.5F / 16F;

  private static final CuboidModel FRAME_MODEL =
      new CuboidModel(PIXEL_OFFSET, PIXEL_OFFSET, PIXEL_OFFSET,
          1 - PIXEL_OFFSET, 1 - PIXEL_OFFSET, 1 - PIXEL_OFFSET);

  private static final CuboidModel CORE_MODEL =
      new CuboidModel(1 / 16F, 1 / 16F, 1 / 16F, 15 / 16F, 15 / 16F, 15 / 16F);

  private final LowSidesMinecartModel<EnergyMinecart> bodyModel;
  private final LowSidesMinecartModel<EnergyMinecart> snowModel;

  public EnergyMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
  }

  @Override
  protected void renderContents(EnergyMinecart cart, float partialTicks, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, float red, float green, float blue,
      float alpha) {
    var minecraft = Minecraft.getInstance();

    CuboidModel.Face frameFace = FRAME_MODEL.new Face()
        .setSprite(minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(FRAME));

    CuboidModel.Face coreFace = CORE_MODEL.new Face()
        .setSprite(minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(CORE));

    FRAME_MODEL.setAll(frameFace);
    FRAME_MODEL.setPackedLight(packedLight);
    FRAME_MODEL.setPackedOverlay(OverlayTexture.NO_OVERLAY);

    CORE_MODEL.setAll(coreFace);
    CORE_MODEL.setPackedLight(packedLight);
    CORE_MODEL.setPackedOverlay(OverlayTexture.NO_OVERLAY);

    poseStack.pushPose();
    var vertexBuilder =
        bufferSource.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
    CuboidModelRenderer.render(FRAME_MODEL, poseStack, vertexBuilder, 0xFFFFFFFF,
        CuboidModelRenderer.FaceDisplay.BOTH, false);
    CuboidModelRenderer.render(CORE_MODEL, poseStack, vertexBuilder, 0xFFFFFFFF,
        CuboidModelRenderer.FaceDisplay.BOTH, false);
    poseStack.popPose();
  }

  @Override
  protected EntityModel<? super EnergyMinecart> getBodyModel(EnergyMinecart cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<? super EnergyMinecart> getSnowModel(EnergyMinecart cart) {
    return this.snowModel;
  }
}
