package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.ItemManipulatorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class ItemManipulatorBlock extends ManipulatorBlock {

  protected ItemManipulatorBlock(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType use(BlockState blockState, World level,
      BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (!level.isClientSide()) {
      TileEntity blockEntity = level.getBlockEntity(blockPos);
      if (!(blockEntity instanceof ItemManipulatorBlockEntity)) {
        return ActionResultType.PASS;
      }

      NetworkHooks.openGui(
          (ServerPlayerEntity) player, (ItemManipulatorBlockEntity) blockEntity, blockPos);
    }
    return ActionResultType.sidedSuccess(level.isClientSide());
  }
}
