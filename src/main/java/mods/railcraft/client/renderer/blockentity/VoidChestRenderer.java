package mods.railcraft.client.renderer.blockentity;

import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.blockentity.state.VoidChestRenderState;
import mods.railcraft.world.level.block.entity.VoidChestBlockEntity;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class VoidChestRenderer implements BlockEntityRenderer<VoidChestBlockEntity,
    VoidChestRenderState> {

  public static final Material VOID_CHEST =
      new Material(Sheets.CHEST_SHEET, RailcraftConstants.rl("entity/chest/void_chest"));

  private final MaterialSet materials;
  private final ChestModel singleModel;

  public VoidChestRenderer(BlockEntityRendererProvider.Context context) {
    this.materials = context.materials();
    this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
  }

  @Override
  public VoidChestRenderState createRenderState() {
    return new VoidChestRenderState();
  }

  @Override
  public void extractRenderState(VoidChestBlockEntity blockEntity, VoidChestRenderState renderState,
      float partialTick, Vec3 cameraPosition,
      @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick,
        cameraPosition, breakProgress);

    var blockstate = blockEntity.getLevel() != null
        ? blockEntity.getBlockState()
        : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
    renderState.angle = blockstate.getValue(ChestBlock.FACING).toYRot();
    renderState.open = blockEntity.getOpenNess(partialTick);
  }

  @Override
  public void submit(VoidChestRenderState renderState, PoseStack poseStack,
      SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
    poseStack.pushPose();
    poseStack.translate(0.5F, 0.5F, 0.5F);
    poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.angle));
    poseStack.translate(-0.5F, -0.5F, -0.5F);

    float lidAngle = renderState.open;
    lidAngle = 1.0F - lidAngle;
    lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;

    var rendertype = VOID_CHEST.renderType(RenderType::entityCutout);
    nodeCollector.submitModel(
        this.singleModel,
        lidAngle,
        poseStack,
        rendertype,
        renderState.lightCoords,
        OverlayTexture.NO_OVERLAY,
        -1,
        this.materials.get(VOID_CHEST),
        0,
        renderState.breakProgress
    );
    poseStack.popPose();
  }

  @Override
  public AABB getRenderBoundingBox(VoidChestBlockEntity blockEntity) {
    var pos = blockEntity.getBlockPos();
    return AABB.encapsulatingFullBlocks(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
  }
}
