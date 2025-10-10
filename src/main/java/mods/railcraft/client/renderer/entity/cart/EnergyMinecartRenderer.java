package mods.railcraft.client.renderer.entity.cart;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.world.entity.vehicle.EnergyMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.ResourceLocation;

public class EnergyMinecartRenderer extends ContentsMinecartRenderer<EnergyMinecart, RailcraftMinecartRenderState> {

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

  private final LowSidesMinecartModel<RailcraftMinecartRenderState> bodyModel;
  private final LowSidesMinecartModel<RailcraftMinecartRenderState> snowModel;

  public EnergyMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
  }

  @Override
  protected void renderContents(RailcraftMinecartRenderState renderState, PoseStack poseStack,
      SubmitNodeCollector collector, int color) {
    var textureAtlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);

    CuboidModel.Face frameFace = FRAME_MODEL.new Face()
        .setSprite(textureAtlas.getSprite(FRAME));

    CuboidModel.Face coreFace = CORE_MODEL.new Face()
        .setSprite(textureAtlas.getSprite(CORE));

    FRAME_MODEL.setAll(frameFace);
    FRAME_MODEL.setPackedLight(renderState.lightCoords);
    FRAME_MODEL.setPackedOverlay(OverlayTexture.NO_OVERLAY);

    CORE_MODEL.setAll(coreFace);
    CORE_MODEL.setPackedLight(renderState.lightCoords);
    CORE_MODEL.setPackedOverlay(OverlayTexture.NO_OVERLAY);

    poseStack.pushPose();
    collector.submitCustomGeometry(poseStack, RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS),
        (pose, vertexConsumer) -> {
      CuboidModelRenderer.render(FRAME_MODEL, pose, vertexConsumer, 0xFFFFFFFF,
          CuboidModelRenderer.FaceDisplay.BOTH, false);
      CuboidModelRenderer.render(CORE_MODEL, pose, vertexConsumer, 0xFFFFFFFF,
          CuboidModelRenderer.FaceDisplay.BOTH, false);
    });
    poseStack.popPose();
  }

  @Override
  protected EntityModel<RailcraftMinecartRenderState> getBodyModel(RailcraftMinecartRenderState cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<RailcraftMinecartRenderState> getSnowModel(RailcraftMinecartRenderState cart) {
    return this.snowModel;
  }

  @Override
  public RailcraftMinecartRenderState createRenderState() {
    return new RailcraftMinecartRenderState();
  }
}
