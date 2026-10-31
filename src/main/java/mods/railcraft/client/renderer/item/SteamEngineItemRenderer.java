package mods.railcraft.client.renderer.item;

import java.util.HashMap;
import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.world.level.block.SteamEngineBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SteamEngineItemRenderer extends BlockEntityWithoutLevelRenderer {

  private final Map<SteamEngineBlock, BlockEntity> blockEntities = new HashMap<>();

  public SteamEngineItemRenderer() {
    super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
        Minecraft.getInstance().getEntityModels());
  }

  @Override
  public void renderByItem(ItemStack stack, ItemDisplayContext displayContext,
      PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
    if (stack.getItem() instanceof BlockItem blockItem
        && blockItem.getBlock() instanceof SteamEngineBlock block) {
      var blockEntity = this.blockEntities.computeIfAbsent(block,
          __ -> block.newBlockEntity(BlockPos.ZERO, block.defaultBlockState()));
      Minecraft.getInstance().getBlockEntityRenderDispatcher()
          .renderItem(blockEntity, poseStack, buffer, packedLight, packedOverlay);
    }
  }
}
