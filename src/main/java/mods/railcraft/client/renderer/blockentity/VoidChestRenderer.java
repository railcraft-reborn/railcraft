package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.VoidChestBlock;
import mods.railcraft.world.level.block.entity.VoidChestBlockEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChestBlock;

public class VoidChestRenderer implements BlockEntityRenderer<VoidChestBlockEntity> {

  private static final Material VOID_CHEST =
      new Material(Sheets.CHEST_SHEET, RailcraftConstants.rl("entity/chest/void_chest"));

  private final ModelPart lid;
  private final ModelPart bottom;
  private final ModelPart lock;

  public VoidChestRenderer(BlockEntityRendererProvider.Context context) {
    ModelPart modelpart = context.bakeLayer(ModelLayers.CHEST);
    this.bottom = modelpart.getChild("bottom");
    this.lid = modelpart.getChild("lid");
    this.lock = modelpart.getChild("lock");
  }

  @Override
  public void render(VoidChestBlockEntity blockEntity, float partialTick, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    var blockstate = blockEntity.getLevel() != null
        ? blockEntity.getBlockState()
        : RailcraftBlocks.VOID_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
    if (blockstate.getBlock() instanceof VoidChestBlock) {
      poseStack.pushPose();
      float f = blockstate.getValue(ChestBlock.FACING).toYRot();
      poseStack.translate(0.5F, 0.5F, 0.5F);
      poseStack.mulPose(Axis.YP.rotationDegrees(-f));
      poseStack.translate(-0.5F, -0.5F, -0.5F);

      float lidAngle = blockEntity.getOpenNess(partialTick);
      lidAngle = 1.0F - lidAngle;
      lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
      VertexConsumer vertexconsumer = VOID_CHEST.buffer(bufferSource, RenderType::entityCutout);
      this.render(poseStack, vertexconsumer, this.lid, this.lock, this.bottom, lidAngle, packedLight, packedOverlay);
      poseStack.popPose();
    }
  }

  private void render(
      PoseStack poseStack,
      VertexConsumer consumer,
      ModelPart lidPart,
      ModelPart lockPart,
      ModelPart bottomPart,
      float lidAngle,
      int packedLight,
      int packedOverlay
  ) {
    lidPart.xRot = -(lidAngle * (float) (Math.PI / 2));
    lockPart.xRot = lidPart.xRot;
    lidPart.render(poseStack, consumer, packedLight, packedOverlay);
    lockPart.render(poseStack, consumer, packedLight, packedOverlay);
    bottomPart.render(poseStack, consumer, packedLight, packedOverlay);
  }
}
