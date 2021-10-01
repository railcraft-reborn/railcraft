package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.RollingTableEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class RollingTable extends Block {

  public RollingTable(Properties properties) {
    super(properties);
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world){
    return new RollingTableEntity();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  protected void openContainer(World world, BlockPos blockPos, PlayerEntity playerEntity) {
    TileEntity tileentity = world.getBlockEntity(blockPos);
    if (tileentity instanceof RollingTableEntity) {
      playerEntity.openMenu((INamedContainerProvider)tileentity);
      // playerEntity.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
      // TODO: interaction stats
    }
  }

  @Override
  public ActionResultType use(BlockState blockState, World world,
    BlockPos pos, PlayerEntity user, Hand userHand, BlockRayTraceResult blockRayResult) {
    if (world.isClientSide) {
      return ActionResultType.SUCCESS;
    } else {
      this.openContainer(world, pos, user);
      return ActionResultType.CONSUME;
    }
  }
}
