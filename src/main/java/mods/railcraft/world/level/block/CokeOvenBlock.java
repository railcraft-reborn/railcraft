package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.multiblock.CokeOvenMultiblockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CokeOvenBlock extends Block {

  public CokeOvenBlock(Properties properties) {
    super(properties);
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new CokeOvenMultiblockEntity();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  protected void openContainer(World world, BlockPos blockPos,
      PlayerEntity player, Direction facingDir) {
    TileEntity blockEntity = world.getBlockEntity(blockPos);
    if (!(blockEntity instanceof CokeOvenMultiblockEntity)) {
      return;
    }
    CokeOvenMultiblockEntity recast = (CokeOvenMultiblockEntity) blockEntity;

    if (!recast.isFormed() || !recast.tryToMakeParent(facingDir)) {
      return;
    }

    recast = recast.getParent();
    if (recast == null) {
      return;
    }
    player.openMenu(recast);
    // player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
    // TODO: interaction stats
  }

  @Override
  public ActionResultType use(BlockState blockState, World world,
      BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (world.isClientSide()) {
      return ActionResultType.SUCCESS;
    } else {
      this.openContainer(world, pos, player, rayTraceResult.getDirection());
      return ActionResultType.CONSUME;
    }
  }

}
