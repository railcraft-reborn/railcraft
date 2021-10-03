package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
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

public class ManualRollingMachineBlock extends Block {

  public ManualRollingMachineBlock(Properties properties) {
    super(properties);
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new ManualRollingMachineBlockEntity();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  protected void openContainer(World world, BlockPos blockPos, PlayerEntity player) {
    TileEntity blockEntity = world.getBlockEntity(blockPos);
    if (blockEntity instanceof ManualRollingMachineBlockEntity) {
      player.openMenu((INamedContainerProvider) blockEntity);
      // player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
      // TODO: interaction stats
    }
  }

  @Override
  public ActionResultType use(BlockState blockState, World world,
      BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (world.isClientSide()) {
      return ActionResultType.SUCCESS;
    } else {
      this.openContainer(world, pos, player);
      return ActionResultType.CONSUME;
    }
  }
}
