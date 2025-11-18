package mods.railcraft.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.VoidChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class VoidChestItemRenderer extends BlockEntityWithoutLevelRenderer {

  private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

  public VoidChestItemRenderer() {
    super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
  }

  @Override
  public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
      MultiBufferSource buffer, int packedLight, int packedOverlay) {
    this.blockEntityRenderDispatcher.renderItem(
        new VoidChestBlockEntity(BlockPos.ZERO, RailcraftBlocks.VOID_CHEST.get().defaultBlockState()),
        poseStack, buffer, packedLight, packedOverlay);
  }
}
